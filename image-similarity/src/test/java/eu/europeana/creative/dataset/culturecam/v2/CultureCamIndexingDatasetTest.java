package eu.europeana.creative.dataset.culturecam.v2;

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
	public ImageIndexingService getImageIndexingService() {
		if(testImageIndexingService == null)
			testImageIndexingService = new SmkImageIndexingServiceImpl(getDataset(), getConfig());
		return testImageIndexingService;
	}
}
