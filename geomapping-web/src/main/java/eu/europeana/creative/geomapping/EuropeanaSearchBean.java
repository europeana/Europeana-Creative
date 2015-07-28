package eu.europeana.creative.geomapping;

import java.io.IOException;

import eu.europeana.api.client.EuropeanaApi2Client;
import eu.europeana.api.client.exception.EuropeanaApiProblem;
import eu.europeana.api.client.model.EuropeanaApi2Results;
import eu.europeana.api.client.search.query.Api2Query;
import eu.europeana.api.client.search.query.EuropeanaComplexQuery;

public class EuropeanaSearchBean {

	public EuropeanaApi2Results getEuropeanaResults(String searchTerms, String country) throws IOException, EuropeanaApiProblem{
		 //create the query object
		Api2Query europeanaQuery = new Api2Query();
        //europeanaQuery.setCreator("picasso");
        europeanaQuery.setType("IMAGE");
        europeanaQuery.setGeneralTerms(searchTerms);
        europeanaQuery.setCountry(country);
        //perform search
        EuropeanaApi2Client europeanaClient = new EuropeanaApi2Client();
        EuropeanaApi2Results res = europeanaClient.searchApi2(europeanaQuery, 5, 1);
     
        return res;
	}
}
