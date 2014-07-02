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
		// TODO Emailadresse eintragen, um Mail-versenden-Funktion ebenfalls zu testen:
		// ret.add();
		return emailadressen;
	}

	@Test
	public void reinstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar("muelltonnen.txt").start(SENDER, getEmpfaenger(), false, "2014-07-03");
		Assert.assertTrue("Es wurde keine Meldung erzeugt.", meldung);
		if (!getEmpfaenger().isEmpty()) {
			Assert.assertEquals("Mail wurde nicht versandt", 1, Oskar.mailcounter);
		}
	}

	// Negativtest
	@Test
	public void nix_reinstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar("muelltonnen.txt").start(SENDER, getEmpfaenger(), false, "2014-07-02");
		Assert.assertFalse("Es wurde fälschlicherweise eine Meldung erzeugt: " + meldung, meldung);
		Assert.assertEquals("Mail wurde fälschlicherweise versandt", 0, Oskar.mailcounter);
	}

	@Test
	public void rausstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar("muelltonnen.txt").start(SENDER, getEmpfaenger(), true, "2014-07-23");
		Assert.assertTrue("Es wurde keine Meldung erzeugt.", meldung);
		if (!getEmpfaenger().isEmpty()) {
			Assert.assertEquals("Mail wurde nicht versandt", 1, Oskar.mailcounter);
		}
	}

	// Negativtest
	@Test
	public void nix_rausstellen() throws IOException {
		Oskar.mailcounter = 0;
		boolean meldung = new Oskar("muelltonnen.txt").start(SENDER, getEmpfaenger(), true, "2014-05-01");
		Assert.assertFalse("Es wurde fälschlicherweise eine Meldung erzeugt: " + meldung, meldung);
		Assert.assertEquals("Mail wurde fälschlicherweise versandt", 0, Oskar.mailcounter);
	}
}
