package com.sjsu.edu.commonhelpers;

public class GarbageCollectorHelper {
	
	public static void invokeGC()
	{
		  Runtime rc = Runtime.getRuntime();
		  
		  rc.gc();
		  
		  try 
		  {
			Thread.sleep(1000);
		  }
		  catch (InterruptedException e) 
		  {
			e.printStackTrace();
		  }
		  
		  rc.gc();
	}

}
