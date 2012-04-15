package info.kyorohiro.helloworld.textviewer.viewer;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndMove;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndZoom;


public class TextViewer extends SimpleDisplayObjectContainer{
	TextViewerBuffer mBuffer = null;
	LineView mLineView = null;

	public TextViewer(int textSize, int screenWidth) {
		mBuffer = new TextViewerBuffer(300, textSize, screenWidth);
		mLineView = new LineView(mBuffer, textSize);
		addChild(mLineView);
		addChild(new MyTouchAndMove(mLineView));
		addChild(new MyTouchAndZoom(mLineView));
		mLineView.setPositionY(2);
		mBuffer.startReadForward(-1);
	}

	@Override
	public void setRect(int w, int h) {
		super.setRect(w,h);
		mLineView.setRect(w,h);
	}

}
