package eu.europeana.service.ir.image.client.impl.abstracts;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.europeana.service.ir.image.client.connection.HttpConnection;
import eu.europeana.service.ir.image.client.exception.ImageSearchApiException;
import eu.europeana.service.ir.image.client.impl.result.ImageSearchResults;

//import eu.europeana.annotation.client.http.HttpConnection;
//import eu.europeana.annotation.client.model.json.AnnotationDeserializer;
//import eu.europeana.annotation.definitions.model.Annotation;

public class BaseIrApiConnection {

	private String apiKey;
	// private String annotationServiceUri =
	// "http://www.europeana.eu/api/v2/search.json";
	private String irServiceUri = "";
	private HttpConnection httpConnection = new HttpConnection();
	protected Logger logger = Logger.getLogger(getClass().getName());

	private String jsonResult = "";
	
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAnnotationServiceUri() {
		return irServiceUri;
	}

	public void setIrServiceUri(String irServiceUri) {
		this.irServiceUri = irServiceUri;
	}

	public HttpConnection getHttpConnection() {
		return httpConnection;
	}

	public void setHttpConnection(HttpConnection httpConnection) {
		this.httpConnection = httpConnection;
	}
	
	protected ImageSearchResults getImageSearchResults(String url) throws ImageSearchApiException, IOException {
		// call the image Search API
        jsonResult = getJSONResult(url);
		
        //Load results object from JSON
        Gson gson = new GsonBuilder().create();
        ImageSearchResults res = gson.fromJson(jsonResult, ImageSearchResults.class);
        
        if(!res.getSuccess())
        	throw new ImageSearchApiException(res.getError(), res.getRequestNumber());
        
        return res;
	}

	/**
	 * Create a new connection to the Annotation Service (REST API).
	 * 
	 * @param apiKey
	 *            API Key required to access the API
	 */
	public BaseIrApiConnection(String irServiceUri, String apiKey) {
		this.apiKey = apiKey;
		this.irServiceUri = irServiceUri;
	}

	
	String getJSONResult(String url) throws IOException {
		logger.trace("Call to Annotation API (GET): " + url);
		return getHttpConnection().getURLContent(url, null);
	}
	
	String getJSONResult(String url, String paramName, String jsonPost) throws IOException {
		logger.trace("Call to Annotation API (POST): " + url);
		return getHttpConnection(). getURLContent(url, paramName, jsonPost);
	}
}
