package info.kyorohiro.helloworld.ext.textviewer.manager;

public interface MiniBufferJob {
	void enter(String line);
	void tab(String line);
	void begin();
	void end();
}
