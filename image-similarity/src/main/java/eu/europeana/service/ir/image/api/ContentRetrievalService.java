package eu.europeana.service.ir.image.api;

import eu.europeana.service.ir.image.IRConfiguration;

public interface ContentRetrievalService {

	public IRConfiguration getConfiguration();
	
	public void init();
}
