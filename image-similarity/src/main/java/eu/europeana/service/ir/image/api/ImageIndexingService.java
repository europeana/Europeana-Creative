package eu.europeana.service.ir.image.api;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.model.IndexingStatus;

/**
 * This interface describes the methods necessary to build an image index
 * @author Paolo Bolettieri <paolo.bolettieri@isti.cnr.it>
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 *
 */
public interface ImageIndexingService extends ContentRetrievalService {
	
	
	/**
	 * Create a new image index. It destroys the previous index (if it exists) to build a new one
	 * @throws ImageIndexingException if something went wrong
	 */
	public void initIndex() throws ImageIndexingException;
	
	/**
	 * Insert an image into the image index
	 * Images to be indexed should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param imageId {@link EuropeanaId} of the image to insert
	 * @param imageURL {@link URL} of the image to insert
	 * @throws ImageIndexingException if something went wrong
	 */
	public void insertImage(String docID, URL imageURL) throws ImageIndexingException;
	
	/**
	 * Insert an image into the image index
	 * Images to be indexed should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param imageId {@link EuropeanaId} of the image to insert
	 * @param imageObj {@link InputStream} of the image to insert
	 * @throws ImageIndexingException if something went wrong
	 */
	public void insertImage(String docID, InputStream imageObj) throws ImageIndexingException;
	
//	public void openIndex() throws ImageIndexingException;
//	
//	public void closeIndex() throws ImageIndexingException;

	
	/**
	 * Insert all images available in the given collection into the image index
	 * Images to be indexed should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param collectionId - the numeric id of the collection to be indexed
	 * @throws ImageIndexingException if something went wrong
	 * @return the number of images inserted into the index
	 */
	public int insertCollection(String collectionId)throws ImageIndexingException;

	/**
	 * This method returns the indexing status (progress) for the given collection ID 
	 * @param collectionId
	 * @return
	 * @throws ImageIndexingException 
	 */
	public IndexingStatus getIndexingStatus(String collectionId) throws ImageIndexingException;

	/**
	 * Insert all images available in the given map into the image index.
	 * 
	 * Images to be indexed should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param dataset - the name of the dataset, see {@link #getDataset()} 
	 * @param thumbnails - the map containing the <numeric id, thumbnail URL> tupples to be indexed
	 * @throws ImageIndexingException if something went wrong
	 * @return the number of images successfully inserted into the index
	 */
	public int insertCollectionByUrls(String dataset, Map<String, String> thumbnails)
			throws ImageIndexingException;


	/**
	 * 
	 * @return the name of the currently used dataset
	 */
	public String getDataset();
	
	
}

