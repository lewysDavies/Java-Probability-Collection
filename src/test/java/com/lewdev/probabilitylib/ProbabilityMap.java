package com.lewdev.probabilitylib;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * ProbabilityMap to easily handle probability <br>
 * <br>
 * 
 * <b>Selection Algorithm Implementation</b>:
 * <p>
 * <ul>
 * <li>Elements have a "box" of space, sized based on their probability share
 * <li>"Boxes" start from index 1 and end at the total probability of elements
 * <li>A random number is selected between 1 and the total probability
 * <li>Which "box" the random number falls in is the element that is selected
 * <li>Therefore "boxes" with larger probability have a greater chance of being
 * selected than those with smaller probability.
 * </p>
 * </ul>
 * 
 * @param <E> Type of elements
 * @version 0.5
 * 
 * @author Lewys Davies
 */
public class ProbabilityMap<E> {

	private LinkedHashMap<E, Integer> map = new LinkedHashMap<>();

	private int totalProbability = 0;

	/**
	 * Construct a empty probability map
	 */
	public ProbabilityMap() { }

	/**
	 * Construct a probability map with initial elements
	 * 
	 * @param elements
	 */
	public ProbabilityMap(Map<E, Integer> elements) {
		this.addAll(elements);
	}

	/**
	 * Add a element to the map
	 * 
	 * @param element
	 * @param probability x > 0
	 */
	public final boolean add(E element, int probability) {
		this.map.put(element, probability);
		this.updateState();
		return true;
	}

	/**
	 * Add all elements from a different map to this one
	 * 
	 * @param elements
	 */
	public final void addAll(Map<E, Integer> elements) {
		this.map.putAll(elements);
		this.updateState();
	}

	/**
	 * Get a random element from the map
	 * 
	 * @return Random element based on probability | null if map is empty
	 */
	public final E get() {
		if (this.map.isEmpty())
			return null;

		// Map is sorted when changed
		// Therefore probability of elements is already in descending order: i.e. 5, 5,
		// 4, 1, 1

		// Random int between 1 and total probability (+1 as nextInt bound is exclusive)
		int randomProb = ThreadLocalRandom.current().nextInt(1, this.totalProbability + 1);

		int cumulativeProb = 0;
		E selectedElm = null;

		for (Entry<E, Integer> entry : this.map.entrySet()) {
			// Calculate the size of this elements box: i.e 1-5, 6-10, 11-14, 15, 16
			int boxStart = cumulativeProb + 1;
			int boxEnd = boxStart + (entry.getValue() - 1);

			// Check if the elements box falls within the randomly chosen index
			if (randomProb >= boxStart && randomProb <= boxEnd) {
				selectedElm = entry.getKey();
				break;
			}

			// If not keep searching
			cumulativeProb = boxEnd;
		}

		return selectedElm;
	}

	/**
	 * Remove a element from the map
	 * 
	 * @param element
	 */
	public final void remove(E element) {
		this.map.remove(element);
		this.updateState();
	}

	/**
	 * Remove all elements from the map
	 */
	public final void clear() {
		this.map.clear();
		this.updateState();
	}

	/**
	 * @return Sum of all the element's probability
	 */
	public final int getTotalProbability() {
		return this.totalProbability;
	}

	private final void updateState() {
		// Update total probability cache
		this.totalProbability = this.map.values().stream().mapToInt(Integer::intValue).sum();

		// Sort LinkedHashMap
		this.map = this.map.entrySet().stream().sorted(Map.Entry.<E, Integer>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
	}
}