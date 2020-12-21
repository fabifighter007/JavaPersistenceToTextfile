package Kunden;

public class Adresse {
	private String ort;
	private int plz;
	private String strasse;
	
	public Adresse(String ort, int plz, String strasse) {
		this.ort = ort;
		this.plz = plz;
		this.strasse = strasse;
	}
	
	public String toCSV() {
		return this.ort + "!" + this.plz + "!" + this.strasse;
	}
	
	@Override
	public String toString() {
		return "	Ort: " + this.ort + ", Plz: " + this.plz + ", Straße: " + this.strasse;
	}
}
