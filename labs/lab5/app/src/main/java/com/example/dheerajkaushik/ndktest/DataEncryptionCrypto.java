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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import android.util.Log;

public class DataEncryptionCrypto implements DataEncryption {
	private String TAG= "DataEncryptionCrypto";
	private int mBlocksize;
	private SecretKey mKey;
	
	public DataEncryptionCrypto() {
		mBlocksize = 128;
		KeyGenerator kgen;
		mKey = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			mKey = kgen.generateKey();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public long encryptFile(String encFilepath,String origFilepath) {
		long start, stop, seconds;
		seconds = -1;
		
		try{

		Log.d(TAG, "Crypto: Encrypting file:" + origFilepath);
		
		// open stream to read origFilepath. We are going to save encrypted contents to outfile
		InputStream fis = new FileInputStream(origFilepath);		
		File outfile = new File(encFilepath);
		int read = 0;
		if (!outfile.exists())
			outfile.createNewFile();
		
		FileOutputStream encfos = new FileOutputStream(outfile);
		// Create Cipher using "AES" provider
		Cipher encipher = Cipher.getInstance("AES");
		encipher.init(Cipher.ENCRYPT_MODE, mKey);
		CipherOutputStream cos = new CipherOutputStream(encfos, encipher);
		
		// capture time it takes to encrypt file
		start = System.nanoTime();
		Log.d(TAG, String.valueOf(start));
		
		byte[] block = new byte[mBlocksize];
		
		while ((read = fis.read(block,0,mBlocksize)) != -1) {
			cos.write(block,0, read);
		}
		cos.close();
		stop = System.nanoTime();
		
		Log.d(TAG, String.valueOf(stop));
		seconds = (stop - start) / 1000000;// for milliseconds
		Log.d(TAG, String.valueOf(seconds));
		
		fis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		
		return seconds;
	}

	@Override
	public long decryptFile(String decFilepath, String encFilepath) {
		long start, stop, seconds;
		seconds = -1;
		
		try{
		Log.d(TAG, "Crypto: Decrypting file:" + encFilepath);
		
		// open stream to read encFilepath. We are going to save decrypted contents to outfile
		InputStream fis = new FileInputStream(encFilepath);
		File outfile = new File(decFilepath);
		int read = 0;
		if (!outfile.exists())
			outfile.createNewFile();
		
		FileOutputStream decfos = new FileOutputStream(outfile);
		// Create Cipher using "AES" provider
		Cipher decipher = Cipher.getInstance("AES");
		decipher.init(Cipher.DECRYPT_MODE, mKey);
		CipherInputStream cis = new CipherInputStream(fis, decipher);
		
		// capture time it takes to decrypt file
		start = System.nanoTime();
		Log.d(TAG, String.valueOf(start));
		
		byte[] block = new byte[mBlocksize];
		
		while ((read = cis.read(block,0,mBlocksize)) != -1) {
			decfos.write(block,0, read);
		}		
		cis.close();
		stop = System.nanoTime();
		
		Log.d(TAG, String.valueOf(stop));
		seconds = (stop - start) / 1000000;// for milliseconds
		Log.d(TAG, String.valueOf(seconds));

		decfos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		
		return seconds;	
		
	}

	@Override
	public void setBlocksize(int blocksize) {
		mBlocksize = blocksize;
	}
	
}
