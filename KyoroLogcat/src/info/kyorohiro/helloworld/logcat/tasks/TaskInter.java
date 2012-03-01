package info.kyorohiro.helloworld.logcat.tasks;

public interface TaskInter {
	void start();
	void run();
	void terminate();
	boolean isAlive();
}
