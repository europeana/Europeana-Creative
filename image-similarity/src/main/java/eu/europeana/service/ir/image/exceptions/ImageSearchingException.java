package eu.europeana.service.ir.image.exceptions;


public class ImageSearchingException extends CommonRetrievalException {

	private static final long serialVersionUID = 8481254315169700838L;
	public static final String MESSAGE_NO_RESULTS = "No search results available! The query was not (successfully) invoked yet!";
	
	public ImageSearchingException(String message) {
		super(message);
	}

	public ImageSearchingException(String message, Throwable th) {
		super(message, th);
	}

}
