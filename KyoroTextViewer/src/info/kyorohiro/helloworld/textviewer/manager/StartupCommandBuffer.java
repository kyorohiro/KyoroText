package info.kyorohiro.helloworld.textviewer.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

//import info.kyorohiro.helloworld.pfdep.android.adapter.SimpleFontForAndroid;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.sample.SimpleSwitchButton;
import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import android.content.Context;

public class StartupCommandBuffer extends TextViewer {

	public static final String GUARD = "guard";
	public static final String UNGUARD = "unguard";

	public StartupCommandBuffer(int textSize, int width, int mergine) {
		super(new EmptyLineViewBufferSpecImpl(400),textSize, width, mergine,
				LineViewManager.getManager().getFont(),//new SimpleFontForAndroid(),
				KyoroSetting.getCurrentCharset());
		if(KyoroSetting.VALUE_LF.equals(KyoroSetting.getCurrentCRLF())){
			getLineView().isCrlfMode(false);
		} else {
			getLineView().isCrlfMode(true);
		}
		readStartupMessage();
		addChild(new SimpleSwitchButton("fit", 1, new FitAction(this)));
		addChild(new SimpleSwitchButton("guard", 3, new GuardAction(this)));
	}
	public class FitAction implements SimpleSwitchButton.SwithAction {
		public TextViewer mViewer = null;
		public FitAction(TextViewer viewer) {
			mViewer = viewer;
		}
		@Override
		public boolean on() {
			return mViewer.getLineView().fittableToView();
		}
		@Override
		public void on(boolean value) {
			mViewer.getLineView().fittableToView(value);
		}		
	}

	public class GuardAction implements SimpleSwitchButton.SwithAction {
		public TextViewer mViewer = null;
		public GuardAction(TextViewer viewer) {
			mViewer = viewer;
		}
		@Override
		public boolean on() {
			return mViewer.isGuard();
		}
		@Override
		public void on(boolean value) {
			mViewer.isGuard(value);
		}		
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
	
	public void readStartupMessage() {
		try {
			Context c = KyoroApplication.getKyoroApplication().getApplicationContext();
			File dir = c.getFilesDir();
			File filePathOfStartMessage = new File(dir, "startup_message.txt");
			createStartupMessageIfNonExist(filePathOfStartMessage);
			readFile(filePathOfStartMessage, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean readFile(File file, boolean updataCurrentPath)
			throws FileNotFoundException, NullPointerException {
		// todo following code dependent application layer.
		// refactring target
		if(updataCurrentPath){
			File datadata = KyoroApplication.getKyoroApplication().getFilesDir();
			File parent = file.getParentFile();
			File grandpa = parent.getParentFile();
			if(!datadata.equals(parent)
					&&!(grandpa!=null&&grandpa.equals(datadata))){
				KyoroSetting.setCurrentFile(file.getAbsolutePath());
			}
		}
		return super.readFile(file, updataCurrentPath);
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
