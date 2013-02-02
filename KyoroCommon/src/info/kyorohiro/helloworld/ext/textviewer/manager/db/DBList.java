package info.kyorohiro.helloworld.ext.textviewer.manager.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.simple.sample.EmptyBreakText;
import info.kyorohiro.helloworld.display.simple.sample.EmptySimpleFont;
import info.kyorohiro.helloworld.io.MarkableFileReader;
import info.kyorohiro.helloworld.io.SimpleTextDecoder;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;

public class DBList {
	private SimpleApplication mApplication = null;
	private LinkedList<Item> mList = null;
	public static final String sLIST_TXT = "list.txt";
	
	public DBList(SimpleApplication application) {
		mApplication = application;
		mList = new LinkedList<DBList.Item>();
	}

	public int getEmptyID() {
		Collections.sort(mList, new ItemComparator());
		int id =0;
		for(Item item :mList) {
			if(id!=item.id()){
				break;
			}
			id = item.id()+1;
		}
		return id;
	}

	public void add(Item item) {
		remove(item.id());
		mList.add(item);
	}

	public void remove(int id) {
		int i = 0;
		for(i=0;id<mList.size();i++) {
			Item item = mList.get(i);
			if(item.id() == id) {
				mList.remove(i);
				break;
			}
		}
	}

	private File getPath() throws IOException {
		return BufferDB.getPath(mApplication, sLIST_TXT);
	}

	public void file2List() throws IOException {
		MarkableFileReader reader = null;
		try {
			mList.clear();
			File listTxt = getPath();
			int writeCash = 0;
			int readCash  = 512;
			reader = new MarkableFileReader(new VirtualFile(listTxt, writeCash), readCash);
			SimpleTextDecoder decoder = new SimpleTextDecoder(Charset.forName("utf8"), reader, new EmptyBreakText(new EmptySimpleFont(), Integer.MAX_VALUE));
			while(!decoder.isEOF()) {
				KyoroString line = (KyoroString)decoder.decodeLine();
				if(line.length() == 0) {
					android.util.Log.v("kiyo","----"
				+decoder.getFilePointer()+","+decoder.length());
					if(decoder.isEOF()) {
						break;
					} else {
						continue;
					}
				}
				char[] tmp = line.getChars();
				int i = 0;
				for(i=0;i<tmp.length;i++) {
					if(tmp[i] == ',') {
						break;
					}
				}
				int idEnd = i;
				int labelStart = i+1;
				int id = Integer.parseInt(""+new String(tmp, 0, idEnd));
				String label = new String(tmp, labelStart, tmp.length-i);
				mList.add(new Item(id, label));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				reader.close();
			}
		}
	}

	public void list2File() throws IOException {
		File listTxt = getPath();
		VirtualFile vFile = new VirtualFile(listTxt, 512);
		try {
			for(Item item:mList) {
				String chunk = ""+item.id()+","+item.label().replaceAll("\n|\r\n", "")+"\n";
				vFile.addChunk(chunk.getBytes("utf8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if(vFile != null) {
				vFile.syncWrite();
				vFile.close();
			}
		}
	}

	//
	// 何度も呼ばないで
	public Item[] getItem() {
		Item[] item = new Item[mList.size()];
		return  mList.toArray(item);
	}

	public static class Item {
		private String mLabel = null;
		private int mId = 0;

		public Item(int id, String label) {
			mId = id;
			mLabel = label;
			android.util.Log.v("kiyo","ITEM#"+id+","+label);
		}

		public int id() {
			return mId;
		}

		public String label() {
			return mLabel;	
		}

		public String idAsString() {
			return ""+mId;
		}
	}


	static class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item lhs, Item rhs) {
			return lhs.id()-rhs.id();
		}
	}
}
