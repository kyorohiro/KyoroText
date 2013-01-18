package info.kyorohiro.helloworld.util.arraybuilder;

import junit.framework.TestCase;

public class TestForByteArrayBuilder extends TestCase {

	//
	// if modify test target code.  you must test at LEVEL = 0
	public static int LEVEL = 30;

	private void log(String log) {
		android.util.Log.v("kiyo","#TestForByteArrayBuilder#"+log);
	}


	public void testHello() {
		log("testHello");
	}

	public void testAppend() {
		ByteArrayBuilder builder = new ByteArrayBuilder();
		byte[] testdata = new byte[1024];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (byte)i;
		}

		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				byte[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((byte)j, result[j]);
					}
				}
			}
		}		
	}

	public void testClear() {
		ByteArrayBuilder builder = new ByteArrayBuilder();
		byte[] testdata = new byte[1024];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (byte)i;
		}

		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				byte[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((byte)j, result[j]);
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
				byte[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((byte)j, result[j]);
					}
				}
			}
		}
	}

	public void testSetBufferLength() {
		ByteArrayBuilder builder = new ByteArrayBuilder();
		byte[] testdata = new byte[777];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (byte)i;
		}

		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			builder.setBufferLength(i+2);
			// check
			{
				byte[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((byte)j, result[j]);
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
				byte[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((byte)j, result[j]);
					}
				}
			}
		}
	}

	public void testRemoveLast() {
		ByteArrayBuilder builder = new ByteArrayBuilder();
		byte[] testdata = new byte[777];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (byte)i;
		}

		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				byte[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((byte)j, result[j]);
					}
				}
			}
		}
		for(int i=0;i<testdata.length;i++) {
			builder.removeLast();
			// check
			{
				byte[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(testdata.length-i-1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((byte)j, result[j]);
					}
				}
			}
		}
	}

}
