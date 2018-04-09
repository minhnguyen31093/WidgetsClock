LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := JniBitmapOperationsLibrary
LOCAL_CFLAGS := -DANDROID_NDK_HOME
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \
	-ljnigraphics \

LOCAL_SRC_FILES := \
	/Users/minh/Android/my_sample/KingdomHeartsClock/JniBitmapOperationsLibrary/jni/Android.mk \
	/Users/minh/Android/my_sample/KingdomHeartsClock/JniBitmapOperationsLibrary/jni/Application.mk \
	/Users/minh/Android/my_sample/KingdomHeartsClock/JniBitmapOperationsLibrary/jni/do_not_delete_me_i_am_workaround.c \
	/Users/minh/Android/my_sample/KingdomHeartsClock/JniBitmapOperationsLibrary/jni/JniBitmapOperationsLibrary.cpp \

LOCAL_C_INCLUDES += /Users/minh/Android/my_sample/KingdomHeartsClock/JniBitmapOperationsLibrary/jni
LOCAL_C_INCLUDES += /Users/minh/Android/my_sample/KingdomHeartsClock/JniBitmapOperationsLibrary/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
