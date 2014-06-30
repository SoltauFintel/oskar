package oskar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateService {
	private static final SimpleDateFormat f = new SimpleDateFormat("d.M.yyyy");

	public static java.sql.Date toDate(String datum) {
		try {
			return new java.sql.Date(f.parse(datum).getTime());
		} catch (ParseException e) {
			throw new RuntimeException("Fehler beim Konvertieren des Datums '" + datum + "'", e);
		}
	}

	public static java.sql.Date vortag(java.sql.Date datum) {
		Calendar c = Calendar.getInstance();
		c.setTime(datum);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return new java.sql.Date(c.getTime().getTime());
	}
}
