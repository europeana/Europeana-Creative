package eu.europeana.service.ir.image.exceptions;


public class ImageIndexingException extends CommonRetrievalException {

	private static final long serialVersionUID = 2853865401612458998L;

	public ImageIndexingException(String message) {
		super(message);
	}

	public ImageIndexingException(String message, Throwable th) {
		super(message, th);
	}

}
