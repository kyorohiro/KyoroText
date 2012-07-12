package info.kyorohiro.helloworld.display.widget.lineview;

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

	private EditableLineViewBuffer mTextBuffer = null;//new EditableLineViewBuffer();

	private EditableLineView(EditableLineViewBuffer buffer) {
		super(buffer, 16, 512);
		mTextBuffer = (EditableLineViewBuffer)getLineViewBuffer();
		mTextBuffer.setBreakText(getBreakText());
	}

	public static EditableLineView newEditableLineView(){
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(new MyBreakText());
		EditableLineView mView = new EditableLineView(buffer);
		return mView;
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
		SimpleStage stage = getStage(this);
		MyInputConnection c = stage.getCurrentInputConnection();
		if (c == null) {return;}

		try {
			CommitText text = c.popFirst();
			if (text != null) {
				mTextBuffer.pushCommit(text.getText(),text.getNewCursorPosition());
			}
		//	graphics.setColor(Color.WHITE);
		//	mTextBuffer.paint(graphics, (int) mPaint.getTextSize());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		super.paint(graphics);
	}


	public static class EditableLineViewBuffer 
	extends  EditableLineViewBufferA 
	implements LineViewBufferSpec {

		private BreakText mBreakText = null;
		public EditableLineViewBuffer(BreakText breakText) {
			super();
			mBreakText = breakText;
		}

		@Override
		public int getNumOfAdd() {
			return 0;
		}

		@Override
		public void clearNumOfAdd() {
		}

		@Override
		public int getNumberOfStockedElement() {
//			android.util.Log.v("kiyo","number="+getLines().size());
			return getLines().size();
		}

		@Override
		public LineViewData[] getElements(LineViewData[] ret, int start, int end) {
			int j=0;
			for(int i=start;i<end&&i<getLines().size();i++){
				ret[j] = new LineViewData(getLines().get(i), Color.YELLOW, LineViewData.INCLUDE_END_OF_LINE);
				j++;
//				android.util.Log.v("kiyo","["+i+"]"+getLines().get(i));
			}
			return ret;
		}

		@Override
		public BreakText getBreakText() {
			return mBreakText;
		}

		@Override
		public LineViewData get(int i) {
			return new LineViewData(getLines().get(i), Color.YELLOW, LineViewData.INCLUDE_END_OF_LINE);
		}
	
	}
	public static class EditableLineViewBufferA implements W {

		private int cursorRow = 0;// line
		private int cursorCol = 0;// point
		private LinkedList<CharSequence> lines = new LinkedList<CharSequence>();
		private BreakText mBreakText = null;
		private MyBuilder builder = new MyBuilder();

		protected LinkedList<CharSequence> getLines() {
			return lines;
		}

		public void paint(SimpleGraphics graphics, int textSize) {
			int len = lines.size();
			graphics.setTextSize(textSize);
			for (int i = 0; i < len; i++) {
				graphics.drawText(lines.get(i), 20, (int) (20 + textSize * 1.2* i));
			}
		}
		public void setBreakText(BreakText breaktext) {
			mBreakText = breaktext;
		}

		@Override
		public void pushCommit(CharSequence text, int cursor) {
			builder.clear();
			pushCommit(text, cursorRow, cursorCol);
			if (cursor > 0) {
				moveCursor(text.length() + (cursor - 1));
			}
		}

		private void moveCursor(int move) {
			if(lines == null || lines.size() == 0){
				return;
			}
			if(cursorRow>=lines.size()){
				cursorRow = lines.size()-1;
			}
			CharSequence c = lines.get(cursorRow);
			int m = cursorCol + move;
			if (m < c.length()) {
				cursorCol = m;
			} else {
				if ((cursorRow + 1) < lines.size()) {
					cursorCol = 0;
					cursorRow += 1;
					moveCursor(m - c.length());
				} else {
					cursorRow = c.length();
				}
			}
		}

		public void pushCommit(CharSequence text, int row, int col) {
			CharSequence current = null;
			if (row < lines.size()) {
				current = lines.get(row);
			} else {
				current = "";
			}
			if (current == null) {
				current = "";
			}

			for (int i = 0; i < col; i++) {
				builder.append(current.charAt(i));
			}
			for (int i = 0; i < text.length(); i++) {
				builder.append(text.charAt(i));
			}
			for (int i = col; i < current.length(); i++) {
				builder.append(current.charAt(i));
			}
			int breakPoint = mBreakText.breakText(builder);
			if (row < lines.size()) {
				lines.set(row, new String(builder.getAllBufferedMoji(), 0,
						breakPoint));
			} else {
				lines.add(new String(builder.getAllBufferedMoji(), 0,
						breakPoint));
			}
			if (breakPoint < builder.getCurrentBufferedMojiSize()) {
				builder.clearFirst(breakPoint);
				pushCommit("", row + 1, 0);
			}
		}

		@Override
		public void setComposing(CharSequence text) {
		}

		public void setCursor(int row, int col) {
			cursorRow = row;
			cursorCol = col;
		}
	}


}
