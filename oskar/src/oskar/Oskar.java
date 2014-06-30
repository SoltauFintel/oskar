package oskar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;

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
	
	public static void main(String[] args) throws IOException {
		System.out.println("Oskar aus der Muelltonne");
		List<String> empfaenger = new ArrayList<String>();
		empfaenger.add("marcus.warm@geneva-id.com");
		new Oskar().start("D:\\muelltonnen.txt", new java.sql.Date(System.currentTimeMillis()), empfaenger, false);
	}
	
	public boolean start(String dn, java.sql.Date heute, List<String> empfaenger, boolean rausstellen) throws IOException {
		List<Muelltonnendienst> list = new MuelltonnendienstReader().read(dn);
		String meldungen = "";
		// WAS FEHLT: wenn die Tonne montags an der Straße stehen muss, dann muss freitags die Info zum Rausstellen kommen.
		// Wenn freitags die Tonne an der Straße stehen muss, dann muss montags drauf die Info zum Reinstellen kommen.
		if (rausstellen) {
			for (Muelltonnendienst d : list) {
				if (vortag(d.getDatum()).equals(heute)) {
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
		System.out.println(meldungen);
		if (!meldungen.isEmpty()) {
			for (String email : empfaenger) {
				MailService ms = new MailService();
				try {
					ms.send("Datum: " + heute + "\r\n\n" + meldungen,
							"Mülldienst", "Oskar <marcus.warm@geneva-id.com>", email, true, false);
					mailcounter++;
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
		return !meldungen.isEmpty();
	}
	
	public static java.sql.Date vortag(java.sql.Date datum) {
		Calendar c = Calendar.getInstance();
		c.setTime(datum);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return new java.sql.Date(c.getTime().getTime());
	}
}
