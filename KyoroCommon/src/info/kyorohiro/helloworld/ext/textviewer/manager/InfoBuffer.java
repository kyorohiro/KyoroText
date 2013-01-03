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
import info.kyorohiro.helloworld.display.widget.lineview.sample.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ModeLineTask;
import info.kyorohiro.helloworld.text.KyoroString;

// now creating 
public class InfoBuffer extends TextViewer {

	public static final String INFO_BUFFER = CursorableLineView.MODE_EDIT+" info plus";

	public InfoBuffer(int textSize, int width, int mergine, boolean message) {
		super(new EmptyLineViewBufferSpecImpl(400, BufferManager.getManager().getFont()),textSize, width, mergine,
				BufferManager.getManager().getFont(),//new SimpleFontForAndroid(),
				BufferManager.getManager().getCurrentCharset());
		//android.util.Log.v("kiyo","new");
		if(BufferManager.getManager().currentBrIsLF()){
			getLineView().isCrlfMode(false);
		} else {
			getLineView().isCrlfMode(true);
		}
		getLineView().setConstantMode(INFO_BUFFER);
	}

	
}
