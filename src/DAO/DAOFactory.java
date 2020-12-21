package DAO;

import java.util.HashMap;
import java.util.Map;

public class DAOFactory {
	private static Map<String, DAO> daoMap;
	private static DAOFactory factory;

	public static DAOFactory getFactory() {
		if (factory == null) {
			factory = new DAOFactory();
		}
		return factory;
	}

	public static DAO getDAO(String daoName) {
		if (daoMap == null) {
			daoMap = new HashMap<String, DAO>();
		}					
		DAO ret = null;
		if (daoName == "KundenDAO") {
			if (daoMap.get("KundenDAO") == null) {
				daoMap.put("KundenDAO", new KundenDAOImplFile());
			}
			ret = daoMap.get("KundenDAO");
		}

		/*
		 * Analog für alle anderen Root-Entries
		 * 
		 */

		return ret;
	}
}
