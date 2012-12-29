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
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ModeLineTask;

// now creating 
public class ModeLineBuffer extends TextViewer {

	public static final String MODE_LINE_BUFFER = "mode_line_buffer";
	private ModeLineTask mTask = null;

	public ModeLineBuffer(int textSize, int width, int mergine, boolean message) {
		super(new EmptyLineViewBufferSpecImpl(400),textSize, width, mergine,
				LineViewManager.getManager().getFont(),//new SimpleFontForAndroid(),
				LineViewManager.getManager().getCurrentCharset());
		if(LineViewManager.getManager().currentBrIsLF()){
			getLineView().isCrlfMode(false);
		} else {
			getLineView().isCrlfMode(true);
		}
		getLineView().setMode(MODE_LINE_BUFFER);
	}

	public void startModeLineTask(ModeLineTask task) {
		mTask = task;
		mTask.begin();
	}
	
}
