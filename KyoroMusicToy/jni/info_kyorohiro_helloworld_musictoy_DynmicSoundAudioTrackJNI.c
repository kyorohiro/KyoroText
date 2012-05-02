#include <jni.h>
#include <math.h>
#include "android_api/android_api.h"
#include "android_api/android_audioformat.h"
#include "android_api/android_audiomanager.h"
#include "android_api/android_audiotrack.h"

#ifndef _Included_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI
#define _Included_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI
#ifdef __cplusplus
extern "C" {
#endif

	JNIEXPORT void JNICALL Java_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI_initialize(JNIEnv *env, jobject obj) {
		jint MONO = 0;
		jint PCM16BIT = 0;
		int _STREAM_MUSIC = 0;
		int _MODE_STREAM = 0;
		jint mbuff = 0;

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
			char buffer[256];
			AudioManagerClassObject clazzA = newAudioManagerClass(env);
			_STREAM_MUSIC = STREAM_MUSIC(env, clazzA);
			destroyAudioManagerClassObject(env,clazzA);

			snprintf(&buffer[0],256,"C=%d",_STREAM_MUSIC);
			showLog(buffer);
		}

		{
			//	  AudioTrackObject object = newAudioTrack(
			//			  env, STREAM_MUSIC, 44100,)
			char buffer[256];
			AudioTrackClassObject obj = newAudioTrackClass(env);
			mbuff = getMinBufferSize(env, obj,
			//8000,MONO, PCM16BIT);
			44100, MONO, PCM16BIT);
			_MODE_STREAM = MODE_STREAM(env, obj);

			destroyAudioTrackClassObject(env, obj);
			snprintf(&buffer[0],256,"MIN=%d",mbuff);
			showLog(buffer);
		}
		{
			jshortArray buffer = (*env)->NewShortArray(env, mbuff);
			short sb[mbuff];
			int i=0;
			double PI = 3.14;
			AudioTrackObject ob;
			for(i=0;i<mbuff;i++){
				sb[i] = 100.0*sin(i*2*PI*440.0/44100.0);
			}
			showLog("new");
			ob = newAudioTrack(env,
					_STREAM_MUSIC,44100, MONO,PCM16BIT,
					mbuff, _MODE_STREAM);
			showLog("/new");
			//
			//
			//
			jshort *jbuf = (*env)->GetPrimitiveArrayCritical(env,buffer, NULL);
			memcpy(jbuf,sb,sizeof(short)*mbuff);
			(*env)->ReleasePrimitiveArrayCritical(env,buffer,jbuf, 0);
			showLog("play");
			play(env, ob);
			showLog("/played");
			for(i=0;i<10;i++){
			write(env, ob, buffer, 0, mbuff);
			}
			showLog("/stoped");
			stop(env, ob);
			showLog("/stoped");
			release(env, ob);
			showLog("/released");
			showLog("destroy");
			destroyAudioTrack(env,ob);
			showLog("/destroyed");
			//				jint streamType, jint sampleRateInHz, jint channelConfig,
			//				jint audioFormat, jint bufferSizeInBytes, jint mode)
			//			  env, STREAM_MUSIC, 44100,)
		}
	}

	JNIEXPORT void JNICALL Java_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI_finalize(JNIEnv *env, jobject obj) {
		showLog("finalize");
	}

	JNIEXPORT void JNICALL Java_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI_start(JNIEnv *env, jobject obj) {
		showLog("start");
	}

	JNIEXPORT void JNICALL Java_info_kyorohiro_helloworld_musictoy_DynmicSoundAudioTrackJNI_stop(JNIEnv *env, jobject obj) {
		showLog("stop");
	}

#ifdef __cplusplus
}
#endif
#endif
