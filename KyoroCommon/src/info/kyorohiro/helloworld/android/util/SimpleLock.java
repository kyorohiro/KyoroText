package info.kyorohiro.helloworld.android.util;

public class SimpleLock {
    boolean working = false;
    Thread currentThread = null;
    int lockNum = 0;

    public synchronized void begin() { 
    	lock();
    }
    public synchronized void end() { 
    	unlock();
    }

    protected synchronized void lock() {
    	Thread t = Thread.currentThread();
        while (working && currentThread != t) {
            try {
            	lockNum++;
                wait();
            } catch (InterruptedException e) {
            }
        }
        working = true;
        currentThread = t;
    }

    protected synchronized void unlock() {
    	lockNum--;
    	if(lockNum <= 0){
    		working = false;
    		currentThread = null;
    		lockNum = 0;
    		notifyAll();
    	}
    }
}
