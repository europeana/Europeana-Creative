package eu.europeana.creative.geomapping;

import java.io.IOException;

import eu.europeana.api.client.Api2Query;
import eu.europeana.api.client.EuropeanaComplexQuery;
import eu.europeana.api.client.connection.EuropeanaApi2Client;
import eu.europeana.api.client.result.EuropeanaApi2Results;

public class EuropeanaSearchBean {

	public EuropeanaApi2Results getEuropeanaResults(String searchTerms, String country) throws IOException{
		 //create the query object
		Api2Query europeanaQuery = new Api2Query();
        //europeanaQuery.setCreator("picasso");
        europeanaQuery.setType(EuropeanaComplexQuery.TYPE.IMAGE);
        europeanaQuery.setGeneralTerms(searchTerms);
        europeanaQuery.setCountry(country);
        //perform search
        EuropeanaApi2Client europeanaClient = new EuropeanaApi2Client();
        EuropeanaApi2Results res = europeanaClient.searchApi2(europeanaQuery, 5, 1);
     
        return res;
	}
}
