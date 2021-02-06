package SimpleL4;

import Atom.Utility.Random;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model implements Serializable {
	ArrayList<ArrayList<Node>> nodes = new ArrayList<>();
	Node end = new Node(Random.getLong());
	
	public Model(int x, int y) {
		for (int i = 0; i < x; i++) {
			ArrayList<Node> node = new ArrayList<>();
			for (int j = 0; j < y; j++) {
				node.add(new Node(Random.getLong()));
			}
			nodes.add(node);
		}
	}
	
	public void update() {
		for (ArrayList<Node> n : nodes) {
			for (Node node : n)
				node.updateScrew(Random.getLong());
		}
		end.updateScrew(Random.getLong());
	}
	
	public boolean get(int data) {
		float datashit = data;
		for (List<Node> n : nodes) {
			for (Node node : n)
				datashit = node.get(datashit);
		}
		System.out.println("[Model] Input: " + data + ", Output: " + datashit);
		return datashit < 0.5f;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int x = 0, y = 0;
		for (ArrayList<Node> n : nodes) {
			x++;
			for (Node node : n) {
				y++;
				sb.append("x:").append(x).append(", y:").append(y).append("  ").append(node.toString()).append("\n");
			}
		}
		return sb.toString();
	}
}
