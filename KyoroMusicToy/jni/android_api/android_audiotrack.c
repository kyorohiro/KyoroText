#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include "android_api/android_api.h"
#include "android_api/android_audiotrack.h"

//#ifndef H_AudioTrackClass_ANDROID_API
//#define H_AudioTrackClass_ANDROID_API
#ifdef __cplusplus
extern "C" {
#endif

void newAudioTrackClassSuper(JNIEnv *env, AudioTrackClassObject obj)
{
    obj->mainClass = (*env)->FindClass(env, "android/media/AudioTrack");
    obj->constructor = (*env)->GetMethodID(env, obj->mainClass, "<init>", "(IIIIII)V");
    obj->play = (*env)->GetMethodID(env, obj->mainClass, "play", "()V");
    obj->write = (*env)->GetMethodID(env, obj->mainClass, "write", "([SII)I");
    obj->stop = (*env)->GetMethodID(env, obj->mainClass, "stop", "()V");
    obj->getMinBufferSize = (*env)->GetStaticMethodID(env, obj->mainClass, "getMinBufferSize", "(III)I");
    obj->release = (*env)->GetMethodID(env, obj->mainClass, "release", "()V");
	obj->MODE_STREAM = (*env)->GetStaticFieldID(env, obj->mainClass, "MODE_STREAM", "I");
}

AudioTrackClassObject newAudioTrackClass(JNIEnv *env) {
    AudioTrackClassObject obj = (AudioTrackClassObject)malloc(sizeof(AudioTrackClass));
    newAudioTrackClassSuper(env, obj);
    return obj;
}

void destroyAudioTrackClassObject(JNIEnv *env, AudioTrackClassObject object)
{
    free(object);
}

jint MODE_STREAM(JNIEnv *env, AudioTrackClassObject obj){
	jint ret = (*env)->GetStaticIntField(env, obj->mainClass, obj->MODE_STREAM);
	return ret;
}
//
//
jobject constructor(JNIEnv *env, AudioTrackClassObject object,
		jint streamType, jint sampleRateInHz, jint channelConfig,
		jint audioFormat, jint bufferSizeInBytes, jint mode)
{
		jobject ret = (jobject)(*env)->NewObject(
			env, object->mainClass, object->constructor,
			streamType, sampleRateInHz, channelConfig,
			audioFormat, bufferSizeInBytes, mode);
		return ret;
}


AudioTrackObject newAudioTrack(JNIEnv *env,
		jint streamType, jint sampleRateInHz, jint channelConfig,
		jint audioFormat, jint bufferSizeInBytes, jint mode)
{
    AudioTrackObject obj = (AudioTrackObject)malloc(sizeof(AudioTrack));
    newAudioTrackClassSuper(env, &obj->base);
    obj->object = constructor(env,&obj->base,
    		streamType, sampleRateInHz, channelConfig,
    		audioFormat, bufferSizeInBytes, mode);
    return obj;
}

void destroyAudioTrack(JNIEnv *env, AudioTrackObject object)
{
    free(object);
}

void play(JNIEnv *env, AudioTrackObject object)
{
	(*env)->CallVoidMethod(env, object->object, ((AudioTrackClassObject)object)->play);
}

void stop(JNIEnv *env, AudioTrackObject object)
{
	(*env)->CallVoidMethod(env, object->object, ((AudioTrackClassObject)object)->stop, NULL);
}

void release(JNIEnv *env, AudioTrackObject object)
{
	(*env)->CallVoidMethod(env, object->object, ((AudioTrackClassObject)object)->release, NULL);
}

void write(JNIEnv *env, AudioTrackObject object,
		jshortArray audioData, int offsetInShorts, int sizeInShorts)
{
	(*env)->CallIntMethod(env, object->object, ((AudioTrackClassObject)object)->write,
			 audioData, offsetInShorts, sizeInShorts);
}

jint getMinBufferSize(JNIEnv *env, AudioTrackClassObject object,
		int sampleRateInHz, int channelConfig, int audioFormat)
{
	jint ret = (jint)(*env)->CallStaticObjectMethod(
			env, ((AudioTrackClassObject)object)->mainClass,
			((AudioTrackClassObject)object)->getMinBufferSize,
			sampleRateInHz, channelConfig, audioFormat);
	return ret;
}

#ifdef __cplusplus
}
#endif
//#endif

