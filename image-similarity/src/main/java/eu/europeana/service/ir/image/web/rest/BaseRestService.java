package eu.europeana.service.ir.image.web.rest;

import eu.europeana.service.ir.image.BaseConfiguration;

public abstract class BaseRestService {

	
	public String getComponentName() {
		return getConfiguration().getComponentName();
	}
	
	public abstract BaseConfiguration getConfiguration(); 
}
