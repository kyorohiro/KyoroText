LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := audiotrack
LOCAL_SRC_FILES := \
    android_api/android_audiotrack.c \
    android_api/android_logcat.c \
    info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI.c \

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)

