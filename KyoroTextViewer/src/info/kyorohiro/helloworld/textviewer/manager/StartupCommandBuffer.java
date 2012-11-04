package info.kyorohiro.helloworld.textviewer.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import info.kyorohiro.helloworld.android.adapter.SimpleTypefaceForAndroid;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleTypeface;
import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.util.Utility;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;

public class StartupCommandBuffer extends TextViewer {

	public StartupCommandBuffer(int textSize, int width, int mergine) {
		super(new EmptyLineViewBufferSpecImpl(),textSize, width, mergine);
		if(KyoroSetting.VALUE_LF.equals(KyoroSetting.getCurrentCRLF())){
			getLineView().isCrlfMode(false);
		} else {
			getLineView().isCrlfMode(true);
		}
		readStartupMessage();
	}


//	@Override
//	public void start() {
//		super.start();
//		Context c = KyoroApplication.getKyoroApplication().getApplicationContext();
//		Resources r = c.getResources();
//		Typeface t = Typeface.createFromAsset(c.getAssets(), "sourcecodepro_roman_blackfont.ttf");
//		Typeface t = Typeface.createFromAsset(c.getAssets(), "inconsolata.ttf");
//		getLineView().setSimpleTypeface(new SimpleTypefaceForAndroid(t));
//	}

	
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
}
