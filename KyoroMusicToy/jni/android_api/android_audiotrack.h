#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include "android_api/android_api.h"

#ifndef H_AudioTrackClass_ANDROID_API
#define H_AudioTrackClass_ANDROID_API
#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
  jclass mainClass;
  jmethodID constructor;
  jmethodID play;
  jmethodID write;
  jmethodID stop;
  jmethodID release;
  jmethodID getMinBufferSize;
  jfieldID MODE_STREAM;
} AudioTrackClass ;
typedef AudioTrackClass *AudioTrackClassObject;

typedef struct {
  AudioTrackClass base;
  jobject object;
  int framerate;
  jshortArray buffer;
} AudioTrack;
typedef AudioTrack *AudioTrackObject;


void newAudioTrackClassSuper(JNIEnv *env, AudioTrackClassObject obj);
AudioTrackClassObject newAudioTrackClass(JNIEnv *env);
void destroyAudioTrackClassObject(JNIEnv *env, AudioTrackClassObject object);
jint MODE_STREAM(JNIEnv *env, AudioTrackClassObject obj);

jobject constructor(JNIEnv *env, AudioTrackClassObject object,
		jint streamType, jint sampleRateInHz, jint channelConfig,
		jint audioFormat, jint bufferSizeInBytes, jint mode);
AudioTrackObject newAudioTrack(JNIEnv *env,
		jint streamType, jint sampleRateInHz, jint channelConfig,
		jint audioFormat, jint bufferSizeInBytes, jint mode);
void destroyAudioTrack(JNIEnv *env, AudioTrackObject object);
void play(JNIEnv *env, AudioTrackObject object);
void stop(JNIEnv *env, AudioTrackObject object);
void release(JNIEnv *env, AudioTrackObject object);
void write(JNIEnv *env, AudioTrackObject object, jshortArray audioData, int offsetInShorts, int sizeInShorts);
jint getMinBufferSize(JNIEnv *env, AudioTrackClassObject object, int sampleRateInHz, int channelConfig, int audioFormat);
#ifdef __cplusplus
}
#endif
#endif

