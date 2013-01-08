package info.kyorohiro.helloworld.ext.textviewer.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

//import android.view.FocusFinder;

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
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferGroup;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.StartupBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.AppDependentAction;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.KeyEventManagerPlus;
import info.kyorohiro.helloworld.ext.textviewer.manager.task.OtherWindowTask;
import info.kyorohiro.helloworld.util.AsyncronousTask;

public class BufferManager extends SimpleDisplayObjectContainer {
	private static BufferManager sInstance = null;
	private CircleControllerManager mCircleManager = new CircleControllerManager();
	private int mWidth = 100;
	private int mTextSize = 16;
	private int mMergine = 10;
	private TextViewer mFocusingViewer = null;
	private SimpleCircleControllerMenuPlus mCircleMenu = new SimpleCircleControllerMenuPlus();
	private BufferGroup mRoot = null;
	private KeyEventManager mKeyEventManager = new KeyEventManagerPlus();
	private MiniBuffer mModeLine = null;
	private BufferList mList = new BufferList();
	private TextViewer mInfo = null;

	public static BufferManager getManager() {
		return sInstance;
	}

	public BufferList getBufferList() {
		return mList;
	}

	public MiniBuffer getMiniBuffer() {
		return mModeLine;
	}

	public TextViewer getInfoBuffer() {
		return mInfo;
	}

	private int mBaseTextSize = 12;
	public int getBaseTextSize() {
		return mBaseTextSize;
	}
	// ���Singletone�ɂ���B
	public BufferManager(SimpleApplication application,
			AppDependentAction builder, int baseTextSize, int textSize,
			int width, int height, int mergine, int menuWidth) {
		mBaseTextSize = baseTextSize;
		mApplication = application;
		mBuilder = builder;
		sInstance = this;
		mWidth = width;
		mTextSize = textSize;
		mMergine = mergine;
		mFocusingViewer = newTextViewr();
		BufferGroup first = new BufferGroup(mFocusingViewer);
		addChild(first);
		addChild(mCircleMenu);
		mCircleManager.init();
		setCircleMenuRadius(menuWidth);
		//
		first.getTextViewer().getLineView().fittableToView();
		// /*
		// command
		// android.util.Log.v("kiyo","###base ="+baseTextSize+","+menuWidth);
		BufferGroup g = first.divideAndNew(true, mModeLine = new MiniBuffer(
				baseTextSize, mWidth, mMergine, false));
		// mModeLine.setCurrentFontSize(baseTextSize);
		mModeLine.getLineView().setKeyEventManager(mKeyEventManager);
		first.setSeparatorPoint(0.05f);
		g.getTextViewer().getLineView().fittableToView(true);
		g.getTextViewer().getLineView().setMode(EditableLineView.MODE_EDIT);
		g.getTextViewer().isGuard(true);
		g.isVisible(false);
		g.getTextViewer().IsExtraUI(false);
		g.isControlBuffer(true);
		// */
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
	private AppDependentAction mBuilder = null;

	public SimpleApplication getApplication() {
		return mApplication;
	}

	public SimpleFont getFont() {
		return mBuilder.newSimpleFont();
	}

	//
	//
	public TextViewer newTextViewr() {
		TextViewer viewer = new StartupBuffer(mTextSize, mWidth, mMergine, true);
		viewer.getLineView().setKeyEventManager(mKeyEventManager);
		mList.add(viewer);
		// viewer.getLineView().fittableToView(true);
		return viewer;
	}

	@Override
	public void insertChild(int index, SimpleDisplayObject child) {
		// android.util.Log.v("kiyo", "---- c");
		if (child instanceof BufferGroup) {
			mRoot = (BufferGroup) child;
			// android.util.Log.v("kiyo", "---- child");
		}
		super.insertChild(index, child);
	}

	public BufferGroup getRoot() {
		return mRoot;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		// android.util.Log.v("kiyo","BufferManager.paint()");
		setRect(graphics.getWidth(), graphics.getHeight());
		_layout();
		// int t = mCircleMenu.getMinRadius();
		// mRoot.setPoint(0, t);
		mRoot.setPoint(0, 0);
		// mRoot.setRect(graphics.getWidth(), graphics.getHeight() - t);
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
//
//	public void convertTextViewer(TextViewer textViewer) {
//		TextViewer prev = getFocusingTextViewer();
//		BufferGroup group = null;
//		if(prev.getParent() instanceof BufferGroup) {
//			group = (BufferGroup)prev.getParent();
//			group.setTextViewer(textViewer);
//		}
// 	}
//
	public void changeFocus(TextViewer textViewer) {
		TextViewer p = BufferManager.getManager().getFocusingTextViewer();
		if (textViewer != p) {
			p.getLineView().isFocus(false);
		}
		textViewer.getLineView().isFocus(true);
		if (mFocusingViewer != null
				&& mFocusingViewer.getLineView() instanceof CursorableLineView) {
			if (CursorableLineView.MODE_EDIT
					.equals(((CursorableLineView) mFocusingViewer.getLineView())
							.getMergine())) {
				((CursorableLineView) mFocusingViewer.getLineView())
						.setMode(CursorableLineView.MODE_VIEW);
			}
		}
		mFocusingViewer = textViewer;
		if (mFocusingViewer.getLineView() instanceof CursorableLineView) {
			mCircleManager
					._circleSelected(((CursorableLineView) (mFocusingViewer
							.getLineView())).getMode());
		}
	}

	public TextViewer getFocusingTextViewer() {
		return mFocusingViewer;
	}


	//
	// following logic dont use now. maybe delete
	// Event
	//
	private Event mEvent = new EmptyEvent();

	public void setEvent(Event event) {
		if (event == null) {
			mEvent = new EmptyEvent();
		} else {
			mEvent = event;
		}
	}

	public boolean notifyEvent(SimpleDisplayObject alive,
			SimpleDisplayObject killtarget) {
		return mEvent.startCombine(alive, killtarget);
	}

	public static interface Event {
		boolean startCombine(SimpleDisplayObject alive,
				SimpleDisplayObject killtarget);
	}

	public static class EmptyEvent implements Event {
		@Override
		public boolean startCombine(SimpleDisplayObject alive,
				SimpleDisplayObject killtarget) {
			return true;
		}
	}
	
	//
	// 
	//
	//
	//
	//
	
	private BufferGroup getParentAsBufferGroup() {
		TextViewer viewer = getFocusingTextViewer();
		if (viewer == null) {
			return null;
		}
		Object parent = viewer.getParent();
		if (parent == null) {
			return null;
		}

		if (!(parent instanceof BufferGroup)) {
			return null;
		}
		return (BufferGroup)parent;
	}

	public void beginInfoBuffer() {
		if(mInfo == null || mInfo.isDispose()) {
			mInfo = splitWindowHorizontally().getTextViewer();
			mInfo.setCurrentFontSize((int)(getBaseTextSize()*1.2));
			if(mInfo == null) {
				return;
			}
		}
		File infoFile = new File(getApplication().getApplicationDirectory(),"info.txt");
		File baseDir = infoFile.getParentFile();
		if(!baseDir.exists()) {
			baseDir.mkdirs();
		}

		if(infoFile.exists()) {
			infoFile.delete();
		}
		if(!infoFile.exists()) {
			try {
				infoFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			mInfo.readFile(infoFile, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void endInfoBuffer() {
		TextViewer info = getInfoBuffer();
		if(info != null && !info.isDispose()) {
			changeFocus(info);
			if(getFocusingTextViewer() == info) {
				deleteWindow();
			}
		}
	}

	//
	// following code otherWindow must to be moving another class
	// 　
	public synchronized void otherWindow() {
		// /*
		OtherWindowTask task = new OtherWindowTask();
		AsyncronousTask atask = new AsyncronousTask(task);
		getMiniBuffer().startTask(atask);
		atask.waitForTask();
	}


	public void deleteOtherWindows() {
		TextViewer viewer = getFocusingTextViewer();
		TextViewer work = getFocusingTextViewer();
		if(null == getParentAsBufferGroup()) {
			return;
		}

		int i = 0;
		do {
			otherWindow();
			work = getFocusingTextViewer();
			if (work == null || work == viewer) {
				break;
			}
			i++;
			if (i > 20) {
				break;
			}
			deleteWindow();
		} while (true);
	}

	public BufferGroup splitWindowVertically() {
		TextViewer viewer = getFocusingTextViewer();
		BufferGroup parent = null;
		parent = getParentAsBufferGroup();
		if(null == parent) {
			return null;
		}
		return parent.splitWindowVertically();
	}

	public BufferGroup splitWindowHorizontally() {
		BufferGroup parent = null;
		parent = getParentAsBufferGroup();
		if(null == parent) {
			return null;
		}
		return ((BufferGroup) parent).splitWindowHorizontally();
	}

	public void deleteWindow() {
		TextViewer viewer = getFocusingTextViewer();
		if (viewer == null) {
			return;
		}
		Object parent = viewer.getParent();
		if (parent == null) {
			return;
		}

		if (!(parent instanceof BufferGroup)) {
			return;
		}

		((BufferGroup) parent).deleteWindow();
	}
	
	
	
}
