package eu.europeana.creative.dataset.indexing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;
import eu.europeana.creative.dataset.BaseCreativeDatasetUtil;
import eu.europeana.creative.dataset.IRTestConfigurations;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.ImageIndexingService;
import eu.europeana.service.ir.image.api.ImageIndexingServiceImpl;

public class LocalImageIndexingTest extends
		BaseCreativeDatasetUtil implements IRTestConfigurations {

	private static int blockSize=-2;
	private static int limit=-2;
	private static int start=-2;
	private static String paramDataset=null;
	
	
	public static final String PREFIX_START="start=";
	public static final String PREFIX_LIMIT="limit=";
	public static final String PREFIX_BLOCKSIZE="blockSize=";
	public static final String PREFIX_DATASET="dataset=";
	public static final int DEFAULT_BLOCKSIZE = 1000;
	
	ImageIndexingService imageIndexingService; 
	// public static String CLASS_WW1 = "ww1";

	public static void main(String[] args) throws Exception {                    
	    parseParams(args);   
		
		JUnitCore.main(
				LocalImageIndexingTest.class.getCanonicalName());            
	}
	
	
	protected static void parseParams(String[] args) {
		for (int i = 0; i < args.length; i++) {
			
			if(args[i].startsWith(PREFIX_START)){
				start = getValue(PREFIX_START, args[i]);
				continue;
			}else if(args[i].startsWith(PREFIX_LIMIT)){
				limit = getValue(PREFIX_LIMIT, args[i]);
				continue;
			}else if(args[i].startsWith(PREFIX_BLOCKSIZE)){
				blockSize = getValue(PREFIX_BLOCKSIZE, args[i]);
				continue;
			}else if(args[i].startsWith(PREFIX_DATASET)){
				paramDataset = getStringValue(PREFIX_DATASET, args[i]);
				//continue;
			}
		}
		
	}

	@Test
	public void indexThumbnails() throws FileNotFoundException, IOException {

		ensureParmsInit();
		
		File datasetFile = getDatasetFileToIndex();
		if(datasetFile == null || !datasetFile.exists())
			System.out.println("Skip test missing file: " + datasetFile);
		else
			updateIndex(datasetFile, false);
		
	}

	
	/**
	 * This method should be enabled for testing in subclasses. By default the indexThumbnails is marked as testcase 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void removeThumbnails() throws FileNotFoundException, IOException {

		ensureParmsInit();
		
		File datasetFile = getDatasetFileToRemove();
		if(datasetFile == null || !datasetFile.exists())
			System.out.println("Skip test missing file: " + datasetFile);
		else
			updateIndex(datasetFile, true);
	}

	protected File getDatasetFileToRemove() {
		//overwrite in subclasses
		return null;
	}


	protected File getDatasetFileToIndex() {
		IRConfiguration config = getConfig();
		//File datasetFile = config.getDatasetFile(DATASET_EU_CREATIVE_COLOR);
		File datasetFile = config.getDatasetFile(getDataset());
		return datasetFile;
	}


	protected void updateIndex(File datasetFile, boolean deleteItems) throws FileNotFoundException,
			IOException {
		
		System.out.println("using image set file : " + datasetFile.getAbsolutePath());
		//setDataset("test");
		
		LargeThumbnailsetProcessing datasetProcessor = new LargeThumbnailsetProcessing(datasetFile);
		ImageIndexingObserver observer = new ImageIndexingObserver(getImageIndexingService(), deleteItems);
		
		datasetProcessor.addObserver(observer);
		datasetProcessor.processThumbnailset(start, limit, blockSize);
		
		System.out.println("Skipped items: " + datasetProcessor.getFailureCount());
	}

	protected void ensureParmsInit() {
		//ensure blocksize set to a positive value
		if(blockSize < 0)
			blockSize = DEFAULT_BLOCKSIZE;
		
		if(paramDataset != null)
			setDataset(paramDataset);
	}

	public ImageIndexingService getImageIndexingService() {
		if (imageIndexingService == null) {
			imageIndexingService = new ImageIndexingServiceImpl(getDataset(),
					(IRConfiguration)getConfig());
			imageIndexingService.init();
		}
		return imageIndexingService;
	}
}
