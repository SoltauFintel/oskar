package oskar;

import java.util.List;

import uk.co.flamingpenguin.jewel.cli.Option;
import uk.co.flamingpenguin.jewel.cli.Unparsed;

/**
 * Oskar Kommandozeilenoptionen
 * 
 * @author Marcus Warm
 * @since  30.06.2014
 */
public interface CommandLine {

	/**
	 * @return true: rausstellen, false: nichts machen
	 */
	@Option
	boolean isRaus();

	/**
	 * @return true: reinstellen, false: nichts machen
	 */
	@Option
	boolean isRein();

	/**
	 * @return Abfallkalenderdatei, z.B. D:\oskar\muelltonnen.txt
	 */
	@Option(shortName = "c")
	String getConfig();
	
	/**
	 * @return Sender Emailadresse
	 */
	@Option(shortName = "s")
	String getSender();
	
	/**
	 * @return Emailadressen
	 */
	@Unparsed
	List<String> getEmpfaenger();
}
