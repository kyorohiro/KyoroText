package info.kyorohiro.helloworld.pfdep.j2se.adapter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleImage;

public class SimpleGraphicsForJ2SE extends SimpleGraphics {

	private Graphics2D mGraphics = null;
	private int mGlobalX = 0;
	private int mGlobalY = 0;
	private int mGlobalW = 400;
	private int mGlobalH = 400;

	private int mTextSize = 16;
	private int mColor = 0xFFFFFF;
	private int mStyle = 0;
	private int mStrokeWidth = 1;
	private SimpleFont mFont = null;

	public SimpleGraphicsForJ2SE(Graphics2D g, int globalX, int globalY,
			int globalW, int globalH) {
		mGraphics = g;
		mGlobalX = globalX;
		mGlobalY = globalY;
		mGlobalH = globalH;
		mGlobalW = globalW;
		setTextSize(12);// mTextSize);
		mFont = new SimpleFontForJ2SE(g.getFont(), g.getFontMetrics());
	}

	public Graphics2D getGraphics() {
		return mGraphics;
	}

	public void setGlobalW(int w) {
		mGlobalW = w;
	}

	public void setGlobalH(int h) {
		mGlobalH = h;
	}

	@Override
	public int getGlobalX() {
		return mGlobalX;
	}

	@Override
	public int getGlobalY() {
		return mGlobalY;
	}

	@Override
	public void setGlobalPoint(SimpleGraphics graphics, int x, int y) {
		mGraphics = ((SimpleGraphicsForJ2SE) graphics).getGraphics();
		mGlobalX = x;
		mGlobalY = y;
		mGlobalW = graphics.getWidth();
		mGlobalH = graphics.getHeight();
	}

	@Override
	public SimpleGraphics getChildGraphics(SimpleGraphics graphics,
			int globalX, int globalY) {
		return new SimpleGraphicsForJ2SE(mGraphics, globalX, globalY, mGlobalW,
				mGlobalH);
	}

	@Override
	public void drawCircle(int x, int y, int radius) {
		mGraphics.drawArc(mGlobalX + x - radius, mGlobalY + y - radius,
				radius * 2, radius * 2, 0, 360);
	}

	@Override
	public void drawLine(int startX, int startY, int stopX, int stopY) {
		BasicStroke wideStroke = new BasicStroke(mStrokeWidth);
		mGraphics.setStroke(wideStroke);
		mGraphics.drawLine(mGlobalX + startX, mGlobalY + startY, mGlobalX
				+ stopX, mGlobalY + stopY);
	}

	@Override
	public void drawBackGround(int color) {
		int c = getColor();
		setColor(color);
		mGraphics.fillRect(mGlobalX, mGlobalY, getWidth(), getHeight());
		// mGraphics.clearRect(mGlobalX, mGlobalY, getWidth(), getHeight());
		setColor(c);
	}

	@Override
	public void drawText(char[] text, int start, int end, int x, int y) {
		setColor(mColor);
		mGraphics.setFont(mGraphics.getFont().deriveFont(mTextSize));
		mGraphics.drawChars(text, start, end - start, mGlobalX + x, mGlobalY
				+ y);
	}

	@Override
	public void drawText(CharSequence text, int x, int y) {
		setColor(mColor);
		mGraphics.setFont(mGraphics.getFont().deriveFont((float) mTextSize));
		mGraphics.drawString("" + text, mGlobalX + x, mGlobalY + y);
	}

	@Override
	public void drawPosText(char[] text, float[] widths, float zoom, int start,
			int end, int x, int y) {
		// text size setting
		// mGraphics.setFont(mGraphics.getFont().deriveFont((float)mTextSize));
		mGraphics
				.setFont(mGraphics.getFont().deriveFont((float) mTextSize ));
		// gdraphics.getDeviceConfiguration().
		// mGraphics.getFont().
		// mGraphics.setPaint(new Color(mColor));
		mGraphics.setColor(new Color(mColor));
		String t ="";
		GlyphVector gv = mGraphics.getFont().createGlyphVector(
				mGraphics.getFontRenderContext(),
				t=new String(text, start, end - start));

		float w = 0;
		gv.setGlyphPosition(0,
				new Point2D.Float(mGlobalX + x + w, mGlobalY + y));
		for (int i = start; i < (end - start); i++) {
			w += widths[i] * zoom;
//			System.out.println("[" + i + "]=" + w + "," + zoom);
			Point2D pos = new Point2D.Float(mGlobalX + x + w, mGlobalY + y);
//			System.out.println("gvc[" + i + "]=" +(mGlobalX + x + w)+","
//			+t.toCharArray()[i]);
//			System.out.println("gv1[" + i + "]="
//					+ gv.getGlyphPosition(i - start + 1).getX());
			gv.setGlyphPosition(i - start + 1, pos);
//			System.out.println("gv2[" + i + "]="
//					+ gv.getGlyphPosition(i - start + 1).getX());

		}

		mGraphics.drawGlyphVector(gv, mGlobalX, mGlobalY);
	}

	@Override
	public int getTextSize() {
		return mTextSize;
	}



	private Polygon mShape = null;
	@Override
	public void startPath() {
		mShape = new Polygon();
	}

	@Override
	public void moveTo(int x, int y) {
		mShape.addPoint(x+mGlobalX, y+mGlobalY);
	}

	@Override
	public void lineTo(int x, int y) {
		mShape.addPoint(x+mGlobalX, y+mGlobalY);
	}

	@Override
	public void endPath() {
		mGraphics.fill(mShape);
	}

	@Override
	public int getWidth() {
		return mGlobalW;
	}

	@Override
	public int getHeight() {
		return mGlobalH;
	}

	@Override
	public int getColor() {
		return mColor;
	}

	@Override
	public void setColor(int color) {
		mColor = color;
		mGraphics.setColor(new Color(color, true));
	}

	@Override
	public void setTextSize(int size) {
		mTextSize = size;
	}

	@Override
	public void setStyle(int style) {
		mStyle = style;
	}

	@Override
	public int getStyle() {
		return mStyle;
	}

	@Override
	public void setStrokeWidth(int w) {
		mStrokeWidth = w;
	}

	@Override
	public SimpleDisplayObject createImage(byte[] data, int offset, int length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drawImageAsTile(SimpleImage image, int x, int y, int w, int h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clipRect(int left, int top, int right, int bottom) {
		mGraphics.clearRect(left, top, right - left, bottom - top);
	}

	@Override
	public void setSimpleFont(SimpleFont f) {
		// if(f instanceof SimpleFontForJ2SE) {
		// mFont = (SimpleFontForJ2SE)f;
		// }
		mFont = f;
	}

	@Override
	public SimpleFont getSimpleFont() {
		return mFont;
	}

}
