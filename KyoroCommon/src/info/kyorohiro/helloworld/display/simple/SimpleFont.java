package info.kyorohiro.helloworld.display.simple;

public class SimpleFont {
	private boolean mAntiAlias = false;
	private float mFontSize = 16.0f;
	public void setAntiAlias(boolean state){
		mAntiAlias = state;
	}

	public boolean getAntiAlias() {
		return mAntiAlias;
	}

	public void setFontSize(float size) {
		mFontSize = size;
	}

	public float getFontSize() {
		return mFontSize;
	}
}
