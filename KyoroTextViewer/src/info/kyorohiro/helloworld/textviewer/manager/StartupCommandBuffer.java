package info.kyorohiro.helloworld.textviewer.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.ObjectInputStream.GetField;

import info.kyorohiro.helloworld.android.adapter.SimpleTypefaceForAndroid;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleMotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleTypeface;
import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.util.Utility;
import android.R.dimen;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;

public class StartupCommandBuffer extends TextViewer {

	public static final String GUARD = "guard";
	public static final String UNGUARD = "unguard";

	public StartupCommandBuffer(int textSize, int width, int mergine) {
		super(new EmptyLineViewBufferSpecImpl(),textSize, width, mergine);
		if(KyoroSetting.VALUE_LF.equals(KyoroSetting.getCurrentCRLF())){
			getLineView().isCrlfMode(false);
		} else {
			getLineView().isCrlfMode(true);
		}
		readStartupMessage();
		addChild(new Button(this));
	}

	@Override
	public void paintGroup(SimpleGraphics graphics) {
		super.paintGroup(graphics);
		if(isGuard()) {
			graphics.drawText(GUARD, graphics.getTextSize(), graphics.getTextSize());
		} else {
			graphics.drawText(UNGUARD, graphics.getTextSize(), graphics.getTextSize());			
		}
	}
/*
	@Override
	public void start() {
		super.start();
		Context c = KyoroApplication.getKyoroApplication().getApplicationContext();
		Resources r = c.getResources();
//		Typeface t = Typeface.createFromAsset(c.getAssets(), "sourcecodepro_roman_blackfont.ttf");
//		Typeface t = Typeface.createFromAsset(c.getAssets(), "inconsolata.ttf");
		Typeface t = Typeface.createFromAsset(c.getAssets(), "xano_mincho_u32.ttf");
		getLineView().setSimpleTypeface(new SimpleTypefaceForAndroid(t));
	}*/

	
	public void readStartupMessage() {
		try {
			Context c = KyoroApplication.getKyoroApplication().getApplicationContext();
			File dir = c.getFilesDir();
			File filePathOfStartMessage = new File(dir, "startup_message.txt");
			createStartupMessageIfNonExist(filePathOfStartMessage);
			readFile(filePathOfStartMessage, false);
//			readFile(filePathOfStartMessage, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createStartupMessageIfNonExist(File f) throws IOException {
		if(f.exists()) {
			return;
		}
		f.createNewFile();
		FileOutputStream output = new FileOutputStream(f);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
		for(String s : message) {
			writer.write(s);
		}
		writer.close();
		output.close();
	}

	String[] message = {
			"Sorry, this application is pre-alpha version\n",
			"Testing and Developing.. now\n",
			"Please mail kyorohiro.android@gmail.com, \n",
			"If you have particular questions or comments, \n",
			"please don't hesitate to contact me. Thank you. \n" };
	
	public static class Button extends SimpleDisplayObject {
		private StartupCommandBuffer mBuffer = null;
		private boolean mOn = false;
		public Button(StartupCommandBuffer buffer) {
			mBuffer = buffer;
			mOn = mBuffer.getLineView().fittableToView();
		}
		@Override
		public void paint(SimpleGraphics graphics) {
			if(mOn) {
				graphics.setColor(SimpleGraphicUtil.parseColor("#FFFF0000"));
			} else {
				graphics.setColor(SimpleGraphicUtil.parseColor("#FFFFFF00"));				
			}
			int w = ((SimpleDisplayObject)getParent()).getWidth();
			int bw = w/20;
			graphics.drawLine(w-bw*3, bw, w-bw, bw);
			graphics.drawLine(w-bw*3, (int)(bw*1.3), w-bw, (int)(bw*1.3));
		}
		
		private boolean isContain(int x, int y) {
			int w = ((SimpleDisplayObject)getParent()).getWidth();
			int bw = w/20;
			int sx = w-bw*4;
			int sy = (int)(bw*0.8);
			int ex = w-bw;
			int ey =(int)(bw*1.4);
			if(sx<x&&x<ex) {
				if(sy<y&&y<ey){
					return true;
				}
			}
			//android.util.Log.v("aaa","sx="+sx+",sy="+sy);
			//android.util.Log.v("aaa","ex="+ex+",ey="+ey);
			return false;
		}

		private void onTouched() {
			boolean  b = mBuffer.getLineView().fittableToView();
			mBuffer.getLineView().fittableToView(mOn=!b);
		}

		private boolean mIsTouched = false;
		@Override
		public boolean onTouchTest(int x, int y, int action) {
			boolean isContain = isContain(x, y);
			//android.util.Log.v("aaa","x="+x+",y="+y+"b="+isContain);
			//mIsTouched = isContain;
			if(!isContain){
				mIsTouched = false;
				return false;
			}
			switch(action) {
			case SimpleMotionEvent.ACTION_DOWN:
				mIsTouched = true;
				break;
			case SimpleMotionEvent.ACTION_MOVE:
				break;
			case SimpleMotionEvent.ACTION_UP:
				if(mIsTouched) {
					onTouched();
				}
				break;
			}
			return super.onTouchTest(x, y, action);
		}
	}
}
