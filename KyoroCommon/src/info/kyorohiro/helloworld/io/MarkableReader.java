package info.kyorohiro.helloworld.io;

import java.io.IOException;

public interface MarkableReader {
	public void pushMark();
	public void backToMark();
	public long popMark();
	public int read() throws IOException;
	public void seek(long pos) throws IOException;	
	public long getFilePointer() throws IOException;
	public long length() throws IOException;
	public void close() throws IOException;
}
