package eu.europeana.creative.dataset;

import java.io.File;

import eu.europeana.api.client.config.ThumbnailAccessConfiguration;
import eu.europeana.api.client.dataset.BaseDatasetUtil;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;

/**
 * 
 * @author Sergiu Gordea
 *  
 *
 */
public class BaseCreativeDatasetUtil extends
		BaseDatasetUtil implements IRTestConfigurations {

	public final String DATASET_EU_CREATIVE = "eucreative";
	public final String DATASET_EU_CREATIVE_CLASSIFIED = "eucreative_classified";
	public final String DATASET_EU_CREATIVE_COLOR = "eucreative_color";
	private String dataset = DATASET_EU_CREATIVE;
	
	
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public void testGetThumbnailsForCollectionLimit() {
		// avoid execution
	}

	public void testGetThumbnailsForCollectionAll() {
		// avoid execution
	}

	public String getDataset() {
		return dataset;
	}
	
	protected String getCollectionsCvsFolder() {
		return getCollectionsCvsFolder(getDataset());
	}
	
	public String getCollectionsCvsFolder(String dataset) {
		return COLLECTIONS_FOLDER + dataset + "/";
	}

//	protected String getCollectionsCvsFolder() {
//		return getCollectionsCvsFolder(getDataset());
//	}


//	BufferedWriter getDataSetFileWriter(boolean urls)
//			throws IOException {
//		File datasetFile = getDataSetFile(urls);
//		datasetFile.getParentFile().mkdirs();
//
//		return new BufferedWriter(new FileWriter(datasetFile));
//	}
	
//	private File getDataSetFile(boolean urls) {
//		IRConfiguration config = getConfig();
//		if (urls)
//			return config.getDatasetUrlsFile(getDataset());
//		else
//			return config.getDatasetFile(getDataset());
//	}
	
	public File getDataSetFile(boolean urls) {
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

	@Override
	public ThumbnailAccessConfiguration getConfiguration() {
		return (ThumbnailAccessConfiguration) getConfig();
	}

}
