#include "com_example_dheerajkaushik_lab1_MainActivity.h"
/*
 * Class:     com_example_dheerajkaushik_lab1_MainActivity
 * Method:    HelloJNI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_dheerajkaushik_lab1_MainActivity_HelloJNI
  (JNIEnv *env, jobject jobj){
  return (*env)->NewStringUTF(env,"Hello from jni");
}

