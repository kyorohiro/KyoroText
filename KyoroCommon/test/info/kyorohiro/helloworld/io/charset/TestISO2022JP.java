package info.kyorohiro.helloworld.io.charset;

import info.kyorohiro.helloworld.io.VirtualMemory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;

import junit.framework.TestCase;

public class TestISO2022JP extends TestCase {

	public void testFindESC() throws FileNotFoundException, IOException {
		File f = getTestFile("iso_2022_jp_crlf_001.txt");
		VirtualMemory vm = new VirtualMemory(f, 64);
		vm.seek(0);
		ISO2022JP iso2022jp = new ISO2022JP();
		iso2022jp.update(vm);
		{
			assertEquals((int)'a',vm.read());//a
			assertEquals((int)'b',vm.read());//b
			assertEquals((int)'c',vm.read());//c
			iso2022jp.update(vm);
			assertArrayEquals(ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZDM4_1, 'B'), iso2022jp.currentG0());
			vm.read();vm.read();vm.read();//ESC
			vm.read();//‚  
			vm.read();//‚ 
			iso2022jp.update(vm);
			assertArrayEquals(ISO2022.DESIGNATED(ISO2022.DESIGNATED_GZD4, 'B'), iso2022jp.currentG0());
			vm.read();vm.read();vm.read();//ESC
			assertEquals((int)'d',vm.read());//d
			assertEquals((int)'e',vm.read());//e
			assertEquals((int)'f',vm.read());//f
//			assertEquals(new byte[] {}, iso2022jp.currentEscape());
		}
	}

	public static File getTestFile(String filename) {
		File dir = new File("test/info/kyorohiro/helloworld/io/charset");
		File path = new File(dir, filename);
		return path;
	}
	
	public void assertArrayEquals(Object expected, Object actual){
		assertEquals(Array.getLength(expected), Array.getLength(actual));
		for(int i=0;i<Array.getLength(expected);i++) {
			assertEquals(Array.get(expected, i),Array.get(actual, i));
		}
	}
}
