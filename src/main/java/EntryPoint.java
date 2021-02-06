import java.util.Random;

public class EntryPoint {
	public static void main(String[] args) throws Throwable {
		Random r = new Random(123);
		for (int i = 0; i < 10; i++) {
			System.out.println(r.nextFloat());
		}
	}
}
