package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

public interface MiniBufferTask {
	void enter(String line);
	void begin();
	void end();
}