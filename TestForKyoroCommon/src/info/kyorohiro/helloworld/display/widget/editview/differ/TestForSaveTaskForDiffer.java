package info.kyorohiro.helloworld.display.widget.editview.differ;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.os.Environment;
import info.kyorohiro.helloworld.display.widget.editview.differ.TestForDiffer.MyBuffer;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.TaskTicket;
import junit.framework.TestCase;

public class TestForSaveTaskForDiffer extends TestCase {

	public void testHello() {
		System.out.println("hello");
		String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456", };
		Differ differ = new Differ();
		MyBuffer buffer = new MyBuffer(message);
		assertEquals("check get", message[0], differ.get(buffer, 0).toString());
		assertEquals("check get", message[1], differ.get(buffer, 1).toString());
		assertEquals("check get", message[2], differ.get(buffer, 2).toString());
		assertEquals("check get", message[3], differ.get(buffer, 3).toString());
		assertEquals("check get", 0, differ.get(buffer, 0).getBeginPointer());
		assertEquals("check get", 7, differ.get(buffer, 1).getBeginPointer());
		assertEquals("check get", 14, differ.get(buffer, 2).getBeginPointer());
		assertEquals("check get", 21, differ.get(buffer, 3).getBeginPointer());	

		assertEquals("check get",  7, differ.get(buffer, 0).getEndPointer());
		assertEquals("check get", 14, differ.get(buffer, 1).getEndPointer());
		assertEquals("check get", 21, differ.get(buffer, 2).getEndPointer());
		assertEquals("check get", 28, differ.get(buffer, 3).getEndPointer());	


		assertEquals("check get", 0, differ.length());
	}

	public void testRandomSenario() throws UnsupportedEncodingException,
			IOException, InterruptedException {
		String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456", };
		{
			//
			// init
			//
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			KyoroString ret = differ.get(buffer, 0);

			File savePath = new File(Environment.getExternalStorageDirectory(),
					"__kyoro_test_00_.txt");
			File restorePath = new File(
					Environment.getExternalStorageDirectory(),
					"__kyoro_test_01_.txt");
			File inputPath = new File(
					Environment.getExternalStorageDirectory(),
					"__kyoro_test_02_.txt");

			VirtualFile vIndex = new VirtualFile(savePath, 512 + 1);
			VirtualFile vInput = new VirtualFile(inputPath, 512 + 1);
			VirtualFile vRestore = new VirtualFile(restorePath, 512 + 1);

			for (String s : message) {
				vInput.addChunk(s.getBytes("utf8"));
			}

			//
			// senario
			//
			differ.deleteLine(0);
			TaskTicket<String> ticket = differ.save(buffer, vIndex);
			String result = ticket.getT();

			//
			// check one
			//
			byte[] b = new byte[2056];
			vIndex.seek(0);
			int len = vIndex.read(b);
			String tag = SaveTaskForDiffer.encodeDeleteLine(buffer.get(0)
					.getBeginPointer(), buffer.get(0).getEndPointer());
			assertEquals("" + tag + "," + new String(b, 0, len, "utf8"), tag,
					new String(b, 0, len, "utf8"));

			//
			// check 2 # restore test
			//
			vIndex.seek(0);
			vInput.seek(0);
			SaveTaskForDiffer.restore(vIndex, vInput, vRestore);

			vRestore.seek(0);
			String m = "ABCDEFG" + "NONONON" + "0123456";
			assertEquals(m, VirtualFile.readLine(vRestore, "utf8"));
		}
	}

	public void testRandomSenario001() throws UnsupportedEncodingException,
			IOException, InterruptedException {
		String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
		String[] expect = { "ABCDEFG" + "NONONON" + "0123456" };
		basicSenario(message, null, expect, new Action() {
			@Override
			public void act(Differ differ) {
				differ.deleteLine(0);
			}
		});
	}

	public void testRandomSenario002() throws UnsupportedEncodingException,
			IOException, InterruptedException {
		String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
		String[] expect = { "NONONON" + "0123456" };
		basicSenario(message, null, expect, new Action() {
			@Override
			public void act(Differ differ) {
				differ.deleteLine(0);
				differ.deleteLine(0);
			}
		});
	}

	public void testRandomSenario003() throws UnsupportedEncodingException,
	IOException, InterruptedException {
		String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
		String[] expect = { "0123456" };
		basicSenario(message, null, expect, new Action() {
			@Override
			public void act(Differ differ) {
				differ.deleteLine(0);
				differ.deleteLine(0);
				differ.deleteLine(0);
			}
		});
	}

	public void testRandomSenario004() throws UnsupportedEncodingException,
	IOException, InterruptedException {
		String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
		String[] expect = { "" };
		basicSenario(message, null, expect, new Action() {
			@Override
			public void act(Differ differ) {
				differ.deleteLine(0);
				differ.deleteLine(0);
				differ.deleteLine(0);
				differ.deleteLine(0);
			}
		});
	}

	public void testRandomSenario005() throws UnsupportedEncodingException,
	IOException, InterruptedException {
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "abcdefg"+"ABCDEFG"+"NONONON" };
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(3);
				}
			});
		}
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "abcdefg"+"ABCDEFG"};
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(3);
					differ.deleteLine(2);
				}
			});
		}
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "abcdefg"};
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(3);
					differ.deleteLine(2);
					differ.deleteLine(1);
				}
			});
		}
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "" };
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(3);
					differ.deleteLine(2);
					differ.deleteLine(1);
					differ.deleteLine(0);
				}
			});
		}
	}

	public void testRandomSenario006() throws UnsupportedEncodingException,
	IOException, InterruptedException {
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "ABCDEFG"+"NONONON"+"0123456" };
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(0);
				}
			});
		}
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "ABCDEFG"+"0123456" };
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(0);
					differ.deleteLine(1);
				}
			});
		}
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "0123456" };
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(0);
					differ.deleteLine(1);
					differ.deleteLine(0);
				}
			});
		}
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "" };
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(0);
					differ.deleteLine(1);
					differ.deleteLine(0);
					differ.deleteLine(0);
				}
			});
		}
	}


	public void testRandomSenario007() throws UnsupportedEncodingException,
	IOException, InterruptedException {
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "abcdefg"+"ABCDEFG"+"NONONON"};
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(3);
				}
			});
		}
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "abcdefg"+"NONONON"};
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(3);
					differ.deleteLine(1);
				}
			});
		}
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { "abcdefg"};
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(3);
					differ.deleteLine(1);
					differ.deleteLine(1);
				}
			});
		}
		{
			String[] message = { "abcdefg", "ABCDEFG", "NONONON", "0123456" };
			String[] expect = { ""};
			basicSenario(message, null, expect, new Action() {
				@Override
				public void act(Differ differ) {
					differ.deleteLine(3);
					differ.deleteLine(1);
					differ.deleteLine(1);
					differ.deleteLine(0);
				}
			});
		}
	}


	public static interface Action {
		public void act(Differ differ);
	}

	public void basicSenario(String[] message, String expectOne,
			String[] expectTwo, Action act)
			throws UnsupportedEncodingException, IOException,
			InterruptedException {
		//
		// init
		//
		Differ differ = new Differ();
		MyBuffer buffer = new MyBuffer(message);
		KyoroString ret = differ.get(buffer, 0);

		File savePath = new File(Environment.getExternalStorageDirectory(),
				"__kyoro_test_00_.txt");
		File restorePath = new File(Environment.getExternalStorageDirectory(),
				"__kyoro_test_01_.txt");
		File inputPath = new File(Environment.getExternalStorageDirectory(),
				"__kyoro_test_02_.txt");

		if (savePath.exists()) {
			savePath.delete();
		}
		if (restorePath.exists()) {
			restorePath.delete();
		}
		if (inputPath.exists()) {
			inputPath.delete();
		}

		VirtualFile vIndex = new VirtualFile(savePath, 512 + 1);
		VirtualFile vInput = new VirtualFile(inputPath, 512 + 1);
		VirtualFile vRestore = new VirtualFile(restorePath, 512 + 1);

		for (String s : message) {
			vInput.addChunk(s.getBytes("utf8"));
		}

		//
		// senario
		//
		// differ.deleteLine(0);
		act.act(differ);
		TaskTicket<String> ticket = differ.save(buffer, vIndex);
		String result = ticket.getT();

		//
		// check one
		//
			byte[] b = new byte[2056];
			vIndex.seek(0);
			int len = vIndex.read(b);
			if(expectOne != null) {
				String tag = SaveTaskForDiffer.encodeDeleteLine(buffer.get(0)
						.getBeginPointer(), buffer.get(0).getEndPointer());
				assertEquals(""+expectOne, expectOne,
						new String(b, 0, len, "utf8"));
			} else {
				android.util.Log.v("kiyo","##co##"+new String(b, 0, len, "utf8"));
			}
		//
		// check 2 # restore test
		//
		vIndex.seek(0);
		vInput.seek(0);
		vRestore.seek(0);
		SaveTaskForDiffer.restore(vIndex, vInput, vRestore);

		vRestore.seek(0);
		int i = 0;
		for (String exp : expectTwo) {
			String line = VirtualFile.readLine(vRestore, "utf8");
			android.util.Log.v("kiyo", "##" + i + "##" + exp + "," + line);
			assertEquals("" + i + "," + exp, exp, line);
			i++;
		}
	}
}
