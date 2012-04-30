#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include "android_api/android_api.h"

#ifndef H_AUDIOFORMAT_ANDROID_API
#define H_AUDIOFORMAT_ANDROID_API
#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
  int framerate;
  jclass mainClass;
  jfield CHANNEL_OUT_MONO;
  jfield ENCODING_PCM_16BIT;
} AudioFormatClass;
typedef AudioFormatClass *AudioFormatClassObject;

AudioFormatClassObject newAudioFormatClass(JNIEnv *env) {
    AudioFormatClassObject obj = (AudioFormatClassObject)malloc(sizeof(AudioFormatClass));
    obj->mainClass = (*env)->FindClass(env, "android/media/AudioFormatClass");
    obj->CHANNEL_OUT_MONO = (*env)->GetStaticFieldID(env, obj->mainClass, "CHANNEL_OUT_MONO", "I");
    obj->ENCODING_PCM_16BIT = (*env)->GetStaticFieldID(env, obj->mainClass, "ENCODING_PCM_16BIT", "I");
    return obj;
}

void destroyAudioTrackObject(JNIEnv *env, AudioTrackObject object) {
	(*env)->DeleteGlobalRef(env, object->buffer);
	(*env)->DeleteGlobalRef(env, object->object);
    free(object);
}


jint CHANNEL_OUT_MONO(JNIEnv *env, AudioFormatClassObject obj) {
	jint ret = (*env)->GetStaticIntField(env, obj->mainClass, obj->CHANNEL_OUT_MONO);
	return ret;
}

jint ENCODING_PCM_16BIT(JNIEnv *env, AudioFormatClassObject obj) {
	jint ret = (*env)->GetStaticIntField(env, obj->mainClass, obj->ENCODING_PCM_16BIT);
	return ret;
}


#ifdef __cplusplus
}
#endif
#endif

