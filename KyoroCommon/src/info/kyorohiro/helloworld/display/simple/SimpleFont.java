package info.kyorohiro.helloworld.display.simple;

import info.kyorohiro.helloworld.text.KyoroString;

public abstract class SimpleFont {
	private boolean mAntiAlias = false;
	private float mFontSize = 16.0f;
	private SimpleTypeface mTypeface = null;

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

	public void setSimpleTypeface(SimpleTypeface typeface) {
		mTypeface = typeface;
	}

	public SimpleTypeface getSimpleTypeface() {
		return mTypeface;
	}

	public abstract int getTextWidths(KyoroString text, int start, int end, float[] widths, float textSize);

}
