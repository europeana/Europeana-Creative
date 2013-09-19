package eu.europeana.service.ir.image.web.model.json;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import eu.europeana.api2.web.model.json.abstracts.AbstractSearchResults;

/**
 * @author Sergiu Gordea <sergiu.gordea_at_ait.ac.at>
 *
 * @param <T>
 */
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class ImageSimilaritySearchResults<T> extends AbstractSearchResults<T> {

	public void setSearchResults(List<T> results){
		this.items = results;
		if(items != null)
			this.itemsCount = items.size();
	}
	
	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;		
	}

	public ImageSimilaritySearchResults(String apikey, String action){
		super(apikey, action);
	}
	
	
}
