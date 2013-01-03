package com.ThreadQueue;

abstract public class SBThread extends Thread {
	
	private SBThread dependentOnMe = null;  //after completion, our dependency will be called
	private ThreadOp wrapperThreadOp = null;   //reference to our LinkedList wrapper class
	private String threadID = "NO_THREAD_ID_ASSIGNED";
	protected ThreadQueue parentQueue = null;
	private boolean isCancelled = false;
	
	public SBThread(){}  //empty
	public SBThread(String id) { this.threadID = id; }  //set id
	
	final public void run () {
		this.runAsync();
		if (this.dependentOnMe != null && this.parentQueue != null && !this.isCancelled) {
			this.parentQueue.addToTopOfQueue(this.dependentOnMe);
		}
		System.out.println(this.toString() + "    is notifying parent queue that we have finished running");
		this.wrapperThreadOp.removeMeFromParentQueue(this);
	}
	
	//method to be overwritten
	abstract public void runAsync ();
	
	//add this as dependency to op
	synchronized public void becomeDependentOn(SBThread op) {
		if (this.isAlive()) {
			System.out.println("can't become dependent on another thread because we are already running");
		} else {
			op.dependentOnMe = this;
		
			//if we are in a queue, remove ourself
			if (this.parentQueue != null && this.wrapperThreadOp != null) {
				System.out.println("Removing ourself from queue because we are now dependent on another thread finishing first");
				this.wrapperThreadOp.removeMeFromParentQueue(this);
			}
		}
			
	}
	
	//cancel operation. This won't halt the thread from finishing running, but will prevent dependencies from running
	synchronized public void cancelThread() {
		this.isCancelled = true;
	}
	
	
	//setters
	public final void setWrapperThreadOp(ThreadOp op) { this.wrapperThreadOp = op; }  //doesn't need to be synchronized, should only be called from one thread once.
	public void setParentQueue(ThreadQueue q) { this.parentQueue = q; }
	
	//getters
	
	
	
	
	public String toString() {
		return ("ThreadID: " + this.threadID); 
	}
}
