package SDL1;

import Atom.Utility.Utility;
import SimpleLM1.SimpleLearning;
import org.deeplearning4j.core.storage.StatsStorage;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.learning.config.Adam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class Main {
	public static File model = new File("SDL1.zip");
	public static double minScore = 1, learningRate = 0.001;
	
	public static void main(String[] args) throws IOException {
		org.apache.log4j.BasicConfigurator.configure();
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().updater(new Adam(learningRate)).optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).list().layer(new DenseLayer.Builder().nIn(3).nOut(2).build()).layer(new DenseLayer.Builder().nIn(2).nOut(2).build()).layer(new OutputLayer.Builder().nIn(2).nOut(2).build()).build();
		
		MultiLayerNetwork nets;
		try {
			nets = MultiLayerNetwork.load(model, true);
			loadInterface(nets);
			System.out.println("Loaded: " + model.getAbsolutePath());
			double d = getAverageScore(nets);
			System.out.println("Score:" + d);
			minScore = Math.min(d, minScore);
		}catch (Throwable ignored) {
			nets = new MultiLayerNetwork(conf);
			loadInterface(nets);
			System.out.println("Creating new model");
			train(nets);
		}
		MultiLayerNetwork net = nets;
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				net.save(model);
			}catch (Throwable e) {
				e.printStackTrace();
			}
		}));
		Utility.convertThreadToInputListener(">", s -> {
			if (s.equalsIgnoreCase("test")) {
				try {
					DataSet ds = getDataSet();
					
					
					INDArray input = ds.getFeatures();
					INDArray dOutput = ds.getLabels();
					System.out.println("Temperature, Humidity, Prefer to use jacket (float)");
					System.out.println(input.toStringFull());
					INDArray output = net.output(input);
					System.out.println("Output:");
					System.out.println(output.toStringFull());
					System.out.println("Desired Output:");
					System.out.println(dOutput.toStringFull());
					System.out.println("Should Use jacket: " + (shouldUse(input) ? "Yes" : "No"));
					System.out.println("NeuralNet Answer: " + (use(output) ? "Yes" : "No"));
					System.out.println("Score: " + net.score(ds));
				}catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (s.equalsIgnoreCase("eval")) System.out.println(evaluation(Main::getDataSet, net).stats());
			if (s.equalsIgnoreCase("min")) System.out.println(minScore);
			if (s.equalsIgnoreCase("score")) System.out.println(getAverageScore(net));
			if (s.equalsIgnoreCase("train")) {
				try {
					train(net);
				}catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public static double getAverageScore(MultiLayerNetwork nets) {
		ArrayList<Double> floats = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			floats.add(nets.score(getDataSet()));
		}
		return floats.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
	}
	
	public static void loadInterface(MultiLayerNetwork net) {
		//Initialize the user interface backend
		UIServer uiServer = UIServer.getInstance();
		
		//Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
		StatsStorage statsStorage = new InMemoryStatsStorage();         //Alternative: new FileStatsStorage(File), for saving and loading later
		
		//Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
		uiServer.attach(statsStorage);
		
		//Then add the StatsListener to collect this information from the network, as it trains
		net.setListeners(new StatsListener(statsStorage));
	}
	
	public static boolean use(INDArray output) {
		return output.getFloat(0) > 0.5 && output.getFloat(1) < 0.5;
	}
	
	public static boolean shouldUse(INDArray nd) {
		return SimpleLearning.shouldUseJacket(nd.toFloatVector());
	}
	
	public static Evaluation evaluation(Function<Void, DataSet> d, MultiLayerNetwork net) {
		Evaluation evaluation = new Evaluation(2);
		
		for (int i = 0; i < 50; i++) {
			DataSet set = d.apply(null);
			INDArray input = set.getFeatures(), dOutput = set.getLabels();
			evaluation.eval(dOutput, net.output(input));
		}
		
		return evaluation;
	}
	
	public static void train(MultiLayerNetwork net) throws IOException {
		
		System.out.println("Accuracy Before: " + evaluation(Main::getDataSet, net).accuracy());
		for (int i = 0; i < 5; i++) {
			double fit = getAverageScore(net);
			while (fit > minScore - learningRate) {
				DataSet d = getDataSet();
				net.fit(d);
				
				
				fit = getAverageScore(net);
			}
			minScore = Math.min(minScore, getAverageScore(net));
		}
		net.save(model, true);
		System.out.println("Accuracy After: " + evaluation(Main::getDataSet, net).accuracy());
	}
	
	public static NDArray randomData() {
		return randomData(SimpleLearning.getRandomData());
	}
	
	public static NDArray randomData(float[] in) {
		return new NDArray(in);
	}
	
	public static DataSet getDataSet(Void v) {
		return getDataSet();
	}
	
	public static DataSet getDataSet() {
		float[] in = SimpleLearning.getRandomData();
		float[] desired = SimpleLearning.shouldUseJacket(in) ? new float[]{1f, 0f} : new float[]{0f, 1f};
		return new DataSet(randomData(in), new NDArray(desired));
	}
}
