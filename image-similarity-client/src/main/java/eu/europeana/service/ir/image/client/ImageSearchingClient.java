package eu.europeana.service.ir.image.client;

import java.io.UnsupportedEncodingException;

import eu.europeana.service.ir.image.client.exception.ImageSearchApiException;
import eu.europeana.service.ir.image.client.impl.result.ImageSearchResults;

public interface ImageSearchingClient {

	public String getComponentNameFromRest();

	public ImageSearchResults searchById(String resourceId, int startPos, int rows)
			throws ImageSearchApiException;

	public ImageSearchResults searchByUrl(String imageUrl, int startPos, int rows)
			throws ImageSearchApiException, UnsupportedEncodingException;

}
