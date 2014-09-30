package eu.europeana.service.ir.image.client.exception;


/**
 * This class is meant to be used for handling server side errors returned in the response of the Search API  
 * @author Sergiu Gordea
 *
 */
public class ImageSearchApiException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2924749433705930735L;

	private long requestNumber;
	
	public ImageSearchApiException(String message, long requestNumber) {
		super(message);
		this.setRequestNumber(requestNumber);
	}

	public ImageSearchApiException(String message) {
		super(message);
	}
	
	public ImageSearchApiException(String message, Throwable th) {
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
