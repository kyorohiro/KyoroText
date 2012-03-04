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
import info.kyorohiro.helloworld.stress.service.KilledProcessStarter;
import info.kyorohiro.helloworld.stress.service.KyoroStressService;
import info.kyorohiro.helloworld.stress.task.DeadOrAliveTask;
import info.kyorohiro.helloworld.util.CyclingList;
import info.kyorohiro.helloworld.util.KyoroMemoryInfo;
import android.app.Activity;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class KyoroStressActivity extends Activity {

	private SimpleStage mStage = null;      
	private LineList mList = null;
	private CyclingList<Object> mData = new CyclingList<Object>(100);
	private SimpleCircleController mController = new SimpleCircleController();
	private Thread mKilledProcessManager = null;
	private Thread mStopThread = null;
	public final static String MENU_STOP = "stop";
	public final static String MENU_START = "start";

	
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
    	if(mKilledProcessManager == null || !mKilledProcessManager.isAlive()) {
    		mKilledProcessManager = new Thread(new ProcessStatusChecker());
    		mKilledProcessManager.start();
    	}
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	if(mKilledProcessManager == null || !mKilledProcessManager.isAlive()) {
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
    	if(mKilledProcessManager == null || !mKilledProcessManager.isAlive()) {
    		mKilledProcessManager = new Thread(new ProcessStatusChecker());
    	}
    }    
    
    @Override
    protected void onPause() {
    	super.onPause();
    	mStage.stop();
    	if(mKilledProcessManager != null) {
    		mKilledProcessManager.interrupt();
    		mKilledProcessManager = null;
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
						android.util.Log.v("kiyohiro", "==tapped="+mDatamPrevDown.mID);
						if(!KyoroStressService.START_SERVICE.equals(KyoroSetting.getData(mDatamPrevDown.mID))){
							start(mDatamPrevDown);
						} else {
							stop(mDatamPrevDown);
						}
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
					//android.os.Process.killProcess(i.pid);
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
		KilledProcessStarter task1 = new KilledProcessStarter(null);
		DeadOrAliveTask task2 = new DeadOrAliveTask(KyoroStressActivity.this);
		public void run() {
			try {
				while(true){
					android.util.Log.v("kiyohiro","---task");
					updateStatus();
					Thread.sleep(100);
					task1.run();
					Thread.sleep(100);
					task2.run();
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				android.util.Log.v("kiyohiro","------task e");
			}

		}
	}

	private void startAll() {
		int num = mData.getNumberOfStockedElement();
		Object[] obj = new Object[num];
		mData.getElements(obj, 0, num);
		for(int i=0;i<num;i++) {
			if(obj[i] instanceof MyListDatam) {
				start((MyListDatam)obj[i]);  
			}
		}
	}

	private void stopAll() {
		int num = mData.getNumberOfStockedElement();
		Object[] obj = new Object[num];
		mData.getElements(obj, 0, num);
		for(int i=0;i<num;i++) {
			if(obj[i] instanceof MyListDatam) {
				stop((MyListDatam)obj[i]);  
			}
		}
	}

	private void start(MyListDatam datam) {
		if(datam == null){
			return;
		}
		KyoroSetting.setData(datam.mID, KyoroStressService.START_SERVICE);
		KyoroStressService.startService(datam.mClazz, KyoroApplication.getKyoroApplication(), "start");
		datam.mMessage = "start";
	}
	
	private void stop(MyListDatam datam) {
		if(datam == null){
			return;
		}
		KyoroSetting.setData(datam.mID, KyoroStressService.STOP_SERVICE);
		KyoroStressService.startService(datam.mClazz, KyoroApplication.getKyoroApplication(), "end");
		//KyoroStressService.stopService(datam.mClazz, KyoroApplication.getKyoroApplication());
		datam.mMessage = "end";
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if(mStopThread == null || !mStopThread.isAlive()){
			menu.add(KyoroStressActivity.MENU_START);
			menu.add(KyoroStressActivity.MENU_STOP);
			Toast.makeText(KyoroStressActivity.this, "now working..", Toast.LENGTH_LONG);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item !=null && KyoroStressActivity.MENU_STOP.equals(item.getTitle())) {
			mStopThread = new Thread() {
				public void run() {
					stopAll();
				}
			};
			mStopThread.start();
			return true;
		}
		else if(item !=null && KyoroStressActivity.MENU_START.equals(item.getTitle())){
			mStopThread = new Thread() {
				public void run() {
					startAll();
				}
			};
			mStopThread.start();
			return true;			
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
