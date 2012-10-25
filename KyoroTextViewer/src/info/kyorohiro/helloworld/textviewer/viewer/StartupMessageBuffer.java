package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CyclingList;
import android.graphics.Color;

public class StartupMessageBuffer extends CyclingList<KyoroString> implements LineViewBufferSpec {

	public StartupMessageBuffer(int listSize) {
		super(listSize);
	}

	public static StartupMessageBuffer getStartupMessageBuffer() {
		String[] message = getStartupMessage();
		int color[] = getStartgupMessageColor();
		StartupMessageBuffer startupMessage = new StartupMessageBuffer(100);
		for (int i = 0; i < message.length; i++) {
			String m = message[i];
			startupMessage.add(new KyoroString(m, color[i]));
		}
		return startupMessage;
	}

	@Override
	public BreakText getBreakText() {
		return new MyBreakText();
	}

	protected static String[] getStartupMessage() {
		String[] message = {
				"Sorry, this application is pre-alpha version",
				"Testing and Developing.. now",
				"Please mail kyorohiro.android@gmail.com, ",
				"If you have particular questions or comments, ",
				"please don't hesitate to contact me. Thank you. \n" };
		return message;
	}

	protected static int[] getStartgupMessageColor() {
		int color[] = {
				Color.BLUE,
				Color.RED, Color.RED, 
				Color.RED, Color.RED, };
		return color;
	}

	@Override
	public void isSync(boolean isSync) {
		// í‚ÉSYNC
	}

}
