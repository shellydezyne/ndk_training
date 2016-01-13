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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	private String TAG = "Preferences";

	public static String CIPHER_AES128 = "0";
	public static String CIPHER_AES256 = "1";
	
	public static String MODE_CBC = "0";

	public static String BLOCKSIZE_16 = "0";
	public static String BLOCKSIZE_64 = "1";
	public static String BLOCKSIZE_256 = "2";
	public static String BLOCKSIZE_1024 = "3";
	public static String BLOCKSIZE_8192 = "4";
	
	public static String KEYSIZE_1024 = "0";
	public static String KEYSIZE_2048 = "1";

	public static String FILE_MPEG4  = "0";
	
	public static String IMPLEMENTATION_CRYPTO = "0";
	public static String IMPLEMENTATION_OPENSSL = "1";
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		
		//mCipherList = (ListPreference) findPreference("cipher");
		//mModeList = (ListPreference) findPreference("mode");
		mBlocksizeList = (ListPreference) findPreference("blocksize");
		//mKeysizeList = (ListPreference) findPreference("keysize");
		//mFileList = (ListPreference) findPreference("file");
		mImplementationList = (ListPreference) findPreference("implementation");
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		
		// Initialize preferences summaries with the current values from keys:
		String value;
		/*
		value = sharedPref.getString("cipher", "-1");
		mCipher = Preferences.cipherPrefToString(value);
		mCipherList.setSummary(mCipher);
		value = sharedPref.getString("mode", "-1");
		mMode = Preferences.modePrefToString(value);
		mModeList.setSummary(mMode);
		*/
		value = sharedPref.getString("blocksize", "-1");
		mBlocksize = Preferences.blocksizePrefToString(value);
		mBlocksizeList.setSummary(mBlocksize);
		/*
		value = sharedPref.getString("keysize", "-1");
		mKeysize = Preferences.keysizePrefToString(value);
		mKeysizeList.setSummary(mKeysize);
		value = sharedPref.getString("file","-1");
		mFile = Preferences.filePrefToString(value);
		mFileList.setSummary(mFile);
		*/
		value = sharedPref.getString("implementation","-1");
		mImplementation = Preferences.implementationPrefToString(value);
		mImplementationList.setSummary(mImplementation);
				
		Log.d(TAG, toString());
	}

    public String toString() {
    	
    	//return mCipher + " " + mMode + " " + mBlocksize + " " + mKeysize + " " + mFile + " " + mImplementation;
    	return mBlocksize + " " + mImplementation;
    }
    
    //private String mCipher;
    //private String mMode;
    private String mBlocksize;
    //private String mKeysize;
    //private String mFile;
    private String mImplementation;
    
    //private ListPreference mCipherList;
    //private ListPreference mModeList;
    private ListPreference mBlocksizeList;
    //private ListPreference mKeysizeList;
    //private ListPreference mFileList;
    private ListPreference mImplementationList;
	
    @Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
    	String value;
    	/*
    	if (key.equals("cipher")) {
    		value = sharedPref.getString("cipher","");
    		mCipher = Preferences.cipherPrefToString(value);
    		mCipherList.setSummary( mCipher );
    	} else if (key.equals("mode")) {
    		value = sharedPref.getString("mode","");
    		mMode = Preferences.modePrefToString(value);
    		mModeList.setSummary(mMode);
    	} else */
    	if (key.equals("blocksize")) {
    		value = sharedPref.getString("blocksize","");
    		mBlocksize = Preferences.blocksizePrefToString(value);
    		mBlocksizeList.setSummary(mBlocksize);
    	/*} else if (key.equals("keysize")) {
    		value = sharedPref.getString("keysize","");
    		mKeysize = Preferences.keysizePrefToString(value);
    		mKeysizeList.setSummary(mKeysize);
    	} else if (key.equals("file")) {
    		value = sharedPref.getString("file", "");
    		mFile = Preferences.filePrefToString(value);
    		mFileList.setSummary(mFile);
    	} */
    	} else if (key.equals("implementation")) {
    		value = sharedPref.getString("implementation","");
    		mImplementation = Preferences.implementationPrefToString(value);
    		mImplementationList.setSummary(mImplementation);
    	} else {
    		//mCipher = "ERRORERROR";
    	}
    	Log.d(TAG,toString());
	}
    
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }   
    
    /*
     *  given the array's return value "0", "1", "2", etc...
     *  return the preference string
     */
    public static String cipherPrefToString(String pref) {
    	String str;
		if (pref.equals(CIPHER_AES128)) {
			str = "AES-128";
		} else if (pref.equals(CIPHER_AES256)) {
			str = "AES-256";
		} else {
			str = "ERROR";
		}
		return str;
    }
    
    public static String blocksizePrefToString(String pref) {
    	String str;
    	if (pref.equals(BLOCKSIZE_16)) {
			str = "16";
		} else if (pref.equals(BLOCKSIZE_64)) {
			str = "64";
		} else if (pref.equals(BLOCKSIZE_256)) {
			str = "256";
		} else if (pref.equals(BLOCKSIZE_1024)) {
			str = "1024";
		} else if (pref.equals(BLOCKSIZE_8192)) {
			str = "8192";
		} else {
			str = "ERROR";
		}
    	return str;
    }
    
    public static String keysizePrefToString(String pref) {
    	String str;
    	if (pref.equals(KEYSIZE_1024)) {
			str = "1024";
		} else if (pref.equals(KEYSIZE_2048)) {
			str = "2048";
		} else {
			str = "ERROR";
		}
    	return str;
    }
    
    public static String modePrefToString(String pref) {
    	String str;
    	if (pref.equals(MODE_CBC)) {
			str = "CBC";
		} else {
			str = "ERROR";
		}
    	return str;
    }
    
    public static String filePrefToString(String pref) {
    	String str;
    	if (pref.equals(FILE_MPEG4)) {
			str = "mpeg4.3gp - 1.66MB";
		} else {
			str = "ERROR";
		}
    	return str;
    }
    
    public static String implementationPrefToString(String pref) {
    	String str;
    	if (pref.equals(IMPLEMENTATION_CRYPTO)) {
			str = "javax.crypto(slow)";
		} else if (pref.equals(IMPLEMENTATION_OPENSSL)) {
			str = "OpenSSL(fast)";
		} else {
			str = "ERROR";
		}
    	return str;
    }
}

