In the build.gradle file
change
classpath 'com.android.tools.build:gradle:1.5.0’
to
classpath 'com.android.tools.build:gradle-experimental:0.4.0’

http://tools.android.com/tech-docs/new-build-system/gradle-experimental#TOC-Known-Issues
apply plugin: 'com.android.model.application'

model {
    android {
        compileSdkVersion = 23
        buildToolsVersion = "23.0.2"

        defaultConfig.with {
            applicationId = "com.example.dheerajkaushik.ndktest"
            minSdkVersion.apiLevel = 21
            targetSdkVersion.apiLevel = 23
            versionCode = 1
            versionName = "1.0"
            buildConfigFields.with {
                create() {
                    type = "int"
                    name = "VALUE"
                    value = "1"
                }
            }
        }

    }
    android.buildTypes {
        release {
            minifyEnabled = false
            proguardFiles.add(file('proguard-rules.pro'))
        }
    }
    compileOptions.with {
        sourceCompatibility=JavaVersion.VERSION_1_7
        targetCompatibility=JavaVersion.VERSION_1_7
    }

    /*
     * native build settings
     */
    android.ndk {
        moduleName = "HelloJNI"
        /*
         * Other ndk flags configurable here are
         * cppFlags.add("-fno-rtti")
         * cppFlags.add("-fno-exceptions")
         * ldLibs.addAll(["android", "log"])
         * stl       = "system"
         */
    }

    android.productFlavors {
        // for detailed abiFilter descriptions, refer to "Supported ABIs" @
        // https://developer.android.com/ndk/guides/abis.html#sa
        create("arm") {
            ndk.abiFilters.add("armeabi")
        }
        create("arm7") {
            ndk.abiFilters.add("armeabi-v7a")
        }
        create("arm8") {
            ndk.abiFilters.add("arm64-v8a")
        }
        create("x86") {
            ndk.abiFilters.add("x86")
        }
        create("x86-64") {
            ndk.abiFilters.add("x86_64")
        }
        create("mips") {
            ndk.abiFilters.add("mips")
        }
        create("mips-64") {
            ndk.abiFilters.add("mips64")
        }
        // To include all cpu architectures, leaves abiFilters empty
        create("all")
    }

}
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:23.1.1'
}
