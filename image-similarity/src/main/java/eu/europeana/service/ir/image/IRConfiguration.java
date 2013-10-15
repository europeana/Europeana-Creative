package eu.europeana.service.ir.image;

import it.cnr.isti.config.index.IndexConfiguration;

	/**
	 * Common configuration facility for retrieval services.<br/>
	 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
	 */
	public interface IRConfiguration extends IndexConfiguration{

//		public static final String PROP_SERVER_URL = "server.url";
//		public static final String PROP_APPLICATION_NAME = "application.name";
		
//		public static final String DEFAULT_SERVER_URL = "http://localhost";
//		public static final String DEFAULT_APPLICATION_NAME = "image-similarity";
		
		//public static final String PATH_SEPARATOR = "/";
		
//		public String getComponentName();
//		
//		public void init();
		public String getApiKey();
}
