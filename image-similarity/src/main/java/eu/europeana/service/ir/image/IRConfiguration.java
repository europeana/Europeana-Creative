package eu.europeana.service.ir.image;

import it.cnr.isti.config.index.IndexConfiguration;

import java.io.File;

	/**
	 * Common configuration facility for retrieval services.<br/>
	 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
	 */
	public interface IRConfiguration extends IndexConfiguration{


		//public String getApiKey();
		
		/**
		 * Return the (.csv.url) file containing the euopeanaID-thumbnailUrl map for the given dataset.
		 * @param dataset
		 * @return the physical location of the (dataset.csv.urls) file  
		 */
		public File getDatasetUrlsFile(String dataset);
		
}
