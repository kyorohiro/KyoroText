package info.kyorohiro.helloworld.j2se.adapter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

	public SimpleGraphicsForJ2SE(Graphics2D g, int globalX, int globalY, int globalW, int globalH) {
		mGraphics = g;
		mGlobalX = globalX;
		mGlobalY = globalY;
		mGlobalH = globalH;
		mGlobalW = globalW;
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
		mGraphics = ((SimpleGraphicsForJ2SE)graphics).getGraphics();
		mGlobalX = x;
		mGlobalY = y;
		mGlobalW = graphics.getWidth();
		mGlobalH = graphics.getHeight();
	}

	@Override
	public SimpleGraphics getChildGraphics(SimpleGraphics graphics,
			int globalX, int globalY) {
		return new SimpleGraphicsForJ2SE(mGraphics, globalX, globalY, mGlobalW, mGlobalH);
	}

	@Override
	public void drawCircle(int x, int y, int radius) {
		mGraphics.drawArc(mGlobalX+x-radius, mGlobalY+y-radius, radius*2, radius*2, 0, 360);
	}

	@Override
	public void drawLine(int startX, int startY, int stopX, int stopY) {
		mGraphics.drawLine(mGlobalX+startX, mGlobalY+startY, mGlobalX+stopX, mGlobalY+stopY);
	}

	@Override
	public void drawBackGround(int color) {
		int c = getColor(); 
		setColor(color);
		mGraphics.clearRect(mGlobalX, mGlobalY, getWidth(), getHeight());
		setColor(c);
	}

	@Override
	public void drawText(char[] text, int start, int end, int x, int y) {
		mGraphics.drawChars(text, start, end-start, mGlobalX+x, mGlobalY+y);
//		mGraphics.drawString(new String(text, start, end), mGlobalX+x, mGlobalY+y);
	}

	@Override
	public void drawText(CharSequence text, int x, int y) {
		mGraphics.drawString(""+text, mGlobalX+x, mGlobalY+y);
	}

	@Override
	public void drawPosText(char[] text, float[] widths, float zoom, int start,
			int end, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTextSize() {
		// TODO Auto-generated method stub
		return mTextSize;
	}

	@Override
	public void startPath() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveTo(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lineTo(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPath() {
		// TODO Auto-generated method stub
		
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
		mGraphics.setColor(new Color(color));
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTextWidth(String line) {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSimpleFont(SimpleFont f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SimpleFont getSimpleFont() {
		// TODO Auto-generated method stub
		return null;
	}

}
