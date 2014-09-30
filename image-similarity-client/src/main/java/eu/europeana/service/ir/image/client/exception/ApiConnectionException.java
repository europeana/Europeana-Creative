package eu.europeana.service.ir.image.client.exception;

/**
 * This class is meant to be used for handling server side errors returned in the response of the Search API  
 * @author Sergiu Gordea
 *
 */
public class ApiConnectionException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2924749433705930735L;

	private long requestNumber;
	
	public ApiConnectionException(String message, long requestNumber) {
		super(message);
		this.setRequestNumber(requestNumber);
	}

	public ApiConnectionException(String message) {
		super(message);
	}
	
	public ApiConnectionException(String message, Throwable th) {
		super(message, th);
	}
	
	@Override
	public String getMessage() {
		if(requestNumber < 0)
			return super.getMessage();
		else
			return super.getMessage() + ". RequestNumber: " + getRequestNumber();
	}

	public long getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(long requestNumber) {
		this.requestNumber = requestNumber;
	}
	
}
