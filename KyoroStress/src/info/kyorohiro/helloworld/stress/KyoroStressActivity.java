package info.kyorohiro.helloworld.stress;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.LineList;
import info.kyorohiro.helloworld.util.CyclingList;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class KyoroStressActivity extends Activity {

	private SimpleStage mStage = null;      
	private LineList mList = null;
	private CyclingList<Object> mData = new CyclingList<Object>(100);
	private SimpleCircleController mController = new SimpleCircleController();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStage = new SimpleStage(this); 
        mList = new LineList(mData,50);
        mStage.getRoot().addChild(new Layout());
        mStage.getRoot().addChild(mList);
        mStage.getRoot().addChild(mController);
        setContentView(mStage);
        mData.add(new MyListDatam("No00 JavaHeapEater", "not run", Color.YELLOW));
        mData.add(new MyListDatam("No01 JavaHeapEater", "not run", Color.GREEN));
        mData.add(new MyListDatam("No02 JavaHeapEater", "not run", Color.RED));
        mData.add(new MyListDatam("No03 JavaHeapEater", "not run", Color.BLACK));
        mData.add(new MyListDatam("No04 JavaHeapEater", "not run", Color.GRAY));
        mData.add(new MyListDatam("No05 JavaHeapEater", "not run", Color.WHITE));
        mData.add(new MyListDatam("No06 JavaHeapEater", "not run", Color.YELLOW));
        mData.add(new MyListDatam("No07 JavaHeapEater", "not run", Color.GREEN));
        mData.add(new MyListDatam("No08 JavaHeapEater", "not run", Color.RED));
        mData.add(new MyListDatam("No09 JavaHeapEater", "not run", Color.BLACK));
        mData.add(new MyListDatam("No10 JavaHeapEater", "not run", Color.GRAY));
        mData.add(new MyListDatam("No11 JavaHeapEater", "not run", Color.WHITE));
        mData.add(new MyListDatam("No12 JavaHeapEater", "not run", Color.GRAY));
        mData.add(new MyListDatam("No13 JavaHeapEater", "not run", Color.WHITE));
        mData.add(new MyListDatam("No14 JavaHeapEater", "not run", Color.GRAY));
        mData.add(new MyListDatam("No15 JavaHeapEater", "not run", Color.WHITE));
        mData.add(new MyListDatam("No16 JavaHeapEater", "not run", Color.GRAY));
        
        mList.setTemplate(new MyListTemplate());
        mList.setOnListItemUIEvent(new MyListItemUIEvent());

        //
        setController();

    }

    private void setController() {
    	mController.setEventListener(new MyCircleControllerEvent());

    	int deviceWidth = getWindowManager().getDefaultDisplay().getWidth();
    	int deviceHeight = getWindowManager().getDefaultDisplay().getHeight();
    	DisplayMetrics metric = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(metric);
    	float xdpi = metric.xdpi;
    	int radius = (int)(xdpi/3);
    	int deviceMinEdge = deviceWidth;
    	if(deviceWidth < deviceHeight){
    		deviceMinEdge = deviceWidth;
    	} else {
    		deviceMinEdge = deviceHeight;
    	}
    	if(radius > deviceMinEdge/1.5) {
    		radius = (int)(deviceMinEdge/1.5);
    	}

    	mController.setRadius(radius);    	
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	mStage.start();
    }    
    
    @Override
    protected void onPause() {
    	super.onPause();
    	mStage.stop();
    }

    public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			mList.setRect(graphics.getWidth(), graphics.getHeight());
	    	mController.setPoint(graphics.getWidth()-mController.getWidth()/2,
	    			graphics.getHeight()-mController.getHeight()/2);
			graphics.drawBackGround(0xAAAAFF);
		}    	
    }


    public static class MyListDatam extends Object {
    	private String mTitle = "";
    	private String mMessage = "";
    	private int mColor = 0;
    	private boolean mSelected = false;
    	public MyListDatam(String title, String message, int color) {
    		mTitle = title;
    		mMessage = message;
    		mColor = color;
		}
    }

    public static class MyListTemplate extends LineList.ListDatamTemplate {
    	 @Override
    	public void paint(SimpleGraphics graphics) {
    		super.paint(graphics);
    		try {
    			MyListDatam datam =(MyListDatam)getListDatam();
    			graphics.setColor(datam.mColor);
    			graphics.setTextSize(15);
    			if(datam.mSelected) {
    				graphics.setStrokeWidth(6);
    			} else {
    				graphics.setStrokeWidth(2);    				
    			}
    			graphics.drawText(""+datam.mTitle, 10, 20);
    			graphics.drawText(""+datam.mMessage, 35, 35);
    			graphics.drawLine(0, 5, graphics.getWidth(), 5);
    			graphics.drawLine(0, 40, graphics.getWidth(), 40);
    		} catch(ClassCastException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public class MyListItemUIEvent implements LineList.ListItemUIEvent {
    	private MyListDatam mCurrentSelected = null;
    	private MyListDatam mDatamPrevDown = null;

    	@Override
		public void selected(Object obj, int action, int index) {
			if(obj !=null && obj instanceof MyListDatam){
				//
				((MyListDatam) obj).mSelected = true;
				if(mCurrentSelected != null && mCurrentSelected != obj) {
					mCurrentSelected.mSelected = false;
				}
				mCurrentSelected = (MyListDatam)obj;
				
				//
				if(action == MotionEvent.ACTION_DOWN) {
					mDatamPrevDown = ((MyListDatam) obj);
				}
				else if(action == MotionEvent.ACTION_UP) {
					if(mDatamPrevDown == obj) {
						mDatamPrevDown.mMessage = "tap";
					}
				}
				else {
					if(mDatamPrevDown != obj) {
						mDatamPrevDown = null;
					}					
				}
			}
		}    	
    }

	private class MyCircleControllerEvent implements SimpleCircleController.CircleControllerAction {
		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerAction.ACTION_MOVE) {
				mList.setPosition(mList.getPosition() + rateDegree/2);
			}
		}

		public void upButton(int action) {
			mList.setPosition(mList.getPosition() + 1);
		}

		public void downButton(int action) {
			mList.setPosition(mList.getPosition() - 1);
		}
	}
}
