package eu.europeana.service.ir.image.client.abstracts;

	/**
	 * Common configuration facility for retrieval services.<br/>
	 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
	 */
	public interface ClientConfiguration {

		public static final String PROP_SERVER_URL = "server.url";
		public static final String PROP_APPLICATION_NAME = "application.name";
		
		public static final String DEFAULT_SERVER_URL = "http://localhost:8989";
		public static final String DEFAULT_APPLICATION_NAME = "";
		
		public static final String PATH_SEPARATOR = "/";
		
		
		public String getConfigProperty(String propertyName);
		
		public String getComponentName();
		
		public String getServerUrl();
		
		public void init();
		
}
