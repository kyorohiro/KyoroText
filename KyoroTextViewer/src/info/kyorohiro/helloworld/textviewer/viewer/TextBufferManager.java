package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.ManagedLineViewBuffer;
import java.util.WeakHashMap;

//
// このクラスが取りあず機能するようになったら、マーケットにアップする。
// 
public class TextBufferManager {

	private static TextBufferManager sManager = new TextBufferManager();
	public static TextBufferManager getInstance() {
		return sManager;
	}
 
	private WeakHashMap<Integer,ManagedLineViewBuffer> mMap = new WeakHashMap<Integer, ManagedLineViewBuffer>();
	

}
