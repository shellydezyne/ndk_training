/* Copyright (c) 2013, Intel Corporation
*
* Redistribution and use in source and binary forms, with or without 
* modification, are permitted provided that the following conditions are met:
*
* - Redistributions of source code must retain the above copyright notice, 
*   this list of conditions and the following disclaimer.
* - Redistributions in binary form must reproduce the above copyright notice, 
*   this list of conditions and the following disclaimer in the documentation 
*   and/or other materials provided with the distribution.
* - Neither the name of Intel Corporation nor the names of its contributors 
*   may be used to endorse or promote products derived from this software 
*   without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
* POSSIBILITY OF SUCH DAMAGE.
*
*/

//package android.intel.sdp.DataEncryption;
package com.example.dheerajkaushik.ndktest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.String;

public class DataEncryptionActivity extends Activity {

	private final String TAG = "SDP_SECURITY_ENCRYPTION";

	private Boolean mDoneEncryption = false;
	private long mTimeTook = 0;

	ProgressThread progThread;
	ProgressDialog progDialog;
	int delay = 40; // Milliseconds of delay in the update loop
	int maxBarValue = 200; // Maximum value of horizontal progress bar
	int typeBar;

	private Handler mHandler = new Handler();
	private String mIndicatorText;
	private TextView mIndicator;
	private SDPUtility mUtility;
	private Button encyptionButton;
	private Intent mPreferences;
	private DataEncryption mEncrypter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_encryption);

		Log.d(TAG,"onCreate called");
		mUtility = new SDPUtility();
		mHandler.postDelayed(mUpdateTimeTask, 100);
		mIndicator = (TextView) findViewById(R.id.encryptionIndicator);
    	int read = 0;
    	
		// check to make sure that file which we will encrypt has been copied to external storage
		/*String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File file1 = new File(filePath + "/test.3gp");
		
        if (!file1.isFile()) {

        	InputStream fis = getResources().openRawResource(R.raw.test);
        	try {
				file1.createNewFile();
				byte[] block = new byte[64];
				
	        	FileOutputStream fos = new FileOutputStream(file1);
	        	
	        	while ( (read = fis.read(block,0,64)) != -1) {
	        		fos.write(block,0,read);
	        	}
	        	
	        	fis.close();
	        	fos.close();
        	} catch (IOException e) {
				e.printStackTrace();
			}
        }*/
        
        // create Button Click Listener for clicking the Encrypt button 
		typeBar = 0;
		encyptionButton = (Button) findViewById(R.id.encryptionButton);
		encyptionButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mDoneEncryption = false;
				showDialog(typeBar);
				new Thread(new Runnable() {
					public void run() {
						encryptVideo();
					}
				}).start();
			}
		}); 
		
		// create new Activity for the Settings (Preferences)
		mPreferences = new Intent(this, PreferencesActivity.class);
		
		// set default preferences
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		
	}
	
	public void settingsMenuOpen(View view) {
		startActivity(mPreferences);
	}

	private void encryptVideo() {
		
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		String filename = filePath + "/test.3gp";
		int pos = filename.lastIndexOf('.');
		String filenameDec = filename.substring(0,pos) + "-dec." + filename.substring(pos+1);
		String filenameEnc = filename.substring(0,pos) + "-enc." + filename.substring(pos+1);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String implementationPref = sharedPref.getString("implementation", "");
		
		if (implementationPref.equals(Preferences.IMPLEMENTATION_CRYPTO)) {
			mEncrypter = new DataEncryptionCrypto();
		} else { // Preferences.IMPLEMENTATION_SSL
			mEncrypter = new DataEncryptionOpenSSL();
		}
		
		// set blocksize
		String blocksizePref = sharedPref.getString("blocksize", "");
		int blocksize = 128;
		if (blocksizePref.equals( Preferences.BLOCKSIZE_16 )) {
			blocksize = 16;
		} else if  (blocksizePref.equals(Preferences.BLOCKSIZE_64 )) {
			blocksize = 64;
		} else if (blocksizePref.equals(Preferences.BLOCKSIZE_256 )) {
			blocksize = 256;
		} else if (blocksizePref.equals(Preferences.BLOCKSIZE_1024 )) {
			blocksize = 1024;
		} else if (blocksizePref.equals(Preferences.BLOCKSIZE_8192 )) {
			blocksize = 8192;
		} else {
			throw new IllegalArgumentException("unexpected selection");
		}
		
		mEncrypter.setBlocksize(blocksize);
		
		// encrypt and decrypt the file. time how long it takes
		long seconds = mEncrypter.encryptFile(filenameEnc, filename);
		seconds += mEncrypter.decryptFile(filenameDec, filenameEnc);
		mTimeTook = seconds;
		mDoneEncryption = true;
		
	}
	
	// Method to create a progress bar dialog of either spinner or horizontal
	// type
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case 0: // Spinner
			progDialog = new ProgressDialog(this);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setMessage("encrypting media file...");
			
			return progDialog;
		case 1: // Horizontal
			progDialog = new ProgressDialog(this);
			progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progDialog.setMax(maxBarValue);
			progDialog.setMessage("Dollars in checking account:");
			
			return progDialog;
		default:
			return null;
		}
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
		progThread = new ProgressThread(handler);
		progThread.start();		
	}

	// Handler on the main (UI) thread that will receive messages from the
	// second thread and update the progress.

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// Get the current value of the variable total from the message data
			// and update the progress bar.
			int total = msg.getData().getInt("total");
			progDialog.setProgress(total);
			if (mDoneEncryption) {
				dismissDialog(typeBar);
				progThread.setState(ProgressThread.DONE);
				//mStatus.setText(mKeygenStr);

			}
		}
	};

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			DecimalFormat df = new DecimalFormat("#.##");
			String cpuUsage = df.format(mUtility.readUsage() * 100) + "%";
			String memUsage = Long
					.toString(mUtility
							.readMem((ActivityManager) getSystemService(ACTIVITY_SERVICE)))
					+ "M";

			mIndicatorText = "CPU usage: " + cpuUsage + "\nMemory usage: "
					+ memUsage;
			if (mDoneEncryption) {
				mIndicatorText = mIndicatorText + "\nTotal Time Spent: "
						+ mTimeTook + " ms";
			}

			mIndicator.setText(mIndicatorText);

			mHandler.postAtTime(this, SystemClock.uptimeMillis() + 2000);
		}
	};

	private class ProgressThread extends Thread {

		// Class constants defining state of the thread
		final static int DONE = 0;
		final static int RUNNING = 1;

		Handler mHandler;
		int mState;
		int total;

		// Constructor with an argument that specifies Handler on main thread
		// to which messages will be sent by this thread.

		ProgressThread(Handler h) {
			mHandler = h;
		}

		@Override
		public void run() {
			mState = RUNNING;
			total = maxBarValue;
			while (mState == RUNNING) {
				// The method Thread.sleep throws an InterruptedException if
				// Thread.interrupt()
				// were to be issued while thread is sleeping; the exception
				// must be caught.
				try {
					// Control speed of update (but precision of delay not
					// guaranteed)
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					Log.e("ERROR", "Thread was Interrupted");
				}

				// Send message (with current value of total as data) to Handler
				// on UI thread
				// so that it can update the progress bar.

				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putInt("total", total);
				msg.setData(b);
				mHandler.sendMessage(msg);

				total--; // Count down
			}
		}

		// Set current state of thread (use state=ProgressThread.DONE to stop
		// thread)
		public void setState(int state) {
			mState = state;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

