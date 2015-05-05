package eu.europeana.creative.flickr.connection;

import java.io.InputStream;
import java.util.Properties;

import eu.europeana.api.client.exception.TechnicalRuntimeException;

public class FlickrClientConfiguration{

	private static final String FLICKR_PROPERTIES_FILE = "/flickr.properties";
	private static final String PROP_FLICKR_API_KEY = "flickr.apiKey";
	private static final String PROP_FLICKR_AUTH_TOKEN = "flickr.authToken";
	private static final String PROP_FLICKR_API_SIG = "flickr.apiSig";
	
	
	/**
	 * Accessor method for the singleton
	 * 
	 * @return
	 */
	public static synchronized FlickrClientConfiguration getInstance() {
		singleton = new FlickrClientConfiguration();
		singleton.loadProperties();
		return singleton;
	}
	
	//local attributes
		private static Properties properties = null;
		private static FlickrClientConfiguration singleton;

		/**
		 * Hide the default constructor
		 */
		FlickrClientConfiguration() {
		}

		/**
		 * Laizy loading of configuration properties
		 */
		public synchronized void loadProperties() {
			try {
				properties = new Properties();
				InputStream resourceAsStream = getClass().getResourceAsStream(
						FLICKR_PROPERTIES_FILE);
				if (resourceAsStream != null)
					getProperties().load(resourceAsStream);
				else
					throw new TechnicalRuntimeException(
							"No properties file found in classpath! "
									+ FLICKR_PROPERTIES_FILE);

			} catch (Exception e) {
				throw new TechnicalRuntimeException(
						"Cannot read configuration file: "
								+ FLICKR_PROPERTIES_FILE, e);
			}

		}

		/**
		 * provides access to the configuration properties. It is not recommended to
		 * use the properties directly, but the
		 * 
		 * @return
		 */
		Properties getProperties() {
			return properties;
		}

		/**
		 * 
		 * @return the name of the file storing the client configuration
		 */
		String getConfigurationFile() {
			return FLICKR_PROPERTIES_FILE;
		}

		/**
		 * This method provides access to the API key defined in the configuration
		 * file
		 * @see PROP_FREEBASE_API_KEY
		 * 
		 * @return
		 */
		public String getApiKey() {
			return getProperties().getProperty(PROP_FLICKR_API_KEY);
		}

		public String getAuthToken() {
			return getProperties().getProperty(PROP_FLICKR_AUTH_TOKEN);
		}

		public String getApiSig() {
			return getProperties().getProperty(PROP_FLICKR_API_SIG);
		}

}
