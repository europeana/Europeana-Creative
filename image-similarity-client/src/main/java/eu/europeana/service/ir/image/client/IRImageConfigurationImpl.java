package eu.europeana.service.ir.image.client;

import eu.europeana.service.ir.image.client.abstracts.BaseClientConfiguration;
import eu.europeana.service.ir.image.client.abstracts.IRImageConfiguration;

public class IRImageConfigurationImpl extends BaseClientConfiguration implements IRImageConfiguration{

	@Override	
	public String getComponentName() {
		return "image-similarity";
	}

	
}
