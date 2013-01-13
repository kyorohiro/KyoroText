package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

public interface MiniBufferJob {
	void enter(String line);
	void tab(String line);
	void begin();
	void end();
}
