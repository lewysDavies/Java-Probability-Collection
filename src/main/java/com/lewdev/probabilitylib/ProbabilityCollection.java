/*
 * Copyright (c) 2020 Lewys Davies
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.lewdev.probabilitylib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
 * @param <E> Type of elements
 * @author Lewys Davies
 * @version 0.8
 */
public class ProbabilityCollection<E> {
    private final NavigableSet<ProbabilitySetElement<E>> collection =
            new TreeSet<>(Comparator.comparingInt(ProbabilitySetElement::getIndex));
    private final SplittableRandom random = new SplittableRandom();

    private int totalProbability = 0;

    /**
     * @return Number of objects inside the collection
     */
    public int size() {
        return this.collection.size();
    }

    /**
     * @return True if collection contains no elements, else False
     */
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    /**
     * @param object Object to test against
     * @return True if collection contains the object, else False
     * @throws IllegalArgumentException if object is null
     */
    public boolean contains(@NotNull E object) {
        return this.collection.stream()
                .anyMatch(entry -> entry.getObject().equals(object));
    }

    /**
     * @return Iterator over this collection
     */
    @NotNull
    public Iterator<ProbabilitySetElement<E>> iterator() {
        return this.collection.iterator();
    }

    /**
     * Add an object to this collection
     *
     * @param object      Non-null object to add.
     * @param probability share. Must be greater than 0.
     * @throws IllegalArgumentException if probability <= 0
     */
    public void add(@NotNull E object, int probability) {
        if (probability <= 0) {
            throw new IllegalArgumentException("Probability must be greater than 0");
        }

        final ProbabilitySetElement<E> entry = new ProbabilitySetElement<>(object, probability);
        entry.setIndex(this.totalProbability + 1);

        this.collection.add(entry);
        this.totalProbability += probability;
    }

    /**
     * Remove a object from this collection
     *
     * @param object Object to remove
     * @return True if object was removed, else False.
     */
    public boolean remove(@NotNull E object) {
        final Iterator<ProbabilitySetElement<E>> it = this.iterator();
        boolean removed = false;

        while (it.hasNext()) {
            ProbabilitySetElement<E> entry = it.next();

            if (entry.getObject().equals(object)) {
                this.totalProbability -= entry.getProbability();
                it.remove();
                removed = true;
            }
        }

        this.updateIndexes();

        return removed;
    }

    /**
     * Remove all objects from this collection
     */
    public void clear() {
        this.collection.clear();
        this.totalProbability = 0;
    }

    /**
     * Get a random object from this collection, based on probability.
     *
     * @return <E> Random object
     * @throws IllegalStateException if this collection is empty
     */
    @NotNull
    public E get() {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot get an object out of a empty collection");
        }

        final ProbabilitySetElement<E> toFind = new ProbabilitySetElement<>(null, 0);
        toFind.setIndex(this.random.nextInt(1, this.totalProbability + 1));

        return Objects.requireNonNull(Objects.requireNonNull(this.collection.floor(toFind)).getObject());
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

        for (final ProbabilitySetElement<E> entry : this.collection) {
            previousIndex = entry.setIndex(previousIndex + 1) + (entry.getProbability() - 1);
        }
    }

    /**
     * Used internally to store information about a object's
     * state in a collection. Specifically, the probability
     * and index within the collection.
     * <p>
     * Indexes refer to the start position of this element's "block" of space.
     * The space between element "block"s represents their probability of being selected
     *
     * @param <T> Type of element
     * @author Lewys Davies
     */
    private static final class ProbabilitySetElement<T> {
        private final T object;
        private final int probability;
        private int index;

        protected ProbabilitySetElement(T object, int probability) {
            this.object = object;
            this.probability = probability;
        }

        /**
         * @return The actual object
         */
        @Nullable
        public T getObject() {
            return this.object;
        }

        /**
         * @return Probability share in this collection
         */
        public int getProbability() {
            return this.probability;
        }

        // Used internally, see this class's documentation
        private int getIndex() {
            return this.index;
        }

        // Used Internally, see this class's documentation
        private int setIndex(int index) {
            this.index = index;
            return this.index;
        }
    }
}
