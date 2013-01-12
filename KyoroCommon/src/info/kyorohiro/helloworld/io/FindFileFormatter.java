package info.kyorohiro.helloworld.io;

import info.kyorohiro.helloworld.text.KyoroString;

import java.io.IOException;
import java.util.LinkedList;

public class FindFileFormatter {

	public KyoroString read(SimpleTextDecoder decoder) throws IOException {
		LinkedList<KyoroString> list = new LinkedList<KyoroString>();

		while(!decoder.isEOF()) {
			//KyoroString tmp = null;
			CharSequence _tmp = decoder.decodeLine();
			if(_tmp == null) {return new KyoroString("");};
			list.add((KyoroString)_tmp);
			//android.util.Log.v("kiyo","::"+_tmp);
			if(_tmp.charAt(_tmp.length()-1) == '\n') {
				String str = "";
				for(int i=0;i<list.size();i++) {
					str += ""+list.get(i);
				}
				String[] li = str.split(":::");
				//li[0];
				//li[1];
				KyoroString ret = null;
				if(li.length == 3){
					ret = new KyoroString(li[0]+"\r\n");
				} else {
					ret = new KyoroString(li[0]);	
				}
				ret.setType("find");
				ret.setExtra(li[1]);
				return ret;
			}
		}
		return new KyoroString("");
	}
}
