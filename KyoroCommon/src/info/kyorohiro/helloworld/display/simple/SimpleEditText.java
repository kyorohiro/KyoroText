 package info.kyorohiro.helloworld.display.simple;

import java.io.File;
import java.io.FileNotFoundException;

import info.kyorohiro.helloworld.display.widget.FlowingLineViewWithFile;
import info.kyorohiro.helloworld.display.widget.FlowingLineViewWithFile.NextTask;

public class SimpleEditText extends SimpleDisplayObject {
	FlowingLineViewWithFile mLineView = new FlowingLineViewWithFile();

	public SimpleEditText(EditableSurfaceView editable) {
		
	}

	public void start(File path) throws FileNotFoundException {
		mLineView.start(path);
	}

	public void setPosition(int position) {
		mLineView.setPosition(position);
	}

	public int getPosition() {
		return mLineView.getPosition();
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		mLineView.paint(graphics);
	}

	public int getX(){
		return super.getX();
	}

	public int getY(){
		return super.getY();
	}

	public void setPoint(int x, int y){
		super.setPoint(x, y);
		mLineView.setPoint(x, y);
	}

	public boolean onTouchTest(int x, int y, int action){
		super.onTouchTest(x, y, action);
		return mLineView.onTouchTest(x, y, action);
	}

	public boolean onKeyUp(int keycode){
		super.onKeyUp(keycode);
		return mLineView.onKeyUp(keycode);
	}

	public boolean onKeyDown(int keycode){
		super.onKeyDown(keycode);
		return mLineView.onKeyDown(keycode);
	}

	@Override
	public int getWidth() {
		return mLineView.getWidth();
	}

	@Override
	public int getHeight() {
		return mLineView.getHeight();
	}
}
