#include "com_example_dheerajkaushik_ndktest_MainActivity.h"
/* Header for class com_example_dheerajkaushik_ndktest_MainActivity */

/*
 * Class:     com_example_dheerajkaushik_ndktest_MainActivity
 * Method:    HelloJNI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_dheerajkaushik_ndktest_MainActivity_HelloJNI
  (JNIEnv *env, jobject jobj)
{
  return (*env)->NewStringUTF(env,"Hello from jni");
}
