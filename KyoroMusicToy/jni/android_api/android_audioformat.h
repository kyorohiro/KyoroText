#include <jni.h>
#include <stdlib.h>
#include <stdio.h>

#ifndef H_AUDIOFORMAT_ANDROID_API
#define H_AUDIOFORMAT_ANDROID_API
#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
  jclass mainClass;
  jfieldID CHANNEL_OUT_MONO;
  jfieldID ENCODING_PCM_16BIT;
} AudioFormatClass;
typedef AudioFormatClass *AudioFormatClassObject;

AudioFormatClassObject newAudioFormatClass(JNIEnv *env);
void destroyAudioFormatClass(JNIEnv *env, AudioFormatClassObject object);
jint CHANNEL_OUT_MONO(JNIEnv *env, AudioFormatClassObject obj);
jint ENCODING_PCM_16BIT(JNIEnv *env, AudioFormatClassObject obj);

#ifdef __cplusplus
}
#endif
#endif

