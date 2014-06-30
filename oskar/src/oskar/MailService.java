package oskar;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Email versenden
 * 
 * @author Marcus Warm
 * @version 2.0 02.12.2011
 */
public class MailService {
	private String host = "gi-d-dom01.comnet.dir";
	
	/**
	 * @return SMTP Mailhost
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param pHost SMTP Mailhost
	 */
	public void setHost(String pHost) {
		host = pHost;
	}

	/**
	 * @param plaintext Mailtext
	 * @param subject Betreff
	 * @param from Absender Emailadresse
	 * @param to Empfänger Emailadresse(n)
	 * <br>Trennzeichen: Komma
	 * @param wichtig
	 * @param lesebestaetigung
	 * @throws MessagingException
	 */
	public void send(String plaintext, String subject, String from, String to,
			boolean wichtig, boolean lesebestaetigung) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(props);
		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		setEmpfaenger(to, msg);
		msg.setSubject(subject);
		if (wichtig) {
			msg.addHeader("Importance", "High");
		}
		if (lesebestaetigung) {
			msg.addHeader("Disposition-Notification-To", from);
		}
		msg.setContent(plaintext, "text/plain");
		Transport.send(msg);
	}

	@SuppressWarnings("cast")
	private void setEmpfaenger(String to, Message msg) throws AddressException,
			MessagingException {
		if (to.contains(",")) {
			List<InternetAddress> list = new ArrayList<InternetAddress>();
			for (String ea : to.split(",")) {
				list.add(new InternetAddress(ea.trim()));
			}
			msg.setRecipients(Message.RecipientType.TO, 
					(InternetAddress[]) list.toArray(new InternetAddress[list.size()]));
		} else {
			InternetAddress addressTo = new InternetAddress(to);
			msg.setRecipient(Message.RecipientType.TO, addressTo);
		}
	}
}
