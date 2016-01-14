Follow the instructions at

http://developer.android.com/ndk/guides/standalone_toolchain.html

https://wiki.openssl.org/index.php/Android
$ chmod a+x setenv-android.sh
$ . ./setenv-android.sh
$ cd openssl-1.0.1g/
$ perl -pi -e 's/install: all install_docs install_sw/install: install_docs install_sw/g' Makefile.org
$ ./config shared no-ssl2 no-ssl3 no-comp no-hw no-engine --openssldir=/usr/local/ssl/android-14/
$ make depend
$ make all
After make completes, verify libcrypto.a and libssl.a were built for the embedded architecture.
$ find . -name libcrypto.a
./libcrypto.a
$ readelf -h ./libcrypto.a | grep -i 'class\|machine' | head -2
  Class:                   ELF32
  Machine:                 ARM
sudo -E make install CC=$ANDROID_TOOLCHAIN/arm-linux-androideabi-gcc RANLIB=$ANDROID_TOOLCHAIN/arm-linux-androideabi-ranlib  
