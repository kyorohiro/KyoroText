package info.kyorohiro.helloworld.display.widget.editview.differ;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.TreeMap;

import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.text.KyoroString;
import junit.framework.TestCase;

public class TestForDiffer extends TestCase {

	public void testHello() {
		System.out.println("hello");
	}

	public void testRandomSenario0() {
		String[] message = {
				"abcdefg",
				"ABCDEFG",
				"NONONON",
				"0123456",
		};
		{
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			KyoroString ret = differ.get(buffer, 0);
			assertEquals("check get", message[0], ret.toString());
			assertEquals("check get", message[1], differ.get(buffer, 1).toString());
			assertEquals("check get", message[2], differ.get(buffer, 2).toString());
			assertEquals("check get", message[3], differ.get(buffer, 3).toString());
			assertEquals("check get", 0, differ.length());
		}

		{
			//senario 00
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			differ.addLine(0, "---0---");
			assertEquals("check get", "---0---",  differ.get(buffer, 0).toString());
			assertEquals("check get", message[0], differ.get(buffer, 1).toString());
			assertEquals("check get", message[1], differ.get(buffer, 2).toString());
			assertEquals("check get", message[2], differ.get(buffer, 3).toString());
			assertEquals("check get", message[3], differ.get(buffer, 4).toString());
			assertEquals("check get", 1, differ.length());
			
			differ.addLine(0, "---1---");
			assertEquals("check get", "---1---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---0---",  differ.get(buffer, 1).toString());
			assertEquals("check get", message[0], differ.get(buffer, 2).toString());
			assertEquals("check get", message[1], differ.get(buffer, 3).toString());
			assertEquals("check get", message[2], differ.get(buffer, 4).toString());
			assertEquals("check get", message[3], differ.get(buffer, 5).toString());
			assertEquals("check get", 2, differ.length());

			differ.addLine(0, "---2---");
			assertEquals("check get", "---2---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---1---",  differ.get(buffer, 1).toString());
			assertEquals("check get", "---0---",  differ.get(buffer, 2).toString());
			assertEquals("check get", message[0], differ.get(buffer, 3).toString());
			assertEquals("check get", message[1], differ.get(buffer, 4).toString());
			assertEquals("check get", message[2], differ.get(buffer, 5).toString());
			assertEquals("check get", message[3], differ.get(buffer, 6).toString());
			assertEquals("check get", 3, differ.length());

		}
	}

	public void testRandomSenario1() {
		String[] message = {
				"abcdefg",
				"ABCDEFG",
				"NONONON",
				"0123456",
		};
		{
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			KyoroString ret = differ.get(buffer, 0);
			assertEquals("check get", message[0], ret.toString());
			assertEquals("check get", message[1], differ.get(buffer, 1).toString());
			assertEquals("check get", message[2], differ.get(buffer, 2).toString());
			assertEquals("check get", message[3], differ.get(buffer, 3).toString());
			assertEquals("check get", 0, differ.length());
		}

		{
			//senario 00
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			differ.addLine(0, "---0---");
			assertEquals("check get", "---0---",  differ.get(buffer, 0).toString());
			assertEquals("check get", message[0], differ.get(buffer, 1).toString());
			assertEquals("check get", message[1], differ.get(buffer, 2).toString());
			assertEquals("check get", message[2], differ.get(buffer, 3).toString());
			assertEquals("check get", message[3], differ.get(buffer, 4).toString());
			assertEquals("check get", 1, differ.length());

			differ.addLine(0, "---1---");
			assertEquals("check get", "---1---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---0---",  differ.get(buffer, 1).toString());
			assertEquals("check get", message[0], differ.get(buffer, 2).toString());
			assertEquals("check get", message[1], differ.get(buffer, 3).toString());
			assertEquals("check get", message[2], differ.get(buffer, 4).toString());
			assertEquals("check get", message[3], differ.get(buffer, 5).toString());
			assertEquals("check get", 2, differ.length());

			differ.addLine(1, "---2---");
			assertEquals("check get", "---1---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---2---",  differ.get(buffer, 1).toString());
			assertEquals("check get", "---0---",  differ.get(buffer, 2).toString());
			assertEquals("check get", message[0], differ.get(buffer, 3).toString());
			assertEquals("check get", message[1], differ.get(buffer, 4).toString());
			assertEquals("check get", message[2], differ.get(buffer, 5).toString());
			assertEquals("check get", message[3], differ.get(buffer, 6).toString());
			assertEquals("check get", 3, differ.length());

			differ.addLine(3, "---3---");
			assertEquals("check get", "---1---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---2---",  differ.get(buffer, 1).toString());
			assertEquals("check get", "---0---",  differ.get(buffer, 2).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 3).toString());
			assertEquals("check get", message[0], differ.get(buffer, 4).toString());
			assertEquals("check get", message[1], differ.get(buffer, 5).toString());
			assertEquals("check get", message[2], differ.get(buffer, 6).toString());
			assertEquals("check get", message[3], differ.get(buffer, 7).toString());
			assertEquals("check get", 4, differ.length());

			differ.addLine(5, "---4---");
			assertEquals("check get", "---1---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---2---",  differ.get(buffer, 1).toString());
			assertEquals("check get", "---0---",  differ.get(buffer, 2).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 3).toString());
			assertEquals("check get", message[0], differ.get(buffer, 4).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 5).toString());
			assertEquals("check get", message[1], differ.get(buffer, 6).toString());
			assertEquals("check get", message[2], differ.get(buffer, 7).toString());
			assertEquals("check get", message[3], differ.get(buffer, 8).toString());
			assertEquals("check get", 5, differ.length());

			differ.addLine(8, "---5---");
			assertEquals("check get", "---1---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---2---",  differ.get(buffer, 1).toString());
			assertEquals("check get", "---0---",  differ.get(buffer, 2).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 3).toString());
			assertEquals("check get", message[0], differ.get(buffer, 4).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 5).toString());
			assertEquals("check get", message[1], differ.get(buffer, 6).toString());
			assertEquals("check get", message[2], differ.get(buffer, 7).toString());
			assertEquals("check get", "---5---",  differ.get(buffer, 8).toString());
			assertEquals("check get", message[3], differ.get(buffer, 9).toString());
			assertEquals("check get", 6, differ.length());

			differ.addLine(10, "---6---");
			assertEquals("check get", "---1---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---2---",  differ.get(buffer, 1).toString());
			assertEquals("check get", "---0---",  differ.get(buffer, 2).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 3).toString());
			assertEquals("check get", message[0], differ.get(buffer, 4).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 5).toString());
			assertEquals("check get", message[1], differ.get(buffer, 6).toString());
			assertEquals("check get", message[2], differ.get(buffer, 7).toString());
			assertEquals("check get", "---5---",  differ.get(buffer, 8).toString());
			assertEquals("check get", message[3], differ.get(buffer, 9).toString());
			assertEquals("check get", "---6---",  differ.get(buffer, 10).toString());
			assertEquals("check get", 7, differ.length());

			//
			differ.deleteLine(0);
			assertEquals("check get", "---2---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---0---",  differ.get(buffer, 1).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 2).toString());
			assertEquals("check get", message[0], differ.get(buffer, 3).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 4).toString());
			assertEquals("check get", message[1], differ.get(buffer, 5).toString());
			assertEquals("check get", message[2], differ.get(buffer, 6).toString());
			assertEquals("check get", "---5---",  differ.get(buffer, 7).toString());
			assertEquals("check get", message[3], differ.get(buffer, 8).toString());
			assertEquals("check get", "---6---",  differ.get(buffer, 9).toString());
			assertEquals("check get", 6, differ.length());

			//
			differ.deleteLine(1);
			assertEquals("check get", "---2---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 1).toString());
			assertEquals("check get", message[0], differ.get(buffer, 2).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 3).toString());
			assertEquals("check get", message[1], differ.get(buffer, 4).toString());
			assertEquals("check get", message[2], differ.get(buffer, 5).toString());
			assertEquals("check get", "---5---",  differ.get(buffer, 6).toString());
			assertEquals("check get", message[3], differ.get(buffer, 7).toString());
			assertEquals("check get", "---6---",  differ.get(buffer, 8).toString());
			assertEquals("check get", 5, differ.length());

			differ.deleteLine(8);
			assertEquals("check get", "---2---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 1).toString());
			assertEquals("check get", message[0], differ.get(buffer, 2).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 3).toString());
			assertEquals("check get", message[1], differ.get(buffer, 4).toString());
			assertEquals("check get", message[2], differ.get(buffer, 5).toString());
			assertEquals("check get", "---5---",  differ.get(buffer, 6).toString());
			assertEquals("check get", message[3], differ.get(buffer, 7).toString());
			assertEquals("check get", 4, differ.length());

			differ.deleteLine(4);
			assertEquals("check get", "---2---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 1).toString());
			assertEquals("check get", message[0], differ.get(buffer, 2).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 3).toString());
			assertEquals("check get", message[2], differ.get(buffer, 4).toString());
			assertEquals("check get", "---5---",  differ.get(buffer, 5).toString());
			assertEquals("check get", message[3], differ.get(buffer, 6).toString());
			assertEquals("check get", 3, differ.length());

			differ.deleteLine(4);
			assertEquals("check get", "---2---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 1).toString());
			assertEquals("check get", message[0], differ.get(buffer, 2).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 3).toString());
			assertEquals("check get", "---5---",  differ.get(buffer, 4).toString());
			assertEquals("check get", message[3], differ.get(buffer, 5).toString());
			assertEquals("check get", 2, differ.length());

			differ.deleteLine(4);
			assertEquals("check get", "---2---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---3---",  differ.get(buffer, 1).toString());
			assertEquals("check get", message[0], differ.get(buffer, 2).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 3).toString());
			assertEquals("check get", message[3], differ.get(buffer, 4).toString());
			assertEquals("check get", 1, differ.length());

			differ.deleteLine(1);
			assertEquals("check get", "---2---",  differ.get(buffer, 0).toString());
			assertEquals("check get", message[0], differ.get(buffer, 1).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 2).toString());
			assertEquals("check get", message[3], differ.get(buffer, 3).toString());
			assertEquals("check get", 0, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", message[0], differ.get(buffer, 0).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 1).toString());
			assertEquals("check get", message[3], differ.get(buffer, 2).toString());
			assertEquals("check get", -1, differ.length());

			differ.addLine(0, "---a---");
			assertEquals("check get", "---a---",  differ.get(buffer, 0).toString());
			assertEquals("check get", message[0], differ.get(buffer, 1).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 2).toString());
			assertEquals("check get", message[3], differ.get(buffer, 3).toString());
			assertEquals("check get", 0, differ.length());

			differ.addLine(4, "---b---");
			assertEquals("check get", "---a---",  differ.get(buffer, 0).toString());
			assertEquals("check get", message[0], differ.get(buffer, 1).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 2).toString());
			assertEquals("check get", message[3], differ.get(buffer, 3).toString());
			assertEquals("check get", "---b---",  differ.get(buffer, 4).toString());
			assertEquals("check get", 1, differ.length());

			differ.addLine(2, "---c---");
			assertEquals("check get", "---a---",  differ.get(buffer, 0).toString());
			assertEquals("check get", message[0], differ.get(buffer, 1).toString());
			assertEquals("check get", "---c---",  differ.get(buffer, 2).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 3).toString());
			assertEquals("check get", message[3], differ.get(buffer, 4).toString());
			assertEquals("check get", "---b---",  differ.get(buffer, 5).toString());
			assertEquals("check get", 2, differ.length());
			
			differ.deleteLine(0);
			assertEquals("check get", message[0], differ.get(buffer, 0).toString());
			assertEquals("check get", "---c---",  differ.get(buffer, 1).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 2).toString());
			assertEquals("check get", message[3], differ.get(buffer, 3).toString());
			assertEquals("check get", "---b---",  differ.get(buffer, 4).toString());
			assertEquals("check get", 1, differ.length());


			differ.deleteLine(4);
			assertEquals("check get", message[0], differ.get(buffer, 0).toString());
			assertEquals("check get", "---c---",  differ.get(buffer, 1).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 2).toString());
			assertEquals("check get", message[3], differ.get(buffer, 3).toString());
			assertEquals("check get", 0, differ.length());


			differ.deleteLine(0);
			assertEquals("check get", "---c---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 1).toString());
			assertEquals("check get", message[3], differ.get(buffer, 2).toString());
			assertEquals("check get", -1, differ.length());

			differ.deleteLine(2);
			assertEquals("check get", "---c---",  differ.get(buffer, 0).toString());
			assertEquals("check get", "---4---",  differ.get(buffer, 1).toString());
			assertEquals("check get", -2, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", "---4---",  differ.get(buffer, 0).toString());
			assertEquals("check get", -3, differ.length());
			

			differ.deleteLine(0);
			assertEquals("check get", -4, differ.length());
		}
	}
		
	public void testRandomSenario2() {
		String[] message = {
				"abcdefg",
				"ABCDEFG",
				"NONONON",
				"0123456",
		};
		{
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			KyoroString ret = differ.get(buffer, 0);
			assertEquals("check get", message[0], ret.toString());
			assertEquals("check get", message[1], differ.get(buffer, 1).toString());
			assertEquals("check get", message[2], differ.get(buffer, 2).toString());
			assertEquals("check get", message[3], differ.get(buffer, 3).toString());
			assertEquals("check get", 0, differ.length());
		}

		{
			//senario 00
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			differ.deleteLine(0);
			assertEquals("check get", message[1], differ.get(buffer, 0).toString());
			assertEquals("check get", message[2], differ.get(buffer, 1).toString());
			assertEquals("check get", message[3], differ.get(buffer, 2).toString());
			assertEquals("check get", -1, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", message[2], differ.get(buffer, 0).toString());
			assertEquals("check get", message[3], differ.get(buffer, 1).toString());
			assertEquals("check get", -2, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", message[3], differ.get(buffer, 0).toString());
			assertEquals("check get", -3, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", -4, differ.length());
		}

		{
			//senario 00
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			differ.deleteLine(2);
			assertEquals("check get", message[0], differ.get(buffer, 0).toString());
			assertEquals("check get", message[1], differ.get(buffer, 1).toString());
			assertEquals("check get", message[3], differ.get(buffer, 2).toString());
			assertEquals("check get", -1, differ.length());

			differ.deleteLine(1);
			assertEquals("check get", message[0], differ.get(buffer, 0).toString());
			assertEquals("check get", message[3], differ.get(buffer, 1).toString());
			assertEquals("check get", -2, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", message[3], differ.get(buffer, 0).toString());
			assertEquals("check get", -3, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", -4, differ.length());

		}

	}

	public void testRandomSenario3() {
		String[] message = {};
		String[] message1 = {"--1--"};
		String[] message2 = {"--1--","--2--"};

		{
			//senario 00
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			differ.addLine(0, "--0--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());
	
			differ.addLine(1, "--1--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--1--", differ.get(buffer, 1).toString());
			assertEquals("check get", 2, differ.length());

			differ.deleteLine(1);
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", 0, differ.length());

			//
			// extra
			//
			differ.addLine(0, "--0--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());
	
			differ.addLine(0, "--1--");
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--0--", differ.get(buffer, 1).toString());
			assertEquals("check get", 2, differ.length());

		}

		{
			//senario 00
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			differ.addLine(0, "--0--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());
	
			differ.addLine(1, "--1--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--1--", differ.get(buffer, 1).toString());
			assertEquals("check get", 2, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", 0, differ.length());

			//
			// extra
			//
			differ.addLine(0, "--0--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());
	
			differ.addLine(0, "--1--");
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--0--", differ.get(buffer, 1).toString());
			assertEquals("check get", 2, differ.length());

			
		}

		{
			//senario 00
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			differ.addLine(0, "--0--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());
	
			differ.addLine(0, "--1--");
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--0--", differ.get(buffer, 1).toString());
			assertEquals("check get", 2, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", 0, differ.length());

			//
			// extra
			//
			differ.addLine(0, "--0--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());
	
			differ.addLine(0, "--1--");
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--0--", differ.get(buffer, 1).toString());
			assertEquals("check get", 2, differ.length());

		}

		{
			//senario 00
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			differ.addLine(0, "--0--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());
	
			differ.addLine(0, "--1--");
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--0--", differ.get(buffer, 1).toString());
			assertEquals("check get", 2, differ.length());

			differ.deleteLine(1);
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());

			differ.deleteLine(0);
			assertEquals("check get", 0, differ.length());
			//
			// extra
			//
			differ.addLine(0, "--0--");
			assertEquals("check get", "--0--", differ.get(buffer, 0).toString());
			assertEquals("check get", 1, differ.length());
	
			differ.addLine(0, "--1--");
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--0--", differ.get(buffer, 1).toString());
			assertEquals("check get", 2, differ.length());
		}

		{
			//senario ------------------------------
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message1);
			differ.deleteLine(0);
			assertEquals("check get", -1, differ.length());

			differ.addLine(0,"--a--");
			assertEquals("check get", "--a--", differ.get(buffer, 0).toString());
			assertEquals("check get", 0, differ.length());
		}
		{
			//senario ------------------------------
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message2);
			differ.deleteLine(0);
			assertEquals("check get", -1, differ.length());
			assertEquals("check get", "--2--", differ.get(buffer, 0).toString());

			differ.addLine(0,"--a--");
			assertEquals("check get", "--a--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--2--", differ.get(buffer, 1).toString());
			assertEquals("check get", 0, differ.length());
		}
		{
			//senario ------------------------------
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message2);
			differ.deleteLine(0);
			assertEquals("check get", -1, differ.length());
			assertEquals("check get", "--2--", differ.get(buffer, 0).toString());

			differ.addLine(1,"--a--");
			assertEquals("check get", "--2--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--a--", differ.get(buffer, 1).toString());
			assertEquals("check get", 0, differ.length());
		}

		{
			//senario ------------------------------
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message2);
			differ.deleteLine(1);
			assertEquals("check get", -1, differ.length());
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());

			differ.addLine(0,"--a--");
			assertEquals("check get", "--a--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--1--", differ.get(buffer, 1).toString());
			assertEquals("check get", 0, differ.length());
		}
		{
			//senario ------------------------------
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message2);
			differ.deleteLine(1);
			assertEquals("check get", -1, differ.length());
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());

			differ.addLine(1,"--a--");
			assertEquals("check get", "--1--", differ.get(buffer, 0).toString());
			assertEquals("check get", "--a--", differ.get(buffer, 1).toString());
			assertEquals("check get", 0, differ.length());
		}

	}

	public static class MyBuffer implements LineViewBufferSpec {
		private LinkedList<KyoroString> mBuffer = new LinkedList<KyoroString>();

		public MyBuffer(String[] buffer) {
			long size = 0;
			for(int i=0;i<buffer.length;i++){
				String text = buffer[i];
				KyoroString tmp = new KyoroString(text);
				tmp.setLinePosition(i);
				tmp.setBeginPointer(size);
				try {
					size = text.getBytes("utf8").length;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				tmp.setEndPointer(size);
				mBuffer.add(tmp);
			}
		}
		@Override
		public KyoroString get(int location) {
			return mBuffer.get(location);
		}

		@Override
		public int getNumberOfStockedElement() {
			return mBuffer.size();
		}

		@Override
		public int getMaxOfStackedElement() {
			return mBuffer.size();
		}

		//
		// following method is unuse
		//
		
		@Override
		public int getNumOfAdd() {
			return 0;
		}

		@Override
		public void clearNumOfAdd() {
		}

		@Override
		public void isSync(boolean isSync) {
		}

		@Override
		public boolean isSync() {
			return false;
		}


		@Override
		public BreakText getBreakText() {
			return null;
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLoading() {
			return false;
		}		
	}
}
