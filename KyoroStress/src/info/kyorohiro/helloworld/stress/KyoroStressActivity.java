package info.kyorohiro.helloworld.stress;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.LineList;
import info.kyorohiro.helloworld.stress.service.BigEater000Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater001Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater002Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater003Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater004Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater005Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater006Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater007Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater008Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater009Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater010Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater011Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater012Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater013Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater014Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater015Gouki;
import info.kyorohiro.helloworld.stress.service.BigEater016Gouki;
import info.kyorohiro.helloworld.stress.service.KilledProcessStarter;
import info.kyorohiro.helloworld.stress.service.KyoroStressService;
import info.kyorohiro.helloworld.util.CyclingList;
import info.kyorohiro.helloworld.util.KyoroMemoryInfo;
import android.app.Activity;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class KyoroStressActivity extends Activity {

	private SimpleStage mStage = null;      
	private LineList mList = null;
	private CyclingList<Object> mData = new CyclingList<Object>(100);
	private SimpleCircleController mController = new SimpleCircleController();
	private Thread mKilledProcessManager = null;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStage = new SimpleStage(this); 
        mList = new LineList(mData,50);
        mStage.getRoot().addChild(new Layout());
        mStage.getRoot().addChild(mList);
        mStage.getRoot().addChild(mController);
        setContentView(mStage);
 
        int len = KyoroStressService.JavaHeapEater.length;
        for(int i=0;i<len;i++){
        	Class clazz = KyoroStressService.JavaHeapEater[i];
        	String id = KyoroStressService.ServiceProcessName[i];
            mData.add(new MyListDatam(clazz, id, getNickName(clazz),"--initilize---", getColor(clazz)));	
        }
 
        mList.setTemplate(new MyListTemplate());
        mList.setOnListItemUIEvent(new MyListItemUIEvent());

        //
        setController();

        //         
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	if(mKilledProcessManager == null) {
    		mKilledProcessManager = new Thread(new ProcessStatusChecker());
    		mKilledProcessManager.start();
    	}

    }
    
    public String getNickName(Class clazz) {
    	Method method;
    	try {
    		method = clazz.getMethod("getNickName", null);
    	} catch (SecurityException e) {
    		throw new RuntimeException(e);
    	} catch (NoSuchMethodException e) {
    		throw new RuntimeException(e);
    	}

    	String ret=""; 
    	try {
    		ret = (String)method.invoke(clazz, null);
    	} catch (IllegalArgumentException e) {
    		e.printStackTrace();
    	} catch (IllegalAccessException e) {
    		e.printStackTrace();
    	} catch (InvocationTargetException e) {
    		e.printStackTrace();
    	}
    	return ret;
    }


    public Integer getColor(Class clazz) {
    	Method method;
    	try {
    		method = clazz.getMethod("getColor", null);
    	} catch (SecurityException e) {
    		throw new RuntimeException(e);
    	} catch (NoSuchMethodException e) {
    		throw new RuntimeException(e);
    	}

    	Integer ret= Color.WHITE; 
    	try {
    		ret = (Integer)method.invoke(clazz, null);
    	} catch (IllegalArgumentException e) {
    		e.printStackTrace();
    	} catch (IllegalAccessException e) {
    		e.printStackTrace();
    	} catch (InvocationTargetException e) {
    		e.printStackTrace();
    	}
    	return ret;
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
    	if(mKilledProcessManager == null) {
    		mKilledProcessManager = new Thread(new ProcessStatusChecker());
    	}
    }    
    
    @Override
    protected void onPause() {
    	super.onPause();
    	mStage.stop();
    	if(mKilledProcessManager != null) {
    		mKilledProcessManager.interrupt();
    	}
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
    	private Class mClazz = null;
    	private String mID = "";
    	public MyListDatam(Class clazz, String id, String title, String message, int color) {
    		mID = id;
    		mTitle = title;
    		mMessage = message;
    		mColor = color;
    		mClazz = clazz;
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
						// action
						//------------------
						android.util.Log.v("kiyohiro", "===id="+mDatamPrevDown.mID);
						if(!KyoroStressService.START_SERVICE.equals(KyoroSetting.getData(mDatamPrevDown.mID))){
							KyoroSetting.setData(mDatamPrevDown.mID, KyoroStressService.START_SERVICE);
							KyoroStressService.startService(mDatamPrevDown.mClazz, KyoroApplication.getKyoroApplication(), "start");
							mDatamPrevDown.mMessage = "start";
						} else {
							KyoroSetting.setData(mDatamPrevDown.mID, KyoroStressService.STOP_SERVICE);
							KyoroStressService.startService(mDatamPrevDown.mClazz, KyoroApplication.getKyoroApplication(), "end");
							mDatamPrevDown.mMessage = "end";
						}
					}
				}
				else {
					if(mDatamPrevDown != obj) {
						mDatamPrevDown = null;
					}					
				}
				
				// test
				KyoroMemoryInfo m = new KyoroMemoryInfo();
				List<RunningAppProcessInfo> a = m.getRunningAppList(KyoroStressActivity.this);
				for(RunningAppProcessInfo b : a) {
					String p = b.processName;
					if(p.matches(".*kyorohiro.*")){
						android.util.Log.v("kiyohiro.info","p="+p);
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

	public void updateStatus() {
		int len = KyoroStressService.JavaHeapEater.length;
		for (int i=0;i<len;i++) {
			task((MyListDatam)mData.get(i),KyoroStressService.JavaHeapEater[i],KyoroStressService.ServiceProcessName[i]);
		}		
	}
	private void task(MyListDatam datam, Class clazz, String processName) {
		KyoroMemoryInfo infos = new KyoroMemoryInfo();
		List<RunningAppProcessInfo> list = null;
		list = infos.getRunningAppList(KyoroApplication.getKyoroApplication());
		String c = KyoroApplication.getKyoroApplication().getApplicationContext().getPackageName()+":"+processName;

		for(RunningAppProcessInfo i : list) {
			String p = i.processName;
			if(p.equals(c)){
				// ë∂ç›Ç∑ÇÈèÍçáÇÕ
				// process kill Ç©Ç«Ç§Ç©Ç≈ï™äÚ
				if(KyoroStressService.START_SERVICE.equals(KyoroSetting.getData(processName))){
					datam.mMessage = "task is alive";
				} else {
					datam.mMessage = "kill task now..";
					KyoroStressService.startService(clazz, KyoroApplication.getKyoroApplication(), "end");
				}

				return;
			}
		}
		if(KyoroStressService.START_SERVICE.equals(KyoroSetting.getData(processName))){
			datam.mMessage = "task is killed by pf";
		} else {
			datam.mMessage = "task is end";
		}

	}

	public class ProcessStatusChecker implements Runnable {
		KilledProcessStarter task = new KilledProcessStarter();
		public void run() {
			try {
				while(true){
					updateStatus();
					Thread.sleep(200);
					task.run();
					Thread.sleep(400);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
