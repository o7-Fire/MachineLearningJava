package SimpleLM1;

import java.io.Serializable;
import java.util.Random;

public abstract class RandomFramework implements Serializable {
	
	protected long seed;
	protected Random random = new Random();
	
	public RandomFramework() {
		this(System.nanoTime());
	}
	
	public RandomFramework(long seed) {
		this.seed = seed;
	}
	
	public RandomFramework check() {
		if (random == null) random = new Random();
		setSeed();
		return this;
	}
	
	public RandomFramework mixSeed(float s) {
		setSeed((long) (nextBoolean() ? seed * s : seed / s));
		return this;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public void setSeed(long seed) {
		this.seed = seed;
		random.setSeed(seed);
	}
	
	public RandomFramework randomSeed() {
		setSeed(nextLong());
		return this;
	}
	
	public boolean nextBoolean() {
		return new Random(seed).nextBoolean();
	}
	
	public float nextFloat() {
		return new Random(seed).nextFloat();
	}
	
	public long nextLong() {
		return new Random(seed).nextLong();
	}
	
	public RandomFramework setSeed() {
		setSeed(seed);
		return this;
	}
}
