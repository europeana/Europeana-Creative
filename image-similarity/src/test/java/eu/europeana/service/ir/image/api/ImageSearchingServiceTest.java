package eu.europeana.service.ir.image.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.api.ImageSearchingService;
import eu.europeana.service.ir.image.api.ImageSearchingServiceImpl;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;
import eu.europeana.service.ir.image.web.model.json.SearchResultItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/image-similarity-context.xml" })
public class ImageSearchingServiceTest {
	
	@Autowired
	ImageSearchingService imageSearchingService;
	
	@Autowired
	IRConfiguration configuration;
	
	@Test
	public void testSearchById() throws ImageIndexingException, IOException, ImageSearchingException{
		String euId = "/10106/2C2A2B381740CC28B01445B9256E11AF9EFCEECA";
		
		imageSearchingService.searchSimilar(euId);
		List<SearchResultItem> results = imageSearchingService.getResults(0, 5);
		
		for (SearchResultItem resultItem : results) {
			System.out.println(resultItem.getResourceId());
		}
		assertTrue(results.size() > 0);
		assertEquals(euId, results.get(0).getResourceId());
	}

	
	@Test
	public void testSearchById_TestDs() throws ImageIndexingException, IOException, ImageSearchingException{
		String euId = "/10106/2C2A2B381740CC28B01445B9256E11AF9EFCEECA";
		
		//create index searcher for test dataset
		ImageSearchingService imageSearchingServiceTest = new ImageSearchingServiceImpl("test", configuration);
		//open index
		imageSearchingServiceTest.init();
		//perform search
		imageSearchingServiceTest.searchSimilar(euId);
		List<SearchResultItem> results = imageSearchingServiceTest.getResults(0, 5);
		
		for (SearchResultItem resultItem : results) {
			System.out.println(resultItem.getResourceId());
		}
		//verify results
		assertTrue(results.size() > 0);
		assertEquals(euId, results.get(0).getResourceId());
	}

	@Test
	public void testSearchById_DemoDs() throws ImageIndexingException, IOException, ImageSearchingException{
		String euId = "/10106/2C2A2B381740CC28B01445B9256E11AF9EFCEECA";
		
		//create index searcher for test dataset
		ImageSearchingService imageSearchingServiceTest = new ImageSearchingServiceImpl("demo", configuration);
		//open index
		imageSearchingServiceTest.init();
		//perform search
		imageSearchingServiceTest.searchSimilar(euId);
		List<SearchResultItem> results = imageSearchingServiceTest.getResults(0, 5);
		
		for (SearchResultItem resultItem : results) {
			System.out.println(resultItem.getResourceId());
		}
		//verify results
		assertTrue(results.size() > 0);
		assertEquals(euId, results.get(0).getResourceId());
	}
	

	

}
