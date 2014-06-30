package oskar;

public class Muelltonnendienst {
	private java.sql.Date datum; // ohne Uhrzeit
	private String art;

	public java.sql.Date getDatum() {
		return datum;
	}

	public void setDatum(java.sql.Date datum) {
		this.datum = datum;
	}

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}
	
	@Override
	public String toString() {
		return datum.toString() + " " + art;
	}
}
