package SimpleLM1;

import java.io.Serializable;
import java.util.ArrayList;

public class Layer extends RandomFramework implements Serializable {
	
	protected ArrayList<Node> nodes;
	protected int nodesSize;
	
	public Layer(int nodesSize, long seed) {
		super(seed);
		this.nodesSize = nodesSize;
		regenerate();
	}
	
	
	public Layer(int nodesSize) {
		this(nodesSize, System.currentTimeMillis());
	}
	
	public Layer check() {
		super.check();
		for (Node n : nodes)
			n.check();
		return this;
	}
	
	public Layer update() {
		for (Node n : nodes)
			n.setSeed(random.nextLong());
		return this;
	}
	
	public ArrayList<Float> get(ArrayList<Float> f, int output) {
		ArrayList<Float> sumFloat = new ArrayList<>();
		for (int i = 0; i < output; i++) {
			sumFloat.add(0f);
		}
		for (Node n : nodes) {
			int i = 0;
			for (float fe : n.get(f, output)) {
				sumFloat.set(i, sumFloat.get(i) + fe);
				i++;
			}
		}
		return sumFloat;
	}
	
	public Layer regenerate() {
		this.nodes = new ArrayList<>(nodesSize);
		check();
		for (int i = 0; i < nodesSize; i++) {
			this.nodes.add(new Node(nextLong()));
		}
		return this;
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int y = 0;
		for (Node n : nodes) {
			sb.append("Node: ").append(y).append("\n ").append(n.toString()).append("\n\n ");
			y++;
		}
		return sb.toString();
	}
	
	public int getNodesSize() {
		return nodesSize;
	}
	
	
}
