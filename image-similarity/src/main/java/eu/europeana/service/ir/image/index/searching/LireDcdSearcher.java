package eu.europeana.service.ir.image.index.searching;

import it.cnr.isti.melampo.index.searching.LireMP7CSearcher;
import it.cnr.isti.melampo.vir.exceptions.VIRException;

public class LireDcdSearcher extends LireMP7CSearcher {

	@Override
	public String prepareQuery(String value, String field, boolean isQueryID)
			throws VIRException {
		// TODO Auto-generated method stub
		return super.prepareQuery(value, field, isQueryID);
	}
}
