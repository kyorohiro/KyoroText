package info.kyorohiro.helloworld.textviewer.manager;

import android.content.Context;
import android.graphics.Color;
import android.text.ClipboardManager;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.SimpleCircleControllerMenuPlus;
import info.kyorohiro.helloworld.display.widget.SimpleCircleControllerMenuPlus.CircleMenuItem;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineView;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;

public class LineViewManager extends SimpleDisplayObjectContainer {
	public static int COLOR_CIRCLE_DEFAULT = Color.parseColor("#44FFAA44");
	private static LineViewManager sInstance = null;

	public static LineViewManager getManager() {
		return sInstance;
	}

	private int mWidth = 100;
	private int mTextSize = 16;
	private int mMergine = 10;
	private TextViewer mFocusingViewer = null;
	private SimpleCircleControllerMenuPlus mCircleMenu = new SimpleCircleControllerMenuPlus();
	private LineViewGroup mRoot = null;
	private TextViewer mCommand = null;

	public void setCurrentFontSize(int textSize) {
		mTextSize = textSize;
	}

	// å„Ç≈SingletoneÇ…Ç∑ÇÈÅB
	public LineViewManager(int textSize, int width, int height, int mergine,
			int menuWidth) {
		sInstance = this;
		mWidth = width;
		mTextSize = textSize;
		mMergine = mergine;
		mFocusingViewer = newTextViewr();
		mCommand = new TextViewer(StartupCommandBuffer.getStartupCommandBuffer(),textSize, width, mergine);
		((EditableLineView) mCommand.getLineView())
				.setMode(EditableLineView.MODE_EDIT);
		addChild(new LineViewGroup(mFocusingViewer));
		addChild(mCommand);
		addChild(mCircleMenu);
		_circle();
	}

	public TextViewer newTextViewr() {
		return new TextViewer(mTextSize, mWidth, mMergine);
	}

	@Override
	public void addChild(SimpleDisplayObject child) {
		if (child instanceof LineViewGroup) {
			mRoot = (LineViewGroup) child;
		}
		super.addChild(child);
	}

	public LineViewGroup getRoot() {
		return mRoot;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		setRect(graphics.getWidth(), graphics.getHeight());
		_layout();
		int t = mCircleMenu.getMinRadius() / 2;
		// mCommand.setPoint(0, graphics.getHeight()-t);
		// mCommand.setRect(graphics.getWidth(), t);
		// mRoot.setRect(graphics.getWidth(), graphics.getHeight()-t);
		mCommand.setPoint(0, 0);
		mCommand.setRect(graphics.getWidth(), t);
		mRoot.setPoint(0, t);
		mRoot.setRect(graphics.getWidth(), graphics.getHeight() - t);
		super.paint(graphics);
		int xy[] = new int[2];
		getFocusingTextViewer().getGlobalXY(xy);
		int x = xy[0] + 20;
		int y = xy[1] + getFocusingTextViewer().getHeight(false) - 20;
		graphics.setColor(Color.RED);
		graphics.drawText("now focusing", x, y);
		graphics.setColor(Color.BLACK);
		graphics.drawLine(mCommand.getX(), mCommand.getY()+mCommand.getHeight(),
				mCommand.getX()+ getWidth(false), mCommand.getY()+mCommand.getHeight());
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
		mFocusingViewer = textViewer;
		if (mFocusingViewer.getLineView() instanceof CursorableLineView) {
			_circleSelected(((CursorableLineView) (mFocusingViewer
					.getLineView())).getMode());
		}
	}

	public TextViewer getFocusingTextViewer() {
		return mFocusingViewer;
	}

	private boolean _circleSelected(CharSequence title) {
		CursorableLineView mLineView = (CursorableLineView) getFocusingTextViewer()
				.getLineView();
		if (title.equals(CursorableLineView.MODE_VIEW)) {
			mCircleMenu.clearCircleMenu();
			mCircleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
			mCircleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			mCircleMenu.addCircleMenu(0, "Search");
			mLineView.setMode(CursorableLineView.MODE_VIEW);

		} else if (title.equals(CursorableLineView.MODE_SELECT)) {
			mCircleMenu.clearCircleMenu();
			mCircleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
			mCircleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			if (!CursorableLineView.MODE_SELECT.equals(mLineView.getMode())) {
				mLineView.setMode(CursorableLineView.MODE_SELECT);
			}
			mCircleMenu.addCircleMenu(0, "Copy");
		} else if (title.equals("Copy")) {
			mCircleMenu.clearCircleMenu();
			mCircleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
			mCircleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			KyoroApplication.getKyoroApplication().getHanler()
					.post(new Runnable() {
						public void run() {
							try {
								CursorableLineView mLineView = (CursorableLineView) getFocusingTextViewer()
										.getLineView();
								ClipboardManager cm = (ClipboardManager) KyoroApplication
										.getKyoroApplication()
										.getSystemService(
												Context.CLIPBOARD_SERVICE);
								CharSequence copy = ((CursorableLineView) mLineView)
										.copy();
								cm.setText("" + copy);
								((SimpleCircleControllerMenuPlus) mCircleMenu)
										.addCircleMenu(0, "Copy");
								KyoroApplication.showMessage("" + copy);
							} catch (Throwable t) {
								t.printStackTrace();
							}
						}
					});
			return true;
		}
		return false;

	}

	private void _circle() {
		if (mCircleMenu instanceof SimpleCircleControllerMenuPlus) {
			((SimpleCircleControllerMenuPlus) mCircleMenu)
					.setCircleMenuItem(new CircleMenuItem() {
						@Override
						public boolean selected(int id, String title) {
							return _circleSelected(title);
						}
					});
			mCircleMenu.addCircleMenu(0, CursorableLineView.MODE_VIEW);
			mCircleMenu.addCircleMenu(0, CursorableLineView.MODE_SELECT);
			mCircleMenu.addCircleMenu(0, "Search");
		}
		mCircleMenu.setEventListener(new MyCircleControllerEvent());
		mCircleMenu.setColorWhenDefault(COLOR_CIRCLE_DEFAULT);
	}

	private class MyCircleControllerEvent implements CircleControllerAction {
		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerAction.ACTION_MOVE) {
				getFocusingTextViewer().getLineView().setPositionY(
						getFocusingTextViewer().getLineView().getPositionY()
								+ rateDegree * 2);
			}
		}

		public void upButton(int action) {
			if (action == CircleControllerAction.ACTION_PRESSED) {
				getFocusingTextViewer().getLineView()
						.setPositionY(
								getFocusingTextViewer().getLineView()
										.getPositionY() + 1);
			}
		}

		public void downButton(int action) {
			if (action == CircleControllerAction.ACTION_PRESSED) {
				getFocusingTextViewer().getLineView()
						.setPositionY(
								getFocusingTextViewer().getLineView()
										.getPositionY() - 1);
			}
		}
	}

}
