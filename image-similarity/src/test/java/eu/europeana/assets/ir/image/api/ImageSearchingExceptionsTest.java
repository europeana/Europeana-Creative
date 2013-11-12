package eu.europeana.assets.ir.image.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import it.cnr.isti.melampo.vir.exceptions.VIRException;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.ImageSearchingService;
import eu.europeana.service.ir.image.api.ImageSearchingServiceImpl;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;

public class ImageSearchingExceptionsTest {

	@Test
	public void testSearchByWrongId_DemoDs() throws ImageIndexingException,
			IOException, ImageSearchingException {
		EuropeanaId euId = new EuropeanaId();
		euId.setNewId("/02301/urn_imss_image_017597");

		IRConfiguration configuration = new IRConfigurationImpl();

		// create index searcher for test dataset
		ImageSearchingService imageSearchingServiceTest = new ImageSearchingServiceImpl(
				"demo", configuration);
		// open index
		imageSearchingServiceTest.init();
		// perform search
		try {
			imageSearchingServiceTest.searchSimilar(euId);
			// ensure that an exception is thrown
			assertTrue(false);
		} catch (Exception e) {
			assertEquals(VIRException.MESSAGE_WRONG_ID, e.getCause()
					.getMessage());
		}

		try {
			imageSearchingServiceTest.getTotalResults();
			// ensure that an exception is thrown
			assertTrue(false);
		} catch (Exception e) {
			assertEquals(ImageSearchingException.MESSAGE_NO_RESULTS, e.getMessage());
		}

		List<EuropeanaId> results = imageSearchingServiceTest.getResults(0, 1);
		assertEquals(null, results);

	}

}
