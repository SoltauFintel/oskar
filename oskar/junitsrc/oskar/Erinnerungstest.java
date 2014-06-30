package oskar;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class Erinnerungstest {

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
	
	@Test
	public void reinstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar().start("muelltonnen.txt", DateService.toDate("3.7.2014"), getEmpfaenger(), false);
		Assert.assertTrue("Es wurde keine Meldung erzeugt.", meldung);
		Assert.assertEquals("Mail wurde nicht versandt", 1, Oskar.mailcounter);
	}

	// Negativtest
	@Test
	public void nix_reinstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar().start("muelltonnen.txt", DateService.toDate("2.7.2014"), getEmpfaenger(), false);
		Assert.assertFalse("Es wurde fälschlicherweise eine Meldung erzeugt: " + meldung, meldung);
		Assert.assertEquals("Mail wurde fälschlicherweise versandt", 0, Oskar.mailcounter);
	}

	@Test
	public void rausstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar().start("muelltonnen.txt", DateService.toDate("23.7.2014"), getEmpfaenger(), true);
		Assert.assertTrue("Es wurde keine Meldung erzeugt.", meldung);
		Assert.assertEquals("Mail wurde nicht versandt", 1, Oskar.mailcounter);
	}

	// Negativtest
	@Test
	public void nix_rausstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar().start("muelltonnen.txt", DateService.toDate("1.5.2014"), getEmpfaenger(), true);
		Assert.assertFalse("Es wurde fälschlicherweise eine Meldung erzeugt: " + meldung, meldung);
		Assert.assertEquals("Mail wurde fälschlicherweise versandt", 0, Oskar.mailcounter);
	}
	
	private List<String> getEmpfaenger() {
		List<String> ret = new ArrayList<String>();
		ret.add("marcus.warm@geneva-id.com");
		return ret;
	}
}
