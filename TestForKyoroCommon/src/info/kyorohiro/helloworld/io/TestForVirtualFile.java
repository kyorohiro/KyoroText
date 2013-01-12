package info.kyorohiro.helloworld.io;

import java.io.File;
import java.io.FileNotFoundException;

import android.os.Environment;

import junit.framework.TestCase;

public class TestForVirtualFile extends TestCase {

	public void log(String message) {
		android.util.Log.v("kiyo", "TestForViertualFile:"+message);
	}

	public void testHello() {
		log("testHello");
	}

	public void testFirst() {
		File testPath = new File(getFile(),"__001__.txt");
		try {
			testPath.delete();
			testPath.createNewFile();
			// 0 byte.
			{
				VirtualFile vf = new VirtualFile(testPath, 200);
				byte[] buffer = new byte[101];
				for(int i=0;i<buffer.length;i++) {
					buffer[0] = 0;
				}
				int ret = vf.read(buffer);
				assertEquals(-1, ret);
				for(int i=0;i<buffer.length;i++) {
					assertEquals(0, buffer[0]);
				}
			}
			//@1byte
			{
				byte[] add = {1, 2, 3, 4, 5};
				VirtualFile vf = new VirtualFile(testPath, 200);
				byte[] read = new byte[6];
				vf.addChunk(add, 0, 5);
				assertEquals(0, vf.getStartChunk());
				assertEquals(1, vf.getChunkCash(0));
				assertEquals(2, vf.getChunkCash(1));
				assertEquals(3, vf.getChunkCash(2));
				assertEquals(4, vf.getChunkCash(3));
				assertEquals(5, vf.getChunkCash(4));
				assertEquals(0, vf.getChunkCash(6));
				
				int ret = vf.read(read);
				assertEquals(1, read[0]);
				assertEquals(2, read[1]);
				assertEquals(3, read[2]);
				assertEquals(4, read[3]);
				assertEquals(5, read[4]);
				assertEquals(0, read[5]);
				assertEquals(5, ret);
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public File getFile() {
		return Environment.getExternalStorageDirectory();
	}
}
