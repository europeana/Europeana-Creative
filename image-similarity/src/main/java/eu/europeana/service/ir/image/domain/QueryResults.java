package eu.europeana.service.ir.image.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.europeana.corelib.tools.lookuptable.EuropeanaId;

public class QueryResults {
	
	private Logger log = Logger.getLogger(getClass());
	//need to preserve the order of results
	private List<EuropeanaId> results;

	public QueryResults() {
	}

	public void setResults(String[][] retval) {
		results = new ArrayList<EuropeanaId>(retval.length);
		for (int i = 0; i < retval.length; i++) {
			EuropeanaId europeanaId = new EuropeanaId();
			europeanaId.setNewId(retval[i][1]);
			if(europeanaId.getNewId() == null)
				break; //in the case that the search returns less than retval.length results
			
			results.add(europeanaId);
			System.out.println("add to results: " + europeanaId.getNewId());
			System.out.println("score:" + retval[i][0]);
		}
			log.debug("results from index service: " + results.toString());
	}

	public List<EuropeanaId> getResults(int startFrom, int numResults) {
		List<EuropeanaId> res = null;
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
