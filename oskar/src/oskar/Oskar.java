package oskar;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.CliFactory;

/**
 * Oskar aus der Mülltonne
 * <p>Dieses Tool erinnert per Email an das Heraus- und Hereinstellen der Mülltonnen.
 * <p>Jenkins soll dieses Tool einmal morgens und einmal nachmittags aufrufen.
 * 
 * @author Marcus Warm
 * @since  30.06.2014
 */
public class Oskar {
	public static int mailcounter = 0;
	
	public static void main(String[] args) throws IOException, ArgumentValidationException {
		System.out.println("Oskar aus der Mülltonne 2.0\n");
		CommandLine cli = CliFactory.parseArguments(CommandLine.class, args);
		System.out.println("Abfallkalender :  " + cli.getConfig());
		if (cli.isRaus()) {
			System.out.println("Modus          :  rausstellen ->");
		} else {
			System.out.println("Modus          :  <- reinstellen");
		}
		if (cli.getEmpfaenger() != null) {
			System.out.println("Emailadressen  :  " + cli.getEmpfaenger());
		}
		System.out.println();
		new Oskar().start(cli.getConfig(), cli.getSender(), cli.getEmpfaenger(), cli.isRaus(), new java.sql.Date(System.currentTimeMillis()));
	}
	
	public boolean start(String dn, String sender, List<String> empfaenger, boolean rausstellen, java.sql.Date heute) throws IOException {
		List<Muelltonnendienst> list = new MuelltonnendienstReader().read(dn);
		if (list.size() == 1) {
			System.out.println("1 Abfallkalendereintrag geladen");
		} else {
			System.out.println(list.size() + " Abfallkalendereinträge geladen");
		}
		String meldungen = "";
		// WAS FEHLT: wenn die Tonne montags an der Straße stehen muss, dann muss freitags die Info zum Rausstellen kommen.
		// Wenn freitags die Tonne an der Straße stehen muss, dann muss montags drauf die Info zum Reinstellen kommen.
		if (rausstellen) {
			for (Muelltonnendienst d : list) {
				if (DateService.vortag(d.getDatum()).equals(heute)) {
					meldungen += "[" + d.getDatum() + "] Mülltonne einen Tag vorher rausstellen: " + d.getArt() + "\r\n";
				}
			}
		} else { // Reinstellen Modus
			for (Muelltonnendienst d : list) {
				if (d.getDatum().equals(heute)) {
					meldungen += "[" + d.getDatum() + "] Mülltonne wieder reinstellen: " + d.getArt() + "\r\n";
				}
			}
		}
		if (meldungen.isEmpty()) {
			System.out.println("- keine Meldungen -");
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
				ms.send(text, "Mülldienst", "Oskar <" + sender + ">", email, true, false);
				mailcounter++;
			} catch (MessagingException e) {
				e.printStackTrace(); // kein Programmabbruch
			}
		}
	}
}
