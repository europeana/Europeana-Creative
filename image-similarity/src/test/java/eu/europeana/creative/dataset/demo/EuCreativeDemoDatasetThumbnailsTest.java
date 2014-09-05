package eu.europeana.creative.dataset.demo;

import it.cnr.isti.indexer.IndexHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import eu.europeana.api.client.config.ThumbnailAccessConfiguration;
import eu.europeana.api.client.thumbnails.download.ThumbnailDownloader;
import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;
import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.creative.dataset.BaseCreativeDatasetUtil;
import eu.europeana.creative.dataset.IRTestConfigurations;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.ImageSearchingService;
import eu.europeana.service.ir.image.api.ImageSearchingServiceImpl;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;

public class EuCreativeDemoDatasetThumbnailsTest extends
		BaseCreativeDatasetUtil implements IRTestConfigurations {

	// public static String CLASS_WW1 = "ww1";

	ImageSearchingService imageSearchingService;
	IRConfiguration config = new IRConfigurationImpl();
	
	public static void main(String[] args) throws Exception {                    
	       JUnitCore.main(
	         "eu.europeana.creative.dataset.pt.EuCreativeDemoDatasetThumbnailsTest");            
	}
	
	
	@Test
	public void downloadThumbnails() throws FileNotFoundException, IOException {

		File datasetFile = getConfig().getDatasetFile(DATASET_EU_CREATIVE);
		File downloadFolder = getConfig().getImageFolderAsFile(DATASET_EU_CREATIVE);

		LargeThumbnailsetProcessing datasetDownloader = new LargeThumbnailsetProcessing(datasetFile);
		datasetDownloader.addObserver(new ThumbnailDownloader(downloadFolder));
		datasetDownloader.processThumbnailset(0, -1, 1000);
		
		System.out.println("Skipped items: " + datasetDownloader.getFailureCount());
//		for (String itemId : skippedItems) {
//			System.out.println(itemId);
//		}
	}

	protected IRConfiguration getConfig() {
		return config;
	}

	//@Test
	public void buildIndexedUrlsFile() throws FileNotFoundException,
			IOException, ImageSearchingException {

		IRConfiguration config = getConfig();
		File datasetFile = config.getDatasetFile(getDataset());

		IndexHelper ixHelper = new IndexHelper();
		Map<String, String> thumbnailsMap = ixHelper
				.getThumbnailsMap(datasetFile);
		BufferedWriter indexedUrlsWriter = getDataSetFileWriter(true);
		EuropeanaId euId = new EuropeanaId();
		int counter = 0;

		for (Map.Entry<String, String> thumbnail : thumbnailsMap.entrySet()) {

			euId.setNewId(thumbnail.getKey());
			try {
				getImageSearchingService().searchSimilar(euId);

				if (getImageSearchingService().getTotalResults() > 0) {
					// write to file
					indexedUrlsWriter.append(thumbnail.getKey()).append("; ");
					indexedUrlsWriter.append(thumbnail.getValue()).append("\n");
					counter++;
				} else {
					// not indexed yet
					System.out.println("Skipped item: " + euId.getNewId());
				}

			} catch (ImageSearchingException e) {
				System.out.println(e.getMessage());
			}
		}

		System.out.println("correct items: " + counter);
	}

	public ImageSearchingService getImageSearchingService() {
		if (imageSearchingService == null) {
			imageSearchingService = new ImageSearchingServiceImpl(getDataset(),
					getConfig());
			imageSearchingService.init();
		}
		return imageSearchingService;
	}
	
}
