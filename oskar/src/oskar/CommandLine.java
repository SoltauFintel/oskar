package oskar;

import java.util.List;

import uk.co.flamingpenguin.jewel.cli.Option;
import uk.co.flamingpenguin.jewel.cli.Unparsed;

public interface CommandLine {

	/**
	 * @return true: rausstellen, false: reinstellen
	 */
	boolean isRaus();

	/**
	 * @return Configdatei, z.B. D:\muelltonnen.txt
	 */
	@Option(shortName = "c")
	String getConfig();
	
	/**
	 * @return Emailadressen
	 */
	@Unparsed
	List<String> getEmpfaenger();
}
