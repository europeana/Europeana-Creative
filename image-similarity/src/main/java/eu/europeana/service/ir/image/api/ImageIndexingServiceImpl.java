package eu.europeana.service.ir.image.api;

import it.cnr.isti.feature.extraction.Image2Features;
import it.cnr.isti.melampo.index.indexing.LireIndexer;
import it.cnr.isti.melampo.index.settings.LireSettings;
import it.cnr.isti.melampo.vir.exceptions.VIRException;
import it.cnr.isti.vir.features.FeaturesCollectorArr;
import it.cnr.isti.vir.features.mpeg7.LireObject;
import it.cnr.isti.vir.file.ArchiveException;
import it.cnr.isti.vir.file.FeaturesCollectorsArchive;
import it.cnr.isti.vir.id.IDString;
import it.cnr.isti.vir.readers.CoPhIRv2Reader;
import it.cnr.isti.vir.similarity.metric.LireMetric;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europeana.api.client.thumbnails.ThumbnailsAccessor;
import eu.europeana.api.client.thumbnails.ThumbnailsForCollectionAccessor;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.index.indexing.ExtendedLireIndexer;
import eu.europeana.service.ir.image.model.IndexingStatus;

/**
 * @author paolo
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 */
public class ImageIndexingServiceImpl implements ImageIndexingService {

	@Autowired
	private IRConfigurationImpl configuration;

	private final String dataset;

	private FeaturesCollectorsArchive coll;

	private LireIndexer mp7cIndex;

	private LireSettings settings;
	private Image2Features img2Features;

	private Logger log = Logger.getLogger(getClass());

	public ImageIndexingServiceImpl(String dataset,
			IRConfigurationImpl configuration) {
		this.configuration = configuration;

		if (dataset == null)
			this.dataset = configuration.getDefaultDataset();
		else
			this.dataset = dataset;
	}

	public ImageIndexingServiceImpl(IRConfigurationImpl configuration) {
		this(null, configuration);
	}

	public void init() {
		getConfiguration();
	}

	/**
	 * @return
	 */
	public IRConfigurationImpl getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration
	 */
	public void setConfiguration(IRConfigurationImpl configuration) {
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
		CoPhIRv2Reader.setFeatures(LireMetric.reqFeatures);

		File featuresArchiveFile = getConfiguration().getFeaturesArchiveFile(
				dataset);
		// create file path if needed
		if (!featuresArchiveFile.exists())
			featuresArchiveFile.getParentFile().mkdirs();

		try {
			//img2Features = new Image2Features(dataset, configuration);
			img2Features = new Image2Features(configuration.getIndexConfFolder(dataset));
			// features archive, Feature classes, VirId, FeaturesCollection
			// array
			coll = new FeaturesCollectorsArchive(featuresArchiveFile,
					new LireMetric().getRequestedFeaturesClasses(),
					IDString.class, FeaturesCollectorArr.class);
			setVariables();
		} catch (Exception e) {
			throw new ImageIndexingException(
					"Exception when opening image index for dataset: "
							+ dataset, e);
		}
	}

	public void closeIndex() throws ImageIndexingException {
		try {
			mp7cIndex.closeIndex();
		} catch (Exception e) {
			log.trace("Unexpected exception thrown when closing image index: ", e);
		}
	}

	public void insertImage(String docID, URL imageURL)
			throws ImageIndexingException {
		try {

			String imgFeatures = img2Features.extractFeatures(imageURL);
			InputStream is = new ByteArrayInputStream(imgFeatures.getBytes());

			// read it with BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			FeaturesCollectorArr features = CoPhIRv2Reader.getObj(br);
			features.setID(new IDString(docID));
			if (coll != null)
				coll.add(features);

			if (settings == null) {
				setVariables();
			}

			LireObject obj = new LireObject(features);
			obj.setThmbURL(imageURL.toString());

			mp7cIndex.addDocument(obj, docID);
		} catch (ArchiveException e) {
			throw new ImageIndexingException(
					"Feature archive access exception:", e);
		} catch (Exception e) {
			throw new ImageIndexingException(
					"Indexing image by URL thows exception:", e);
		}

	}

	public void insertImage(String docID, InputStream imageObj)
			throws ImageIndexingException {

		try {
			String imgFeatures = img2Features.extractFeatures(imageObj);
			InputStream is = new ByteArrayInputStream(imgFeatures.getBytes());

			// read it with BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			FeaturesCollectorArr features = CoPhIRv2Reader.getObj(br);
			features.setID(new IDString(docID));
			if (coll != null)
				coll.add(features);

			if (settings != null) {
				setVariables();
			}

			LireObject obj = new LireObject(features);
			mp7cIndex.addDocument(obj, docID);
		} catch (ArchiveException e) {
			throw new ImageIndexingException(
					"Feature archive access exception:", e);
		} catch (Exception e) {
			throw new ImageIndexingException(
					"Indexing image by URL thows exception:", e);
		}

	}

	private void setVariables() throws IOException, VIRException {
		// File home =
		// this.configuration.getConfigProperty("image_index_home"));
		// TODO: check if this code is not redundant
		settings = this.configuration.getLireSettings(getDataset());
		//TODO: verify the correctness of this implementations. Is this redundant or not?
		if (coll== null && settings.getFCArchives().size() > 0)
			coll = settings.getFCArchives().getArchive(0);
		// mp7cIndex = new LireIndexer();
		mp7cIndex = new ExtendedLireIndexer();
		mp7cIndex.OpenIndex(settings);
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
		Map<String, String> thumbnails = tfca.getThumbnailsForCollection(0, -1, ThumbnailsAccessor.ERROR_POLICY_RETHROW);
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

}
