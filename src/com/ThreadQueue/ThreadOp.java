package com.ThreadQueue;

public class ThreadOp {

	// will be a doubly linked list
	private ThreadOp next = null;
	private ThreadOp prev = null;

	// pointer to parent queue
	private ThreadQueue parentQueue;

	// Thread to run
	private SBThread mySBThread; // the purpose of separating this class from
									// SBThread is that I want people to
									// subclass(SBThread), not this class, is I
									// don't want people messing with how

	// the ordering of the linked list goes. If people subclasses this class,
	// they could change all kinds of things such as the prev,next,parentQueue
	// pointers

	public ThreadOp(SBThread sb_t, ThreadQueue queue) { // constructor
		this.setMySBThread(sb_t);
		this.setParentQueue(queue);
		sb_t.setWrapperThreadOp(this); // pointer from thread back to us
	}

	public void startAsyncRun() {
		this.mySBThread.start();
	}

	public void removeMeFromParentQueue(SBThread sb_t) {
		// just make sure it is our thread calling us
		if (sb_t == this.mySBThread) {
			this.parentQueue.threadOpFinishedRunning(this);
		}
	}

	// These aren't synchronized because ThreadQueue should be the only class
	// calling these methods
	// setters
	public void setNext(ThreadOp n) {
		this.next = n;
	}

	public void setPrev(ThreadOp p) {
		this.prev = p;
	}

	public void setParentQueue(ThreadQueue q) { // set thread's parent queue too
		this.parentQueue = q;
		this.mySBThread.setParentQueue(q);
	}

	private void setMySBThread(SBThread t) {
		this.mySBThread = t;
	} // note private

	// getters
	public ThreadOp getNext() {
		return this.next;
	}

	public ThreadOp getPrev() {
		return this.prev;
	}

	public ThreadQueue getParentQueue() {
		return this.parentQueue;
	}

	public SBThread getMySBThread() {
		return this.mySBThread;
	}

	synchronized public boolean isAlive() {
		return this.mySBThread.isAlive();
	}

}
