package eu.europeana.service.ir.image.domain;

import java.util.List;

import org.apache.log4j.Logger;

import eu.europeana.service.ir.image.web.model.json.SearchResultItem;

public class QueryResults {
	
	private Logger log = Logger.getLogger(getClass());
	//need to preserve the order of results
	private List<SearchResultItem> results;

	public QueryResults() {
	}

	public void setResults(List<SearchResultItem> results) {
		this.results = results;
	}

	public List<SearchResultItem> getResults(int startFrom, int numResults) {
		List<SearchResultItem> res = null;
		if (results != null && startFrom >= 0 && startFrom < results.size()) {
			if (numResults == -1) {
				res = results;
			} else if (numResults > 0){
				int endPos = Math.min(startFrom + numResults, results.size());
				res = results.subList(startFrom, endPos);
			}
		}
		log.debug("results from " + startFrom + " to " + (startFrom + numResults) + ": " + (res == null ? null: res.toString()));
		return res;
	}

}
