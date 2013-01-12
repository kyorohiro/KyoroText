package info.kyorohiro.helloworld.io;

import java.io.IOException;

public interface KyoroFile {
	public void seek(long point) throws IOException;
	public long length() throws IOException;
	public int read(byte[] buffer) throws IOException;
	public void close() throws IOException;	
	public void addChunk(byte[] buffer, int begin, int end) throws IOException;
	public void syncWrite() throws IOException;
}
