package info.kyorohiro.helloworld.pdf.pdflexer;

import info.kyorohiro.helloworld.io.BigLineData;
import info.kyorohiro.helloworld.io.VirtualMemory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//
// if this library use bic size text. 
// library could not load all text to memory.
// this class load a part text to memory. other save external storage. 
//
public class Source {
	private VirtualMemory mLineData = null;
	private long mCurrentPosition = 0;
	public static Pattern sSpace = Pattern.compile("\\s");
	Pattern regex = Pattern.compile(".");

	public Source(String path) {
		try {
			mLineData = new VirtualMemory(new File(path), 2048);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	// progress pointer, when mactch pattern
	//Å@else not move pointer; 
	public boolean muchHead(SourcePattern pattern) {		
		boolean ret = false;

		try {
			mLineData.seek(mCurrentPosition);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[Source#0001] unexpected error");
		}

		ret = pattern.matchHead(mLineData);
		if(!ret) {
			pattern.back(mLineData);
		}

		try {
			mCurrentPosition = mLineData.getFilePointer();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[Source#0002] unexpected error");
		}
		return ret;
	}

	public long getCurrentPointer() {
		return mCurrentPosition;
	}

	public void setCurrentPointer(long position) {
		try {
			mLineData.seek(position);
			mCurrentPosition = position;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
