package oskar;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.CliFactory;

/**
 * Oskar aus der M�lltonne
 * <p>Dieses Tool erinnert per Email an das Heraus- und Hereinstellen der M�lltonnen.
 * <p>Jenkins soll dieses Tool einmal morgens und einmal nachmittags aufrufen.
 * 
 * @author Marcus Warm
 * @since  30.06.2014
 */
public class Oskar {
	public static int mailcounter = 0;
	private List<Muelltonnendienst> list;
	
	public static void main(String[] args) throws IOException, ArgumentValidationException {
		System.out.println("Oskar aus der M�lltonne 2.01\n");
		CommandLine cli = CliFactory.parseArguments(CommandLine.class, args);
		System.out.println("Abfallkalender :  " + cli.getConfig());
		if (cli.getEmpfaenger() != null) {
			System.out.println("Emailadressen  :  " + cli.getEmpfaenger());
		}
		final String heute = new java.sql.Date(System.currentTimeMillis()).toString();
		Oskar oskar = new Oskar(cli.getConfig());
		if (cli.isRein()) {
			System.out.println("<- reinstellen");
			oskar.start(cli.getSender(), cli.getEmpfaenger(), false, heute);
		}
		if (cli.isRaus()) {
			System.out.println("rausstellen ->");
			oskar.start(cli.getSender(), cli.getEmpfaenger(), true, heute);
		}
	}
	
	/**
	 * @param dn Abfallkalender Dateiname
	 * @throws IOException 
	 */
	public Oskar(String dn) throws IOException {
		list = new MuelltonnendienstReader().read(dn);
		if (list.size() == 1) {
			System.out.println("1 Abfallkalendereintrag geladen\n");
		} else {
			System.out.println(list.size() + " Abfallkalendereintr�ge geladen\n");
		}
	}
	
	/**
	 * 
	 * @param sender
	 * @param empfaenger
	 * @param rausstellen
	 * @param heute JJJJ-MM-TT
	 * @return
	 */
	public boolean start(String sender, List<String> empfaenger, boolean rausstellen, String heute) {
		String meldungen = "";
		// WAS FEHLT: wenn die Tonne montags an der Stra�e stehen muss, dann muss freitags die Info zum Rausstellen kommen.
		// Wenn freitags die Tonne an der Stra�e stehen muss, dann muss montags drauf die Info zum Reinstellen kommen.
		if (rausstellen) {
			for (Muelltonnendienst d : list) {
				if (DateService.vortag(d.getDatum()).toString().equals(heute)) {
					meldungen += "[" + d.getDatum() + "] M�lltonne einen Tag vorher rausstellen: " + d.getArt() + "\r\n";
				}
			}
		} else { // Reinstellen Modus
			for (Muelltonnendienst d : list) {
				if (d.getDatum().toString().equals(heute)) {
					meldungen += "[" + d.getDatum() + "] M�lltonne wieder reinstellen: " + d.getArt() + "\r\n";
				}
			}
		}
		if (meldungen.isEmpty()) {
			System.out.println("- keine Meldungen -\n");
		} else {
			System.out.println(meldungen);
			if (empfaenger != null) {
				sendeMails("Datum: " + heute + "\r\n\n" + meldungen, sender, empfaenger);
			}
		}
		return !meldungen.isEmpty();
	}

	private void sendeMails(String text, String sender, List<String> emailadressen) {
		for (String email : emailadressen) {
			MailService ms = new MailService();
			try {
				ms.send(text, "M�lldienst", "Oskar <" + sender + ">", email, true, false);
				mailcounter++;
			} catch (MessagingException e) {
				e.printStackTrace(); // kein Programmabbruch
			}
		}
	}
}
