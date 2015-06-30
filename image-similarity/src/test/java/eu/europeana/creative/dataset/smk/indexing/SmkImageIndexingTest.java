package eu.europeana.creative.dataset.smk.indexing;

import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.indexer.IndexHelper;
import it.cnr.isti.melampo.vir.exceptions.VIRException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.SmkImageIndexingServiceImpl;
import eu.europeana.service.ir.image.api.SmkPivotManagementServiceImpl;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;

public class SmkImageIndexingTest {

	IRConfiguration configuration;

	SmkImageIndexingServiceImpl testImageIndexingService;

	SmkPivotManagementServiceImpl pivotManager;
	
	private Map<String, String> getThumbnailsMap(String dataset)
			throws IOException {

		File datasetFile = configuration.getDatasetFile(dataset);

		IndexHelper ixHelper = new IndexHelper();
		return ixHelper.getThumbnailsMap(datasetFile);
	}

//	@Test
//	public void testFeatureExtraction() throws ImageIndexingException,
//			IOException {
//		String dataset = "smk";
//		initIndexingService(dataset);
//
//		// DominantColorExtractor extractor;
//		File testImagesFolder = new File("/tmp/dcd");
//		String[] fileNames = testImagesFolder.list();
//		File imageFile;
//		// DominantColorValues values;
//		String fileName = null;
//		String features;
//
//		for (int i = 0; i < fileNames.length; i++) {
//
//			fileName = fileNames[i];
//			if (fileName.endsWith(".jpg")) {
//				imageFile = new File(testImagesFolder, fileName);
//				System.out.println(imageFile.getAbsolutePath());
//				features = testImageIndexingService.extractFeatures(fileName,
//						new FileInputStream(imageFile));
//				System.out.println(features);
//			}
//
//			// imageFile = new File(testImagesFolder, fileName);
//			// extractor = new DominantColorExtractor();
//			// values = extractor.extractDescriptor(imageFile);
//			// System.out.println(imageFile);
//			// System.out.println(values);
//		}
//
//		// String imgFeatures = testImageIndexingService.extractFeatures(docID,
//		// imageObj);
//
//		// int indexedImages = testImageIndexingService.insertCollectionByUrls(
//		// dataset, getThumbnailsMap(dataset));
//		//
//		// System.out.println("successfully indexed images: " + indexedImages);
//
//		// assertEquals(42, indexedImages);
//		// TODO: check if indexing process introduces duplications
//	}

	protected void initIndexingService(String dataset) {
		configuration = new IRConfigurationImpl();
		testImageIndexingService = new SmkImageIndexingServiceImpl(dataset,
				configuration);
	}
	
	protected void initPivotManagementService(String dataset) {
		configuration = new IRConfigurationImpl();
		pivotManager = new SmkPivotManagementServiceImpl(
				configuration, dataset);
	}

	@Test
	public void testPivotFeaturesExtraction() throws IOException, FeatureExtractionException, VIRException{
		initPivotManagementService("smk");
		Map<String, String> thumbnails = getThumbnailsMap("smk");
		pivotManager.extractPivotFeatures(thumbnails.keySet());
		pivotManager.generateLireObjectPivotsBin();
		
	}
	
	@Test
	public void testIndexDataset() throws ImageIndexingException, IOException {
			String dataset = "smk";
			initIndexingService(dataset);

			Map<String, String> thumbnails = getThumbnailsMap("smk");
			int indexedImages = testImageIndexingService.insertDatasetByIds(thumbnails.keySet()); 
					
//					insertCollectionByUrls(
//					dataset, getThumbnailsMap(dataset));

			System.out.println("successfully indexed images: " + indexedImages);

			// assertEquals(42, indexedImages);
			// TODO: check if indexing process introduces duplications
		}


	
}
