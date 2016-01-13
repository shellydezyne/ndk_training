LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
#LOCAL_CPP_FEATURES += rtti
#LOCAL_CPPFLAGS += -frtti
LOCAL_MODULE    := HelloJNI
#LOCAL_CPPFLAGS := -I/Users/dheerajkaushik/Projects/ndktest2_prebuilt_dynamic_final/openssl/include

#LOCAL_LDFLAGS := -L/Users/dheerajkaushik/Projects/ndktest2_prebuilt_dynamic_final/openssl/$(TARGET_ARCH_ABI)/
#LOCAL_LDLIBS := -lcrypto
LOCAL_SRC_FILES := HelloJNI.cpp
### define dependency on the other library
LOCAL_SHARED_LIBRARIES := openssl

include $(BUILD_SHARED_LIBRARY)
$(call import-add-path,$(LOCAL_PATH)/../../../../.)
$(call import-module,openssl)