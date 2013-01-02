package info.kyorohiro.helloworld.ext.textviewer.manager;

import java.io.File;

import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.ext.textviewer.manager.CircleControllerManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewGroup;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.StartupCommandBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.TextViewBuilder;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.KeyEventManagerPlus;

public class LineViewManager extends SimpleDisplayObjectContainer {
	private static LineViewManager sInstance = null;
	private CircleControllerManager mCircleManager = new CircleControllerManager();
	private int mWidth = 100;
	private int mTextSize = 16;
	private int mMergine = 10;
	private TextViewer mFocusingViewer = null;
	private SimpleCircleControllerMenuPlus mCircleMenu = new SimpleCircleControllerMenuPlus();
	private LineViewGroup mRoot = null;
	private KeyEventManager mKeyEventManager = new KeyEventManagerPlus();
	private ModeLineBuffer mModeLine = null;

	public static LineViewManager getManager() {
		return sInstance;
	}

	public ModeLineBuffer getModeLineBuffer() {
		return mModeLine;
	}

	// ���Singletone�ɂ���B
	public LineViewManager(SimpleApplication application, TextViewBuilder builder, int baseTextSize, int textSize, int width, int height, int mergine, int menuWidth) {
		mApplication = application;
		mBuilder = builder;
		sInstance = this;
		mWidth = width;
		mTextSize = textSize;
		mMergine = mergine;
		mFocusingViewer = newTextViewr();
		LineViewGroup first = new LineViewGroup(mFocusingViewer);
		addChild(first);
		addChild(mCircleMenu);
		mCircleManager.init();
		setCircleMenuRadius(menuWidth);
		//
		first.getTextViewer().getLineView().fittableToView();
		///*
		 // command
//		android.util.Log.v("kiyo","###base ="+baseTextSize+","+menuWidth);
		LineViewGroup g = first.divideAndNew(true, mModeLine = new ModeLineBuffer(baseTextSize, mWidth, mMergine, false));
//		mModeLine.setCurrentFontSize(baseTextSize);
		mModeLine.getLineView().setKeyEventManager(mKeyEventManager);
		first.setSeparatorPoint(0.05f);
		g.getTextViewer().getLineView().fittableToView(true);
		g.getTextViewer().getLineView().setMode(EditableLineView.MODE_EDIT);
		g.getTextViewer().isGuard(true);		
		g.isVisible(false);
		g.getTextViewer().IsExtraUI(false);
		g.isControlBuffer(true);		
		//*/
	}

	@Override
	public void start() {
		super.start();
		changeFocus(mFocusingViewer);
	}
	public void setCurrentFile(String path) {
		mBuilder.setCurrentFile(path);
	}

	public String getCurrentCharset() {
		return mBuilder.getCurrentCharset();
	}

	public void setCurrentFontSize(int textSize) {
		mTextSize = textSize;
	}

	public boolean currentBrIsLF() {
		return mBuilder.currentBrIsLF();
	}
	public File getFilesDir() {
		return mBuilder.getFilesDir();
	}

	public void copyStart() {
		mBuilder.copyStart();
	}

	public void pastStart() {
		mBuilder.pastStart();
	}

	private SimpleApplication mApplication = null;
	private TextViewBuilder mBuilder = null;
	public SimpleApplication getApplication() {
		return mApplication;
	}

	public SimpleFont getFont() {
		return mBuilder.newSimpleFont();
	}

	//
	//
	public TextViewer newTextViewr() {
		TextViewer viewer = new StartupCommandBuffer(mTextSize, mWidth, mMergine ,true);
		viewer.getLineView().setKeyEventManager(mKeyEventManager);
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
	

	//
	// following code otherWindow must to be moving another class
	//　
	public void otherWindow() {
	//	android.util.Log.v("kiyo","----otherWindow");
		TextViewer v = getFocusingTextViewer();
		if(v.getParent() instanceof LineViewGroup) {
			int i = ((LineViewGroup)v.getParent()).getIndex(v);
			TextViewer f = otherWindow(v.getParent(), i+1);
			if(f!=null) {
				changeFocus(f);
			}
		}
		if(getFocusingTextViewer() == mModeLine&& mModeLine.isEmptyTask()) {
			otherWindow();
		}
		//android.util.Log.v("kiyo","----/otherWindow");
	}

	private TextViewer otherWindow(Object v, int index) {
		//android.util.Log.v("kiyo","----otherWindow -1-:" +index);
		if(v instanceof LineViewGroup) {
			//android.util.Log.v("kiyo","----otherWindow -1-2:");
			return otherWindow((LineViewGroup)v, index);
		} else if(v instanceof TextViewer) {
			//android.util.Log.v("kiyo","----otherWindow -1-3:");
			return (TextViewer)v;
		} else {
			//android.util.Log.v("kiyo","----otherWindow -1-4:");
			return otherWindow(mRoot, 0);
		}
	}
	private TextViewer otherWindow(LineViewGroup v, int index) {
		//android.util.Log.v("kiyo","----otherWindow -2-" +index+","+v.numOfChild());
		for(int i=index;i<v.numOfChild();i++) {
			if(v.getChild(i) instanceof TextViewer) {
			//	android.util.Log.v("kiyo","----otherWindow -2-1 " +i);
				return (TextViewer)v.getChild(i);
			} else if(v.getChild(i) instanceof LineViewGroup) {
				//android.util.Log.v("kiyo","----otherWindow -2-2 " +i);
				return otherWindow(v.getChild(i), 0);
			}
		}
		if(v instanceof SimpleDisplayObjectContainer){
			int j = ((SimpleDisplayObjectContainer)v.getParent()).getIndex(v);
			//android.util.Log.v("kiyo","----otherWindow -3-"+j);
			return otherWindow(v.getParent(), j+1);
		} else {
		//	android.util.Log.v("kiyo","----otherWindow -4-");
			return otherWindow(this, 0);
		}
	}

	//
	//following logic dont use now. maybe delete 
	// Event 
	//
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
