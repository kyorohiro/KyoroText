package info.kyorohiro.helloworld.display.simple;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SimpleImage {
	private Bitmap mImage = null;
	public SimpleImage(InputStream is) {
		mImage = BitmapFactory.decodeStream(is);
	}
	
	public Bitmap getImage() {
		return mImage;
	}
}
