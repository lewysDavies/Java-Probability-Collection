package com.lewdev.probabilitylib;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;

/**
 * @author Lewys Davies
 */
public class ProbabilityCollectionTest {

	@RepeatedTest(value = 1000)
	public void test_insert() {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
		collection.add("A", 2);
		assertTrue(collection.contains("A"));
		assertEquals(1, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(2, collection.getTotalProbability()); // 2
		
		collection.add("B", 5);
		assertTrue(collection.contains("B"));
		assertEquals(2, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(7, collection.getTotalProbability()); // 5 + 2

		collection.add("C", 10);
		assertTrue(collection.contains("C"));
		assertEquals(3, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(17, collection.getTotalProbability()); // 5 + 2 + 10
		
		for(int i = 0; i < 100; i++) {
			collection.add("C", 1);
			
			assertTrue(collection.contains("C"));
			assertEquals(4 + i, collection.size()); // 4 + i
			assertFalse(collection.isEmpty());
			assertEquals(18 + i, collection.getTotalProbability()); // 5 + 2 + 10 + i
		}
	}
	
	@RepeatedTest(value = 1000)
	public void test_remove() {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		assertTrue(collection.size() == 0);
		assertTrue(collection.isEmpty());
		assertTrue(collection.getTotalProbability() == 0);
		
		String t1 = "Hello";
		String t2 = "World";
		String t3 = "!";
		
		collection.add(t1, 10);
		collection.add(t2, 10);
		collection.add(t3, 10);
		
		assertTrue(collection.size() == 3);
		assertFalse(collection.isEmpty());
		assertTrue(collection.getTotalProbability() == 30);
		
		collection.remove(t2);
		
		assertTrue(collection.size() == 2);
		assertFalse(collection.isEmpty());
		assertTrue(collection.getTotalProbability() == 20);
		
		collection.remove(t1);
		
		assertTrue(collection.size() == 1);
		assertFalse(collection.isEmpty());
		assertTrue(collection.getTotalProbability() == 10);
		
		collection.remove(t3);
		
		assertTrue(collection.size() == 0);
		assertTrue(collection.getTotalProbability() == 0);
		assertTrue(collection.isEmpty());
	}
	
	@RepeatedTest(value = 1000)
	public void test_remove_duplicates() {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		assertTrue(collection.size() == 0);
		assertTrue(collection.isEmpty());
		
		String t1 = "Hello";
		String t2 = "World";
		String t3 = "!";
		
		for(int i = 0; i < 10; i++) {
			collection.add(t1, 10);
		}
		
		for(int i = 0; i < 10; i++) {
			collection.add(t2, 10);
		}
		
		for(int i = 0; i < 10; i++) {
			collection.add(t3, 10);
		}
		
		assertTrue(collection.size() == 30);
		assertFalse(collection.isEmpty());
		assertTrue(collection.getTotalProbability() == 300);
		
		collection.remove(t2);
		
		assertTrue(collection.size() == 20);
		assertFalse(collection.isEmpty());
		assertTrue(collection.getTotalProbability() == 200);
		
		collection.remove(t1);
		
		assertTrue(collection.size() == 10);
		assertFalse(collection.isEmpty());
		
		assertTrue(collection.getTotalProbability() == 100);
		collection.remove(t3);
		
		assertTrue(collection.size() == 0);
		assertTrue(collection.isEmpty());
		assertTrue(collection.getTotalProbability() == 0);
	}

	@RepeatedTest(1_000_000)
	public void test_probability() {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
		collection.add("A", 50);
		collection.add("B", 25);
		collection.add("C", 10);
		
		int a = 0, b = 0, c = 0;
		int totalGets = 100000;
		
		for(int i = 0; i < totalGets; i++) {
			String random = collection.get();
			
			if(random.equals("A")) a++;
			else if(random.equals("B")) b++;
			else if(random.equals("C")) c++;
		}
		
		double aProb = 50.0 / (double) collection.getTotalProbability() * 100;
		double bProb = 25.0 / (double) collection.getTotalProbability() * 100;
		double cProb = 10.0 / (double) collection.getTotalProbability() * 100;
		
		double aResult = a / (double) totalGets * 100;
		double bResult = b / (double) totalGets * 100;
		double cResult = c / (double) totalGets * 100;
		
		double acceptableDeviation = 1;
		
		assertTrue(Math.abs(aProb - aResult) <= acceptableDeviation);
		assertTrue(Math.abs(bProb - bResult) <= acceptableDeviation);
		assertTrue(Math.abs(cProb - cResult) <= acceptableDeviation);
	}
}
