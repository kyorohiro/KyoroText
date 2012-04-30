package info.kyorohiro.helloworld.musictoy;

public class DynmicSoundAudioTrackJNI {
	static {
		System.loadLibrary("audiotrack");
	}
	public native void initialize();
	public native void finalize();
	public native void start();
	public native void stop();
}
