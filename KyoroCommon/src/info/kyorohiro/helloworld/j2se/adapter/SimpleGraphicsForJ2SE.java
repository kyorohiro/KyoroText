package info.kyorohiro.helloworld.j2se.adapter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleImage;

public class SimpleGraphicsForJ2SE extends SimpleGraphics {

	private Graphics2D mGraphics = null;
	private int mGlobalX = 0;
	private int mGlobalY = 0;
	
	public SimpleGraphicsForJ2SE(Graphics2D g, int globalX, int globalY) {
		mGraphics = g;
		mGlobalX = globalX;
		mGlobalY = globalY;
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
	public void setGlobalPoint(int x, int y) {
		mGlobalX = x;
		mGlobalY = y;
	}

	@Override
	public SimpleGraphics getChildGraphics(SimpleGraphics graphics,
			int globalX, int globalY) {
		return new SimpleGraphicsForJ2SE(mGraphics, globalX, globalY);
	}

	@Override
	public void drawCircle(int x, int y, int radius) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawLine(int startX, int startY, int stopX, int stopY) {
		mGraphics.drawLine(mGlobalX+startX, mGlobalY+startY, mGlobalX+stopX, mGlobalY+stopY);
	}

	@Override
	public void drawBackGround(int color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawText(char[] text, int start, int end, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawText(CharSequence text, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawPosText(char[] text, float[] widths, float zoom, int start,
			int end, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTextSize() {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setColor(int color) {
		mGraphics.setColor(new Color(color));
	}

	@Override
	public void setTextSize(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStyle(int style) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return 0;
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
