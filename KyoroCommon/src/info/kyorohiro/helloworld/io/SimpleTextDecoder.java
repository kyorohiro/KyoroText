package info.kyorohiro.helloworld.io;

import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.arraybuilder.ByteArrayBuilder;
import info.kyorohiro.helloworld.util.arraybuilder.CharArrayBuilder;
import info.kyorohiro.helloworld.util.arraybuilder.FloatArrayBuilder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import android.provider.UserDictionary.Words;


public class SimpleTextDecoder {

	private MarkableReader mReader = null;
	private CharArrayBuilder mBuffer = new CharArrayBuilder();
	private FloatArrayBuilder mWidths = new FloatArrayBuilder();
	private ByteArrayBuilder mSource = new ByteArrayBuilder();
	private CharsetDecoder mDecoder = null;
	private ByteBuffer mByteBuffer = ByteBuffer.allocateDirect(32); // bb�͏������߂���
	private CharBuffer mCharBuffer = CharBuffer.allocate(16); // cb�͏������߂���

	private Charset mCharset = null;
	private BreakText mBreakText;

	public SimpleTextDecoder(Charset _cs, MarkableReader reader, BreakText breakText) {
		mCharset = _cs;
		mReader = reader;
		mBreakText = breakText;
	}

	public long getFilePointer() throws IOException {
		return mReader.getFilePointer();
	}
	public long length() throws IOException {
		return mReader.length();
	}
	public boolean isEOF() {
		try {
			if (mReader.getFilePointer() < mReader.length()) {
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			return false;
		}
	}

	public CharSequence decodeLine() throws IOException {
		return decodeLine(null);
	}


	public CharsetDecoder getCharsetDecoder() {
		if (mDecoder == null) {
			mDecoder = mCharset.newDecoder();
		}
		return mDecoder;
	}


	public synchronized CharSequence decodeLine(byte[] escape) throws IOException {
		//long time1 = 0;
		//long time2 = 0;
		//time1 = System.currentTimeMillis();

		mBuffer.clear();
		mWidths.clear();
		mByteBuffer.clear();
		mCharBuffer.clear();
		mSource.clear();
		CharsetDecoder decoder = getCharsetDecoder();
		boolean end = false;

		if (escape != null) {
			mByteBuffer.put(escape);
		}

		long todoPrevPosition = mReader.getFilePointer();
		
		float[] ws = new float[10];
		float textLength = 0;
		int width = Integer.MAX_VALUE;
		float textSize = 12;
		if(mBreakText != null) {
			width = mBreakText.getWidth();
			textSize = mBreakText.getSimpleFont().getFontSize();
		}
	//	android.util.Log.v("kiyo","textSize="+textSize+","+width);
		int len = 0;

		int numOfRead = 0;
		outside: do {
			int d = mReader.read();
			numOfRead++;
			if (d >= 0) {
				mByteBuffer.put((byte) d); // bb�ւ̏�������
				mSource.append((byte)d);
			} else {
				break;
			}
			mByteBuffer.flip();
			decoder.decode(mByteBuffer, mCharBuffer, end);

			mCharBuffer.flip();
			char c = ' ';
			boolean added = false;
			while (mCharBuffer.hasRemaining()) {
				added = true;
				mByteBuffer.clear();
				c = mCharBuffer.get();
				mBuffer.append(c);
				{
					// following code maby out of memor error , you must change
					// kb
					//int len = Integer.MAX_VALUE;// example -->1024*4;// force
												// crlf 12kb
					if (mBreakText != null) {
//						len = mBreakText.breakText(mBuffer);
						int size = mBuffer.length();
						ws[0] = 0;
						
						mBreakText.getTextWidths(mBuffer.getBuffer(), size-1, size, ws, textSize);
						textLength += ws[0];
						mWidths.append(ws[0]);
						//if(c=='\t'){
						//android.util.Log.v("kiyo","time2 ["+mBuffer.getAllBufferedMoji()[size-1]+"]"+c+"="+width+","+textLength +","+ ws[0]);
						//}

						if(textLength>width){
							//android.util.Log.v("kiyo","dd1 s="+textLength+",w="+width+",l="+len+",s="+size);
							len += 0;
						} else {
							//android.util.Log.v("kiyo","dd2 s="+textLength+",w="+width+",l="+len+",s="+size);
							len +=1;
						}
					}
					if (len < mBuffer.length()) {
						numOfRead = (int)(mReader.getFilePointer()-todoPrevPosition);
						mBuffer.removeLast();
						for(int i=0;i<numOfRead;i++) {
							mSource.removeLast();
						}
						mReader.seek(todoPrevPosition);
						numOfRead=0;
						break outside;
					} else {
						todoPrevPosition = mReader.getFilePointer();
						numOfRead = 0;
					}
				}
			}
			if (c == '\n') {
				break;
			}

			if (!added) {
				mByteBuffer.compact();
			}
			mCharBuffer.clear(); // cb���������ݏ�ԂɕύX
		} while (!end);
		KyoroString ret =new KyoroString(mBuffer.getBuffer(),
				mBuffer.length());
		ret.setCash(mWidths.getBuffer(),mWidths.length(), (int)textSize);
		ret.setCashContent(mSource.getBuffer(),mSource.length());
		//time2 = System.currentTimeMillis();
		//android.util.Log.v("kiyo","time a="+(time2-time1));
		return ret;
	}
}
