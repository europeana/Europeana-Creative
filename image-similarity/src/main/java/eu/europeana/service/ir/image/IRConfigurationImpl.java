package eu.europeana.service.ir.image;

import it.cnr.isti.config.index.IndexConfigurationImpl;

import java.io.File;

import org.apache.log4j.Logger;

import eu.europeana.api.client.config.ThumbnailAccessConfiguration;


public class IRConfigurationImpl extends IndexConfigurationImpl implements IRConfiguration, ThumbnailAccessConfiguration {

	protected static final String COMPONENT_NAME = "image-similarity";
	protected static final String PROP_API_KEY = "europeana.api.key";
	
	protected Logger log = Logger.getLogger(getClass());
	
	String apiKey = null;
	
	@Override
	public String getComponentName() {
		return COMPONENT_NAME;
	}

	public IRConfigurationImpl() {
		super();
		//init();
	}


	
	public String getApiKey() {
		if (apiKey == null)
			apiKey = getConfigProperty(PROP_API_KEY);
		return apiKey;
	}

	
	/**
	 * see also {@link #getDatasetFile(String)}
	 * @param dataset
	 * @return
	 */
	@Override
	public File getDatasetUrlsFile(String dataset) {
		if(dataset == null)
			return new File(getDatasetsFolderAsFile(), getDefaultDataset() + ".urls.csv");
		else
			return new File(getDatasetsFolderAsFile(), dataset+".urls.csv");
	}

	@Override
	public String getBaseFolder() {
		//redirect to INDEXHOME folder
		return getIndexHomeFolder().getAbsolutePath();
	}

	/**
	 * @see #getDatasetsFolderAsFile()
	 */
	@Override
	public String getDatasetsFolder() {
		//the datasets folder is hardcoded
		return getDatasetsFolderAsFile().getAbsolutePath();
	}

	/**
	 * @see #getImageFolderAsFile(String)
	 */
	@Override
	public String getImageFolder(String dataset) {
		return null;
	}
	

}
