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
			// 0 byte.
			{
				testPath.delete();
				testPath.createNewFile();
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
			//　5byte
			{
				testPath.delete();
				testPath.createNewFile();
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
			//　5byte
			{
				testPath.delete();
				testPath.createNewFile();
				byte[] add = {1, 2, 3, 4, 5};
				VirtualFile vf = new VirtualFile(testPath, 200);
				byte[] read = new byte[6];
				vf.addChunk(add, 0, 5);
				vf.syncWrite();
				assertEquals(5, vf.getStartChunk());
				assertEquals(0, vf.getChunkCash(0));
				assertEquals(0, vf.getChunkCash(1));
				assertEquals(0, vf.getChunkCash(2));
				assertEquals(0, vf.getChunkCash(3));
				assertEquals(0, vf.getChunkCash(4));
				assertEquals(0, vf.getChunkCash(6));
				vf.addChunk(add, 0, 1);

				int ret = vf.read(read);
				assertEquals(1, read[0]);
				assertEquals(2, read[1]);
				assertEquals(3, read[2]);
				assertEquals(4, read[3]);
				assertEquals(5, read[4]);
				assertEquals(1, read[5]);
				assertEquals(6, ret);
			}

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testSecound() {
		File testPath = new File(getFile(),"__001__.txt");
		String testdata= 
		 "「この間はすまなかった。いつも間が悪くて君に会う機会がない。"+
		 "きょうは歌舞伎座の切符が二枚手に入ったから一緒に見に行か"+
		 "ないか。午後一時の開場だから十時頃の電車で銀座あたりへ来"+
		 "てくれるといい。君の知っているカフェーかレストランがあるだろう」";
		try {
			testPath.delete();
			testPath.createNewFile();
			VirtualFile vf = new VirtualFile(testPath, 101);
			byte[] buffer = new byte[1000];
			for(int i=0;i<buffer.length;i++) {
				buffer[0] = 0;
			}
			int ret = vf.read(buffer);
			assertEquals(-1, ret);
			for(int i=0;i<buffer.length;i++) {
				assertEquals(0, buffer[0]);
			}
			byte[] buff = testdata.getBytes("utf8");
			vf.addChunk(buff, 0, buff.length);
			{
				// first read
				ret = vf.read(buffer);
				String result = new String(buffer, 0 , ret);
				assertEquals(testdata, result);
				assertEquals(ret-ret%VirtualFile.CHUNK_SIZE, vf.getStartChunk());

				// second read
				{
					for(int i=0;i<buffer.length;i++) {
						buffer[0]=0;
					}
					ret = vf.read(buffer);
					assertEquals(-1, ret);					
					assertEquals(buff.length, vf.getStartChunk()+vf.getChunkSize());
				}
			}
			{
				// sync
				for(int i=0;i<buffer.length;i++) {
					buffer[0]=0;
				}
				vf.syncWrite();
				vf.seek(0);
				ret = vf.read(buffer);
				String result = new String(buffer, 0 , ret);
				assertEquals(testdata, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testThird() {
		File testPath = new File(getFile(),"__001__.txt");
		String testdata= ""
						+"「ウップ。怪しい結論だね。恐ろしく無駄骨の折れる虚栄じゃないか」\r\n"
						+"「ええ。それがね。あの人は地道に行きたい行きたい。"
						+"みんなに信用されていたいいたいと、思い詰めているのがあの娘"
						+"ひと"
						+"の虚栄なんですからね。そのために虚構"
						+"うそ"
						+"を吐"
						+"つ"
						+"くんですよ」\r\n"
						+"「それが第一おかしいじゃないか。第一、そんなにまでしてこちらの信用を博する必要が何処に在るんだい。看護婦としての手腕はチャント認められているんだし、実家"
						+"うち"
						+"が裕福だろうが貧乏だろうが看護婦としての資格や信用には無関係だろう。それくらいの事がわからない馬鹿じゃ、姫草はないと思うんだが」\r\n"
						+"「ええ。そりゃあ解ってるわ。たとえドンナ女"
						+"ひと"
						+"だっても現在ウチの病院の大切なマスコットなんですから、疑ったり何かしちゃすまないと思うんですけど……ですけど毎月二日か三日頃になると印形"
						+"ハンコ"
						+"で捺"
						+"お"
						+"したように白鷹先生の話が出て来るじゃないの。おかしいわ……」\r\n"
						+"「そりゃあ庚戌会がその頃にあるからさ」\r\n";
		try {
			testPath.delete();
			testPath.createNewFile();
			VirtualFile vf = new VirtualFile(testPath, 101);
			byte[] buffer = new byte[1300];
			for(int i=0;i<buffer.length;i++) {
				buffer[0] = 0;
			}
			int ret = vf.read(buffer);
			assertEquals(-1, ret);
			for(int i=0;i<buffer.length;i++) {
				assertEquals(0, buffer[0]);
			}
			byte[] testdataBuffer = testdata.getBytes("utf8");
			vf.addChunk(testdataBuffer, 0, testdataBuffer.length);
			{
				//
				assertTrue(""+testdataBuffer.length +"<="+buffer.length,testdataBuffer.length <= buffer.length);
				// first read
				ret = vf.read(buffer);
				String result = new String(buffer, 0 , ret);
				assertEquals(testdata, result);
				assertEquals(ret-ret%VirtualFile.CHUNK_SIZE, vf.getStartChunk());

				// second read
				{
					for(int i=0;i<buffer.length;i++) {
						buffer[0]=0;
					}
					ret = vf.read(buffer);
					assertEquals(-1, ret);					
					assertEquals(testdataBuffer.length, vf.getStartChunk()+vf.getChunkSize());
				}
			}
			{
				// sync
				for(int i=0;i<buffer.length;i++) {
					buffer[0]=0;
				}
				vf.syncWrite();
				vf.seek(0);
				ret = vf.read(buffer);
				String result = new String(buffer, 0 , ret);
				assertEquals(testdata, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testExtra1() {
		File testPath = new File(getFile(),"__001__.txt");
		String[] testdata= {
				"●..1/:::/storage/sdcard0\r\n",
				"  2013/01/12 20:41:08:::/storage/sdcard0\r\n",
				"  8192byte:::/storage/sdcard0:::HR\r\n",
				"●keyword/:::/storage/sdcard0/.jota/keyword\r\n",
				"  2012/12/14 23:21:43:::/storage/sdcard0/.jota/keyword\r\n",
				"  4096byte:::/storage/sdcard0/.jota/keyword:::HR\r\n",
				"●..2/:::/storage/sdcard0\r\n",
				"  2013/01/12 20:41:08:::/storage/sdcard0\r\n",
				"  8192byte:::/storage/sdcard0:::HR\r\n"
		};
		try {
			testPath.delete();
			testPath.createNewFile();
			VirtualFile vf = new VirtualFile(testPath, 501);
			byte[] buffer = new byte[1300];
			for(int i=0;i<buffer.length;i++) {
				buffer[0] = 0;
			}
			int ret = vf.read(buffer);
			assertEquals(-1, ret);
			for(int i=0;i<buffer.length;i++) {
				assertEquals(0, buffer[0]);
			}
			for(String t : testdata) {
				byte[] testdataBuffer = t.getBytes("utf8");
				vf.addChunk(testdataBuffer, 0, testdataBuffer.length);
			}
			StringBuilder expected = new StringBuilder();
			for(String t : testdata) {
				expected.append(t);
			}


			{
				//
//                                            				assertTrue(""+testdataBuffer.length +"<="+buffer.length,testdataBuffer.length <= buffer.length);
				// first read
				ret = vf.read(buffer);
				String result = new String(buffer, 0 , ret);
				assertEquals(expected.toString(), result);
				assertEquals("check:start chunk",0, vf.getStartChunk());
				assertEquals("check:file pointer",ret, vf.getFilePointer());
				assertEquals("check:start chunk",ret, vf.getChunkSize());
				assertEquals("check:length",ret, vf.length());

			}
			{
				// sync
				for(int i=0;i<buffer.length;i++) {
					buffer[0]=0;
				}
				vf.syncWrite();
				vf.seek(0);
				ret = vf.read(buffer);
				String result = new String(buffer, 0 , ret);
				assertEquals(expected.toString(), result);
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

/*
 * ●..1/:::/storage/sdcard0
/storage/sdcard0
  8192byte:::/storage/sdcard0:::HR
●keyword/:::/storage/sdcard0/.jota/keyword
  2012/12/14 23:21:43:::/storage/sdcard0/.jota/keyword
  4096byte:::/storage/sdcard0/.jota/keyword:::HR
●..2/:::/storage/sdcard0
  2013/01/12 20:41:08:::/storage/sdcard0
  8192byte:::/storage/sdcard0:::HR
*/
