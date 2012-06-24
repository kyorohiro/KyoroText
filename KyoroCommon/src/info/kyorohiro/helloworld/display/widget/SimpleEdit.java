package info.kyorohiro.helloworld.display.widget;

import android.graphics.Color;
import android.text.Editable;
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
			len =0;
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
