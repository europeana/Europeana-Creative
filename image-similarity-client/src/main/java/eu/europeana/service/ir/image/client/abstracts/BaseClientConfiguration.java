package eu.europeana.service.ir.image.client.abstracts;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class supports properties evaluation from configuration file.
 * 
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 */
public abstract class BaseClientConfiguration implements ClientConfiguration {

	/**
		 * 
		 */
	private static final long serialVersionUID = 7820702978291293396L;
	protected Logger log = Logger.getLogger(getClass());
	private Properties properties = new Properties();
	private static final String CONFIG_FILE_PREFIX = "/";
	private static final String CONFIG_FILE_EXT = "-client.properties";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.assets.Configuration#getConfigProperty(java.lang.String)
	 */
	public String getConfigProperty(String propertyName) {
		return (String) getProperties().get(propertyName);
	}
	
	protected BaseClientConfiguration(){
			log.trace("Initializing configuration object: " + getClass());
	}

	/**
	 * This method evaluates property from configuration file.
	 * 
	 * @param propertyName
	 * @param defaultValue
	 * @return property from configuration file or default value
	 */
	public String getConfigProperty(String propertyName, String defaultValue) {
		String ret = getConfigProperty(propertyName);
		return (ret != null) ? ret : defaultValue;
	}

	/**
	 * @return server URL from configuration file or default value
	 */
	public String getServerUrl() {
		String res = getConfigProperty(PROP_SERVER_URL);
		return (res != null) ? res : DEFAULT_SERVER_URL;
	}

	/**
	 * @return application name from configuration file or default value
	 */
	public String getApplicationName() {
		String res = getConfigProperty(PROP_APPLICATION_NAME);
		return (res != null) ? res : DEFAULT_APPLICATION_NAME;
	}

	/**
	 * @return default service URL
	 */
	public String getServiceUrl() {
		return getServiceUrl(null, null);
	}

	/**
	 * This method creates URL to the rest service for particular component.
	 * 
	 * @param componentName
	 * @param serviceName
	 * @return URL to the rest service for particular component
	 */
	public String getServiceUrl(String componentName, String serviceName) {
		String res = getServerUrl() + PATH_SEPARATOR + getApplicationName();

		if (componentName != null)
			res += (PATH_SEPARATOR + componentName);
		else
			res += (PATH_SEPARATOR + getComponentName());

		if (serviceName != null)
			res += (PATH_SEPARATOR + serviceName);

		return (res + "/rest");
	}

	/**
	 * Method used for initialization of this configuration object
	 */
	public void init() {
		loadProperties();
	}

	/**
	 * This method reads a configuration file and retrieves properties.
	 */
	protected void loadProperties() {

		try {
			InputStream resourceAsStream = getClass().getResourceAsStream(
					getConfigFile());
			if (resourceAsStream != null)
				getProperties().load(resourceAsStream);
			else
				log.info("No configuration file found for component: "
						+ getComponentName());

		} catch (Exception e) {
			throw new RuntimeException(
					"Cannot read configuration file: " + getConfigFile(), e);
		}

	}

	/**
	 * @return configuration file name containing configuration file prefix,
	 *         component name and file extension
	 */
	protected String getConfigFile() {
		return CONFIG_FILE_PREFIX + getComponentName() + CONFIG_FILE_EXT;
	}

	/**
	 * @return properties from configuration file
	 */
	public Properties getProperties() {
		return properties;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
