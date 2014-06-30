package oskar;

import java.util.List;

/**
 * Oskar aus der Mülltonne
 * <p>Dieses Tool erinnert per Email an das Heraus- und Hereinstellen der Mülltonnen.
 * 
 * @author Marcus Warm
 * @since  30.06.2014
 */
public class Oskar {

	public static void main(String[] args) throws Exception {
		System.out.println("Oskar aus der Muelltonne");
		List<Muelltonnendienst> list = new MuelltonnendienstReader().read("D:\\muelltonnen.txt");
		System.out.println(list);
	}
}
