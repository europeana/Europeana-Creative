package eu.europeana.service.ir.image;

import it.cnr.isti.config.index.IndexConfiguration;
import it.cnr.isti.melampo.tools.thumbnail.locator.ImageLocator;

import java.io.File;
import java.util.Map;

	/**
	 * Common configuration facility for retrieval services.<br/>
	 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
	 */
	public interface IRConfiguration extends IndexConfiguration{

		public static final String PROP_API_KEY = "europeana.api.key";
		public static final String PROP_LOCATOR_REPOSITORY_URL = ImageLocator.PROP_LOCATOR_REPOSITORY_URL;
		public static final String PROP_IR_IMAGE_PIVOTS_ARCHIVE = "image.index.pivots.archive";

		//public String getApiKey();
		
		/**
		 * Return the (.csv.url) file containing the euopeanaID-thumbnailUrl map for the given dataset.
		 * @param dataset
		 * @return the physical location of the (dataset.csv.urls) file  
		 */
		public File getDatasetUrlsFile(String dataset);
		
		public Map<String, String> getLocatorConfigurations();

		public String getPivotsCsvFile(String dataset);

		public String getPivotsFolder(String dataset);
		
		public String getPivotsFCArchive(String dataset);
		
		public String getSubsetFCArchive(String dataset, String subset);
		
}
