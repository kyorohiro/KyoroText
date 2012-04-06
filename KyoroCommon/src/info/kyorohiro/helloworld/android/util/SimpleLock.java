package info.kyorohiro.helloworld.android.util;

public class SimpleLock {
    boolean working = false;
    Thread currentThread = null;

    public void begin() { 
    	lock();
    }
    public void end() { 
    	unlock();
    }

    protected synchronized void lock() {
    	Thread t = Thread.currentThread();
        while (working && currentThread != t) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        working = true;
        currentThread = t;
    }

    protected synchronized void unlock() {
        working = false;
        currentThread = null;
        notifyAll();
    }
}
