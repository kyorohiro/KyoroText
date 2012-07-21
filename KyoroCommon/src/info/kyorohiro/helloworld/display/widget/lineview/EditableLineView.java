package info.kyorohiro.helloworld.display.widget.lineview;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import android.graphics.Color;

import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.CommitText;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.MyInputConnection;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.io.MyBuilder;
import info.kyorohiro.helloworld.io.BigLineDataBuilder.W;

public class EditableLineView extends CursorableLineView {

	private EditableLineViewBuffer mTextBuffer = null;

	public  EditableLineView(LineViewBufferSpec v) {
		super(new EditableLineViewBuffer(v), 16, 512);
		mTextBuffer = (EditableLineViewBuffer)getLineViewBuffer();
	}

	public  EditableLineView(LineViewBufferSpec v, int textSize, int cashSize)  {
		super(new EditableLineViewBuffer(v), textSize, cashSize);
		mTextBuffer = (EditableLineViewBuffer)getLineViewBuffer();
	}

	@Override
	public synchronized void setLineViewBufferSpec(LineViewBufferSpec inputtedText) {
		try {
		lock();
		super.setLineViewBufferSpec(mTextBuffer =new EditableLineViewBuffer(inputtedText));
		}finally {
			releaseLock();
		}
	}
	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if (0 < x && x < this.getWidth() && 0 < y && y < this.getHeight()) {
			SimpleStage stage = getStage(this);
			stage.showInputConnection();
		}
		return super.onTouchTest(x, y, action);
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		//
		mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());

		//
		SimpleStage stage = getStage(this);
		MyInputConnection c = stage.getCurrentInputConnection();
		if (c == null) {return;}

		try {
			CommitText text = c.popFirst();
			if (text != null) {
				mTextBuffer.pushCommit(text.getText(),text.getNewCursorPosition());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		super.paint(graphics);
	}


	//
	// 親を上書きする。
	//
	public static class EditableLineViewBuffer implements LineViewBufferSpec , W {

		private LineViewBufferSpec mOwner = null;
		public EditableLineViewBuffer(LineViewBufferSpec owner) {
			super();
			mOwner = owner;
		}

		@Override
		public int getNumOfAdd() {return mOwner.getNumOfAdd();}
		@Override
		public void clearNumOfAdd() {mOwner.clearNumOfAdd();}
		@Override
		public int getNumberOfStockedElement() {return mOwner.getNumberOfStockedElement();}
		@Override
		public BreakText getBreakText() {return mOwner.getBreakText();}

		@Override
		public LineViewData[] getElements(LineViewData[] ret, int start, int end) {
			// todo 
			// refactaring 親クラスがまったく呼ばれない!1
			for(int i=start,j=0;i<end;i++){
				ret[j]=get(i);j++;
			}
			return ret;
		}

		@Override
		public LineViewData get(int i) {
			if(mData.containsKey(i)){
				android.util.Log.v("kiyo","get("+i+")data:"+mData.get(i).toString());
				return mData.get(i);
			} else {
				android.util.Log.v("kiyo","get("+i+")owner:"+mOwner.get(i).toString());
				return mOwner.get(i);				
			}
		}
		@Override
		public int getMaxOfStackedElement() {
			return mOwner.getMaxOfStackedElement();
		}
		LineViewBufferSpec getLineViewBufferSpec(){
			return this;
		}
		@Override
		public void pushCommit(CharSequence text, int cursor) {
			if(cursor < 0){
				return;
			}
			if(!mData.containsKey(mCursorCol)){
				android.util.Log.v("kiyo","containsKey="+mCursorCol);
				mData.put(mCursorCol, mOwner.get(mCursorCol));
			}
			LineViewData data = mData.get(mCursorCol);
			CharSequence c = null;
			android.util.Log.v("kiyo","if -1-="+mCursorRow+","+data.length());
			if(mCursorRow < data.length()){
				CharSequence a = data.subSequence(0, mCursorRow);
				CharSequence b = data.subSequence(mCursorRow, data.length());
				c = a.toString()+b.toString()+text;
			} else {
				c = data.toString()+text;				
			}
			android.util.Log.v("kiyo","-2-"+mCursorCol+"="+c.toString());
			mData.put(mCursorCol, new LineViewData(c, data.getColor(), data.getStatus()));
		}
		@Override
		public void setCursor(int row, int col) {
			mCursorRow = row;
			mCursorCol = col;
		}

		private int mCursorRow = 0;// line
		private int mCursorCol = 0;// point
		private LinkedHashMap<Integer, LineViewData> mData = new LinkedHashMap<Integer, LineViewData>();
	}

}
