package oskar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class Erinnerungstest {
	private static final String SENDER = "test@google.de"; 
	
	private List<String> getEmpfaenger() {
		List<String> emailadressen = new ArrayList<String>();
		// TODO Emailadresse eintragen:
		// ret.add();
		return emailadressen;
	}

	@Test
	public void reinstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar().start("muelltonnen.txt", SENDER, getEmpfaenger(), false, DateService.toDate("3.7.2014"));
		Assert.assertTrue("Es wurde keine Meldung erzeugt.", meldung);
		if (!getEmpfaenger().isEmpty()) {
			Assert.assertEquals("Mail wurde nicht versandt", 1, Oskar.mailcounter);
		}
	}

	// Negativtest
	@Test
	public void nix_reinstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar().start("muelltonnen.txt", SENDER, getEmpfaenger(), false, DateService.toDate("2.7.2014"));
		Assert.assertFalse("Es wurde f�lschlicherweise eine Meldung erzeugt: " + meldung, meldung);
		Assert.assertEquals("Mail wurde f�lschlicherweise versandt", 0, Oskar.mailcounter);
	}

	@Test
	public void rausstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar().start("muelltonnen.txt", SENDER, getEmpfaenger(), true, DateService.toDate("23.7.2014"));
		Assert.assertTrue("Es wurde keine Meldung erzeugt.", meldung);
		if (!getEmpfaenger().isEmpty()) {
			Assert.assertEquals("Mail wurde nicht versandt", 1, Oskar.mailcounter);
		}
	}

	// Negativtest
	@Test
	public void nix_rausstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar().start("muelltonnen.txt", SENDER, getEmpfaenger(), true, DateService.toDate("1.5.2014"));
		Assert.assertFalse("Es wurde f�lschlicherweise eine Meldung erzeugt: " + meldung, meldung);
		Assert.assertEquals("Mail wurde f�lschlicherweise versandt", 0, Oskar.mailcounter);
	}
}