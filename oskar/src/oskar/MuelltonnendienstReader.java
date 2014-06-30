package oskar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>// Dateiformat:
 * Jahr: 2014
 * Monat: 7
 * [Tag] TAB [Art]
 * ...
 * </pre>
 */
public class MuelltonnendienstReader {

	public List<Muelltonnendienst> read(String dn) throws IOException, ParseException {
		BufferedReader r = new BufferedReader(new FileReader(dn));
		try {
			List<Muelltonnendienst> ret = new ArrayList<Muelltonnendienst>();
			String zeile;
			int monat = 0, jahr = 0;
			while ((zeile = r.readLine()) != null) {
				if (zeile.trim().startsWith("//")) continue;
				if (zeile.toLowerCase().startsWith("jahr:")) {
					jahr =  Integer.parseInt(zeile.substring("jahr:".length()).trim());
				} else if (zeile.toLowerCase().startsWith("monat:")) {
					monat = Integer.parseInt(zeile.substring("monat:".length()).trim());
				} else if (zeile.contains("\t")) {
					if (jahr == 0 || monat == 0) {
						throw new RuntimeException("Datei fehlerhaft. Jahr=" + jahr + ", Monat=" + monat);
					}
					int o = zeile.indexOf("\t");
					int tag = Integer.parseInt(zeile.substring(0, o).trim());
					Muelltonnendienst m = new Muelltonnendienst();
					SimpleDateFormat f = new SimpleDateFormat("d.M.yyyy");
					m.setDatum(new java.sql.Date(f.parse(
							tag + "." + monat + "." + jahr).getTime()));
					m.setArt(zeile.substring(o + 1).trim());
					ret.add(m);
				}
			}
			return ret;
		} finally {
			r.close();
		}
	}
}
