package info.kyorohiro.helloworld.test.io;

import info.kyorohiro.helloworld.io.VirtualMemory;
import info.kyorohiro.helloworld.io.VirtualMemory.CyclingByteArrayException;
import info.kyorohiro.helloworld.test.io.TestVirturalMemory;
import info.kyorohiro.helloworld.test.io.TestVirturalMemory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestVirturalMemory extends junit.framework.TestCase {

	public TestVirturalMemory() {
		super("001");
	}

	public void testHello() {
		System.out.println("hello test");
		File f = TestVirturalMemory.getTestFile();
		System.out.println("hello test " + f.getAbsolutePath());
		System.out.println("hello test " + f.exists());
		System.out.println("hello test " + f.isFile());
	}

	public void testCyclingByteArray_initialize() {
		VirtualMemory.CyclingByteArray array = new VirtualMemory.CyclingByteArray(1000);
		assertEquals(0, array.getPointer());
		assertEquals(false, array.isBuffered(0));
		try {
			array.read(0);
			assertTrue(false);
		} catch(CyclingByteArrayException e) {
		}
	}

	public void testCyclingByteArray_setData_first() {
		VirtualMemory.CyclingByteArray array = new VirtualMemory.CyclingByteArray(100);
		String message = "abcdefg";
		byte[] buffer = message.getBytes();
		try {
			array.set(0, buffer, 0, buffer.length);
//			assertEquals(0, array.getPointer());
			assertEquals(false, array.isBuffered(-1));
			assertEquals(true, array.isBuffered(0));
			assertEquals(true, array.isBuffered(buffer.length-1));
			assertEquals(false, array.isBuffered(buffer.length));
			try {
				assertEquals((byte)'a',array.read(0));
				assertEquals((byte)'b',array.read(1));
				assertEquals((byte)'f',array.read(5));
				assertEquals((byte)'g',array.read(6));
				array.read(7);
				assertTrue(false);
			} catch(CyclingByteArrayException e) {
			}
		} catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testCyclingByteArray_setData_refresh() {
		VirtualMemory.CyclingByteArray array = new VirtualMemory.CyclingByteArray(100);
		String prevMessge = "zzzzzzzzzz";
		String message    = "abcdefg";
		byte[] prevBuffer = prevMessge.getBytes();
		byte[] buffer = message.getBytes();
		try{
			array.set(0, prevBuffer, 0, prevBuffer.length);
			array.set(0, buffer, 0, buffer.length);
//			assertEquals(0, array.getPointer());
			assertEquals(false, array.isBuffered(-1));
			assertEquals(true, array.isBuffered(0));
			assertEquals(true, array.isBuffered(buffer.length-1));
			assertEquals(false, array.isBuffered(buffer.length));
			try {
				assertEquals((byte)'a',array.read(0));
				assertEquals((byte)'b',array.read(1));
				assertEquals((byte)'f',array.read(5));
				assertEquals((byte)'g',array.read(6));
				array.read(7);
				assertTrue(false);
			} catch(CyclingByteArrayException e) {
			}
		} catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}

	public void testCyclingByteArray_setData_recycle() {
		VirtualMemory.CyclingByteArray array = new VirtualMemory.CyclingByteArray(100);
		String prevMessge = "zzzzzzzzzz";
		String message    = "abcdefg";
		byte[] prevBuffer = prevMessge.getBytes();
		byte[] buffer = message.getBytes();
		try {
			array.set(0, prevBuffer, 0, prevBuffer.length);
			array.set(1, buffer, 0, buffer.length);
//			assertEquals(0, array.getPointer());
			assertEquals(false, array.isBuffered(-1));
			assertEquals(true, array.isBuffered(0));
			assertEquals(true, array.isBuffered(buffer.length));
			assertEquals(false, array.isBuffered(buffer.length+1));
			try {
				assertEquals((byte)'z',array.read(0));
				assertEquals((byte)'a',array.read(1));
				assertEquals((byte)'b',array.read(2));
				assertEquals((byte)'f',array.read(6));
				assertEquals((byte)'g',array.read(7));
				array.read(8);
				assertTrue(false);
			} catch(CyclingByteArrayException e) {
			}
		} catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}	

	public void testCyclingByteArray_setData_exception() {
		VirtualMemory.CyclingByteArray array = new VirtualMemory.CyclingByteArray(10);
		String prevMessge = "012345678";
		String message    = "abc";
		byte[] prevBuffer = prevMessge.getBytes();
		byte[] buffer = message.getBytes();
		try {
			array.set(0, prevBuffer, 0, prevBuffer.length);
			array.set(1, buffer, 0, buffer.length);
//			assertEquals(0, array.getPointer());
			assertEquals(false, array.isBuffered(-1));
			assertEquals(true, array.isBuffered(0));
			assertEquals(true, array.isBuffered(buffer.length));
			assertEquals(false, array.isBuffered(buffer.length+1));
			try {
				assertEquals((byte)'0',array.read(0));
				assertEquals((byte)'a',array.read(1));
				assertEquals((byte)'b',array.read(2));
				assertEquals((byte)'c',array.read(3));
				array.read(4);
				assertTrue(false);
			} catch(CyclingByteArrayException e) {
			}

			try {
				String exceptMessage = "0123456789a";
				byte[] exceptBuffer = exceptMessage.getBytes();
				array.set(0, exceptBuffer, 0, exceptBuffer.length);
				assertTrue(false);
			} catch(CyclingByteArrayException e) {
			}


		} catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}	

	public void testVirtualMemory_default() {
		File base = getTestFile();
		VirtualMemory memory;
		try {
			memory = new VirtualMemory(base, 2);
			System.out.println("-a-");
			assertEquals('a', memory.read());
			System.out.println("-b-");
			assertEquals('b', memory.read());
			System.out.println("-c-");
			assertEquals('c', memory.read());
			System.out.println("-d-");
			assertEquals('\r', memory.read());
			System.out.println("-e-");
			assertEquals('\n', memory.read());
			System.out.println("--");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			assertTrue(false);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testVirtualMemory_seek() {
		File base = getTestFile();
		VirtualMemory memory;
		try {
			memory = new VirtualMemory(base, 2);
			System.out.println("-a-");
			assertEquals('a', memory.read());
			System.out.println("-b-");
			assertEquals('b', memory.read());
			System.out.println("-c-");
			memory.seek(0);
			System.out.println("-a2-");
			assertEquals('a', memory.read());
			System.out.println("-b2-");
			assertEquals('b', memory.read());
			System.out.println("-c2-");
			assertEquals('c', memory.read());
			System.out.println("-d-");
			assertEquals('\r', memory.read());
			System.out.println("-e-");
			assertEquals('\n', memory.read());
			System.out.println("--");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			assertTrue(false);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public static File getTestFile(){
		File file = new File("src/info/kyorohiro/helloworld/test/io/test.txt");
		return file;
	}
}
