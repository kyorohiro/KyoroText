package info.kyorohiro.helloworld.textviewer.viewer;
import java.io.File;

import android.graphics.Color;
import android.os.Environment;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndMove;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndZoom;
import info.kyorohiro.helloworld.util.CyclingList;


public class TextViewer extends SimpleDisplayObjectContainer{
	private CyclingList<FlowingLineDatam> mBuffer = null;
	private LineView mLineView = null;
	private int mWidth = 0;
	private int mTextSize = 0;
	private String mCharset = "utf8";

	public TextViewer(int textSize, int screenWidth) {
		mWidth = screenWidth;
		mTextSize = textSize;

		File testDir = Environment.getExternalStorageDirectory();
		File f = new File(testDir, "a.txt");

		mBuffer = new CyclingList<FlowingLineDatam>(100);//new TextViewerBuffer(1000, textSize, screenWidth, f);
		mBuffer.add(new FlowingLineDatam("please open file", Color.CYAN, FlowingLineDatam.INCLUDE_END_OF_LINE));
		mLineView = new LineView(mBuffer, textSize);
		addChild(mLineView);
		addChild(new MyTouchAndMove(mLineView));
		addChild(new MyTouchAndZoom(mLineView));
		mLineView.isTail(false);
	}

	public void setCharset(String charset) {
		if(charset == null){
			mCharset = "utf8";
		} else {
			mCharset = charset;
		}
	}

	public void readFile(File file) {
		mBuffer = new TextViewerBuffer(1000, mTextSize
				, mWidth, file, mCharset);
		mLineView.setCyclingList(mBuffer);
		((TextViewerBuffer)mBuffer).startReadForward(-1);
	}

	@Override
	public void setRect(int w, int h) {
		super.setRect(w,h);
		mLineView.setRect(w,h);
	}

}
