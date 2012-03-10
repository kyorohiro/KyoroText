package info.kyorohiro.helloworld.test.a;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SampleFragment extends PreferenceFragment {
	public static SampleFragment mInstance = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		android.util.Log.v("kiyo", "Prefs1Fragment#onCreate()");
        super.onCreate(savedInstanceState);
        mInstance = this;
    }
}