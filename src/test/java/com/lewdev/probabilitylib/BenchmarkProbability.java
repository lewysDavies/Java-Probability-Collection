package com.lewdev.probabilitylib;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class BenchmarkProbability {

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(BenchmarkProbability.class.getSimpleName())
				.forks(1)
				.build();
		
		new Runner(opt).run();
	}
	
	public int elements = 1_000;
	
	public int toAdd = elements + 1;
	public int toAddProb = 10;
	
	private ProbabilityMap<Integer> map;
	private ProbabilityCollection<Integer> collection;
	
	@Setup(Level.Iteration)
	public void setup() {
		this.map = new ProbabilityMap<>();
		this.collection = new ProbabilityCollection<>();
		
		for(int i = 0; i < elements; i++) {
			map.add(i, 1);
			collection.add(i, 1);
		}
	}
	
	@TearDown(Level.Iteration)
	public void tearDown() {
		this.map.clear();
		this.collection.clear();
		
		this.map = null;
		this.collection = null;
	}
	
	@Benchmark
	public void mapAddSingle() {
		this.map.add(toAdd, toAddProb);
	}
	
	@Benchmark
	public void collectionAddSingle() {
		this.collection.add(toAdd, toAddProb);
	}
	
	@Benchmark
	public void mapGet(Blackhole bh) {
		bh.consume(this.map.get());
	}
	
	@Benchmark
	public void collectionGet(Blackhole bh) {
		bh.consume(this.collection.get());
	}
}
