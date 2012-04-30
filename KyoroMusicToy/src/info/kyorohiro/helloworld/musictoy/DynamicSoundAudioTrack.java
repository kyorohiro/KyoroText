package info.kyorohiro.helloworld.musictoy;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;


public class DynamicSoundAudioTrack {
	private int mBufferSize = 1024;
	private short[] mSoundBuffer = new short[mBufferSize];
	private SinWaveOscillator mOscillator = new SinWaveOscillator();
	private AudioTrack mStreamTrack = null;

	public DynamicSoundAudioTrack() {
		mBufferSize = AudioTrack.getMinBufferSize( 44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		mStreamTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				mBufferSize, AudioTrack.MODE_STREAM);
	}

	public int getBufferSize() {
		return mBufferSize;
	}

	public void start() {
		
	}

	private class Task implements Runnable {
		public void run() {
			try {
				int bufferSize = mBufferSize;
				mStreamTrack.play();
				double hz = SinWaveOscillator.toHz(60);
				for(int i=0;i<100;i++){
					mOscillator.generateSineWave(
							hz, 100-i, mSoundBuffer, bufferSize*i, bufferSize);
					mStreamTrack.write(mSoundBuffer, 0, bufferSize);
				}

			} finally {
				mStreamTrack.stop();
				mStreamTrack.release();
			}
		}
	}
}
