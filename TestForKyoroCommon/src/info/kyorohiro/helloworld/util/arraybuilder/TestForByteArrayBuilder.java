package info.kyorohiro.helloworld.util.arraybuilder;

import junit.framework.TestCase;

public class TestForByteArrayBuilder extends TestCase {


	private void log(String log) {
		android.util.Log.v("kiyo","#TestForByteArrayBuilder#"+log);
	}


	public void testHello() {
		log("testHello");
	}

	public void testA() {
		ByteArrayBuilder builder = new ByteArrayBuilder();
		byte[] testdata = new byte[1024];
		for(int i=0;i<testdata.length;i++) {
			testdata[i] = (byte)i;
		}

		for(int i=0;i<testdata.length;i++) {
			builder.append(testdata[i]);
			// check
			{
				byte[] result = builder.getBuffer(); 
				int s = 0;
				int e = builder.length();
				assertEquals(i+1, e);
				for(int j=s;j<e;j++) {
					assertEquals((byte)j, result[j]);
				}
			}
		}

		
	}
}
