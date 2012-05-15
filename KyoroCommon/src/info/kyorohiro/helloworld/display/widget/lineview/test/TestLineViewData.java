package info.kyorohiro.helloworld.display.widget.lineview.test;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import junit.framework.TestCase;

public class TestLineViewData extends TestCase{

	public void testToString() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		assertEquals(line, datam.toString());
	}

	public void testSubSequenceA() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		assertEquals("a", datam.subSequence(0, 1).toString());
		assertEquals("a", datam.subSequence(1, 0).toString());
	}

	public void testSubSequenceB() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		assertEquals("b", datam.subSequence(1, 2).toString());
		assertEquals("b", datam.subSequence(2, 1).toString());
	}

	public void testSubSequenceC() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		assertEquals("c", datam.subSequence(2, 3).toString());
		assertEquals("c", datam.subSequence(3, 2).toString());
	}

	public void testSubSequenceD() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		assertEquals("abc", datam.subSequence(0, 3).toString());
		assertEquals("abc", datam.subSequence(3, 0).toString());
	}

	public void testSubSequenceE() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		assertEquals("ab", datam.subSequence(0, 2).toString());
		assertEquals("ab", datam.subSequence(2, 0).toString());
	}

	public void testSubSequenceF() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		assertEquals("bc", datam.subSequence(1, 3).toString());
		assertEquals("bc", datam.subSequence(3, 1).toString());

		assertEquals(line.subSequence(1, 3), datam.subSequence(1, 3).toString());

	}

	public void testSubSequenceG() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		assertEquals("", datam.subSequence(0, 0).toString());
		assertEquals(line.subSequence(0, 0), datam.subSequence(0, 0).toString());
	}

	public void testSubSequenceH() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		assertEquals("", datam.subSequence(3, 3).toString());
		assertEquals(line.subSequence(3, 3), datam.subSequence(3, 3).toString());

	}

	public void testSubSequenceI() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		try {
			datam.subSequence(4, 4);
			assertTrue(false);
		} catch(IndexOutOfBoundsException e){
			// expect
		}
	}

	public void testSubSequenceJ() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		try {
			datam.subSequence(-1, -1);
			assertTrue(false);
		} catch(IndexOutOfBoundsException e){
			// expect
		}
	}





	public void testInsertA() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		datam.insert("n", 0);
		System.out.println("mo:"+"datam="+datam.toString());
		assertEquals("nabc", datam.toString());
		datam.insert("m", 0);
		System.out.println("mo:"+"datam="+datam.toString());
		assertEquals("mnabc", datam.toString());

	}

	public void testInsertB() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		datam.insert("n", 1);
		System.out.println("mo:"+"datam="+datam.toString());
		assertEquals("anbc", datam.toString());
		datam.insert("m", 1);
		System.out.println("mo:"+"datam="+datam.toString());
		assertEquals("amnbc", datam.toString());

	}

	public void testInsertC() {
		String line = "abc";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		datam.insert("n", 2);
		System.out.println("mo:"+"datam="+datam.toString());
		assertEquals("abnc", datam.toString());

		datam.insert("m", 2);
		System.out.println("mo:"+"datam="+datam.toString());
		assertEquals("abmnc", datam.toString());

	}

	public void testInsertD() {
		String line = "abcdefghijklmnopqrstuvwxyz0123456789";
		LineViewData datam =
				new LineViewData(line, 999, LineViewData.EXCLUDE_END_OF_LINE);
		datam.insert("™", 10);
		System.out.println("mo:"+"datam="+datam.toString());
		assertEquals("abcdefghij"+"™"+"klmnopqrstuvwxyz0123456789", datam.toString());
	}
}
