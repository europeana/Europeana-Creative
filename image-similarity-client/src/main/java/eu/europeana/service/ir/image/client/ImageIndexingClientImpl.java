package eu.europeana.service.ir.image.client;

import org.springframework.beans.factory.annotation.Autowired;

import eu.europeana.service.ir.image.client.abstracts.IRImageConfiguration;
import eu.europeana.service.ir.image.client.abstracts.ImageIndexingClient;

public class ImageIndexingClientImpl implements ImageIndexingClient{

	@Autowired
	private IRImageConfiguration configuration;

	public void setConfiguration(IRImageConfiguration configuration) {
		this.configuration = configuration;
	}

	public IRImageConfiguration getConfiguration() {
		return configuration;
	} 
	
}
