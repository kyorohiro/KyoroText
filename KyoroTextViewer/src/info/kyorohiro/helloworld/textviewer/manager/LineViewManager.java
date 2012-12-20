package info.kyorohiro.helloworld.textviewer.manager;

import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.pfdep.android.adapter.SimpleFontForAndroid;

public class LineViewManager extends SimpleDisplayObjectContainer {
	private static LineViewManager sInstance = null;
	private CircleControllerManager mCircleManager = new CircleControllerManager();
	private int mWidth = 100;
	private int mTextSize = 16;
	private int mMergine = 10;
	private TextViewer mFocusingViewer = null;
	private SimpleCircleControllerMenuPlus mCircleMenu = new SimpleCircleControllerMenuPlus();
	private LineViewGroup mRoot = null;

	public static LineViewManager getManager() {
		return sInstance;
	}

	public void setCurrentFontSize(int textSize) {
		mTextSize = textSize;
	}

	private SimpleApplication mApplication = null;
	private SimpleFont mFont = null;
	public SimpleApplication getApplication() {
		return mApplication;
	}

	public SimpleFont getFont() {
		return new SimpleFontForAndroid();//mFont;
	}

	// ���Singletone�ɂ���B
	public LineViewManager(SimpleApplication application, SimpleFont font,int textSize, int width, int height, int mergine, int menuWidth) {
		mApplication = application;
		mFont = font;
		sInstance = this;
		mWidth = width;
		mTextSize = textSize;
		mMergine = mergine;
		mFocusingViewer = newTextViewr();
		addChild(new LineViewGroup(mFocusingViewer));
		addChild(mCircleMenu);
		mCircleManager.init();
//		mCommand.getLineView().fittableToView(true);
	}

	public TextViewer newTextViewr() {
		TextViewer viewer = new StartupCommandBuffer(mTextSize, mWidth, mMergine);
		//viewer.getLineView().fittableToView(true);
		return viewer;
	}

	@Override
	public void insertChild(int index, SimpleDisplayObject child) {
//		android.util.Log.v("kiyo", "---- c");
		if (child instanceof LineViewGroup) {
			mRoot = (LineViewGroup) child;
//			android.util.Log.v("kiyo", "---- child");
		}
		super.insertChild(index, child);
	}

	public LineViewGroup getRoot() {
		return mRoot;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		setRect(graphics.getWidth(), graphics.getHeight());
		_layout();
		//int t = mCircleMenu.getMinRadius();
		//mRoot.setPoint(0, t);
		mRoot.setPoint(0, 0);
//		mRoot.setRect(graphics.getWidth(), graphics.getHeight() - t);
		mRoot.setRect(graphics.getWidth(), graphics.getHeight());
		super.paint(graphics);
		int xy[] = new int[2];
		getFocusingTextViewer().getGlobalXY(xy);
		int x = xy[0] + 20;
		int y = xy[1] + getFocusingTextViewer().getHeight(false) - 20;
		graphics.setColor(SimpleGraphicUtil.RED);
		graphics.drawText("now focusing", x, y);
		graphics.setColor(SimpleGraphicUtil.BLACK);
	}

	public void setCircleMenuRadius(int radius) {
		mCircleMenu.setRadius(radius);
	}

	public SimpleCircleControllerMenuPlus getCircleMenu() {
		return mCircleMenu;
	}

	private void _layout() {
		int cr = mCircleMenu.getMaxRadius();
		int pw = getWidth(false);
		int ph = getHeight(false);
		mCircleMenu.setPoint(pw - cr, ph - cr);
	}

	public int getMergine() {
		return mMergine;
	}

	public int getTextSize() {
		return mTextSize;
	}

	public void changeFocus(TextViewer textViewer) {
		TextViewer p = LineViewManager.getManager().getFocusingTextViewer();
		if(textViewer != p){
			p.getLineView().isFocus(false);
		}
		textViewer.getLineView().isFocus(true);
		if(mFocusingViewer !=null && mFocusingViewer.getLineView() instanceof CursorableLineView) {
			if( CursorableLineView.MODE_EDIT.equals(
					((CursorableLineView)mFocusingViewer.getLineView()).getMergine())){
				((CursorableLineView)mFocusingViewer.getLineView()).setMode(CursorableLineView.MODE_VIEW);
			}
		}
		mFocusingViewer = textViewer;
		if (mFocusingViewer.getLineView() instanceof CursorableLineView) {
			mCircleManager._circleSelected(((CursorableLineView) (mFocusingViewer
					.getLineView())).getMode());
		}
	}

	public TextViewer getFocusingTextViewer() {
		return mFocusingViewer;
	}

	private Event mEvent = new EmptyEvent();
	public void setEvent(Event event){
		if(event == null){
			mEvent = new EmptyEvent();
		}
		else {
			mEvent = event;
		}
	}

	public boolean notifyEvent(SimpleDisplayObject alive, SimpleDisplayObject killtarget){
		return mEvent.startCombine(alive, killtarget);
	}

	public static interface Event {
		boolean startCombine(SimpleDisplayObject alive, SimpleDisplayObject killtarget);
	}

	public static class EmptyEvent implements Event {
		@Override
		public boolean startCombine(SimpleDisplayObject alive, SimpleDisplayObject killtarget) {
			return true;
		}
	}
}
