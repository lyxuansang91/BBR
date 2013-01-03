package com.ThreadQueue;

import java.util.Collection;


public class ThreadQueue {

	private ThreadOp head = null;
	private ThreadOp tail = null;
	private int queueSize = 0;
	private int maxNumberOfConcurrentThreads = 2;  //default is two concurrent events
	private int numberOfThreadsCurrentlyRunning = 0;
	
	
	
	//Additions to queue
	synchronized public void addToTopOfQueue (SBThread sb_t) {
		//make sure thread isn't already rynning
		if( !sb_t.isAlive() ) {
			ThreadOp aThread = new ThreadOp(sb_t, this);
			if (this.queueSize > 0) {
				//add to head of queue
				aThread.setPrev(this.head);
				aThread.setNext(null);
				this.head.setNext(aThread);
				this.head = aThread;
			} else {  //first item
				System.out.println("setting first item in the empty queue");
			
				this.head = aThread;
				this.tail = aThread;
	
				this.head.setNext(null); //nothing after the head
				this.tail.setPrev(null);  //nothing before the tail 
			}
			this.finishAddingThreadToQueue(aThread);
		} else {
			System.out.println("Can't add an already running thread to the queue");
		}
	}
	
	synchronized public void addToQueue(SBThread sb_t) {
		//make sure thread isn't already running
		if ( !sb_t.isAlive() ) {
			ThreadOp aThread = new ThreadOp(sb_t, this);
		
			if (this.queueSize > 0) {
				aThread.setNext(this.tail);
				aThread.setPrev(null);   //nothing before the tail
				this.tail.setPrev(aThread);
				this.tail = aThread;
			} else { //set first item
				System.out.println("setting first item in the empty queue");
				
				this.head = aThread;
				this.tail = aThread;
	
				this.head.setNext(null); //nothing after the head
				this.tail.setPrev(null);  //nothing before the tail 
			}
		
			this.finishAddingThreadToQueue(aThread);
		} else {
			System.out.println("Can't add an already running thread to the queue");
		}
	}
	
	//what each add to queue method will call
	synchronized private void finishAddingThreadToQueue(ThreadOp aThread) { 
		System.out.println("ADDED THREAD TO QUEUE");
		this.queueSize += 1;
		this.dispatchNextThreadIfPossible();
	}
	
	//convenience additions
	public void addToQueue(SBThread []sb_threads) {
		for (int i = 0; i < sb_threads.length; i++) {
			this.addToQueue(sb_threads[i]);
		}
	}
	public void addToQueue(Collection <SBThread> sb_threads) {
		for (SBThread t : sb_threads) {
			this.addToQueue(t);
		}
	}
	
	
	
	
	
	//removal from queue
	//at the moment, this doesn't have an elegant solution for stopping the thread, I need to come up with a solution.
	synchronized public void removeOperationFromQueue(ThreadOp op) {
		
		if (op.getParentQueue() == this) { //only remove operations that belong to us
			
			// I need to consider whether to check if the thread is running before removing it ... and how I would handle a removal of a live thread...
			// i.e  if (op.isAlive()) 
			
			if (this.queueSize == 1) { //last element
				this.tail = null;
				this.head = null;
			} else if (op == this.head) {
				if (this.head.getPrev() != null) {
					this.head.getPrev().setNext(null); //make new head
				}
				//this is redundant, but makes head null when removing the final item
				this.head = this.head.getPrev() == null ? null : this.head.getPrev();	
			} else if (op == this.tail) {
				if (this.tail.getNext() != null) {
					this.tail.getNext().setPrev(null);
				}
				//this line is redundant, but makes tail null when removing the final item
				this.tail = (this.tail.getNext() == null) ? null : this.tail.getNext();
			} else {
				//remove pointers referring to op from prev/next
				// .... note, there is some redundancy here
				if (op.getPrev() != null) {
					op.getPrev().setNext( (op.getNext() == null) ? null : op.getNext() );
				} 
				if (op.getNext() != null) {
					op.getNext().setPrev( (op.getPrev() == null) ? null : op.getPrev() );
				}
			}
			
			op.setPrev(null);
			op.setNext(null);
			op.setParentQueue(null);
			
			this.queueSize -= 1;
			
			System.out.println("Removed an element from the queue\n" + this.toString());
		} else {
			System.out.println("CANT REMOVE A THREAD FROM QUEUE THAT DOESN'T BELONG TO ME");
		}
	}
	
	
	
	//dispatching and receiving finished events
	synchronized private void dispatchNextThreadIfPossible() {
		ThreadOp anOp = this.head;
		while (this.numberOfThreadsCurrentlyRunning < this.maxNumberOfConcurrentThreads && anOp != null) {
			//cache pointer in case new thread finishes running and is removed from queue before next iteration of loop finishes (this will probably never happen, but better to be safe)
			ThreadOp nextOpToExec = anOp.getPrev(); 
			if (!anOp.isAlive()) {
				anOp.startAsyncRun(); //start the thread
				this.numberOfThreadsCurrentlyRunning += 1;					
				System.out.println("Queue just dispatched a thread \n" + this.toString());
			}
			
			anOp = nextOpToExec;
		}		
	}
	
	public void threadOpFinishedRunning(ThreadOp anOp) {
		this.removeOperationFromQueue(anOp);
		synchronized(this) {
			this.numberOfThreadsCurrentlyRunning -= 1;
		}
		this.dispatchNextThreadIfPossible();
		System.out.println("A thread finished running\n" + this.toString());
	}
	
	
	synchronized public void setMaxNumberOfConcurrentThreads (int n) {
		this.maxNumberOfConcurrentThreads = n;
		this.dispatchNextThreadIfPossible();
	}
	
	
	public String toString() {
		return ("\tNumber of items in queue: " + this.queueSize + "\n\tCurrently running: " + this.numberOfThreadsCurrentlyRunning + " Threads" + "\n\tMax number threads: " + this.maxNumberOfConcurrentThreads);
	}
	
}







