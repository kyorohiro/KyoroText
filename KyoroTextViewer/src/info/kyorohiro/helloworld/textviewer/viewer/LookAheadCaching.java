package info.kyorohiro.helloworld.textviewer.viewer;

import java.lang.ref.WeakReference;

public class LookAheadCaching {
	private WeakReference<TextViewerBuffer> mBuffer = null;

	public  LookAheadCaching(TextViewerBuffer buffer) {
		mBuffer = new WeakReference<TextViewerBuffer>(buffer);
	}

	public void updateBufferedStatus() {
		;
	}
	
}
