package info.kyorohiro.samples.android.surfaceview_editor;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Buffer {
	private LinkedList<String> edit = new LinkedList<String>();
	private int mY = 0;
	private int mX = 0;
	
	private void normalize(int y) {
		if(mY>edit.size()-1) {
			mY = edit.size()-1;
		}
		if(mY<0) {
			mY=0;
		}
		if(edit.size() == 0) {
			edit.add("");
		}

		String line = ""+edit.get(mY);
		if(mX > line.length()){
			mX = line.length();
		}
		
		if(mX<0) {
			mX = 0;
		}
	}

	public int getX() {
		return mX;
	}

	public int getY() {
		return mY;
	}

	public void commit(String commit) {
		android.util.Log.v("kiyo","#commitText="+commit);		
		// normalize
		String line = edit.get(mY);
		if(mX > line.length()){
			mX = line.length();
		}
		if(mX<0) {
			mX = 0;
		}

		//
		String f = line.substring(0, mX);
		String e = line.substring(mX, line.length());
		edit.set(mY, ""+f+commit+e);
		mX += commit.length();
	}

	public void crlf() {
		android.util.Log.v("kiyo","#crlf=");
		normalize(mY);
		//
		String line = edit.get(mY);
		String f = line.substring(0, mX);
		String e = line.substring(mX, line.length());
		edit.set(mY, f);
		if(mY<edit.size()) {
			edit.add(mY+1, ""+e);
		} else {
			edit.add(""+e);
		}
		mY++;
		mX=0;//e.length();
	}

	public void up() {
		android.util.Log.v("kiyo","#up=");
		mY--;
	}
	public void dowm() {
		android.util.Log.v("kiyo","#down=");
		mY++;
	}

	public void right() {
		android.util.Log.v("kiyo","#right=");
		mX++;
	}

	public void left() {
		android.util.Log.v("kiyo","#left=");
		mX--;
	}

	public void delete() {
		android.util.Log.v("kiyo","#delete=");
		// normalize
		String line = edit.get(mY);
		if(mX > line.length()){
			mX = line.length();
		}
		if(mX<0) {
			mX = 0;
		}
		if(mX==0&&mY==0){
			return;
		}
		if(mX==0) {
			edit.remove(mY);
			mY--;
			if(mY>=0) {
				String p =edit.get(mY);
				edit.set(mY, edit.get(mY)+line);
				mX = p.length();
			}
		} else {
			String f = line.substring(0, mX-1);
			String e = line.substring(mX, line.length());
			edit.set(mY, ""+f+e);
			mX -=1;
		}
	}
	
	public void drawText(Canvas canvas, int x, int y, Paint p) {		
		normalize(mY);
		for(int i=0;i<edit.size();i++){
			String l = edit.get(i);
			canvas.drawText(l, x, y+(int)(p.getTextSize()*1.2*(i+2)), p);
		}
		//*/
	}
}
