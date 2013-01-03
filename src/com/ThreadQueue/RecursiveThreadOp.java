package com.ThreadQueue;
public class RecursiveThreadOp extends SBThread {
	static int NUM_OF_RECURSIONS = 0;
	public RecursiveThreadOp () { super("RecursiveThread"); }
	public void myRun() throws InterruptedException {
		NUM_OF_RECURSIONS ++;
		for (int i = 0; i < 100; i++) {
			Thread.sleep(10);
			System.out.println("Running op ---- RECURSIVE");
		}	
		if (NUM_OF_RECURSIONS <= 3) {
			RecursiveThreadOp op = new RecursiveThreadOp();
			op.becomeDependentOn(this);
		}
	}
	@Override
	public void runAsync() {
		
	}
}
