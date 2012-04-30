#include <jni.h>
#include "android_api/android_api.h"
#include "android_api/android_audioformat.h"
#include "android_api/android_audiomanager.h"

#ifndef _Included_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI
#define _Included_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI
#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT void JNICALL Java_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI_initialize(JNIEnv *env, jobject obj) {
  jint MONO = 0;
  jint PCM16BIT = 0;
  showLog("init start a");
  AudioFormatClassObject clazz = newAudioFormatClass(env);

  showLog("init --1--"); 
  MONO = CHANNEL_OUT_MONO(env, clazz);

  showLog("init --2--");
  PCM16BIT = ENCODING_PCM_16BIT(env, clazz);
  
  showLog("init --3--");
  destroyAudioFormatClass(env, clazz);
  showLog("init end");
  {
  char buffer[256];
  snprintf(&buffer[0],256,"A=%d,B=%d",MONO,PCM16BIT);
  showLog(buffer);
  showLog("init end");
  }
 
  { 
  int a = 0;
  char buffer[256];
  AudioManagerClassObject clazzA = newAudioManagerClass(env);
  a = STREAM_MUSIC(env, clazzA);
  destroyAudioManagerClassObject(env,clazzA);

  snprintf(&buffer[0],256,"C=%d",a);
  showLog(buffer);
  
  }
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
