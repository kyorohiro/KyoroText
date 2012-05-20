package info.kyorohiro.helloworld.memoryinfo.task;

public class TaskRunner {

	Thread th = null;
	public void start(Runnable r) {
		stop();
		th = new Thread(r);
		th.start();
	}

	public void stop() {
		Thread tmp = th;
		th = null;
		if(tmp != null && tmp.isAlive()){
			tmp.interrupt();
		}		
	}
}
