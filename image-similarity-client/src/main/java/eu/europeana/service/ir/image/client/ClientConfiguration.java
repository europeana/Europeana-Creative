package eu.europeana.service.ir.image.client;

	/**
	 * Common configuration facility for retrieval services.<br/>
	 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
	 */
	public interface ClientConfiguration {

		
		public static final String PATH_SEPARATOR = "/";
		
		
		public String getConfigProperty(String propertyName);
		
		public String getComponentName();
		
		//public String getServerUrl();
		
		public void init();
		
}
