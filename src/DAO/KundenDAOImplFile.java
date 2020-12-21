package DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Exceptions.KundenDAOException;
import Kunden.Adresse;
import Kunden.Kunde;

public class KundenDAOImplFile implements KundenDAO {

	@Override
	public Kunde create(Kunde kunde) throws KundenDAOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("kundenRepository.txt", Charset.defaultCharset(), true));

			/*
			 * File f = new File(myTempFile); if(f.exists() && !f.isDirectory()) { inputfile
			 * = kundenRep.txt }
			 */

			writer.write(kunde.toCSV() + "\n");
		} catch (IOException e) {
			throw new KundenDAOException("Interner Fehler bei create()");
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// swallow exception
			}
		}
		return kunde;
	}

	@Override
	public Kunde read(String kundenNr) throws KundenDAOException {
		BufferedReader reader = null;
		Kunde newCustomer = null;
		try {
			reader = new BufferedReader(new FileReader("kundenRepository.txt", Charset.defaultCharset()));
			String line = "";
			String[] vals;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(kundenNr)) {
					vals = line.split(";");

					List<String> orders = new ArrayList<>();
					if (vals[1] != "") {
						// inserting all orderNrs
						orders = Arrays.asList(vals[1].split(","));
					}

					List<Adresse> adrs = new ArrayList<Adresse>();
					String[] adrsString = null;
					if (vals[3] != "") {
						adrsString = vals[3].split(",");
						// splitting multiple Addresses and create for each one new Address Instance and
						// put it into ArrayList
						for (int i = 0; i < adrsString.length; i++) {
							String[] currAdr = adrsString[i].split("!");
							adrs.add(new Adresse(currAdr[0], Integer.valueOf(currAdr[1]), currAdr[2]));
						}
					}
					// initializing new Customer
					newCustomer = new Kunde(vals[0], Integer.valueOf(vals[2]), orders, adrs);
				}
			}
		} catch (IOException e) {
			throw new KundenDAOException("Interner Fehler bei read()");
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// swallow Exception
			}
		}
		return newCustomer;
	}

	@Override
	public Kunde update(Kunde kunde) throws KundenDAOException {
		File inputFile = new File("kundenRepository.txt");
		File tempFile = new File("myTempFile.txt");

		Kunde ret = null;

		BufferedReader reader = null;
		BufferedWriter writer = null;

		String kundenNr = kunde.getKundenNr();
		String currentLine;

		try {
			reader = new BufferedReader(new FileReader(inputFile));
			writer = new BufferedWriter(new FileWriter(tempFile));

			while ((currentLine = reader.readLine()) != null) {
				// trim newline when comparing with lineToRemove
				String trimmedLine = currentLine.trim();
				// skip current line if it start with "kundenNr"
				if (trimmedLine.startsWith(kundenNr)) {
					continue;
				}
				writer.write(currentLine + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			throw new KundenDAOException("Interner Fehler bei update()");
		} finally {
			try {
				writer.close();
				reader.close();
				inputFile.delete();
				boolean successful = tempFile.renameTo(inputFile);
				this.create(kunde); // ohne wird kunde komplett geloescht
				ret = kunde;
				if (!successful) {
					// ???
					// throw new KundenDAOException("Interner Fehler bei update()");
				}
			} catch (IOException e) {
				// swallow exception
			}
		}
		return ret;
	}

	@Override
	public boolean delete(String kundenNr) throws KundenDAOException {
		File inputFile = new File("kundenRepository.txt");
		File tempFile = new File("myTempFile.txt");

		BufferedReader reader = null;
		BufferedWriter writer = null;

		boolean ret = false;

		String currentLine;

		try {
			reader = new BufferedReader(new FileReader(inputFile));
			writer = new BufferedWriter(new FileWriter(tempFile));

			while ((currentLine = reader.readLine()) != null) {
				// trim newline when comparing with lineToRemove
				String trimmedLine = currentLine.trim();
				// skip current line if it start with "kundenNr"
				if (trimmedLine.startsWith(kundenNr)) {
					continue;
				}
				writer.write(currentLine + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new KundenDAOException("Interner Fehler bei update()");
		} finally {
			try {
				writer.close();
				reader.close();
				inputFile.delete();
				boolean successful = tempFile.renameTo(inputFile);
				ret = true;
				if (!successful) {
					throw new KundenDAOException("Interner Fehler bei delete()");
				}
			} catch (IOException e) {
				// swallow exception
			}
		}
		return ret;
	}

	@Override
	public List<Kunde> getAll() throws KundenDAOException {
		BufferedReader reader = null;
		List<Kunde> ret = new LinkedList<Kunde>();
		try {
			reader = new BufferedReader(new FileReader("kundenRepository.txt", Charset.defaultCharset()));
			String line = "";
			String[] vals;
			while ((line = reader.readLine()) != null) {
				vals = line.split(";");

				List<String> orders = new ArrayList<>();
				if (vals[1] != "") {
					// inserting all orderNrs
					orders = Arrays.asList(vals[1].split(","));
				}

				List<Adresse> adrs = new ArrayList<Adresse>();
				String[] adrsString = null;
				if (vals[3] != "") {
					adrsString = vals[3].split(",");
					// splitting multiple Addresses and create for each one new Address Instance and
					// put it into ArrayList
					for (int i = 0; i < adrsString.length; i++) {
						String[] currAdr = adrsString[i].split("!");
						adrs.add(new Adresse(currAdr[0], Integer.valueOf(currAdr[1]), currAdr[2]));
					}
				}
				// initializing new Customer
				ret.add(new Kunde(vals[0], Integer.valueOf(vals[2]), orders, adrs));

			}
		} catch (IOException e) {
			throw new KundenDAOException("Interner Fehler bei read()");
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// swallow Exception
			}
		}
		return ret;
	}

}
