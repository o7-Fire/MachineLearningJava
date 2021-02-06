package SimpleL4;


import java.io.Serializable;
import java.util.Random;

public class Node implements Serializable {
	long s;
	transient Random random = new Random();
	
	public Node(long screw) {
		updateScrew(screw);
		
	}
	
	public void check() {
		if (random == null) {
			random = new Random();
			applyScrew();
		}
	}
	
	public void applyScrew() {
		updateScrew(s);
	}
	
	public void updateScrew(long screw) {
		if (random == null) random = new Random();
		s = screw;
		random.setSeed(screw);
	}
	
	public float get(float data) {
		check();
		if (random.nextBoolean()) return Math.min(random.nextFloat(), data * random.nextFloat());
		else return Math.max(random.nextFloat(), data * random.nextFloat());
	}
	
	@Override
	public String toString() {
		return super.toString() + ": " + s;
	}
}
