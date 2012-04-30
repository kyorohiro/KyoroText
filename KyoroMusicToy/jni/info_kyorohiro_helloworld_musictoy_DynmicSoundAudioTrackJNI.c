#include <jni.h>
#include "android_api/android_api.h"

#ifndef _Included_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI
#define _Included_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI_initialize(JNIEnv *env, jobject obj) {
  showLog("init");
}

JNIEXPORT void JNICALL Java_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI_finalize(JNIEnv *env, jobject obj) {
  showLog("finalize");
}

JNIEXPORT void JNICALL Java_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI_start(JNIEnv *env, jobject obj){
  showLog("start");
}

JNIEXPORT void JNICALL Java_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI_stop(JNIEnv *env, jobject obj) {
  showLog("stop");
}

#ifdef __cplusplus
}
#endif
#endif
