package info.kyorohiro.helloworld.pdf.pdflexer;

import info.kyorohiro.helloworld.io.VirtualMemory;

public abstract  class SourcePattern {
	public abstract boolean matchHead(VirtualMemory source);
	public abstract byte[] matchedData(VirtualMemory source);
	public abstract void back(VirtualMemory source);
	public abstract long start();
	public abstract long end();
	public abstract Token newToken();
}