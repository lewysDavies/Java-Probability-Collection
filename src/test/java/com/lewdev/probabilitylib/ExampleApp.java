package com.lewdev.probabilitylib;

public class App {

	public static void main(String[] args) {
		ProbabilityCollection<String> collection = new ProbabilityCollection<>();
		
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
		
		System.out.println("   Prob     |  Actual");
		System.out.println("-----------------------");
		System.out.printf("A: %.3f%%  |  %.3f%% \n", aProb, aResult);
		System.out.printf("B: %.3f%%  |  %.3f%% \n", bProb, bResult);
		System.out.printf("C: %.3f%%  |  %.3f%% \n", cProb, cResult);
	}
}
