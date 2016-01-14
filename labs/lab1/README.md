1. Add native functions to the Main Activity

   public native String HelloJNI();

2. Build the project

  static {
        System.loadLibrary("lab1JNI");

    }

2. Create headers
javah -d jni -classpath "/Applications/adt-bundle-mac-x86_64-20140702/sdk/platforms/android-21/android.jar:/Applications/adt-bundle-mac-x86_64-20140702/sdk/extras/android/support/v7/appcompat/libs/android-support-v7-appcompat.jar:../../build/intermediates/classes/debug:/Applications/adt-bundle-mac-x86_64-20140702/sdk/extras/android/support/v7/appcompat/libs/android-support-v4.jar"  com.example.dheerajkaushik.lab1.MainActivity

3. Create HelloJNI.c file
with the following Contents

JNIEXPORT jstring JNICALL Java_com_example_dheerajkaushik_lab1_MainActivity_HelloJNI
  (JNIEnv *env, jobject jobj){
  return (*env)->NewStringUTF(env,"Hello from jni");
}

4. Add Android NDK path in local.properties file
ndk.dir=/Applications/android-ndk-r10e

5. Edit app:build.gradle file
import com.android.build.gradle.tasks.NdkCompile

apply plugin: 'com.android.application'

android {
    compileSdkVersion 23
    buildToolsVersion "23.0.2"

    defaultConfig {
        applicationId "com.example.dheerajkaushik.lab1"
        minSdkVersion 21
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"
        ndk {
            moduleName "lab1JNI"
            ldLibs "log"
            stl "gnustl_static"
            //cFlags "-I${openssl_path}/include"
        }
    }
    sourceSets.main {
         jni.srcDirs = []
         jniLibs.srcDir 'src/main/libs' //set .so files location to libs
    }

    tasks.withType(NdkCompile) { // disable automatic ndk-build call
    compileTask -> compileTask.enabled = false
    }
    productFlavors{
        arm{
            ndk{
                abiFilter "armeabi-v7a"
                //versionCode 1
            }
        }
        x86 {
        ndk{
              abiFilter "x86"
        }
        }
        fat
    }
    project.ext.versionCodes = ['armeabi-v7a':1,'x86':2]
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:23.1.1'
}


6. Add android.useDeprecatedNdk=true in gradle.propoerties

7. Add android.mk file

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
#LOCAL_CPP_FEATURES += rtti
#LOCAL_CPPFLAGS += -frtti
LOCAL_MODULE    := lab1JNI
#LOCAL_CPPFLAGS := -I/Users/dheerajkaushik/Projects/ndktest2_prebuilt_dynamic_final/openssl/include

#LOCAL_LDFLAGS := -L/Users/dheerajkaushik/Projects/ndktest2_prebuilt_dynamic_final/openssl/$(TARGET_ARCH_ABI)/
#LOCAL_LDLIBS := -lcrypto
LOCAL_LDLIBS := -llog
LOCAL_SRC_FILES := HelloJNI.c

include $(BUILD_SHARED_LIBRARY)

http://developer.android.com/ndk/guides/android_mk.html

8. Add application.mk
LOCAL_PATH := $(call my-dir)
#APP_ABI:= all armeabi armeabi-v7a arm64-v8a x86 x86_64 mips mips64
APP_ABI:= armeabi-v7a
#APP_CPPFLAGS += -frtti
APP_OPTIM=debug //put to release(default) in Release build
#APP_CFLAGS += -Isources/bar
#APP_CFLAGS += -I$(LOCAL_PATH)/../bar
#APP_CPPFLAGS
#APP_LDFLAGS
#APP_PLATFORM = android-3 android-4
Go to further reference
http://developer.android.com/ndk/guides/stable_apis.html

Go to the following for further reference:

http://developer.android.com/ndk/guides/application_mk.html

#APP_STL
http://developer.android.com/ndk/guides/cpp-support.html#runtimes

#NDK_TOOLCHAIN_VERSION = 4.9 or 4.8

9. Go to Terminal
$cd app/src/main/
$ndk-build

Go to
http://developer.android.com/ndk/guides/ndk-build.html
