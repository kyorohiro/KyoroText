package info.kyorohiro.helloworld.textviewer.manager;

import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.ManagedLineViewBuffer;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;

public class CircleControllerEvent implements CircleControllerAction {
	private int mCurrentDegree = -999;
	private boolean mMoved = false;

	public void moveCircle(int action, int degree, int rateDegree) {
		TextViewer viewer = LineViewManager.getManager()
				.getFocusingTextViewer();
		if (CursorableLineView.MODE_EDIT == viewer.getLineView().getMode()) {
			moveCircleAtEditMode(action, degree, rateDegree);
		} else {
			moveCircleAtViewMode(action, degree, rateDegree);
		}
	}

	private int mAcc = 0;
	private final int P=15;

	private void moveCircleAtEditMode(int action, int degree, int rateDegree) {
		if (action == CircleControllerAction.ACTION_PRESSED) {
			mCurrentDegree = degree;
			mMoved = false;
			mAcc = rateDegree;
		}
		if (action == CircleControllerAction.ACTION_RELEASED) {
			if (mMoved != true) {
				move(false);
				mMoved = true;
			}
		}
		if (action == CircleControllerAction.ACTION_MOVE) {
			mAcc += rateDegree;
			int len = mAcc / P;
			if (mAcc > P) {
				for (int i = 0; i < len; i++) {
					move(true);
				}
				mAcc = 0;
			} else if (mAcc < -P) {
				len *= -1;
				for (int i = 0; i < len; i++) {
					move(false);
				}
				mAcc = 0;
			}
			if (Math.abs(mCurrentDegree - degree) > 30) {
				mMoved = true;
			}
		}
	}

	private void move(boolean isReverse) {
		CursorableLineView cv = LineViewManager.getManager()
				.getFocusingTextViewer().getLineView();

		if (-50 < mCurrentDegree && mCurrentDegree < 50) {
			if (!isReverse) {
				cv.front();
			} else {
				cv.back();
			}
		} else if (60 < mCurrentDegree && mCurrentDegree < 120) {
			if (!isReverse) {
				cv.next();
			} else {
				cv.prev();
			}
		} else if (-120 < mCurrentDegree && mCurrentDegree < -60) {
			if (!isReverse) {
				cv.prev();
			} else {
				cv.next();
			}
		} else if (mCurrentDegree < -150 || mCurrentDegree > 150) {
			if (!isReverse) {
				cv.back();
			} else {
				cv.front();
			}
		}
	}

	private void moveCircleAtViewMode(int action, int degree, int rateDegree) {
		if (action == CircleControllerAction.ACTION_MOVE) {
			if (Math.abs(mCurrentDegree - degree) > 20) {
				mCurrentDegree = -999;
			}
			LineViewManager
					.getManager()
					.getFocusingTextViewer()
					.getLineView()
					.setPositionY(
							LineViewManager.getManager()
									.getFocusingTextViewer().getLineView()
									.getPositionY()
									+ rateDegree * 2);
		}
	}

	public void upButton(int action) {
		TextViewer viewer = LineViewManager.getManager()
				.getFocusingTextViewer();
		if (CursorableLineView.MODE_VIEW == viewer.getLineView().getMode()) {
			viewer.getLineView().setPositionY(
					LineViewManager.getManager().getFocusingTextViewer()
							.getLineView().getPositionY() + 1);
		}
	}

	public void downButton(int action) {
		TextViewer viewer = LineViewManager.getManager()
				.getFocusingTextViewer();
		if (CursorableLineView.MODE_VIEW == viewer.getLineView().getMode()) {
			viewer.getLineView().setPositionY(
					LineViewManager.getManager().getFocusingTextViewer()
							.getLineView().getPositionY() - 1);
		}
	}

}