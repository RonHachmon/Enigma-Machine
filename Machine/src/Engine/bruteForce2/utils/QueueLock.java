package Engine.bruteForce2.utils;

public class QueueLock {
    private Object mLock = new Object();
    private volatile boolean mLocked = false;
    private boolean isLocked=false;

    public void lock() {
        mLocked = true;
        isLocked=true;
    }

    public void unlock() {
        synchronized(this.mLock) {
            mLocked = false;
            mLock.notify();
        }

    }
    public void checkIfLocked()
    {

            synchronized(mLock) {
                while (mLocked) {
                    try {
                        mLock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

    }


}
