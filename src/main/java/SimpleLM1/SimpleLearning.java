package SimpleLM1;

import Atom.File.SerializeData;
import Atom.Time.Timer;
import Atom.Utility.Random;
import Atom.Utility.Utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SimpleLearning {
	static int useJacketAtCentigrade = 15, useJacketAtHumidity = 40;
	File f = new File("SimpleLM1.SimpleLearning.mdl");
	Model model = null;
	
	public SimpleLearning() {
		try {
			loadModel();
		}catch (Throwable a) {
			System.out.println(a.toString());
			model = new Model();
			rebuildModel();
			trainModel();
		}
		
	}
	
	public static void saveModel(Model m, File f) {
		try {
			SerializeData.dataOut(m, f);
			System.out.println("Saved to: " + f.getAbsolutePath());
		}catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		SimpleLearning simpleLearning = new SimpleLearning();
		Runtime.getRuntime().addShutdownHook(new Thread(simpleLearning::saveModel));
		
		
		Utility.convertThreadToInputListener(">", s -> {
			if (s == null) {
				try {
					Thread.currentThread().interrupt();
				}catch (Throwable ignored) {}
				return;
			}
			if (s.equalsIgnoreCase("test")) {
				simpleLearning.testModel();
			}
			if (s.equalsIgnoreCase("train")) {
				simpleLearning.trainModel();
			}
			if (s.equalsIgnoreCase("print")) System.out.println(simpleLearning.model.toString());
		});
	}
	
	public void saveModel() {
		saveModel(model, f);
	}
	
	public void loadModel() throws IOException, ClassNotFoundException {
		model = SerializeData.dataIn(f);
		model.check();
		System.out.println("Model Loaded: " + f.getAbsolutePath());
	}
	
	private void rebuildModel() {
		model.clearLayer().check().addLayer(3).addLayer(1).check();
	}
	
	public boolean shouldUseJacket(ArrayList<Float> data) {
		if (data.size() != 3) throw new IllegalArgumentException("Data is corrupted");
		boolean preferToUse = data.get(2) > 0.5f;
		if (data.get(0) < useJacketAtCentigrade && preferToUse) return true;
		if (data.get(1) < useJacketAtHumidity && preferToUse) return true;
		if (data.get(0) < useJacketAtCentigrade && data.get(1) < useJacketAtHumidity) return true;
		return false;
	}
	
	public boolean get(float centigrade, float humidity, float preference) {
		ArrayList<Float> data = new ArrayList<>();
		
		data.add(centigrade);
		data.add(humidity);
		data.add(preference);
		ArrayList<Float> out = model.get(data);
		return out.get(0) > 0.5;
	}
	
	public boolean testModel() {
		int centigrade = Random.getInt(-60, 40), humidity = Random.getInt(0, 100);
		float preference = Random.getFloat();
		boolean output = get(centigrade, humidity, preference);
		boolean should = shouldUseJacket(new ArrayList<>(Arrays.asList((float) centigrade, (float) humidity, preference)));
		if (Settings.debug) {
			System.out.println("Centigrade: " + centigrade + "\nHumidity:" + humidity + "\nPreference:" + preference);
			System.out.println("Should use: " + should);
			System.out.println("Output: " + output);
		}
		return should == output;
	}
	
	public boolean testRigid() {
		for (int i = 0; i < 40; i++) {
			if (Settings.debug) System.out.println("\nIteration:" + i);
			if (!testModel()) return false;
		}
		return true;
	}
	
	public void trainModel() {
		boolean d = Settings.debug;
		Settings.debug = false;
		int iteration = 0;
		Timer timer = new Timer(TimeUnit.MICROSECONDS, 1000);
		while (!testRigid()) {
			model.update();
			iteration++;
			Settings.debug = timer.get();
			if (Settings.debug) System.out.println("\n\nBatch: " + iteration);
			
		}
		Settings.debug = d;
		System.out.println("\n" + model.toString());
	}
}
