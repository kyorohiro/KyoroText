package info.kyorohiro.helloworld.display.widget;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.MyInputConnection;

//
//
// now coding
public class SimpleEdit extends SimpleDisplayObjectContainer {

	private TextView mViewer = new TextView();

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
			CharSequence commit = c.getCommitText();
			CharSequence composing = c.getComposingText();
			graphics.setColor(Color.BLACK);
			graphics.drawText(""+composing, 40, 40);

			graphics.drawText(""+len, 40, 60);
			for(int i=0;i<len;i++){
				graphics.drawText(""+pushed[i], 40+i*20, 60);			
			}
			
			graphics.drawText("d"+((SpannableStringBuilder)c.getEditable()).toString(),40,80);
			len =0;
			if(composing instanceof SpannableString) {
				graphics.drawText("SpannableString", 40, 100);							
//				((SpannableString)composing).;
			}
			if(composing instanceof SpannableStringBuilder) {
				graphics.drawText("SpannableStringBuilder", 40, 120);							
			}
			if(composing instanceof Spannable) {
				graphics.drawText("Spannable", 40, 140);
				Spannable s = ((Spannable)composing);
				//s.setSpan(s, 0, 1, Spannable.SPAN_COMPOSING);
				if(3<=s.length()){
					graphics.drawText(""+s.getSpanFlags(s.charAt(0)), 40, 160);							
					graphics.drawText(""+s.getSpanFlags(s.charAt(1)), 80, 160);							
					graphics.drawText(""+s.getSpanFlags(s.charAt(2)), 120, 160);							
//					graphics.drawText(""+s.getSpanStart(c.getEditable().), 120, 160);							
				}
//				for(int i=0;i<s.length();i++){
//					android.util.Log.v("kiyo","["+i+"]"+s.getSpanFlags(s.charAt(i)));
//				}
			}
		}

		private int[] pushed = new int[100];
		private int len = 0;
		@Override
		public boolean onKeyDown(int keycode) {
			//if(keycode == KeyEvent.KEYCODE_DEL) {
				pushed[len] = keycode;
				len++;
			//}
			return super.onKeyDown(keycode);
		}
		public void drawBG(SimpleGraphics graphics) {
			graphics.setColor(Color.parseColor("#ff80c9f4"));
			graphics.setStyle(SimpleGraphics.STYLE_FILL);
			graphics.startPath();
			graphics.moveTo(0, 0);
			graphics.lineTo(0, getWidth());
			graphics.lineTo(getHeight(), getWidth());
			graphics.lineTo(getHeight(), 0);
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
}
