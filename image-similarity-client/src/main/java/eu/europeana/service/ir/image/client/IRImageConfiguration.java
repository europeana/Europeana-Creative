package eu.europeana.service.ir.image.client;


public interface IRImageConfiguration extends ClientConfiguration{
	//API CONFIG KEYS
	public static final String PROP_IR_IMAGE_API_KEY = "europeana.api.ir.image.key";
	public static final String PROP_IR_IMAGE_API_URI = "europeana.api.ir.image.uri";

	public abstract String getIrImageSearchUri();

	public abstract String getApiKey();
}
