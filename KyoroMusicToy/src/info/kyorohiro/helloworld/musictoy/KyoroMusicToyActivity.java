package info.kyorohiro.helloworld.musictoy;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class KyoroMusicToyActivity extends Activity {
    /** Called when the activity is first created. */
	
	private LinearLayout mRoot = null;
	private Button mStart = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoot = new LinearLayout(this);
        mStart = new Button(this);
        mStart.setText("start");
        mStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Thread th = new Thread(new SoundTestTask());
				th.start();
			}
		});

        mRoot.setOrientation(LinearLayout.VERTICAL);
        mRoot.addView(mStart);
        setContentView(mRoot);
    }

	
	public static class SoundTestTask implements Runnable {
		short[] mSoundBuffer = new short[1024];
		SinWaveOscillator mOscillator = new SinWaveOscillator();

		private int mBufferSize = 
			AudioTrack.getMinBufferSize( 44100,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
		private AudioTrack mStreamTrack = null;
		public SoundTestTask() {
			mStreamTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT,
					mBufferSize,
					AudioTrack.MODE_STREAM);
		}

		public void run() {
			DynmicSoundAudioTrackJNI dsat = null;
			try {
				dsat = new DynmicSoundAudioTrackJNI();
				
				dsat.initialize();
				dsat.start();
				mStreamTrack.play();
				double hz = SinWaveOscillator.toHz(60);
				for(int i=0;i<100;i++){
					mOscillator.generateSineWave(
							hz, 100-i, mSoundBuffer, 1024*i, 1024);
					mStreamTrack.write(mSoundBuffer, 0, 1024);
				}
				
			} finally {
				mStreamTrack.stop();
				mStreamTrack.release();
				dsat.stop();
				dsat.finalize();

			}
		}
	}
}