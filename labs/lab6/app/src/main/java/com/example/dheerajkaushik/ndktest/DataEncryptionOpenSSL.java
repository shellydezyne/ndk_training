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

import android.util.Log;

public class DataEncryptionOpenSSL implements DataEncryption {
	private String TAG = "DataEncryptionOpenSSL";
	
	public DataEncryptionOpenSSL() {
		mBlocksize = 128;
	}
	
	@Override
	public long encryptFile(String encFilepath, String origFilepath) {
		int ret = -1;
		File fileIn = new File(origFilepath);
        long start, stop, seconds;

        Log.d(TAG, "OpenSSL: Encrypting file:" + origFilepath);

        if (fileIn.isFile()) {            
    		// capture time it takes to encrypt file
			start = System.nanoTime();
	        ret = encodeFileFromJNI(encFilepath, origFilepath);
			stop = System.nanoTime();
			seconds = (stop - start) / 1000000;// for milliseconds
        } else {
        	Log.d(TAG, "ERROR*** File does not exist:" + origFilepath);
        	seconds = -1;
        }
        
        if (ret == -1) {
        	throw new IllegalArgumentException("encrypt file execution did not succeed.");
        }
        
		return seconds;
	}

	@Override
	public long decryptFile(String decFilepath, String encFilepath) {
		int ret = -1;
		File fileIn = new File(encFilepath);
        long start, stop, seconds;

        Log.d(TAG, "OpenSSL: Decrypting file:" + encFilepath);

        if (fileIn.isFile()) {    
    		// capture time it takes to decrypt file
			start = System.nanoTime();
	        ret = decodeFileFromJNI(decFilepath, encFilepath);
			stop = System.nanoTime();
			seconds = (stop - start) / 1000000;// for milliseconds
        } else {
        	Log.d(TAG, "ERROR*** File does not exist:" + encFilepath);
        	seconds = -1;
        }
        
        if (ret == -1) {
        	throw new IllegalArgumentException("decrypt file execution did not succeed.");
        }
		
		return seconds;
	}

	/* native function available from encodeFile library */
    public native int encodeFileFromJNI(String fileOut, String fileIn);
    public native int decodeFileFromJNI(String fileOut, String fileIn);
    public native void setBlocksizeFromJNI(int blocksize);
    public native byte[] generateKeyFromJNI(int keysize);
    
    /*
     * this is used to load encodeFile library on application startup.
     * The library has already been unpacked into /data/data/com.example.openssldataencryption/lib/libencodeFile.so
     * at installation time by the package manager.
     */
    static {
    	//System.loadLibrary("crypto");
    	System.loadLibrary("HelloJNI");
    }

	@Override
	public void setBlocksize(int blocksize) {
		mBlocksize = blocksize;
		setBlocksizeFromJNI(blocksize);
	}
	
	
	private int mBlocksize;
}
