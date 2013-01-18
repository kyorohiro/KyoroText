package info.kyorohiro.helloworld.util.arraybuilder;

import junit.framework.TestCase;

public class TestForCharArrayBuilder extends TestCase {

	//
	// if modify test target code.  you must test at LEVEL = 0
	private static final int LEVEL = 30;

	private void log(String log) {
		android.util.Log.v("kiyo","#TestForCharArrayBuilder#"+log);
	}


	public void testHello() {
		log("testHello");
	}

	public void testAppend() {
		CharArrayBuilder builder = new CharArrayBuilder();
		char[] testdata = new char[1024];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (char)i;
		}

		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				char[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertTrue((char)j==result[j]);
					}
				}
			}
		}		
	}

	public void testClear() {
		CharArrayBuilder builder = new CharArrayBuilder();
		char[] testdata = new char[1024];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (char)i;
		}

		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				char[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((char)j, result[j]);
					}
				}
			}
		}		
		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				char[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((char)j, result[j]);
					}
				}
			}
		}
	}

	public void testSetBufferLength() {
		CharArrayBuilder builder = new CharArrayBuilder();
		char[] testdata = new char[777];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (char)i;
		}

		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			builder.setBufferLength(i+2);
			// check
			{
				char[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((char)j, result[j]);
					}
				}
			}
		}		
		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.setBufferLength(i+2);
			builder.append(testdata[i]);
			// check
			{
				char[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((char)j, result[j]);
					}
				}
			}
		}
	}

	public void testRemoveLast() {
		CharArrayBuilder builder = new CharArrayBuilder();
		char[] testdata = new char[777];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (char)i;
		}

		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				char[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((char)j, result[j]);
					}
				}
			}
		}
		for(int i=0;i<testdata.length;i++) {
			builder.removeLast();
			// check
			{
				char[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(testdata.length-i-1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((char)j, result[j]);
					}
				}
			}
		}
	}

}
