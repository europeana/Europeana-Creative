package eu.europeana.service.ir.image.api;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;

/**
 * This interface describes the available methods to perform an image similarity search in the image index
 * @author Paolo Bolettieri <paolo.bolettieri@isti.cnr.it>
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 *
 */
public interface ImageSearchingService extends ContentRetrievalService {
	
	/**
	 * Search similar images starting from an {@link EuropeanaId} already in the index.
	 * @param imageQueryId  {@link EuropeanaId} of the query image 
	 * @throws ImageSearchingException if something went wrong
	 */
	public void searchSimilar(EuropeanaId imageQueryId) throws ImageSearchingException;
	
	/**
	 * Search similar images starting from a sample image. 
	 * A query image should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param imageQueryObj {@link InputStream} of the query image
	 * @throws ImageSearchingException if something went wrong
	 */
	public void searchSimilar(InputStream imageQueryObj) throws ImageSearchingException;
	
	/**
	 * Search similar images starting from a sample image. 
	 * A query image should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param imageQueryURL {@link URL} of the query Image
	 * @throws ImageSearchingException if something went wrong
	 */
	public void searchSimilar(URL imageQueryURL) throws ImageSearchingException;
	

	/**
	 * Return the results of the query
	 * @param startFrom index to start
	 * @param numResults number of results to return. If the value is set to -1, it returns all the query results.
	 * @return {@link List} of {@link EuropeanaId} containing the ids of the query results
	 * @throws ImageSearchingException 
	 */
	public List<EuropeanaId> getResults(int startFrom, int numResults) throws ImageSearchingException;

	public abstract int getTotalResults();

	public abstract void init();

}
