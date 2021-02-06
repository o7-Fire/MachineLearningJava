package SimpleL4;/*
 * Copyright 2021 Itzbenz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Atom.File.SerializeData;
import Atom.Utility.Random;
import Atom.Utility.Utility;

import java.io.File;
//somehow work, no idea how this work
public class SimpleLearning {
	
	static int useJacketAtCentigrade = 10;
	static File f = new File("SimpleL4.SimpleLearning.mdl");
	static Model n = new Model(1, 1);
	
	public static void main(String[] args) throws Throwable {
		
		try {
			n = SerializeData.dataIn(f);
		}catch (Throwable gay) {
			trainModel(n);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(SimpleLearning::saveModel));
		
		Utility.convertThreadToInputListener(">", s -> {
			if (s == null) {
				try {
					Thread.currentThread().interrupt();
				}catch (Throwable ignored) {}
				return;
			}
			if (s.equalsIgnoreCase("train")) {
				trainModel(n);
				return;
			}
			try {
				int i = Integer.parseInt(s);
				boolean b = n.get(i);
				while (b != useJacketAtCentigrade > i) {
					try {
						trainModel(n);
						b = n.get(i);
					}catch (Throwable e) {
						e.printStackTrace();
					}
				}
				System.out.println("[Model] use jacket:" + (b ? "yes" : "no"));
			}catch (NumberFormatException n) {
				System.out.println(n.toString());
				System.out.println("Integer only");
			}catch (Throwable t) {
				System.out.println(t.toString());
				t.printStackTrace();
			}
		});
		
		
	}
	
	public static void saveModel(Model m, File f) {
		try {
			SerializeData.dataOut(n, f);
			System.out.println("Saved to: " + f.getAbsolutePath());
		}catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static void saveModel() {
		saveModel(n, f);
	}
	
	public static void trainModel(Model n) {
		System.out.println("Model not exists, training one");
		long i = 0;
		while (!test(n)) {
			i++;
			System.out.println("Iteration:" + i);
			n.update();
		}
		while (!test(n)) {
			i++;
			System.out.println("Iteration:" + i);
			n.update();
		}
		System.out.println("Training successful");
		System.out.println("Model: \n" + n.toString());
		saveModel(n, f);
	}
	
	public static boolean test(Model n) {
		
		for (int i = 0; i < 20; i++) {
			int r = Random.getInt(-2000, 2000);
			
			boolean b = n.get(r);
			if (b != useJacketAtCentigrade > r) return false;
		}
		return true;
	}
	
}
