package eu.europeana.service.ir.image.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.exceptions.CommonRetrievalException;

public class QueryResultsWS {
	
	private Logger log = Logger.getLogger(getClass());

	private List<EuropeanaId> results;
	private Unmarshaller mc;

	public QueryResultsWS() {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(ImageIndexResponse.class);
			mc = jc.createUnmarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setResults(String indexServiceResponse) throws IOException,
			JAXBException, CommonRetrievalException {
		InputStream is = null;
		InputStreamReader reader = null;
		results = null;
		try {
			is = new ByteArrayInputStream(indexServiceResponse.getBytes("UTF-8"));
			reader = new InputStreamReader(is);
			ImageIndexResponse res = (ImageIndexResponse) mc.unmarshal(reader);
			results = new ArrayList<EuropeanaId>();
			for (int i = 0; i < res.getResults().size(); i++) {
				EuropeanaId europeanaId = new EuropeanaId();
				//the image index will store the new version of the generated ids
				europeanaId.setNewId(res.getResults().get(i));
				results.add(europeanaId);
			}
			log.debug("results from index service: " + results.toString());
		} finally {
			if (is != null)
				is.close();
			if (reader != null)
				reader.close();
		}
	}

	public List<EuropeanaId> getResults(int startFrom, int numResults) {
		List<EuropeanaId> res = null;
		if (results != null && startFrom >= 0 && startFrom < results.size()) {
			if (numResults == -1) {
				res = results;
			} else if (numResults > 0){
				res = results.subList(startFrom, Math.min(startFrom + numResults, results.size()));
			}
		}
		log.debug("results from " + startFrom + " to " + (startFrom + numResults) + ": " + (res == null ? null: res.toString()));
		return res;
	}

}
