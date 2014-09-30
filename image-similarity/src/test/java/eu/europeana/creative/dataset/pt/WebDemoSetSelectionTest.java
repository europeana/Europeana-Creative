package eu.europeana.creative.dataset.pt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;
import eu.europeana.creative.dataset.BaseCreativeDatasetUtil;
import eu.europeana.creative.dataset.IRTestConfigurations;
import eu.europeana.creative.dataset.pt.classification.ThumbnailSelector;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;

public class WebDemoSetSelectionTest extends
		BaseCreativeDatasetUtil implements IRTestConfigurations {

	private static int blockSize=-2;
	private static int limit=-2;
	private static int start=-2;
	
	public static final String PREFIX_START="start=";
	public static final String PREFIX_LIMIT="limit=";
	public static final String PREFIX_BLOCKSIZE="blockSize=";
	public static final int DEFAULT_BLOCKSIZE = 1000;
	
	
	// public static String CLASS_WW1 = "ww1";

	public static void main(String[] args) throws Exception {                    
	    parseParams(args);   
		
		JUnitCore.main(
				WebDemoSetSelectionTest.class.getCanonicalName());            
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
				//continue;
			}
		}
		
	}


	protected static int getValue(String argPrefix, final String arg) {
		return Integer.parseInt(arg.substring(argPrefix.length()));
	}


	@Test
	public void selectThumbnails() throws FileNotFoundException, IOException {

		ensureParmsInit();
		
		IRConfiguration config = getConfig();
		File datasetFile = config.getDatasetFile(DATASET_EU_CREATIVE_COLOR);
		File outputFile = config.getDatasetUrlsFile(getDataset());
		cleanOutFile(outputFile);

		LargeThumbnailsetProcessing thumbnailSetSelector = new LargeThumbnailsetProcessing(datasetFile);
		ThumbnailSelector observer = new ThumbnailSelector(5);
		observer.setOutputFile(outputFile);
		
		thumbnailSetSelector.addObserver(observer);
		thumbnailSetSelector.processThumbnailset(start, limit, blockSize);
		
		System.out.println("Items selected: " + observer.getSelectedItemsCount());
		System.out.println("from: " + observer.getItemCounter());
	}


	protected void cleanOutFile(File outputFile) {
		System.out.println("Output file deleted: " +outputFile.delete());
		
	}

	private void ensureParmsInit() {
		//ensure blocksize set to a positive value
		if(blockSize < 0)
			blockSize = DEFAULT_BLOCKSIZE;
		
		setDataset(DATASET_EU_CREATIVE);
	}


	protected IRConfiguration getConfig() {
		return new IRConfigurationImpl();
	}

	
}
