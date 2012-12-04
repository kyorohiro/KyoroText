package info.kyorohiro.helloworld.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public class Utility {
	public static void copyTransfer(File srcPath, File destPath) throws IOException {
	    FileChannel srcChannel = new FileInputStream(srcPath).getChannel();
	    FileChannel destChannel = new FileOutputStream(destPath).getChannel();
	    try {
	        srcChannel.transferTo(0, srcChannel.size(), destChannel);
	    } finally {
	        srcChannel.close();
	        destChannel.close();
	    }

	}

}
