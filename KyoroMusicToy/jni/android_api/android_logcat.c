#include <jni.h>
#include <android/log.h>

void showLog(char *message) {
	__android_log_write(ANDROID_LOG_INFO, __FILE__, "log begin");
	__android_log_write(ANDROID_LOG_INFO, __FILE__, message);
	__android_log_write(ANDROID_LOG_INFO, __FILE__, "log end");
}
