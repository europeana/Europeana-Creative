package eu.europeana.creative.ir.image.api;

import static org.junit.Assert.assertEquals;
import it.cnr.isti.indexer.IndexHelper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.ImageIndexingService;
import eu.europeana.service.ir.image.api.ImageIndexingServiceImpl;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;

public class HistorianaImageIndexingTest {

	
	IRConfigurationImpl configuration;

	ImageIndexingService testImageIndexingService;


	@Test
	public void testIndexHistorianaDataset() throws ImageIndexingException,
			IOException {
		String dataset = "historiana";
		configuration = new IRConfigurationImpl();
		testImageIndexingService = new ImageIndexingServiceImpl(dataset,
				configuration);

		int indexedImages = testImageIndexingService.insertCollectionByUrls(
				dataset, getThumbnailsMap(dataset));
		
		System.out.println("successfully indexed images: " + indexedImages);
		
		//assertEquals(42, indexedImages);
		//TODO: check if indexing process introduces duplications
	}
	
	private Map<String, String> getThumbnailsMap(String dataset)
			throws IOException {
		
		File datasetFile = configuration.getDatasetFile(dataset);

		IndexHelper ixHelper = new IndexHelper();
		return ixHelper.getThumbnailsMap(datasetFile);
	}
}
