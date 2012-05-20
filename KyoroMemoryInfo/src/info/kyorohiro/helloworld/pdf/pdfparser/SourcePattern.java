package info.kyorohiro.helloworld.pdf.pdfparser;

import info.kyorohiro.helloworld.io.VirtualMemory;

public abstract  class SourcePattern {
	public abstract boolean matchHead(VirtualMemory source);
	public abstract byte[] muchedData();
	public abstract void back(VirtualMemory source);
	public abstract long start();
	public abstract long end();
}