package oskar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>// Dateiformat:
 * def [Art]=[Langtext]
 * Jahr: 2014
 * Monat: 7
 * [Tag] TAB [Art]
 * ...
 * </pre>
 */
public class MuelltonnendienstReader {
	private static final SimpleDateFormat f = new SimpleDateFormat("d.M.yyyy");

	public List<Muelltonnendienst> read(String dn) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(dn));
		try {
			Map<String, String> vars = new HashMap<String, String>();
			List<Muelltonnendienst> ret = new ArrayList<Muelltonnendienst>();
			String zeile;
			int monat = 0, jahr = 0;
			while ((zeile = r.readLine()) != null) {
				if (zeile.trim().isEmpty() || zeile.trim().startsWith("//")) continue;
				if (zeile.toLowerCase().startsWith("jahr:")) {
					jahr = Integer.parseInt(zeile.substring("jahr:".length()).trim());
				} else if (zeile.toLowerCase().startsWith("monat:")) {
					monat = Integer.parseInt(zeile.substring("monat:".length()).trim());
				} else if (zeile.toLowerCase().startsWith("def")) {
					String a = zeile.substring("def".length()).trim();
					int o = a.indexOf("=");
					String name = a.substring(0, o).trim();
					String inhalt = a.substring(o + 1).trim();
					vars.put(name, inhalt);
				} else if (zeile.contains("\t")) {
					if (jahr == 0 || monat == 0) {
						throw new RuntimeException("Datei fehlerhaft. Jahr=" + jahr + ", Monat=" + monat);
					}
					ret.add(readMuelldienst(vars, zeile, monat, jahr));
				}
			}
			return ret;
		} finally {
			r.close();
		}
	}

	private Muelltonnendienst readMuelldienst(Map<String, String> vars, String zeile, int monat, int jahr) {
		int o = zeile.indexOf("\t");
		int tag = Integer.parseInt(zeile.substring(0, o).trim());
		Muelltonnendienst m = new Muelltonnendienst();
		m.setDatum(toDate(tag + "." + monat + "." + jahr));
		String art = zeile.substring(o + 1).trim();
		m.setArt(vars.get(art));
		if (m.getArt() == null) {
			m.setArt(art);
		}
		return m;
	}
	
	public static java.sql.Date toDate(String datum) {
		try {
			return new java.sql.Date(f.parse(datum).getTime());
		} catch (ParseException e) {
			throw new RuntimeException("Fehler beim Konvertieren des Datums '" + datum + "'", e);
		}
	}
}
