package eu.europeana.service.ir.image.web.rest;

import eu.europeana.service.ir.image.IRConfiguration;

public abstract class BaseRestService {

	
	public String getComponentName() {
		return getConfiguration().getComponentName();
	}
	
	public abstract IRConfiguration getConfiguration(); 
}
