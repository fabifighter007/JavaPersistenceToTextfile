package DAO;

import java.util.List;

import Exceptions.KundenDAOException;
import Kunden.Kunde;

public interface KundenDAO extends DAO{
	public Kunde create(Kunde kunde) throws KundenDAOException;
	public Kunde read(String kundenNr) throws KundenDAOException;
	public Kunde update(Kunde kunde) throws KundenDAOException;
	public boolean delete(String kundenNr) throws KundenDAOException;
	
	public List<Kunde> getAll() throws KundenDAOException;
}
