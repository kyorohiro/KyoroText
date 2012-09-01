package info.kyorohiro.helloworld.textviewer.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import info.kyorohiro.helloworld.io.BigLineData;
import info.kyorohiro.helloworld.io.MarkableReader;
import info.kyorohiro.helloworld.io.SimpleTextDecoder;
import info.kyorohiro.helloworld.io.MarkableFileReader;

//
// http://wiki.theory.org/BitTorrentSpecification
// http://bittorrent.org/beps/bep_0003.html
public class BEncoding {

	MarkableReader mMemory = null;//new VirtualMemory();
	public BEncoding(String path) throws FileNotFoundException {
		mMemory = new MarkableFileReader(new File(path), 1024);
	}

	//
	//
	public Object next() throws IOException {
		SimpleTextDecoder textDecoder = new SimpleTextDecoder(Charset.forName("utf8"), mMemory, null);
		if(!textDecoder.isEOF()){
			textDecoder.readCharacter();
		}
		return null;
	}
}
