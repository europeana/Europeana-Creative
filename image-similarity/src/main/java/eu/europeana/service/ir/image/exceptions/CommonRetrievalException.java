package eu.europeana.service.ir.image.exceptions;

public class CommonRetrievalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8979456152037029274L;
	
	public CommonRetrievalException(String message){
		super(message);
	}
	
	public CommonRetrievalException(String message, Throwable th){
		super(message, th);
	}

}
