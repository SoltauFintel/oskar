package oskar;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class Grundlagen {
	
	@Test
	public void auflisten() throws IOException, ParseException {
		List<Muelltonnendienst> a = new MuelltonnendienstReader().read("muelltonnen.txt");
		System.out.println(a);
		Assert.assertFalse("Liste ist leer", a.isEmpty());
		Assert.assertTrue("Art ist zu kurz: '" + a.get(0).getArt() + "'", a.get(0).getArt().length() >= 4);
	}

	@Test
	public void vortag() {
		java.sql.Date d = DateService.toDate("3.7.2014");
		Assert.assertEquals("Vortag-Datum falsch berechnet 1", DateService.vortag(d).toString(), "2014-07-02");
		java.sql.Date dd = DateService.toDate("1.1.2014");
		Assert.assertEquals("Vortag-Datum falsch berechnet 2", DateService.vortag(dd).toString(), "2013-12-31");
	}
}
