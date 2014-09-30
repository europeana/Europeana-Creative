package eu.europeana.service.ir.image.client.impl;

import eu.europeana.service.ir.image.client.IRImageConfiguration;
import eu.europeana.service.ir.image.client.impl.abstracts.BaseClientConfiguration;

public class IRImageConfigurationImpl extends BaseClientConfiguration implements IRImageConfiguration{

	@Override	
	public String getComponentName() {
		return "image-similarity";
	}

	@Override
	public String getIrImageSearchUri() {
		return getConfigProperty(PROP_IR_IMAGE_API_URI);
	}

	@Override
	public String getApiKey() {
		return getConfigProperty(PROP_IR_IMAGE_API_KEY, "");
	}

	
}
