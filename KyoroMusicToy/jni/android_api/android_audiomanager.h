#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include "android_api/android_api.h"

#ifndef H_AUDIOMANAGER_ANDROID_API
#define H_AUDIOMANAGER_ANDROID_API
#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
  jclass mainClass;
  jfieldID STREAM_MUSIC;
} AudioManagerClass;
typedef AudioManagerClass *AudioManagerClassObject;

AudioManagerClassObject newAudioManagerClass(JNIEnv *env);
void destroyAudioManagerClassObject(JNIEnv *env, AudioManagerClassObject object);
jint STREAM_MUSIC(JNIEnv *env, AudioManagerClassObject obj);

#ifdef __cplusplus
}
#endif
#endif

