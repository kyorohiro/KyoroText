package info.kyorohiro.helloworld.textviewer.manager;

import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;

public class MyCircleControllerEvent implements CircleControllerAction {
	private int mCurrentDegree = -999; 
	public void moveCircle(int action, int degree, int rateDegree) {
		if(action == CircleControllerAction.ACTION_PRESSED) {
			mCurrentDegree = degree;
		}
		if(action == CircleControllerAction.ACTION_RELEASED) {
			if(mCurrentDegree != -999){
				LineView v = LineViewManager.getManager().getFocusingTextViewer().getLineView();
				CursorableLineView cv = (CursorableLineView)v;
				
				if(-50<mCurrentDegree&&mCurrentDegree<50){
					cv.front();
				}
				else if(60<mCurrentDegree&&mCurrentDegree<120) {
					cv.next();
				}
				else if(-120<mCurrentDegree&&mCurrentDegree<-60) {
					cv.prev();
				}
				else if(mCurrentDegree<-150||mCurrentDegree>150) {
					cv.back();
				}
//				android.util.Log.v("kiyo","degree2="+mCurrentDegree);				
			}
		}
		if (action == CircleControllerAction.ACTION_MOVE) {
			if(Math.abs(mCurrentDegree-degree)>20){
				mCurrentDegree = -999;
			}
			LineViewManager.getManager().getFocusingTextViewer().getLineView().setPositionY(
					LineViewManager.getManager().getFocusingTextViewer().getLineView().getPositionY()
							+ rateDegree * 2);
		}
	}

	public void upButton(int action) {
	}

	public void downButton(int action) {
	}
}