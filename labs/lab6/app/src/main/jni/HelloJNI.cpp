//#include "com_example_dheerajkaushik_ndktest_MainActivity.h"


//#include <iostream>
//#include <fstream>
//#include <openssl/ssl.h>
//#include <openssl/aes.h>
#include <openssl/evp.h>
#include <openssl/rand.h>
#include <openssl/err.h>
//#include <android/log.h>

//#include <string>
#include <string.h>
#include <stdio.h>
#include <jni.h>


#define KEYSIZE 32 // 32 bytes, 256 bits
#define TAG "EncodeFile"
int aes_blocksize = 1024;
unsigned char cKeyBuffer[KEYSIZE/sizeof(unsigned char)];
unsigned char iv[] = "01234567890123456";
int opensslIsSeeded = 0;

#define DEBUG 0

int encodeFile(const char* filenameOut, const char* filenameIn); // function below
int decodeFile(const char* filenameOut, const char* filenameIn); // function below
void setBlocksize(int _blocksize); // function below


/*
 * generate a key
 */
/*
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {

	int seedbytes = 1024;

	memset(cKeyBuffer, 0, KEYSIZE );

	if (!opensslIsSeeded) {
		if (!RAND_load_file("/dev/urandom", seedbytes)) {
			//__android_log_print(ANDROID_LOG_ERROR, TAG, "Failed to seed OpenSSL RNG");
			return -1;
		}
		opensslIsSeeded = 1;
	}

	if (!RAND_bytes((unsigned char *)cKeyBuffer, KEYSIZE )) {
		//__android_log_print(ANDROID_LOG_ERROR, TAG, "Faled to create OpenSSSL random integers: %ul", ERR_get_error);
	}

	return 0;
}
*/

/*
 * JNI function call for MainActivity.java inside package:
 *     package com.example.openssldataencryption;
 *     jint - int
 */

//extern "C" JNIEXPORT jint JNICALL Java_com_example_openssldataencryption_MainActivity_encodeFileFromJNI(JNIEnv* env, jobject obj, jstring jFilename ) {
extern "C" JNIEXPORT jint JNICALL Java_com_example_dheerajkaushik_ndktest_DataEncryptionOpenSSL_encodeFileFromJNI(JNIEnv* env, jobject obj, jstring jFilenameOut, jstring jFilenameIn ) {
  int ret = 0;
  const char* filenameIn = env->GetStringUTFChars(jFilenameIn, 0);
  const char* filenameOut = env->GetStringUTFChars(jFilenameOut, 0);

  ret = encodeFile(filenameOut, filenameIn);

  // release
  env->ReleaseStringUTFChars(jFilenameIn, filenameIn);
  env->ReleaseStringUTFChars(jFilenameOut, filenameOut);

  return ret;
}

extern "C" JNIEXPORT jint JNICALL Java_com_example_dheerajkaushik_ndktest_DataEncryptionOpenSSL_decodeFileFromJNI(JNIEnv* env, jobject obj, jstring jFilenameOut, jstring jFilenameIn ) {
  int ret = 0;
  const char* filenameIn = env->GetStringUTFChars(jFilenameIn, 0);
  const char* filenameOut = env->GetStringUTFChars(jFilenameOut, 0);

  ret = decodeFile(filenameOut, filenameIn);

  // release
  env->ReleaseStringUTFChars(jFilenameIn, filenameIn);
  env->ReleaseStringUTFChars(jFilenameOut, filenameOut);

  return ret;
}

extern "C" JNIEXPORT void JNICALL Java_com_example_dheerajkaushik_ndktest_DataEncryptionOpenSSL_setBlocksizeFromJNI(JNIEnv* env, jobject obj, jint jBlocksizeIn ) {

  int blocksize = (int) jBlocksizeIn;
  setBlocksize(blocksize);

}


int encodeFile(const char* filenameOut, const char* filenameIn) {

  int ret = 0;
  int filenameInSize = strlen(filenameIn)*sizeof(char)+1;
  int filenameOutSize = strlen(filenameOut)*sizeof(char)+1;

  char filename[filenameInSize];
  char encFilename[filenameOutSize];

  // create key, if it's uninitialized
  int seedbytes = 1024;

  memset(cKeyBuffer, 0, KEYSIZE );

  if (!opensslIsSeeded) {
    if (!RAND_load_file("/dev/urandom", seedbytes)) {
      //__android_log_print(ANDROID_LOG_ERROR, TAG, "Failed to seed OpenSSL RNG");
      return -1;
    }
    opensslIsSeeded = 1;
  }

  if (!RAND_bytes((unsigned char *)cKeyBuffer, KEYSIZE )) {
    //__android_log_print(ANDROID_LOG_ERROR, TAG, "Faled to create OpenSSSL random integers: %ul", ERR_get_error);
  }

  strncpy(encFilename, filenameOut, filenameOutSize);
  encFilename[filenameOutSize-1]=0;
  strncpy(filename, filenameIn, filenameInSize);
  filename[filenameInSize-1]=0;

  EVP_CIPHER_CTX *e_ctx = EVP_CIPHER_CTX_new();

//    	unsigned char * key = new unsigned char[KEYSIZE];
//    	loadKey("key", key, KEYSIZE);
//    	unsigned char key[] = "01234567890123450123456789012345"; // 256-bit

  FILE *orig_file, *enc_file;

  printf ("filename: %s\n" ,filename );
  printf ("enc filename: %s\n" ,encFilename );
  orig_file = fopen( filename, "rb" );
  enc_file = fopen ( encFilename, "wb" );

  unsigned char *encData, *origData;
  int encData_len = 0;
  int len = 0;
  int bytesread = 0;

  /**
   * ENCRYPT
   */
  //if (!(EVP_EncryptInit_ex(e_ctx, EVP_aes_256_cbc(), NULL, key, iv ))) {
  if (!(EVP_EncryptInit_ex(e_ctx, EVP_aes_256_cbc(), NULL, cKeyBuffer, iv ))) {
    ret = -1;
    printf( "ERROR: EVP_ENCRYPTINIT_EX\n");
  }

  // go through file, and encrypt
  if ( orig_file != NULL ) {
    origData = new unsigned char[aes_blocksize];
    encData = new unsigned char[aes_blocksize+EVP_CIPHER_CTX_block_size(e_ctx)]; // potential for encryption to be 16 bytes longer than original

    printf( "Encoding file: %s\n", filename);

    bytesread = fread(origData, 1, aes_blocksize, orig_file);
    // read bytes from file, then send to cipher
    while ( bytesread ) {


      if (!(EVP_EncryptUpdate(e_ctx, encData, &len, origData, bytesread))) {
        ret = -1;
        printf( "ERROR: EVP_ENCRYPTUPDATE\n");
      }
      encData_len = len;

      fwrite(encData, 1, encData_len, enc_file );
      // read more bytes
      bytesread = fread(origData, 1, aes_blocksize, orig_file);
    }
    // last step encryption
    if (!(EVP_EncryptFinal_ex(e_ctx, encData, &len))) {
      ret = -1;
      printf( "ERROR: EVP_ENCRYPTFINAL_EX\n");
    }
    encData_len = len;

    fwrite(encData, 1, encData_len, enc_file );

    // free cipher
    EVP_CIPHER_CTX_free(e_ctx);

    // 	close files
    printf( "\t>>\n");

    fclose(orig_file);
    fclose(enc_file);
  } else {
    printf( "Unable to open files for encoding\n");
    ret = -1;
    return ret;
  }
  return ret;
}



int decodeFile(const char* filenameOut, const char* filenameIn) {

  int ret = 0;
  int filenameSizeIn = strlen(filenameIn)*sizeof(char)+1;
  int filenameSizeOut = strlen(filenameOut)*sizeof(char)+1;

  char encFilename[filenameSizeIn];
  char decFilename[filenameSizeOut];

  strncpy(encFilename, filenameIn, filenameSizeIn);
  encFilename[filenameSizeIn-1]=0;
  strncpy(decFilename, filenameOut, filenameSizeOut);
  decFilename[filenameSizeOut-1]=0;

  EVP_CIPHER_CTX *d_ctx = EVP_CIPHER_CTX_new();

//    	unsigned char * key = new unsigned char[KEYSIZE];
//    	loadKey("key", key, KEYSIZE);
//    	unsigned char key[] = "01234567890123450123456789012345"; // 256-bit
//    	unsigned char iv[] = "01234567890123456";

  FILE *enc_file, *dec_file;

  printf("dec filename: %s\n", decFilename);
  enc_file = fopen ( encFilename, "rb" );
  dec_file = fopen ( decFilename, "wb" );

  unsigned char *encData, *decData;
  int decData_len = 0;
  int len = 0;
  int bytesread = 0;

  /**
   * DECRYPT
   */

  //if (!( EVP_DecryptInit_ex(d_ctx, EVP_aes_256_cbc(), NULL, key, iv) )) {
  if (!( EVP_DecryptInit_ex(d_ctx, EVP_aes_256_cbc(), NULL, cKeyBuffer, iv) )) {
    ret = -1;
    printf("ERROR: EVP_DECRYPTINIT_EX\n");
  }

  // go through file, and decrypt
  if ( enc_file != NULL ) {
    encData = new unsigned char[aes_blocksize];
    decData = new unsigned char[aes_blocksize+EVP_CIPHER_CTX_block_size(d_ctx)]; // potential for output to be 16 bytes longer than original

    printf( "Decoding file: %s\n", decFilename);

    bytesread = fread(encData, 1, aes_blocksize, enc_file);
    // read bytes from file, then send to cipher
    while ( bytesread ) {


      if (!(EVP_DecryptUpdate(d_ctx, decData, &len, encData, bytesread ))) {
        ret = -1;
        printf( "ERROR: EVP_DECRYPTUPDATE\n");
      }
      decData_len = len;

      fwrite(decData, 1, decData_len, dec_file );
      // read more bytes
      bytesread = fread(encData, 1, aes_blocksize, enc_file);
    }
    // last step of decryption
    if (!(EVP_DecryptFinal_ex(d_ctx, decData, &len))) {
      ret = -1;
      printf( "ERROR: EVP_DECRYPTFINAL_EX\n");
    }
    decData_len = len;

    fwrite(decData, 1, decData_len, dec_file );

    // free cipher
    EVP_CIPHER_CTX_free(d_ctx);

    // close files
    printf( "\t>>\n");

    fclose(enc_file);
    fclose(dec_file);

  } else {
    printf( "Unable to open files for encoding\n");
    ret = -1;
    return ret;
  }

  return ret;
}


void setBlocksize(int _blocksize) {
  aes_blocksize = _blocksize;
}
/*
int main(int argc, char** argv) {
	int ret = 0;

	ret |= encodeFile("mpeg4-enc.3gp","mpeg4.3gp");
	ret |= decodeFile("mpeg4-dec.3gp","mpeg4-enc.3gp");
//	ret |= encodeFile("key-enc","key");
//	ret |= decodeFile("key-dec","key-enc");

	return ret;
}
*/

/* Header for class com_example_dheerajkaushik_ndktest_MainActivity */

/*
 * Class:     com_example_dheerajkaushik_ndktest_MainActivity
 * Method:    HelloJNI
 * Signature: ()Ljava/lang/String;

extern "C" JNIEXPORT jstring JNICALL Java_com_example_dheerajkaushik_ndktest_MainActivity_HelloJNI
        (JNIEnv *env, jobject jobj)
{
  env->NewStringUTF("Hello from jni");
}
 */

