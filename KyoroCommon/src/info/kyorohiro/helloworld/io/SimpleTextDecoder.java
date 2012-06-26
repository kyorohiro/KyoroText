package info.kyorohiro.helloworld.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

public class SimpleTextDecoder {

	private VirtualMemory mReader = null;
	private MyBuilder mBuffer = new MyBuilder();
	private Charset mCs = null;
	private BreakText mBreakText;

	public SimpleTextDecoder(Charset _cs, VirtualMemory reader,
			BreakText breakText) {
		mCs = _cs;
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

	public CharSequence decodeLine(byte[] escape) throws IOException {
		mBuffer.clear();
		CharsetDecoder decoder = mCs.newDecoder();
		boolean end = false;
		ByteBuffer bb = ByteBuffer.allocateDirect(32); // bb‚Í‘‚«‚ß‚éó‘Ô
		CharBuffer cb = CharBuffer.allocate(16); // cb‚Í‘‚«‚ß‚éó‘Ô

		if (escape != null) {
			bb.put(escape);
		}

		// todo
		int mode = TODOCRLFString.MODE_INCLUDE_LF;
		long todoPrevPosition = mReader.getFilePointer();
		outside: do {
			int d = mReader.read();
			if (d >= 0) {
				bb.put((byte) d); // bb‚Ö‚Ì‘‚«‚İ
			} else {
				break;
			}
			bb.flip();
			CoderResult cr = decoder.decode(bb, cb, end);

			cb.flip();
			char c = ' ';
			boolean added = false;
			while (cb.hasRemaining()) {
				added = true;
				bb.clear();
				c = cb.get();
				mBuffer.append(c);
				int len = mBreakText.breakText(mBuffer);
				if (len < mBuffer.getCurrentBufferedMojiSize()) {
					// todo add test case following pattern.
					// add breakText return 2
					// but b.length return 1
					// >> before modify coding if(len != tmp.length())
					//

					// ‚Ğ‚Æ‚Â‘O‚Å‰üs
					mBuffer.removeLast();
					mReader.seek(todoPrevPosition);
					mode = TODOCRLFString.MODE_EXCLUDE_LF;
					break outside;
				} else {
					todoPrevPosition = mReader.getFilePointer();
				}
			}
			if (c == '\n') {
				break;
			}

			if (!added) {
				bb.compact();
			}
			cb.clear(); // cb‚ğ‘‚«‚İó‘Ô‚É•ÏX
		} while (!end);
		return new TODOCRLFString(mBuffer.getAllBufferedMoji(),
				mBuffer.getCurrentBufferedMojiSize(), mode);
	}
}
