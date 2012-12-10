package info.kyorohiro.helloworld.ext.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.ManagedLineViewBuffer;
import java.util.WeakHashMap;

//
// ���̃N���X����肠���@�\����悤�ɂȂ�����A�}�[�P�b�g�ɃA�b�v����B
// 
public class TextBufferManager {

	private static TextBufferManager sManager = new TextBufferManager();
	public static TextBufferManager getInstance() {
		return sManager;
	}
 
	private WeakHashMap<Integer,ManagedLineViewBuffer> mMap = new WeakHashMap<Integer, ManagedLineViewBuffer>();
	

}
