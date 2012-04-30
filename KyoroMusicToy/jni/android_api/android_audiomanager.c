#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include "android_api/android_api.h"
#include "android_api/android_audiomanager.h"

//#ifndef H_AUDIOMANAGER_ANDROID_API
//#define H_AUDIOMANAGER_ANDROID_API
#ifdef __cplusplus
extern "C" {
#endif

AudioManagerClassObject newAudioManagerClass(JNIEnv *env) {
	AudioManagerClassObject obj = (AudioManagerClassObject) malloc(
			sizeof(AudioManagerClass));
	obj->mainClass = (*env)->FindClass(env, "android/media/AudioManager");
	obj->STREAM_MUSIC = (*env)->GetStaticFieldID(env, obj->mainClass,
			"STREAM_MUSIC", "I");
	return obj;
}

void destroyAudioManagerClassObject(JNIEnv *env, AudioManagerClassObject object) {
	free(object);
}

jint STREAM_MUSIC(JNIEnv *env, AudioManagerClassObject obj) {
	jint ret =
			(*env)->GetStaticIntField(env, obj->mainClass, obj->STREAM_MUSIC);
	return ret;
}

#ifdef __cplusplus
}
#endif
//#endif

