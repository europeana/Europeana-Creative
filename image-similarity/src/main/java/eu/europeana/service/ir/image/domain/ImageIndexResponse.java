package eu.europeana.service.ir.image.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="assets")
public class ImageIndexResponse {
	
	@XmlElementWrapper(name="results")
	@XmlElement(name="result")
	private List<String> results;
	public List<String> getResults() {      
		return results;   
	}

}
