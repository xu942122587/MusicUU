package com.qtfreet.musicuu.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPool {
	private static MyThreadPool myThreadPool=new MyThreadPool();
	private ExecutorService executorService;

	private MyThreadPool(){
		 executorService=Executors.newSingleThreadExecutor();
		
	}
	
	public static MyThreadPool getInstance(){
		return myThreadPool;
	}
	
	public ExecutorService getMyExecutorService(){
		return executorService;
	}
	
}
