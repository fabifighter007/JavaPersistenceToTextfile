package Kunden;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import DAO.DAOFactory;
import DAO.KundenDAO;
import Exceptions.KundenDAOException;

public class Kunde {

	private String septchar = ";";

	private String kundenNr;
	private List<String> bestellungsNummern;
	private int kategorie;
	private List<Adresse> adressen;

	public Kunde(String kundenNr, int kategorie) {
		this.kundenNr = kundenNr;
		this.kategorie = kategorie;
		this.bestellungsNummern = new ArrayList<String>();
		this.adressen = new ArrayList<Adresse>();
	}
	
	public Kunde(String kundenNr, int kategorie, List<String> bestellungsNummern, List<Adresse> adressen) {
		this(kundenNr, kategorie);
		this.bestellungsNummern = bestellungsNummern;
		this.adressen = adressen;
	}
	
	public void addAdresse(String ort, int plz, String strasse) {
		this.adressen.add(new Adresse(ort, plz, strasse));
	}
	
	public void addBestellungsNr(String bestellNr) {
		this.bestellungsNummern.add(bestellNr);
	}

	
	/**
	 * Format:	
	 * kundenNr;[bestellNummer,bestellNummer, ... ];Kategorie;[plz,ort,strasse]
	 * 	
	 */
	public String toCSV() {
		String ret = "";

		ret += this.kundenNr + septchar;

		if (bestellungsNummern.size() > 0) {
			for (int i = 0; i < bestellungsNummern.size(); i++) {
				if (i == bestellungsNummern.size() - 1) {
					ret += bestellungsNummern.get(i);
				} else {
					ret += bestellungsNummern.get(i) + ",";
				}
			}
		}
		ret += septchar;

		ret += this.kategorie + septchar;

		if (adressen.size() > 0) {
			for (int i = 0; i < adressen.size(); i++) {
				if (i == adressen.size() - 1) {
					ret += adressen.get(i).toCSV();
				} else {
					ret += adressen.get(i).toCSV() + ",";
				}
			}
		}
		ret += septchar;

		return ret;
	}

	@Override
	public String toString() {
		String res = "Kategorie: " + this.kategorie + "\n" + "KundenNr: " + this.kundenNr + "\n" + "Bestellungen:"+ "\n" + "\t";
		for(String bestNr : this.bestellungsNummern) {
			res += bestNr + ", ";
		}
		res += "\n" + "Adressen: " + "\n" ;
		for(Adresse adr : this.adressen) {
			res += adr.toString() + "\n" ;
		}
		return res;
	}

	
	/**	 
	 * vvvvvv TEST-METHOD: vvvvvv 
	 */
	public static void main(String[] args) {
		
		try {
			FileWriter fw = new FileWriter("kundenRepository.txt", false);
			fw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		KundenDAO kundenDao = (KundenDAO) DAOFactory.getDAO("KundenDAO");
		
		/** 1st test-customer ("1a2b3c4d"): 
		 *  	kundenNr: 			"1a2b3c4d"
		 *  	bestellungsNummern:	"BEST1234", "BEST6789"
		 *  	kategorie:			1
		 *  	adressen:			(1) Ort: 	"Neumarkt"
		 *  							Plz: 	92318
		 *  							Straße:	"Frankenweg 3"
		 *  						(2)	Ort: 	"Ingolstadt"
		 *  							Plz: 	89000
		 *  							Straße:	"Friedrichshofener Straße 1s"
		 */		
		Kunde kunde1 = new Kunde("1a2b3c4d", 1);
		kunde1.addAdresse("Neumarkt", 92318, "Frankenweg 3");
		kunde1.addAdresse("Ingolstadt", 89000, "Friedrichshofener Straße 1s");
		kunde1.addBestellungsNr("BEST1234");
		kunde1.addBestellungsNr("BEST6789");
		
		/** 
		 * 	Test-Scenario I: 
		 * 		- Save 1st test-Customer: "1a2b3c4d" into File
		 */
		try {
			System.out.println("### TEST I ?? ###");
			kundenDao.create(kunde1);			
			System.out.println("### TEST I OK ###\n");
		} catch (KundenDAOException e) {
			System.err.println(e.getMessage());
		}
		
		
		/** 
		 * 	Test-Scenario II: 
		 * 		- Read customer: "1a2b3c4d" from file ...
		 * 		- ... save into new variable (k2)
		 * 		- ... and print k2 to console
		 */
		try {
			System.out.println("### TEST II ?? ###");
			Kunde k2 = kundenDao.read("1a2b3c4d");
			System.out.println(k2);
			System.out.println("### TEST II OK ###\n");
		} catch (KundenDAOException e) {
			System.err.println(e.getMessage());
		}
		
		
		/** 
		 * 	Test-Scenario III: 
		 * 		- Update customer: kunde ("1a2b3c4d"):
		 * 			> add new address:	
		 * 							Ort: 	"Berlin"
		 *							Plz: 	10115
		 *  						Straße:	"Bierbrauerstrasse 8"
		 *  		> add new order:
		 *  						"ORDER66"
		 *  	- Then: read customer "1a2b3c4d" from file and save into k3 ...
		 *  	- ... and print k3 onto console
		 *  	
		 */
		try {
			System.out.println("### TEST III ?? ###");
			kunde1.addAdresse("Berlin", 10115, "Bierbrauerstrasse 8");
			kunde1.addBestellungsNr("ORDER66");
			kundenDao.update(kunde1);
			System.out.println(kunde1);
			System.out.println("### TEST III OK ###\n");
		} catch (KundenDAOException e) {
			System.err.println(e.getMessage());
		}
		
		
		/** 
		 * 	Test-Scenario IV: 
		 * 	2nd test-customer ("5e6f7g8h"): 
		 *  	kundenNr: 			"5e6f7g8h"
		 *  	bestellungsNummern:	"BEST9999"
		 *  	kategorie:			5
		 *  	adressen:			(1) Ort: 	"Hamburg"
		 *  							Plz: 	20095
		 *  							Straße:	"Fischstrasse 9"
		 *  - Save 2nd test-Customer: "5e6f7g8h" into File
		 *  - get all customers of file ...
		 *  - ... and print them onto console
		 *  						  	
		 */
		try {
			System.out.println("### TEST IV ?? ###");
			Kunde kunde2 = new Kunde("5e6f7g8h", 5);
			kunde2.addAdresse("Hamburg", 20095, "Fischstrasse 9");
			kunde2.addBestellungsNr("BEST9999");
			kundenDao.create(kunde2);
			List<Kunde> list = kundenDao.getAll();
			for(Kunde k : list) {
				System.out.println(k);
			}
			System.out.println("### TEST IV OK ###\n");
		} catch (KundenDAOException e) {
			System.err.println(e.getMessage());
		}
		
		
		/** 
		 * 	Test-Scenario V: 
		 * 	- delete customer: "1a2b3c4d"
		 * 	- try to read "1a2b3c4d" into k3
		 * 	- if 'null' : print 'Kunde nicht gefunden!' onto console
		 *  - then: get all customers of file ...
		 *  - ... and print them onto console
		 *  						  	
		 */
		try {
			System.out.println("### TEST V ?? ###");
			kundenDao.delete("1a2b3c4d");
			Kunde k3 = kundenDao.read("1a2b3c4d");
			if(k3 == null) System.out.println("Kunde nicht gefunden!	<---");
			List<Kunde> list = kundenDao.getAll();
			for(Kunde k : list) {
				System.out.println(k);
			}
			System.out.println("### TEST V OK ###\n");
		} catch (KundenDAOException e) {
			System.err.println(e.getMessage());
		}
		
		
	}

	public String getKundenNr() {
		return kundenNr;
	}
}
