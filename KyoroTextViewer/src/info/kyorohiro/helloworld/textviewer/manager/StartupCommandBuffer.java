package info.kyorohiro.helloworld.textviewer.manager;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.util.CyclingList;
import android.content.Context;
import android.graphics.Color;

//
// ãﬂÅXÇ≈çÌèúÇ∑ÇÈÅB
public class StartupCommandBuffer extends TextViewer {

	public StartupCommandBuffer(int textSize, int width, int mergine) {
		super(new EmptyLineViewBufferSpecImpl(),textSize, width, mergine);
		readStartupMessage();
	}

	public void readStartupMessage() {
		try {
			Context c = KyoroApplication.getKyoroApplication().getApplicationContext();
			File dir = c.getFilesDir();
			File filePathOfStartMessage = new File(dir, "startup_message.txt");
			createStartupMessageIfNonExist(filePathOfStartMessage);
			readFile(filePathOfStartMessage);
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
