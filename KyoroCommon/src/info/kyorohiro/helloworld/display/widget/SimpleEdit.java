package info.kyorohiro.helloworld.display.widget;

import java.util.LinkedList;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.CommitText;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.MyInputConnection;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBuilder;
import info.kyorohiro.helloworld.io.BigLineDataBuilder.W;

//
//
// now coding
public class SimpleEdit extends SimpleDisplayObjectContainer {

	private TextView mViewer = new TextView();
	private Paint mPaint = new Paint();
	public class MyBreaktext implements BreakText {
		@Override
		public int breakText(MyBuilder b) {
			int len = mPaint.breakText(b.getAllBufferedMoji(), 0, 
					b.getCurrentBufferedMojiSize(), getWidth()*8/10, null);
			return len;
		}
	}
	private SampleText mTextBuffer = new SampleText(new MyBreaktext());
	public SimpleEdit() {
		addChild(mViewer);
	}

	@Override
	public void setRect(int w, int h) {
		super.setRect(w, h);
		mViewer.setRect(w, h);
	}

	public class TextView extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			drawBG(graphics);
			SimpleStage stage = getStage(this);
			MyInputConnection c = stage.getCurrentInputConnection();
			if(c == null) {
				return;
			}
			mPaint.setTextSize(12);
			CharSequence composing = c.getComposingText();
			graphics.setColor(Color.BLACK);
			graphics.drawText(""+composing, 40, 40);

			graphics.drawText(""+len, 40, 60);
			for(int i=0;i<len;i++){
				graphics.drawText(""+pushed[i], 40+i*20, 60);			
			}
			
			graphics.drawText("d"+((SpannableStringBuilder)c.getEditable()).toString(),40,80);
			len =0;
			try {
				CommitText text = c.popFirst();
				if(text != null) {
					mTextBuffer.pushCommit(text.getText(),text.getNewCursorPosition());
				}
				mTextBuffer.paint(graphics, (int)mPaint.getTextSize());
			} catch(Throwable e){
				e.printStackTrace();
			}
		}

		private int[] pushed = new int[100];
		private int len = 0;
		@Override
		public boolean onKeyDown(int keycode) {
			if(keycode == KeyEvent.KEYCODE_DEL) {
				pushed[len] = keycode;
				len++;
			}
			return super.onKeyDown(keycode);
		}
		public void drawBG(SimpleGraphics graphics) {
			graphics.setColor(Color.parseColor("#ff80c9f4"));
			graphics.setStyle(SimpleGraphics.STYLE_FILL);
			graphics.startPath();
			graphics.moveTo(0, 0);
			graphics.lineTo(0, getHeight());
			graphics.lineTo(getWidth(), getHeight());
			graphics.lineTo(getWidth(), 0);
			graphics.lineTo(0, 0);
			graphics.endPath();
		}

		@Override
		public boolean onTouchTest(int x, int y, int action) {
			if (0 < x && x < this.getWidth() && 0 < y && y < this.getHeight()) {
				//
				//
				SimpleStage stage = getStage(this);
				stage.showInputConnection();
			}
			return super.onTouchTest(x, y, action);
		}
	}
	
	public static class SampleText implements W {
		private int cursorRow = 0;//line
		private int cursorCol = 0;//point
		private LinkedList<CharSequence> lines = new LinkedList<CharSequence>();
		private BreakText mBreakText = null;
		private MyBuilder builder = new MyBuilder();

		public void paint(SimpleGraphics graphics, int textSize) {
			int len = lines.size();
			graphics.setTextSize(textSize);
			android.util.Log.v("kiyo",""+len);
			for(int i=0;i<len;i++) {
				graphics.drawText(lines.get(i),20,(int)(20+textSize*1.2*i));
				android.util.Log.v("kiyo","["+i+"]"+lines.get(i));
			}
		}
		//
		//todo
		
		public SampleText(BreakText breaktext) {
			mBreakText = breaktext;
		}

		@Override
		public void pushCommit(CharSequence text, int cursor) {
			builder.clear();
			pushCommit(text, cursorRow, cursorCol);
			if(cursor > 0) {
				moveCursor(text.length()+(cursor-1));
			}
		}

		private void moveCursor(int move) {
			CharSequence c = lines.get(cursorRow);
			int m = cursorCol+move;
			if(m<c.length()) {
				cursorCol = m;
			} else {
				if( (cursorRow+1)<lines.size()) {
					cursorCol = 0;
					cursorRow += 1;
					moveCursor(m-c.length());
				} else {
					cursorRow = c.length();
				}
			}
		}
		public void pushCommit(CharSequence text, int row, int col) {
			CharSequence current = null;
			if(row <lines.size()) {
				current = lines.get(row);
			} else {
				current = "";
			}
			if(current == null) {
				current = "";
			}

			int p = 0;
			for(int i=0;i<col;i++) {
				builder.append(current.charAt(i));
				p++;
			}
			for(int i=0;i<text.length();i++){
				builder.append(text.charAt(i));
			}
			for(int i=col;i<current.length();i++){
				builder.append(current.charAt(i));
			}
			int breakPoint = mBreakText.breakText(builder);
			if(row <lines.size()) {
				lines.set(row, new String(builder.getAllBufferedMoji(),0,breakPoint));
			} else {
				lines.add(new String(builder.getAllBufferedMoji(),0,breakPoint));				
			}
			if(breakPoint < builder.getCurrentBufferedMojiSize()) {
				builder.clearFirst(breakPoint);
				pushCommit("",row+1,0);
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
