package eu.europeana.service.ir.image;

import it.cnr.isti.config.index.IndexConfigurationImpl;

import java.io.File;

import org.apache.log4j.Logger;


public class IRConfigurationImpl extends IndexConfigurationImpl implements IRConfiguration {

	protected static final String COMPONENT_NAME = "image-similarity";
	protected static final String PROP_API_KEY = "europeana.api.key";
	
	protected Logger log = Logger.getLogger(getClass());
	
//	protected static final String LIRE_SETTINGS = "LIRE_MP7ALL.properties";
//	protected static final String FEATURES_ARCHIVE_FILE = "FCArchive-lire.dat";
//	protected static final String IMAGE_FX_FILE = "image-fx.properties";
//	protected static final String CONF_FOLDER = "conf";
//	protected static final String PROP_IMAGE_INDEX_HOME = "image.index.home";
//	protected static final String PROP_DATA_SET_DEFAULT = "dataset.default";
	
//	File indexHome;
//	LireSettings lireSettings;
//	File featuresArchiveFile = null;
	String apiKey = null;
	
	@Override
	public String getComponentName() {
		return COMPONENT_NAME;
	}

	public IRConfigurationImpl() {
		super();
		//init();
	}

//	File getIndexHomeFolder() {
//		if (indexHome == null) {
//			indexHome = new File(getConfigProperty(PROP_IMAGE_INDEX_HOME));
//		}
//		return indexHome;
//	}
//
//	public File getIndexFolder(String dataset) {
//		if (indexHome == null) {
//			indexHome = new File(getConfigProperty(PROP_IMAGE_INDEX_HOME));
//		}
//		
//		if (dataset != null)
//			return new File(indexHome, dataset);
//		else
//			return new File(indexHome, getDefaultDataset());
//	}
//	
//	public File getIndexConfFolder(String dataset) {
//		return new File(getIndexFolder(dataset), CONF_FOLDER);
//	}
//	
//	public LireSettings getLireSettings(String dataset) throws IOException, VIRException {
//		if (lireSettings == null)
//			lireSettings = new LireSettings(new File(getIndexConfFolder(dataset),
//					LIRE_SETTINGS));
//		return lireSettings;
//	}
//
//	public File getFeaturesArchiveFile(String dataset) {
//
//		return new File(getIndexFolder(dataset),
//				FEATURES_ARCHIVE_FILE);
//	}
//
//	public File getImageFxFile(String dataset) {
//
//		return new File(getIndexConfFolder(dataset),
//				IMAGE_FX_FILE);
//	}
//	
//	public String getDefaultDataset(){
//		return getConfigProperty(PROP_DATA_SET_DEFAULT);
//	}
	
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
			return new File(getDatasetsFolder(), getDefaultDataset() + ".urls.csv");
		else
			return new File(getDatasetsFolder(), dataset+".urls.csv");
	}
	

}
