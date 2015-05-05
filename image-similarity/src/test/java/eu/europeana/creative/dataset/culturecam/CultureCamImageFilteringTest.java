package eu.europeana.creative.dataset.culturecam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;
import eu.europeana.creative.dataset.culturecam.observer.FileSizeFilteringObserver;
import eu.europeana.creative.dataset.indexing.LocalImageIndexingTest;
import eu.europeana.service.ir.image.IRConfiguration;


public class CultureCamImageFilteringTest extends LocalImageIndexingTest{

	@Before
	public void init(){
		String dataset = "culturecam";
		setDataset(dataset);
	}
	
	public void indexThumbnails() throws FileNotFoundException, IOException{
		//disable test
	}
	
	@Test
	public void filterThumbnails() throws FileNotFoundException, IOException {

		//ensureParamsInit();
		
		IRConfiguration config = getConfig();
		//File datasetFile = config.getDatasetFile(DATASET_EU_CREATIVE_COLOR);
		File datasetFile = config.getDatasetFile(getDataset());
		System.out.println("using configuration file : " + datasetFile.getAbsolutePath());
		//setDataset("test");
		
		LargeThumbnailsetProcessing datasetProcessor = new LargeThumbnailsetProcessing(datasetFile);
		File imageFolder = config.getImageFolderAsFile(getDataset());
		File outFile = getBadThumbnailsCvsFile();
		
		if(!outFile.exists())
			outFile.getParentFile().mkdirs();
		else
			outFile.delete();
		
		int placeHolderSize = readPlaceHolderSize();
		
		FileSizeFilteringObserver observer = new FileSizeFilteringObserver(imageFolder, outFile, 1000,  placeHolderSize);
		
		datasetProcessor.addObserver(observer);
		datasetProcessor.processThumbnailset(0, -1, 1000);
		
		System.out.println("Skipped items: " + datasetProcessor.getFailureCount());
	}

	private int readPlaceHolderSize() {
		File placeholderFile = new File(getCollectionsCvsFolder() + "selection/img/placeholder_3583Bytes.jpg");
		if(placeholderFile.exists())
			return (int)placeholderFile.length();
		else
			return -1;
	}

	protected File getBadThumbnailsCvsFile() {
		File outFile = new File(getCollectionsCvsFolder() + "filtering/badThumbnails.csv");
		return outFile;
	}
	
	
	
}
