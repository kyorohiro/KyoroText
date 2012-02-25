package info.kyorohiro.helloworld.android.adapter;

import android.graphics.Bitmap;
import android.graphics.Rect;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;

public class SimpleImageForAndroid extends SimpleDisplayObject {

	private Bitmap mBitmap = null;
	//
	// 開放するタイミングだとか、重複して同じImageを生成したくないので
	// 管理する仕組みは作る必要がある。
	public SimpleImageForAndroid(Bitmap bitmap) {
		mBitmap = bitmap;
		setRect(bitmap.getWidth(), bitmap.getHeight());
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		if(graphics instanceof SimpleGraphicsForAndroid) {
	        int imageW = mBitmap.getWidth();
	        int imageH = mBitmap.getHeight();
	        int w = getWidth();
	        int h = getHeight();
	        Rect src = new Rect(0, 0, imageW, imageH);
	        Rect dst = new Rect(0, 200, imageW*2, 200 + imageH*2);
			((SimpleGraphicsForAndroid) graphics).getCanvas().drawBitmap(mBitmap, src, dst, null);
		}
	}
	
	@Override
	public void dispose() {
		// 
		// このタイミングでリソースを開放するのは、仮実装
		// Imageを使いまわすような利用方法を間がると適当ではない。
		if(mBitmap != null && mBitmap.isRecycled()){
			mBitmap.recycle();
		}
		super.dispose();
	}
}
