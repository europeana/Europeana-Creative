package eu.europeana.service.ir.image.api;

import eu.europeana.service.ir.image.IRConfiguration;

public class SmkImageSearchingServiceImpl extends ImageSearchingServiceImpl {

	public SmkImageSearchingServiceImpl(String dataset,
			IRConfiguration configuration) {
		super(dataset, configuration);
	}
	
	public SmkImageSearchingServiceImpl(
			IRConfiguration configuration) {
		super(configuration);
	}
	
	
	
}
