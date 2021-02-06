package SimpleLM1;

import java.io.Serializable;
import java.util.ArrayList;

public class Model extends RandomFramework implements Serializable {
	protected ArrayList<Layer> layers = new ArrayList<>();
	
	public Model() {
		this(System.currentTimeMillis());
	}
	
	public Model(long seed) {
		super(seed);
	}
	
	public Model update() {
		for (Layer l : layers) {
			l.setSeed(random.nextLong());
			l.update();
		}
		return this;
	}
	
	@Override
	public Model check() {
		super.check();
		for (Layer l : layers)
			l.check();
		return this;
	}
	
	public ArrayList<Float> get(ArrayList<Float> f) {
		Layer layer = null;
		if (layers.isEmpty()) return f;
		for (Layer l : layers) {
			if (layer == null) {
				layer = l;
				continue;
			}
			f = layer.get(f, l.getNodesSize());
			layer = l;
		}
		return layer.get(f, layer.getNodesSize());
	}
	
	public Layer getInputLayer() {
		return layers.get(0);
	}
	
	public Layer getOutputLayer() {
		return layers.get(layers.size() - 1);
	}
	
	public ArrayList<Layer> getLayers() {
		return layers;
	}
	
	public Model clearLayer() {
		layers.clear();
		return this;
	}
	
	public Model addLayer(int nodeSize) {
		return addLayer(new Layer(nodeSize, nextLong()));
	}
	
	public Model addLayer(Layer l) {
		layers.add(l);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int x = 0;
		for (Layer l : layers) {
			sb.append("\nLayer: ").append(x).append("\n ").append(l.toString());
			x++;
		}
		return sb.toString();
	}
}
