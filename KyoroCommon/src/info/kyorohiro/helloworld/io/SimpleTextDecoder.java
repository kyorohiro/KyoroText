package info.kyorohiro.helloworld.io;

import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CharArrayBuilder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class SimpleTextDecoder {

	private MarkableReader mReader = null;
	private CharArrayBuilder mBuffer = new CharArrayBuilder();
	private Charset mCharset = null;
	private SimpleTextDecoderBreakText mBreakText;

	public SimpleTextDecoder(Charset _cs, MarkableReader reader, SimpleTextDecoderBreakText breakText) {
		mCharset = _cs;
		mReader = reader;
		mBreakText = breakText;
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

	private CharsetDecoder mDecoder = null;
	private ByteBuffer mByteBuffer = ByteBuffer.allocateDirect(32); // bb‚Í‘‚«ž‚ß‚éó‘Ô
	private CharBuffer mCharBuffer = CharBuffer.allocate(16); // cb‚Í‘‚«ž‚ß‚éó‘Ô

	public CharsetDecoder getCharsetDecoder() {
		if (mDecoder == null) {
			mDecoder = mCharset.newDecoder();
		}
		return mDecoder;
	}

//	public char decodeChar(byte[] escape) {
//		mBuffer.clear();
//	}

	public synchronized CharSequence decodeLine(byte[] escape) throws IOException {
		mBuffer.clear();
		mByteBuffer.clear();
		mCharBuffer.clear();
		CharsetDecoder decoder = getCharsetDecoder();
		boolean end = false;

		if (escape != null) {
			mByteBuffer.put(escape);
		}

		long todoPrevPosition = mReader.getFilePointer();
		outside: do {
			int d = mReader.read();
			if (d >= 0) {
				mByteBuffer.put((byte) d); // bb‚Ö‚Ì‘‚«ž‚Ý
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
					int len = Integer.MAX_VALUE;// example -->1024*4;// force
												// crlf 12kb
					if (mBreakText != null) {
						len = mBreakText.breakText(mBuffer);
						
					}
					if (len < mBuffer.getCurrentBufferedMojiSize()) {
						// ‚Ð‚Æ‚Â‘O‚Å‰üs
						mBuffer.removeLast();
						mReader.seek(todoPrevPosition);
						break outside;
					} else {
						todoPrevPosition = mReader.getFilePointer();
					}
				}
			}
			if (c == '\n') {
				break;
			}

			if (!added) {
				mByteBuffer.compact();
			}
			mCharBuffer.clear(); // cb‚ð‘‚«ž‚Ýó‘Ô‚É•ÏX
		} while (!end);
		return new KyoroString(mBuffer.getAllBufferedMoji(),
				mBuffer.getCurrentBufferedMojiSize());
	}
}
