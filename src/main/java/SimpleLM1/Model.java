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
	
	public Layer getInputLayer() {
		return layers.get(0);
	}
	
	public Layer getOutputLayer() {
		return layers.get(layers.size() - 1);
	}
	
	public void clearLayer() {
		layers.clear();
	}
	
	public void addLayer(Layer l) {
		layers.add(l);
	}
}
