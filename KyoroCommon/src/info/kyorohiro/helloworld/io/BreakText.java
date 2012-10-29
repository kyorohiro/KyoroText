package info.kyorohiro.helloworld.io;

//todo android dependent now
public interface BreakText extends SimpleTextDecoderBreakText {
	public int getWidth();

	public void setTextSize(float textSize);

	public float getTextSize();

	public int breakText(MyBuilder mBuffer);

	public int breakText(MyBuilder mBuffer, int width);

	public int breakText(CharSequence data, int index, int count, int width);

	public int getTextWidths(CharSequence text, int start, int end,
			float[] widths, float textSize);
}
