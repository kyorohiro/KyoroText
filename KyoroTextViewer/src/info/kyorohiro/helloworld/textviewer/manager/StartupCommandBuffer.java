package info.kyorohiro.helloworld.textviewer.manager;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.util.CyclingList;
import android.graphics.Color;

public class StartupCommandBuffer extends CyclingList<LineViewData> implements LineViewBufferSpec {

	public StartupCommandBuffer(int listSize) {
		super(listSize);
	}

	public static StartupCommandBuffer getStartupCommandBuffer() {
		String[] message = getStartupMessage();
		int color[] = getStartgupMessageColor();
		StartupCommandBuffer startupMessage = new StartupCommandBuffer(100);
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
		String[] message = {".*" };
		return message;
	}

	protected static int[] getStartgupMessageColor() {
		int color[] = {Color.BLUE};
		return color;
	}

}
