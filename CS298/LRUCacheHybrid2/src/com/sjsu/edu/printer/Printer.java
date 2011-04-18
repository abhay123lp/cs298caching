package com.sjsu.edu.printer;

public class Printer {

	public static void printString(String toBePrinted){
		System.out.println(toBePrinted);
	}
	
	public static void printInteger(int integerToBePrinted){
		
		Integer toBePrinted = new Integer(integerToBePrinted);
		
		System.out.println(toBePrinted.toString());
	}
}
