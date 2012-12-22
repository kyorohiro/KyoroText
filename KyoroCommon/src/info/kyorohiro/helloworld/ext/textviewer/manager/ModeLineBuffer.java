package info.kyorohiro.helloworld.ext.textviewer.manager;

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
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;

// now creating 
public class ModeLineBuffer extends TextViewer {

	public static final String GUARD = "guard";
	public static final String UNGUARD = "unguard";
	private SimpleSwitchButton mFitButton = null;
	private SimpleSwitchButton mGuardButton = null;

	public ModeLineBuffer(int textSize, int width, int mergine, boolean message) {
		super(new EmptyLineViewBufferSpecImpl(400),textSize, width, mergine,
				LineViewManager.getManager().getFont(),//new SimpleFontForAndroid(),
				LineViewManager.getManager().getCurrentCharset());
		if(LineViewManager.getManager().currentBrIsLF()){
			getLineView().isCrlfMode(false);
		} else {
			getLineView().isCrlfMode(true);
		}
		if(message) {
			readStartupMessage();
		}
		addChild(mFitButton = new SimpleSwitchButton("fit", 1, new FitAction(this)));
		addChild(mGuardButton = new SimpleSwitchButton("guard", 3, new GuardAction(this)));
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
		if(!IsExtraUI()) {
			mFitButton.isVisible(false);
			mGuardButton.isVisible(false);
		} else {
			mFitButton.isVisible(true);
			mGuardButton.isVisible(true);
		}
		super.paintGroup(graphics);
		if(!IsExtraUI()) {
			return;
		}
		if(isGuard()) {
			graphics.drawText(GUARD, graphics.getTextSize(), graphics.getTextSize());
		} else {
			graphics.drawText(UNGUARD, graphics.getTextSize(), graphics.getTextSize());			
		}
	}
	
	public void readStartupMessage() {
		try {
			File dir = LineViewManager.getManager().getFilesDir();
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
			File datadata = LineViewManager.getManager().getFilesDir();
			File parent = file.getParentFile();
			File grandpa = parent.getParentFile();
			if(!datadata.equals(parent)
					&&!(grandpa!=null&&grandpa.equals(datadata))){
				LineViewManager.getManager().setCurrentFile(file.getAbsolutePath());
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
