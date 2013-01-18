package info.kyorohiro.helloworld.util.arraybuilder;

import junit.framework.TestCase;

public class TestForFloatArrayBuilder extends TestCase {


	private static final int LEVEL = 10;

	private void log(String log) {
		android.util.Log.v("kiyo","#TestForFloatArrayBuilder#"+log);
	}


	public void testHello() {
		log("testHello");
	}

	public void testAppend() {
		FloatArrayBuilder builder = new FloatArrayBuilder();
		float[] testdata = new float[1024];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (float)i;
		}

		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				float[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertTrue((float)j==result[j]);
					}
				}
			}
		}		
	}

	public void testClear() {
		FloatArrayBuilder builder = new FloatArrayBuilder();
		float[] testdata = new float[1024];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (float)i;
		}

		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				float[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((float)j, result[j]);
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
				float[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((float)j, result[j]);
					}
				}
			}
		}
	}

	public void testSetBufferLength() {
		FloatArrayBuilder builder = new FloatArrayBuilder();
		float[] testdata = new float[777];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (float)i;
		}

		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			builder.setBufferLength(i+2);
			// check
			{
				float[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((float)j, result[j]);
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
				float[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((float)j, result[j]);
					}
				}
			}
		}
	}

	public void testRemoveLast() {
		FloatArrayBuilder builder = new FloatArrayBuilder();
		float[] testdata = new float[777];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (float)i;
		}

		builder.clear();
		for(int i=0;i<testdata.length;i++) {
			assertEquals(i, builder.length());
			builder.append(testdata[i]);
			// check
			{
				float[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((float)j, result[j]);
					}
				}
			}
		}
		for(int i=0;i<testdata.length;i++) {
			builder.removeLast();
			// check
			{
				float[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(testdata.length-i-1, e);
				if(i==0||i%LEVEL==0){
					for(int j=s;j<e;j++) {
						assertEquals((float)j, result[j]);
					}
				}
			}
		}
	}

}
