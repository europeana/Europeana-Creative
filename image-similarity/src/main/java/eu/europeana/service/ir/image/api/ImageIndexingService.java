package eu.europeana.service.ir.image.api;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.model.IndexingStatus;

/**
 * This interface describes the methods available for building/adding images into the image index.
 * The images can be inserted individually, by collection or as dataset
 * @author Paolo Bolettieri <paolo.bolettieri@isti.cnr.it>
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 *
 */
public interface ImageIndexingService extends ContentRetrievalService {
	
	
	/**
	 * Creates a new image index. It destroys the previous index (if exists) and builds a new one
	 * @throws ImageIndexingException if something went wrong
	 */
	public void initIndex() throws ImageIndexingException;
	
	/**
	 * Insert an image into the image index, identified by the given docID, being available at the given web location
	 * Images to be indexed should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param imageId {@link EuropeanaId} of the image to insert
	 * @param imageURL {@link URL} of the image to insert
	 * @throws ImageIndexingException if something went wrong
	 */
	public void insertImage(String docID, URL imageURL) throws ImageIndexingException;
	
	/**
	 * Insert an image into the image index identified by the given docID, being read from the local file system 
	 * Images to be indexed should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param imageId {@link EuropeanaId} of the image to insert
	 * @param imageFile {@link java.io.File} the (absolute) location of the image on the disk
	 * @throws ImageIndexingException if something went wrong. E.g. the image file doesn't exist
	 */
	public void insertImage(String docID, File imageFile)
			throws ImageIndexingException;
	
	
	/**
	 * Insert an image into the image index identified by the given docID, being accessible through the given InputStream
	 * Images to be indexed should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * @param imageId {@link EuropeanaId} of the image to insert
	 * @param imageObj {@link InputStream} of the image to insert
	 * @throws ImageIndexingException if something went wrong
	 */
	public void insertImage(String docID, InputStream imageObj) throws ImageIndexingException;
	
	
	/**
	 * Insert all images (thumbnails) available in the given collection into the image index
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
	 * Insert all images available in the given map into the image index of the given dataset
	 * 
	 * @param dataset - the name of the dataset, see {@link #getDataset()} 
	 * @param thumbnails - the map containing the <object id, thumbnail URL> tupples to be indexed
	 * @throws ImageIndexingException if something went wrong
	 * @return the number of images successfully inserted into the index
	 */
	public int insertCollectionByUrls(String dataset, Map<String, String> thumbnails)
			throws ImageIndexingException;


	/**
	 * Insert all images available in the given set into the image index.
	 * 
	 * Images to be indexed should have a size of at least 500x500 pixels and available
	 * in one of the following formats: JPG, TIFF, PNG, GIF, BMP, PPM, PGM, PBM
	 * The image files will be determined from the ID 
	 * @param ids - the set containing the <object id> of the images to be indexed   
	 * @throws ImageIndexingException if something went wrong
	 * @return the number of images successfully inserted into the index
	 */
	public int insertDatasetByIds(Set<String> ids)
			throws ImageIndexingException;

	/**
	 * Delete from the image index all images identified by the Strings available in the given set.
	 * 
	 @param ids - the set containing the <object id> of the images to be removed from the index   
	 * @throws ImageIndexingException if something went wrong
	 * @return the number of images successfully removed from the index
	 */
	public int deleteDatasetByIds(Set<String> ids) throws ImageIndexingException;
	
	
	/**
	 * 
	 * @return the name of dataset used by the current instance of the service
	 */
	public String getDataset();

	
	
	
}

