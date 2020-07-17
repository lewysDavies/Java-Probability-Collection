package com.lewdev.probabilitylib;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Lewys Davies
 */
public class ProbabilityCollectionTest {

	@RepeatedTest(value = 10_000)
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
	
	@RepeatedTest(value = 10_000)
	public void test_remove() {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
		String t1 = "Hello";
		String t2 = "World";
		String t3 = "!";
		
		collection.add(t1, 10);
		collection.add(t2, 10);
		collection.add(t3, 10);
		
		assertEquals(3, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(30, collection.getTotalProbability());
		
		// Remove t2
		assertTrue(collection.remove(t2));
		
		assertEquals(2, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(20, collection.getTotalProbability());
		
		// Remove t1	
		assertTrue(collection.remove(t1));
		
		assertEquals(1, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(10, collection.getTotalProbability());
		
		//Remove t3
		assertTrue(collection.remove(t3));
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
	}
	
	@RepeatedTest(value = 10_000)
	public void test_remove_duplicates() {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		assertEquals(0, collection.size());
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
		
		assertEquals(30, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(300, collection.getTotalProbability());
		
		//Remove t2
		assertTrue(collection.remove(t2));
		
		assertEquals(20, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(200, collection.getTotalProbability());
		
		// Remove t1
		assertTrue(collection.remove(t1));
		
		assertEquals(10, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(100, collection.getTotalProbability());
		
		//Remove t3
		assertTrue(collection.remove(t3));
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
	}
	
	@RepeatedTest(value = 10_000)
	public void test_clear() {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
		collection.clear();
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
		collection.add("tmp", 1);
		
		assertEquals(1, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(1, collection.getTotalProbability());
		
		collection.clear();
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
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
		
		assertEquals(30, collection.size());
		assertFalse(collection.isEmpty());
		assertEquals(300, collection.getTotalProbability());
		
		collection.clear();
		
		assertEquals(0, collection.getTotalProbability());
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
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

		int totalGets = 100_000;
		
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
		
		double acceptableDeviation = 1; // %
		
		assertTrue(Math.abs(aProb - aResult) <= acceptableDeviation);
		assertTrue(Math.abs(bProb - bResult) <= acceptableDeviation);
		assertTrue(Math.abs(cProb - cResult) <= acceptableDeviation);
	}
	
	@RepeatedTest(1_000_000)
	public void test_get_never_null() {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		// Tests get will never return null
		// Just one smallest element get, must not return null
		collection.add("A", 1);
		assertNotNull(collection.get());
		
		// Reset state
		collection.remove("A");
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		
		// Just one large element, must not return null
		collection.add("A", 5_000_000);
		assertNotNull(collection.get());
	}
	
	@Test
	public void test_Errors() {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());

		// Cannot get from empty collection
		assertThrows(IllegalStateException.class, collection::get);
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
		// Cannot add prob 0
		assertThrows(IllegalArgumentException.class, () -> collection.add("A", 0));
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
		
		assertEquals(0, collection.size());
		assertTrue(collection.isEmpty());
		assertEquals(0, collection.getTotalProbability());
	}
}
