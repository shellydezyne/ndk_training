export NDK=/Applications/android-ndk-r10e
export TOOL=arm-linux-androideabi
export TOOLCHAIN_PATH=/Applications/android-ndk-r10e/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin/
export NDK_TOOLCHAIN_BASENAME=${TOOLCHAIN_PATH}/${TOOL}
export CC=$NDK_TOOLCHAIN_BASE-gcc
export CXX=$NDK_TOOLCHAIN_BASENAME-g++
export LINK=$CXX
export LD=$NDK_TOOLCHAIN_BASENAME-ld

export AR=$NDK_TOOLCHAIN_BASENAME-ar
export STRIP=$NDK_TOOLCHAIN_BASENAME-strip
export ARCH_FLAGS=”-march=armv7-a –mfloat-abi=softfp –mfpu=vfpv3-d16”
export ARCH_LINK=”-march=armv7-a –Wl, --flx-cortex-a”
export CPPFLAGS=”${ARCH_FLAGS} –fpic –ffunction-sections –funwind-tables –fstack-protector –fno-strict-aliasing –finline-limited=64”
export LDFLAGS=$ARCH_LINK
export CXXFLAGS=”$ARCH_FLAGS –fpic –ffunction-sections –funwind-tables –fstack-protector –fno-strict-aliasing –finline-limited=64 –frtti –fexceptions”


