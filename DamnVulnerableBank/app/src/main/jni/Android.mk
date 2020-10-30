LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := frida-check
LOCAL_SRC_FILES := frida-check.c

include $(BUILD_SHARED_LIBRARY)