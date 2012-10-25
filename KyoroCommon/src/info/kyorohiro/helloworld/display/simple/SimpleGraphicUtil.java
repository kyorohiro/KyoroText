package info.kyorohiro.helloworld.display.simple;

public class SimpleGraphicUtil {
	public static void fillRect(SimpleGraphics graphics, int w, int h) {
		graphics.startPath();
		graphics.setStyle(SimpleGraphics.STYLE_FILL);
		graphics.moveTo(0, 0);
		graphics.lineTo(0, h);
		graphics.lineTo(w, h);
		graphics.lineTo(w, 0);
		graphics.lineTo(0, 0);
		graphics.endPath();
	}
}
