package eu.europeana.creative.dataset.culturecam.v2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;

import eu.europeana.creative.dataset.indexing.LocalImageIndexingTest;
import eu.europeana.service.ir.image.api.ImageIndexingService;
import eu.europeana.service.ir.image.api.SmkImageIndexingServiceImpl;

public class CultureCamIndexingDatasetTest extends LocalImageIndexingTest{

	SmkImageIndexingServiceImpl testImageIndexingService;

	@Before
	public void init(){
		String dataset = "culturecam";
		setDataset(dataset);
	}
	
	@Override
	protected void updateIndex(File datasetFile, boolean deleteItems)
			throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		super.updateIndex(datasetFile, deleteItems);
	}
	
	@Override
	public ImageIndexingService getImageIndexingService() {
		if(testImageIndexingService == null)
			testImageIndexingService = new SmkImageIndexingServiceImpl(getDataset(), getConfig());
		// TODO Auto-generated method stub
		//return super.getImageIndexingService();
		return testImageIndexingService;
	}
}
