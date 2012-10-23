package info.kyorohiro.helloworld.textviewer.manager;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CyclingList;
import android.graphics.Color;

public class StartupCommandBuffer extends CyclingList<KyoroString> implements LineViewBufferSpec {

	public StartupCommandBuffer(int listSize) {
		super(listSize);
	}

	public static StartupCommandBuffer getStartupCommandBuffer() {
		String[] message = getStartupMessage();
		int color[] = getStartgupMessageColor();
		StartupCommandBuffer startupMessage = new StartupCommandBuffer(100);
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
		String[] message = {".*" };
		return message;
	}

	protected static int[] getStartgupMessageColor() {
		int color[] = {Color.BLUE};
		return color;
	}

}
