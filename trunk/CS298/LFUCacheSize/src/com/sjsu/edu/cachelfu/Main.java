package com.sjsu.edu.cachelfu;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {
	
	public static void main(String[] args)
	{
		
		Map<String,String> map = new TreeMap<String, String>();
		
		Collection<String> c = map.values();
		
		map.put("a", "a");
		map.put("b", "b");
		map.put("c", "c");
		map.put("d", "d");
		
		int i = 5;
		int j = 1;
		
		TreeSet<String> s = null;
		
		String key = null;
		
		while(i > j)
		{
		
			s = new TreeSet<String>();
			
			s.addAll(c);
		
			key = s.first();
			
			System.out.println("Removing " + key);
			
			map.remove(key);
			
			s.remove(key);
			
			i--;
		}
		
	
		
	}

}
