LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := openssl

MY_LIBRARY_NAME := crypto_1_0_0

### export include path
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/include

### path to library
LOCAL_SRC_FILES := lib/$(TARGET_ARCH_ABI)/lib$(MY_LIBRARY_NAME).so

### export dependency on the library
LOCAL_EXPORT_LDLIBS := -L$(LOCAL_PATH)/lib/$(TARGET_ARCH_ABI)/
LOCAL_EXPORT_LDLIBS += -l$(MY_LIBRARY_NAME)

include $(PREBUILT_SHARED_LIBRARY)

