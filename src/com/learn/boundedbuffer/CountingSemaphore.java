package com.learn.boundedbuffer;

public class CountingSemaphore {

    int usedPermits = 0;
    int maxCount;

    public CountingSemaphore(int count) {
        this.maxCount = count;
    }

    public CountingSemaphore(int count, int initialPermits) {
        this.maxCount = count;
        this.usedPermits = this.maxCount - initialPermits;
    }

    public synchronized void acquire() throws InterruptedException {

        while (usedPermits == maxCount)
            wait();

        notify();
        usedPermits++;
    }

    public synchronized void release() throws InterruptedException {

        while (usedPermits == 0)
            wait();

        usedPermits--;
        notify();
    }
}

class Demo{
    public static void main(String[] args) {
	final CountingSemaphore cs = new CountingSemaphore(1);
	
	Thread t1 = new Thread(new Runnable() {
	    public void run() {
		
		    try {
			for(int i =0;i<5;i++) {
			    cs.acquire();
			    System.out.println("Ping"+i);
			}
		    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  
		    
		}
	    }
	});
	
	Thread t2 = new Thread(new Runnable() {
	    public void run() {
		
		    try {
			for(int i =0;i<5;i++) {
			    cs.release();
			    System.out.println("Pong"+i);
			}
		    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  
		    
		}
	    }
	});
	
	t1.start();
	t2.start();
	
	
	
    }
}

