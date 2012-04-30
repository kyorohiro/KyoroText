#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include "android_api/android_api.h"

#ifndef H_AUDIOTRACK_ANDROID_API
#define H_AUDIOTRACK_ANDROID_API
#ifdef __cplusplus
extern "C" {
#endif

/*
typedef struct {
  int framerate;
  jclass mainClass;
  jmethodID constructor;
  jmethodID play;
  jmethodID write;
  jmethodID stop;
  jmethodID release;
  jmethodID getMinBufferSize;
  jshortArray buffer;
  jobject object;
} AudioTrack ;

typedef AudioTrack *AudioTrackObject;

void constructor(JNIEnv *env, AudioTrackObject object);
AudioTrackObject newAudioTrack(JNIEnv *env) {
    AudioTrackObject obj = (AudioTrackObject)malloc(sizeof(AudioTrack));
    obj->framerate = 44100;
    obj->mainClass = (*env)->FindClass(env, "android/media/AudioTrack");
    obj->constructor = (*env)->GetMethodID(env, obj->mainClass, "<init>", "(IIIIII)V");
    obj->play = (*env)->GetMethodID(env, obj->mainClass, "play", "()V");
    obj->write = (*env)->GetMethodID(env, obj->mainClass, "write", "([SII)I");
    obj->stop = (*env)->GetMethodID(env, obj->mainClass, "stop", "()V");
    obj->getMinBufferSize = (*env)->GetMethodID(env, obj->mainClass, "getMinBufferSize", "(III)I");
    obj->release = (*env)->GetMethodID(env, obj->mainClass, "release", "()V");
    obj->buffer = (*env)->NewShortArray(env, 1024);
    constructor(env, obj);
    return obj;
}

void constructor(JNIEnv *env, AudioTrackObject object) {
	//object->object = (*env)->NewObject(env, object->constructor);
}

void destroyAudioTrackObject(JNIEnv *env, AudioTrackObject object) {
	(*env)->DeleteGlobalRef(env, object->buffer);
	(*env)->DeleteGlobalRef(env, object->object);
    free(object);
}

void play(JNIEnv *env, AudioTrackObject object) {
	(*env)->CallVoidMethod(env, object->mainClass, object->play, NULL);
}

void stop(JNIEnv *env, AudioTrackObject object) {
	(*env)->CallVoidMethod(env, object->mainClass, object->stop, NULL);
}

void release(JNIEnv *env, AudioTrackObject object) {
	(*env)->CallVoidMethod(env, object->mainClass, object->release, NULL);
}

void write(JNIEnv *env, AudioTrackObject object, short buffer, int numOfSample) {
	(*env)->CallIntMethod(env, object->mainClass, object->write,
			NULL, 0, numOfSample);
}
*/
#ifdef __cplusplus
}
#endif
#endif

