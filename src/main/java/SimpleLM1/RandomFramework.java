package SimpleLM1;

import java.io.Serializable;
import java.util.Random;

public abstract class RandomFramework extends Random implements Serializable {
	
	protected long seed;
	
	public RandomFramework() {
		this(System.nanoTime());
	}
	
	public RandomFramework(long seed) {
		this.seed = seed;
		check();
	}
	
	public void check() {
		setSeed();
	}
	
	public void mixSeed(float s) {
		setSeed((long) (nextBoolean() ? seed * s : seed / s));
	}
	
	public long getSeed() {
		return seed;
	}
	
	public void setSeed(long seed) {
		super.setSeed(seed);
		this.seed = seed;
	}
	
	public void randomSeed() {
		setSeed(nextLong());
	}
	
	public void setSeed() {
		setSeed(seed);
	}
}
