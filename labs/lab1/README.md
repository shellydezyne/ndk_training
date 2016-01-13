1. Add native functions to the Main Activity

public native String HelloJNI();
    static {
        System.loadLibrary("HelloJNI");

    }

2. Create headers 
javah -d jni -classpath "/Applications/adt-bundle-mac-x86_64-20140702/sdk/platforms/android-21/android.jar:/Applications/adt-bundle-mac-x86_64-20140702/sdk/extras/android/support/v7/appcompat/libs/android-support-v7-appcompat.jar:../../build/intermediates/classes/debug:/Applications/adt-bundle-mac-x86_64-20140702/sdk/extras/android/support/v7/appcompat/libs/android-support-v4.jar"  com.example.dheerajkaushik.lab1.MainActivity

3. Create HelloJNI.c file
4. Add Android NDK path in local.properties file
5. Edit build.gradle file
6. Add android.useDeprecatedNdk=true in gradle.propoerties 
6. Add android.mk file
7. Add application.mk
