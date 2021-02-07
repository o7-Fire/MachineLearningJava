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
	
	//here the magic happened
	//it is work ? if not throw away else keep it
	public ArrayList<Float> get(ArrayList<Float> ar, int max) {
		ArrayList<Float> arr = new ArrayList<>(max);
		long s = seed;
		for (int i = 0; i < max; i++) {
			float f = i;
			for (float ff : ar)
				try { f += ff; }catch (IndexOutOfBoundsException ignored) {}
			if (nextBoolean()) f = Math.min(nextFloat(), f * nextFloat());
			else f = Math.max(nextFloat(), f * nextFloat());
			arr.add(f);
			setSeed(s);
		}
		return arr;
	}
	
	@Override
	public String toString() {
		return "Seed: " + seed + ", " + super.toString();
	}
}
