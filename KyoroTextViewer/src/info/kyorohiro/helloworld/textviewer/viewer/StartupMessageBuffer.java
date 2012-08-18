package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.util.CyclingList;
import android.graphics.Color;

public class StartupMessageBuffer extends CyclingList<LineViewData> implements LineViewBufferSpec {

	public StartupMessageBuffer(int listSize) {
		super(listSize);
	}

	public static StartupMessageBuffer getStartupMessageBuffer() {
		String[] message = getStartupMessage();
		int color[] = getStartgupMessageColor();
		StartupMessageBuffer startupMessage = new StartupMessageBuffer(100);
		for (int i = 0; i < message.length; i++) {
			String m = message[i];
			int crlf = LineViewData.INCLUDE_END_OF_LINE;
			if (!m.endsWith("\n")) {
				crlf = LineViewData.EXCLUDE_END_OF_LINE;
			}
			startupMessage.add(new LineViewData(m, color[i], crlf));
		}
		return startupMessage;
	}

	@Override
	public BreakText getBreakText() {
		return new MyBreakText();
	}

	protected static String[] getStartupMessage() {
		String[] message = { "Please open file\n",
				"Sorry, this application is pre-alpha version",
				"Testing and Developing.. now",
				"Please mail kyorohiro.android@gmail.com, ",
				"If you have particular questions or comments, ",
				"please don't hesitate to contact me. Thank you. \n" };
		return message;
	}

	protected static int[] getStartgupMessageColor() {
		int color[] = {
				Color.BLUE, Color.BLUE,
				Color.RED, Color.RED, 
				Color.RED, Color.RED, };
		return color;
	}

}
