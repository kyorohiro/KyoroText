#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include "android_api/android_api.h"
#include "android_api/android_audioformat.h"
//#ifndef H_AUDIOFORMAT_ANDROID_API
//#define H_AUDIOFORMAT_ANDROID_API
#ifdef __cplusplus
extern "C" {
#endif


AudioFormatClassObject newAudioFormatClass(JNIEnv *env) {
    AudioFormatClassObject obj = (AudioFormatClassObject)malloc(sizeof(AudioFormatClass));
    obj->mainClass = (*env)->FindClass(env, "android/media/AudioFormat");
    obj->CHANNEL_OUT_MONO = (*env)->GetStaticFieldID(env, obj->mainClass, "CHANNEL_OUT_MONO", "I");
    obj->ENCODING_PCM_16BIT = (*env)->GetStaticFieldID(env, obj->mainClass, "ENCODING_PCM_16BIT", "I");
    return obj;
}

void destroyAudioFormatClass(JNIEnv *env, AudioFormatClassObject object) {
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
//#endif

