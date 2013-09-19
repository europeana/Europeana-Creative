package eu.europeana.assets.ir.image.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.assets.ir.image.api.evaluation.IndexHelper;
import eu.europeana.service.ir.image.IRImageConfiguration;
import eu.europeana.service.ir.image.api.ImageIndexingService;
import eu.europeana.service.ir.image.api.ImageIndexingServiceImpl;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/image-similarity-context.xml" })
public class ImageIndexingServiceTest {

	@Autowired
	ImageIndexingService imageIndexingService;

	@Autowired
	IRImageConfiguration configuration;

	ImageIndexingService testImageIndexingService;

	// @Test
	public void testIndexDefaultDataset() throws ImageIndexingException,
			IOException {
		String datasetName = null;
		// TODO: - use config.getDefaultDataset();
		int indexedImages = imageIndexingService.insertCollectionByUrls(
				datasetName,
				getTestThumbnails(configuration.getDefaultDataset()));
		System.out.println("successfully indexed images: " + indexedImages);
		assertEquals(6, indexedImages);
	}

	@Test
	public void testIndexCollectionByUrls() throws ImageIndexingException,
			IOException {
		String testDataset = "test";
		testImageIndexingService = new ImageIndexingServiceImpl(testDataset,
				configuration);

		int indexedImages = testImageIndexingService.insertCollectionByUrls(
				testDataset, getTestThumbnails(testDataset));
		System.out.println("successfully indexed images: " + indexedImages);
		assertEquals(42, indexedImages);
		//TODO: check if indexing process introduces duplications
	}

	private Map<String, String> getTestThumbnails(String dataset)
			throws IOException {
		String datasetFile = "./src/test/resources/datasets/" + dataset
				+ ".csv";

		IndexHelper ixHelper = new IndexHelper();
		return ixHelper.getThumbnailsMap(datasetFile);
	}
}
