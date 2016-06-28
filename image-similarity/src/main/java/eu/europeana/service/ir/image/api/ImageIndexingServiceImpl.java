package eu.europeana.service.ir.image.api;

import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.feature.extraction.Image2Features;
import it.cnr.isti.melampo.index.indexing.LireIndexer;
import it.cnr.isti.melampo.index.settings.LireSettings;
import it.cnr.isti.melampo.vir.exceptions.VIRException;
import it.cnr.isti.vir.features.FeatureClassCollector;
import it.cnr.isti.vir.features.FeaturesCollectorArr;
import it.cnr.isti.vir.features.FeaturesCollectorException;
import it.cnr.isti.vir.features.mpeg7.LireObject;
import it.cnr.isti.vir.features.mpeg7.vd.MPEG7VDFormatException;
import it.cnr.isti.vir.file.ArchiveException;
import it.cnr.isti.vir.file.FeaturesCollectorsArchive;
import it.cnr.isti.vir.id.IDString;
import it.cnr.isti.vir.readers.CoPhIRv2Reader;
import it.cnr.isti.vir.similarity.metric.LireMetric;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europeana.api.client.thumbnails.ThumbnailsAccessor;
import eu.europeana.api.client.thumbnails.ThumbnailsForCollectionAccessor;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.index.indexing.ExtendedLireIndexer;
import eu.europeana.service.ir.image.model.IndexingStatus;

/**
 * @author paolo
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 */
public class ImageIndexingServiceImpl implements ImageIndexingService {

	@Autowired
	private IRConfiguration configuration;

	private final String dataset;

	private FeaturesCollectorsArchive featuresArchive;

	public FeaturesCollectorsArchive getFeatureCollectorArchive() {
		return featuresArchive;
	}

	private LireIndexer mp7cIndex;

	private LireSettings settings;
	private Image2Features img2Features;

	private Logger log = Logger.getLogger(getClass());

	public ImageIndexingServiceImpl(String dataset,
			IRConfiguration configuration) {
		this.configuration = configuration;

		if (dataset == null)
			this.dataset = configuration.getDefaultDataset();
		else
			this.dataset = dataset;
	}

	public ImageIndexingServiceImpl(IRConfiguration configuration) {
		this(null, configuration);
	}

	public void init() {
		getConfiguration();
	}

	/**
	 * @return
	 */
	public IRConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration
	 */
	public void setConfiguration(IRConfiguration configuration) {
		this.configuration = configuration;
	}

	public void initIndex() throws ImageIndexingException {

		// try {
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void openIndex(String dataset) throws ImageIndexingException {
		registerFeaturesCollector();

		File featuresArchiveFile = getConfiguration().getFeaturesArchiveFile(
				dataset);
		// create file path if needed
		if (!featuresArchiveFile.exists())
			featuresArchiveFile.getParentFile().mkdirs();

		try {
			//img2Features = new Image2Features(dataset, configuration);
			img2Features = initFeatureExtractor(dataset);
			// features archive, Feature classes, VirId, FeaturesCollection
			// array
			featuresArchive = initFeaturesArchive(featuresArchiveFile);
			setVariables();
		} catch (Exception e) {
			throw new ImageIndexingException(
					"Exception when opening image index for dataset: "
							+ dataset, e);
		}
	}

	protected void registerFeaturesCollector() {
		CoPhIRv2Reader.setFeatures(LireMetric.reqFeatures);
	}

	protected FeaturesCollectorsArchive initFeaturesArchive(
			File featuresArchiveFile) throws Exception {
		return new FeaturesCollectorsArchive(featuresArchiveFile,
				getVirFeatureClasses(),
				IDString.class, FeaturesCollectorArr.class);
	}

	protected FeatureClassCollector getVirFeatureClasses() {
		return new LireMetric().getRequestedFeaturesClasses();
	}

	protected Image2Features initFeatureExtractor(String dataset)
			throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return new Image2Features(configuration.getIndexConfFolder(dataset));
	}

	public void closeIndex() throws ImageIndexingException {
		try {
			mp7cIndex.closeIndex();
		} catch (Exception e) {
			log.trace("Unexpected exception thrown when closing image index: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see eu.europeana.service.ir.image.api.ImageIndexingService#insertImage(java.lang.String, java.net.URL)
	 */
	@Override
	public void insertImage(String docID, URL imageURL)
			throws ImageIndexingException {


		String imgFeatures;
		try {
			imgFeatures = img2Features.extractFeatures(imageURL);
		} catch (FeatureExtractionException e) {
			throw new ImageIndexingException(
					"Cannot extract features for image:" + imageURL, e);
		}
		
		String thumbnailUrl= imageURL.toString();
		insertFeatures(docID, thumbnailUrl, imgFeatures);

	}

	protected void insertFeatures(String docID, String thumbnailUrl,
			String imgFeatures) throws FactoryConfigurationError,
			ImageIndexingException {
		
		BufferedReader br = null;
		try {
			InputStream is = new ByteArrayInputStream(imgFeatures.getBytes());

			// read it with BufferedReader
			br = new BufferedReader(new InputStreamReader(is));
			FeaturesCollectorArr features = readFeatures(br);
			features.setID(new IDString(docID));
			if (featuresArchive != null)
				featuresArchive.add(features);
			
			//settings || indexer = null?
			if (settings == null) {
				setVariables();
			}

			LireObject obj = new LireObject(features);
			obj.setThmbURL(thumbnailUrl);

			mp7cIndex.addDocument(obj, docID);
		} catch (ArchiveException e) {
			throw new ImageIndexingException(
					"Feature archive access exception:", e);
		} catch (Exception e) {
			throw new ImageIndexingException(
					"Indexing image by URL thows exception:", e);
		}finally{
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					//this exception should not occur
					//if it occurs nothing harmful should occur
					System.out.println("warning: exception occured when closing buffered reader of image features for image " 
					+ docID + "\nError message"+ e.getLocalizedMessage());
				}
		}
		
	}

	protected FeaturesCollectorArr readFeatures(BufferedReader br)
			throws IOException, FactoryConfigurationError,
			MPEG7VDFormatException, XMLStreamException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, FeaturesCollectorException {
		registerFeaturesCollector();
		FeaturesCollectorArr features = CoPhIRv2Reader.getObj(br);
		return features;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.europeana.service.ir.image.api.ImageIndexingService#insertImage(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void insertImage(String docID, InputStream imageObj)
			throws ImageIndexingException {

		String imgFeatures = extractFeatures(docID, imageObj);
		
		String thumbnailUrl="image/"+docID;
		insertFeatures(docID, thumbnailUrl, imgFeatures);

	}

	protected String extractFeatures(String docID, InputStream imageObj)
			throws ImageIndexingException {
		String imgFeatures;
		//ensure initialized feature extractor
		try{
		if(img2Features == null)
			img2Features = initFeatureExtractor(getDataset());
		} catch (Exception e) {
			throw new ImageIndexingException(
					"Cannot init feature extractor for dataset!" + getDataset(), e);
		}
		//perform feature extraction
		try {
			imgFeatures = img2Features.extractFeatures(imageObj);
		} catch (FeatureExtractionException e) {
			throw new ImageIndexingException(
					"Cannot extract features from input stream. docId" + docID, e);
		}
		return imgFeatures;
	}
	
	public void insertImage(String docID, File imageFile)
			throws ImageIndexingException {
	
		try {
			insertImage(docID, new FileInputStream(imageFile));
		} catch (FileNotFoundException e) {
			throw new ImageIndexingException(
					"Cannot access file:" + imageFile, e);
		}
	}

	protected void deleteImage(String docID)
			throws ImageIndexingException {
	
		try {
				//settings || indexer = null?
				if (settings == null) {
					setVariables();
				}

//				LireObject obj = new LireObject(features);
//				obj.setThmbURL(thumbnailUrl);

				mp7cIndex.deleteDocument(docID);
			} catch (Exception e) {
				throw new ImageIndexingException(
						"Removing image by ID thows exception:", e);
			
		}
	}
	private void setVariables() throws IOException, VIRException {
		// File home =
		// this.configuration.getConfigProperty("image_index_home"));
		// TODO: check if this code is not redundant
		settings = this.configuration.getLireSettings(getDataset());
		//TODO: verify the correctness of this implementations. Is this redundant or not?
		if (featuresArchive== null && settings.getFCArchives().size() > 0)
			featuresArchive = settings.getFCArchives().getArchive(0);
		
		//TODO: move to method open new indexer
		// mp7cIndex = new LireIndexer();
		mp7cIndex = initFeatureIndexer();
		mp7cIndex.OpenIndex(settings);
	}

	protected ExtendedLireIndexer initFeatureIndexer() {
		return new ExtendedLireIndexer();
	}

	@Override
	public int insertCollection(String collectionId)
			throws ImageIndexingException {
		String collectionName = collectionId;
		// collectionName starts with "collectionId_". Use wild-card search
		if (!collectionName.endsWith("*"))
			collectionName += "_*";

		ThumbnailsForCollectionAccessor tfca = new ThumbnailsForCollectionAccessor(
				collectionName);
		Map<String, String> thumbnails;
		try {
			thumbnails = tfca.getThumbnailsForCollection(0, -1, ThumbnailsAccessor.ERROR_POLICY_RETHROW);
		} catch (Throwable th) {
			throw new ImageIndexingException("Cannot access thumbnails map!", th);
		}
		return insertCollectionByUrls(getDataset(), thumbnails);
	}

	public int insertCollectionByUrls(String dataset,
			Map<String, String> thumbnails) throws ImageIndexingException {
		int indexedImageCount = 0;
		int skipedFileCount = 0;
		URL imageUrl = null;
		String imageId = null;
		// open index
		openIndex(dataset);

		for (Entry<String, String> thumbnail : thumbnails.entrySet()) {
			try {
				imageUrl = new URL(thumbnail.getValue());
				imageId = thumbnail.getKey();
				insertImage(imageId, imageUrl);
				indexedImageCount++;
			} catch (MalformedURLException e) {
				log.warn("Wrong thumbnail URL format:" + imageUrl);
				skipedFileCount++;
				// e.printStackTrace();
			}catch (ImageIndexingException e) {
				log.warn("Cannot index thumbnail:" + imageUrl, e);
				skipedFileCount++;
				// e.printStackTrace();
			}

			if ((indexedImageCount % 1000) == 0) {
				// mp7cIndex.commit(); - not needed. auto flush is used
				System.out.println("Processed items count: "
						+ indexedImageCount);
			}
		}

		log.info("Skiped wrong thumbnail URLs :" + skipedFileCount);
		log.info("Successfully indexed thumbnail URLs :" + indexedImageCount);

		closeIndex();
		return indexedImageCount;
	}

	@Override
	public IndexingStatus getIndexingStatus(String collectionId)
			throws ImageIndexingException {
		throw new ImageIndexingException("Not implemented");
	}

	public String getDataset() {
		return dataset;
	}

	@Override
	public int insertDatasetByIds(Set<String> ids)
			throws ImageIndexingException {
		
		int indexedImageCount = 0;
		int skipedFileCount = 0;
		File imageFile;
		
		// open index
		openIndex(dataset);
		
		for (String imageId: ids) {
			try {
				imageFile = getConfiguration().getImageFile(dataset, imageId);
				insertImage(imageId, imageFile);
				indexedImageCount++;
			}catch (ImageIndexingException e) {
				log.warn("Cannot index thumbnail with id:" + imageId, e);
				skipedFileCount++;
				// e.printStackTrace();
			}

			if ((indexedImageCount % 1000) == 0) {
				// mp7cIndex.commit(); - not needed. auto flush is used
				System.out.println("Processed items count: "
						+ indexedImageCount);
			}
		}

		log.info("Skiped wrong thumbnail URLs :" + skipedFileCount);
		log.info("Successfully indexed thumbnail URLs :" + indexedImageCount);

		closeIndex();
		return indexedImageCount;
	}

	@Override
	public int deleteDatasetByIds(Set<String> ids)
			throws ImageIndexingException {
		int removedCount = 0;
		int skipedCount = 0;
		
		// open index
		openIndex(dataset);
		
		for (String imageId: ids) {
			try {
				
				deleteImage(imageId);
				removedCount++;
			}catch (ImageIndexingException e) {
				log.warn("Cannot remove thumbnail from index. id:" + imageId, e);
				skipedCount++;
				// e.printStackTrace();
			}

			if ((removedCount % 1000) == 0) {
				// mp7cIndex.commit(); - not needed. auto flush is used
				log.info("Processed items count: "
						+ removedCount);
			}
		}

		log.info("Skiped wrong thumbnail URLs :" + skipedCount);
		log.info("Successfully indexed thumbnail URLs :" + removedCount);

		closeIndex();
		return removedCount;
	}

}
