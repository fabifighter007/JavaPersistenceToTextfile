package Exceptions;

@SuppressWarnings("serial")
public class KundenDAOException extends Exception {

	private String error;
	
	public KundenDAOException(String error) {
		printStackTrace();
		this.error = error;
	}
	
	public String getMessage() {
		return this.error;
	}
}
