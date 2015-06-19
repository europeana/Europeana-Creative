package eu.europeana.service.ir.image.api;

import it.cnr.isti.feature.extraction.Image2Features;
import it.cnr.isti.vir.features.FeatureClassCollector;
import it.cnr.isti.vir.features.lire.vd.CcDominantColor;
import it.cnr.isti.vir.features.lire.vd.LireColorLayout;
import it.cnr.isti.vir.features.lire.vd.LireEdgeHistogram;
import it.cnr.isti.vir.features.lire.vd.LireScalableColor;
import it.cnr.isti.vir.file.FeaturesCollectorsArchive;
import it.cnr.isti.vir.readers.CoPhIRv2Reader;
import it.cnr.isti.vir.similarity.metric.LireMetric;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;

import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.index.indexing.ExtendedLireIndexer;
import eu.europeana.service.ir.image.index.indexing.SmkLireIndexer;

/**
 * @author paolo
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 */
public class SmkImageIndexingServiceImpl extends ImageIndexingServiceImpl implements ImageIndexingService {

	FeatureClassCollector featureClasses = new FeatureClassCollector(
			LireColorLayout.class,
			LireScalableColor.class,
			LireEdgeHistogram.class, 
			CcDominantColor.class );

	
	public SmkImageIndexingServiceImpl(String dataset,
			IRConfiguration configuration) {
		super(dataset, configuration);
	}

	public SmkImageIndexingServiceImpl(IRConfiguration configuration) {
		this(null, configuration);
	}
	
	public void openIndex(String dataset) throws ImageIndexingException {
//		CoPhIRv2Reader.setFeatures(LireMetric.reqFeatures);
//
//		File featuresArchiveFile = getConfiguration().getFeaturesArchiveFile(
//				dataset);
//		// create file path if needed
//		if (!featuresArchiveFile.exists())
//			featuresArchiveFile.getParentFile().mkdirs();
//
//		try {
//			//img2Features = new Image2Features(dataset, configuration);
//			img2Features = initFeatureExtractor(dataset);
//			// features archive, Feature classes, VirId, FeaturesCollection
//			// array
//			featuresArchive = initFeaturesArchive(featuresArchiveFile);
//			setVariables();
//		} catch (Exception e) {
//			throw new ImageIndexingException(
//					"Exception when opening image index for dataset: "
//							+ dataset, e);
//		}
		
		super.openIndex(dataset);
	}
	
	@Override
	protected void registerFeaturesCollector() {
		//TODO what's the relation to image-fx.properties?
		//CoPhIRv2Reader.setFeatures(LireMetric.reqFeatures);
		CoPhIRv2Reader.setFeatures(featureClasses);
	}
	
	
	@Override
	protected Image2Features initFeatureExtractor(String dataset)
			throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		//return new Image2Features(configuration.getIndexConfFolder(dataset));
		return super.initFeatureExtractor(dataset);
	}
	
	
	@Override
	protected ExtendedLireIndexer initFeatureIndexer() {
		//return new ExtendedLireIndexer();
		return new SmkLireIndexer();
	}
	
	@Override
	protected void insertFeatures(String docID, String thumbnailUrl,
			String imgFeatures) throws FactoryConfigurationError,
			ImageIndexingException {
		// TODO Auto-generated method stub
		super.insertFeatures(docID, thumbnailUrl, imgFeatures);
	}
	

	@Override
	protected FeatureClassCollector getVirFeatureClasses() {
		//return super.getVirFeatureClasses();
		return featureClasses;
	}
}
