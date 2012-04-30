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
  jfield STREAM_MUSIC;
} AudioManager ;

typedef AudioManagerClass *AudioManagerClassObject;

AudioManagerClassObject newAudioManagerClass(JNIEnv *env) {
    AudioManagerClassObject obj = (AudioManagerClassObject)malloc(sizeof(AudioManagerClass));
    obj->mainClass = (*env)->FindClass(env, "android/media/AudioManagerClass");
    obj->STREAM_MUSIC (*env)->GetStaticFieldID(env, obj->mainClass, "STREAM_MUSIC", "I");
    return obj;
}

void destroyAudioManagerClassObject(JNIEnv *env, AudioManagerClassObject object) {
	(*env)->DeleteGlobalRef(env, object->buffer);
	(*env)->DeleteGlobalRef(env, object->object);
    free(object);
}

jint STREAM_MUSIC(JNIEnv *env, AudioManagerClassObject obj) {
	jint ret = (*env)->GetStaticIntField(env, obj->mainClass, obj->STREAM_MUSIC);
	return ret;
}



#ifdef __cplusplus
}
#endif
#endif

