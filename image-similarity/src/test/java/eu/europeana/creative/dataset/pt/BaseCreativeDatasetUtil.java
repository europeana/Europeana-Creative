package eu.europeana.creative.dataset.pt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import eu.europeana.api.client.thumbnails.ThumbnailsForCollectionAccessorTest;
import eu.europeana.creative.dataset.IRTestConfigurations;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.ImageSearchingService;

public class BaseCreativeDatasetUtil extends
		ThumbnailsForCollectionAccessorTest implements IRTestConfigurations {

	final String DATASET_EU_CREATIVE = "eucreative";
	final String DATASET_EU_CREATIVE_CLASSIFIED = "eucreative_classified";
	private String dataset = DATASET_EU_CREATIVE;
	protected ImageSearchingService imageSearchingService;

	
	public void testGetThumbnailsForCollectionLimit() {
		// avoid execution
	}

	public void testGetThumbnailsForCollectionAll() {
		// avoid execution
	}

	public String getDataset() {
		return dataset;
	}
	
	public String getCollectionsCvsFolder(String dataset) {
		return COLLECTIONS_FOLDER + dataset + "/";
	}

	@Override
	protected String getCollectionsCvsFolder() {
		return getCollectionsCvsFolder(getDataset());
	}

	BufferedWriter getDataSetFileWriter(boolean urls)
			throws IOException {
		File datasetFile = getDataSetFile(urls);
		datasetFile.getParentFile().mkdirs();

		return new BufferedWriter(new FileWriter(datasetFile));
	}
	
	private File getDataSetFile(boolean urls) {
		IRConfiguration config = getConfig();
		if (urls)
			return config.getDatasetUrlsFile(getDataset());
		else
			return config.getDatasetFile(getDataset());
	}
	
	protected IRConfiguration getConfig() {
		IRConfiguration config = new IRConfigurationImpl();
		return config;
	}
}
