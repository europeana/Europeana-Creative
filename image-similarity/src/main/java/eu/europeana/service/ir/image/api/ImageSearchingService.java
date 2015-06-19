package eu.europeana.service.ir.image.api;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;
import eu.europeana.service.ir.image.web.model.json.SearchResultItem;

/**
 * This interface specifies the methods available to perform an image similarity search on the image index.
 * For successful invocation of the service and retrieval of search results the init(), search(), getSearchResults() methods need to be called  
 * @author Paolo Bolettieri <paolo.bolettieri@isti.cnr.it>
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 *
 */
public interface ImageSearchingService extends ContentRetrievalService {
	
	/**
	 * Search similar images for an image available in the index and identified by the given EuropeanaId. 
	 * @param imageQueryId  {@link EuropeanaId} of the query image 
	 * @throws ImageSearchingException if something went wrong
	 */
	public void searchSimilar(String imageQueryId) throws ImageSearchingException;
	
	/**
	 * Search similar images by using a sample image accessible through the given input stream. 
	 * A query image should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param imageQueryObj {@link InputStream} of the query image
	 * @throws ImageSearchingException if something went wrong
	 */
	public void searchSimilar(InputStream imageQueryObj) throws ImageSearchingException;
	
	/**
	 * Search similar images starting from a sample image available in the Web at the given location (URL). 
	 * A query image should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param imageQueryURL {@link URL} of the query Image
	 * @throws ImageSearchingException if something went wrong
	 */
	public void searchSimilar(URL imageQueryURL) throws ImageSearchingException;
	

	/**
	 * Returns the results of the previously executed search query.    
	 * @param startFrom index to start
	 * @param numResults number of results to return. If the value is set to -1, it returns all the query results.
	 * @return {@link List} of {@link SearchResultItem} containing the ids of the query results
	 * @throws ImageSearchingException 
	 */
	public List<SearchResultItem> getResults(int startFrom, int numResults) throws ImageSearchingException;

	/**
	 * This method returns the number of the results fetched with the previously invoked  
	 * @return the total number of results
	 * @throws ImageSearchingException if the search was not successfully invoked before asking the number of total results
	 */
	public abstract int getTotalResults() throws ImageSearchingException;

	/**
	 * Initializes the Image searching service by opening the image indices.
	 * It loads the configuration and initializes the index searcher.
	 */
	public abstract void init();

	void searchSimilar(String resourceId, String queryType)
			throws ImageSearchingException;

	void searchSimilar(InputStream imageQueryObj, String queryType)
			throws ImageSearchingException;

	void searchSimilar(URL imageQueryURL, String queryType)
			throws ImageSearchingException;

	public static final String QUERY_TYPE_DC = "dc";
	public static final String QUERY_TYPE_MP7 = "mp7";
	public static final String QUERY_TYPE_MP7_DC = "mp7_dc";
	
}
