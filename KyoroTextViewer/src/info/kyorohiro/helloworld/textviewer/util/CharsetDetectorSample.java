package info.kyorohiro.helloworld.textviewer.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

public class CharsetDetectorSample {

	LinkedList<String> foundedCharset = new LinkedList<String>();
	public synchronized void detect(File file) {
		foundedCharset.clear();
		int lang = nsPSMDetector.ALL;
//		int lang = nsPSMDetector.JAPANESE;
		nsDetector detector = new nsDetector(lang);
		InputStream is = null;
		detector.Init(new Observer());

		boolean done = false ;
		boolean isAscii = true ;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[1024] ;
			int len;
			int num = 0;
			while((len=is.read(buf, 0, buf.length))!=-1){
				// Check if the stream is only ascii.
				if (isAscii) {
					isAscii = detector.isAscii(buf,len);
				}
				// DoIt if non-ascii and not done yet.
				if (!isAscii && !done) {
		 		    done = detector.DoIt(buf,len, false);
		 		    if(done){
		 		    	android.util.Log.v("found charset","done!!");
		 		    }
				}
				if(num<2){
					num++;
				} else {
					break;
				}
			}
			detector.DataEnd();
			if(isAscii) {
				foundedCharset.add("ascii");
			}
			android.util.Log.v("isAscii",""+isAscii);
			String[] prob = detector.getProbableCharsets();
			for(String p : prob) {
				android.util.Log.v("prob",""+p);
				foundedCharset.add(p);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String[] getResult() {
		int size = foundedCharset.size();
		String[] ret = {"utf8"};
		if(size >0){
			ret = new String[size];
			foundedCharset.toArray(ret);
		}
		return ret;
	}

	class Observer implements nsICharsetDetectionObserver {
		@Override
		public void Notify(String arg0) {
			if(foundedCharset.contains(arg0)){
				foundedCharset.add(arg0);
				android.util.Log.v("found charset",""+arg0);
			}
		}
	}
}
