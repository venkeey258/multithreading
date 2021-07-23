package com.learn.boundedbuffer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class LrnBlockingQueue {

    public static void main(String[] args) {
	// TODO Auto-generated method stub
	final BlockingQueue<Integer> bq = new LinkedBlockingDeque<Integer>(5);
	 Semaphore semCon = new Semaphore(0);
	 Semaphore semProd = new Semaphore(1);
	Producer P1 = new Producer(bq,semCon,semProd);
	Producer P2 = new Producer(bq,semCon,semProd);
	Producer P3 = new Producer(bq,semCon,semProd);
	Consumer C1 = new Consumer(bq,semCon,semProd);
	Consumer C2 = new Consumer(bq,semCon,semProd);
	Thread t1 = new Thread(P1);
	t1.setName("Producer-1");
	Thread t2 = new Thread(C1);
	t2.setName("Consumer-1");
	Thread t3 = new Thread(C1);
	t3.setName("Consumer-2");
	Thread t4 = new Thread(C1);
	t4.setName("Consumer-3");
	Thread t5 = new Thread(C1);
	t5.setName("Consumer-4");
	Thread t6 = new Thread(C1);
	t6.setName("Consumer-5");
	t1.start();
	t2.start();
	t3.start();
	t4.start();
	t5.start();
	t6.start();
    }

}

class Producer implements Runnable{
    
    private final BlockingQueue<Integer> bq;
    private Semaphore semCon;
    private Semaphore semProd;
    
    

    public Producer(BlockingQueue<Integer> bq,Semaphore semCon,Semaphore semProd) {
	// TODO Auto-generated constructor stub
	this.bq = bq;
	this.semCon = semCon;
	this.semProd = semProd;
    }

    @Override
    public void run() {
	// TODO Auto-generated method stub
	while(true)
	{
	    try {
		semProd.acquire();
		bq.put((int)Math.random());
		System.out.println("Producer Producing: "+Thread.currentThread().getName()+"  with Size: "+bq.size());
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    semCon.release();
	}
    }
    
}

class Consumer implements Runnable{

    private final BlockingQueue<Integer> bq;
    private Semaphore semCon;
    private Semaphore semProd;
    public Consumer(BlockingQueue<Integer> bq,Semaphore semCon,Semaphore semProd) {
	// TODO Auto-generated constructor stub
	this.bq = bq;
	this.semCon = semCon;
	this.semProd = semProd;
    }
    @Override
    public void run() {
	// TODO Auto-generated method stub
	while(true) {
	    int output;
	    try {
		semCon.acquire();
		System.out.println("Consumer Thread consuming: "+Thread.currentThread().getName()+" ::"+bq.take()+" with Size "+bq.size());
		
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    semProd.release();
	    }
	
    }}
