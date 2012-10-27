package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.ManagedLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineViewBuffer;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.text.KyoroString;

import java.lang.ref.WeakReference;
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
