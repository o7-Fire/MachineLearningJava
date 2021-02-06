package SimpleLM1;

import java.io.Serializable;
import java.util.ArrayList;

public class Node extends RandomFramework implements Serializable {
	
	
	public Node() {
		super();
	}
	
	public Node(long seed) {
		super(seed);
	}
	
	public ArrayList<Float> get(ArrayList<Float> ar, int max) {
		ArrayList<Float> arr = new ArrayList<>(max);
		long s = seed;
		for (int i = 0; i < max; i++) {
			float f = i;
			try {
				f *= ar.get(i);
			}catch (IndexOutOfBoundsException ignored) {}
			mixSeed(f);
			arr.add(nextFloat());
			setSeed(s);
		}
		return arr;
	}
	
	@Override
	public String toString() {
		return "Seed: " + seed + ", " + super.toString();
	}
}
