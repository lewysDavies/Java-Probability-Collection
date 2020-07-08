package com.lewdev.probabilitylib;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ProbabilityCollection for retrieving random elements based on probability.
 * <br>
 * <br>
 * <b>Selection Algorithm Implementation</b>:
 * <p>
 * <ul>
 * <li>Elements have a "block" of space, sized based on their probability share
 * <li>"Blocks" start from index 1 and end at the total probability of all elements
 * <li>A random number is selected between 1 and the total probability
 * <li>Which "block" the random number falls in is the element that is selected
 * <li>Therefore "block"s with larger probability have a greater chance of being
 * selected than those with smaller probability.
 * </p>
 * </ul>
 * 
 * @author Lewys Davies
 * @version 0.6
 *
 * @param <E> Type of elements
 */
public class ProbabilityCollection<E> {

	protected final Comparator<ProbabilitySetElement<E>> comparator = 
			(o1, o2)-> Integer.compare(o1.getIndex(), o2.getIndex());
	
	private final TreeSet<ProbabilitySetElement<E>> collection;

	private int totalProbability;
	
	/**
	 * Construct a new Probability Collection
	 */
	public ProbabilityCollection() {
		this.collection = new TreeSet<>(this.comparator);
		this.totalProbability = 0;
	}

	/**
	 * @return Number of objects inside the collection
	 */
	public int size() {
		return this.collection.size();
	}
	
	/**
	 * @return Collection contains no elements
	 */
	public boolean isEmpty() {
		return this.collection.isEmpty();
	}
	
	/**
	 * @param object
	 * @return True if the collection contains the object, else False
	 * @throws IllegalArgumentException if object null
	 */
	public boolean contains(E object) {
		if(object == null) {
			throw new IllegalArgumentException("Cannot check if null object is contained in a collection");
		}
		
		return this.collection.stream()
			.anyMatch(entry -> entry.getObject().equals(object));
	}

	/**
	 * @return Iterator over collection
	 */
	public Iterator<ProbabilitySetElement<E>> iterator() {
		return this.collection.iterator();
	}
	
	/**
	 * Add an object to this collection
	 * 
	 * @param object. Not null.
	 * @param probability share. Must be greater than 0.
	 * 
	 * @throws IllegalArgumentException if object is null
	 * @throws IllegalArgumentException if probability <= 0
	 */
	public void add(E object, int probability) {
		if(object == null) {
			throw new IllegalArgumentException("Cannot add null object");
		}
		
		if(probability <= 0) {
			throw new IllegalArgumentException("Probability must be greater than 0");
		}
		
		this.collection.add(new ProbabilitySetElement<E>(object, probability));
		this.totalProbability += probability;
		
		this.updateIndexes();
	}

	/**
	 * Remove a object from this collection
	 * 
	 * @param object
	 * @return True if object was removed, else False.
	 * 
	 * @throws IllegalArgumentException if object null
	 */
	public boolean remove(E object) {
		if(object == null) {
			throw new IllegalArgumentException("Cannot remove null object");
		}
		
		Iterator<ProbabilitySetElement<E>> it = this.iterator();
		boolean removed = it.hasNext();
		
		while(it.hasNext()) {
			ProbabilitySetElement<E> entry = it.next();
			if(entry.getObject().equals(object)) {
				this.totalProbability -= entry.getProbability();
				it.remove();
			}
		}
		
		this.updateIndexes();
		
		return removed;
	}
	
	/**
	 * Get a random object from this collection, based on probability.
	 * 
	 * @return <E> Random object
	 * 
	 * @throws IllegalStateException if this collection is empty
	 */
	public E get() {
		if(this.isEmpty()) {
			throw new IllegalStateException("Cannot get an element out of a empty set");
		}
		
		ProbabilitySetElement<E> toFind = new ProbabilitySetElement<>(null, 0);
		toFind.setIndex(ThreadLocalRandom.current().nextInt(1, this.totalProbability + 1));
		
		return Objects.requireNonNull(this.collection.floor(toFind).getObject());
	}
	
	/**
	 * @return Sum of all element's probability
	 */
	public final int getTotalProbability() {
		return this.totalProbability;
	}
	
	/*
	 * Calculate the size of all element's "block" of space: 
	 * i.e 1-5, 6-10, 11-14, 15, 16
	 * 
	 * We then only need to store the start index of each element,
	 * as we make use of the TreeSet#floor
	 */
	private void updateIndexes() {
		int previousIndex = 0;
		
		for(ProbabilitySetElement<E> entry : this.collection) {
			previousIndex = entry.setIndex(previousIndex + 1) + (entry.getProbability() - 1);
		}
	}
	
	/**
	 * Used internally to store information about a object's
	 * state in a collection. Specifically, the probability 
	 * and index within the collection.
	 * 
	 * Indexes refer to the start position of this element's "block" of space.
	 * The space between element "block"s represents their probability of being selected
	 * 
	 * @author Lewys Davies
	 *
	 * @param <T> Type of element
	 */
	final static class ProbabilitySetElement<T> {
		private final T object;
		private final int probability;
		private int index;
		
		/**
		 * @param object
		 * @param probability
		 */
		protected ProbabilitySetElement(T object, int probability) {
			this.object = object;
			this.probability = probability;
		}

		/**
		 * @return The actual object
		 */
		public final T getObject() {
			return this.object;
		}

		/**
		 * @return Probability share in this collection
		 */
		public final int getProbability() {
			return this.probability;
		}
		
		// Used internally, see this class's documentation
		private final int getIndex() {
			return this.index;
		}
		
		// Used Internally, see this class's documentation
		private final int setIndex(int index) {
			this.index = index;
			return this.index;
		}
	}
}
