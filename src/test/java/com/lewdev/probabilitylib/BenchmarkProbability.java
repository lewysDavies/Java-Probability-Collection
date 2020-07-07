package com.lewdev.probabilitylib;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 5)
@Timeout(time = 25, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2)
public class BenchmarkProbability {

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(BenchmarkProbability.class.getSimpleName())
				.forks(1)
				.build();
		
		new Runner(opt).run();
	}
	
	private final int elements = 10_000;
	
	ProbabilityMap<Integer> map = new ProbabilityMap<>();
	ProbabilityCollection<Integer> collection = new ProbabilityCollection<>();
	
	private Map<Integer, Integer> addAllTest = new HashMap<>();
	
	@Setup
	public void setup() {
		for(int i = 0; i < elements; i++) {
			map.add(i, 1);
			collection.add(i, 1);
		}
		
		for(int i = elements; i < elements * 2; i++) {
			addAllTest.put(i, 1);
		}
	}
	
	@Benchmark
	public void mapAddSingle(Blackhole bh) {
		boolean added = this.map.add(25000, 1);
		bh.consume(added);
	}
	
	@Benchmark
	public void mapAddAll() {
		map.addAll(addAllTest);
	}
	
	@Benchmark
	public void collectionAddSingle() {
		this.collection.add(25000, 1);
	}
	
	@Benchmark
	public void mapGet(Blackhole bh) {
		for(int i = 0; i < elements * 2; i++) {
			bh.consume(map.get());
		}
	}
	
	@Benchmark
	public void collectionGet(Blackhole bh) {
		for(int i = 0; i < elements * 2; i++) {
			bh.consume(collection.get());
		}
	}
}
