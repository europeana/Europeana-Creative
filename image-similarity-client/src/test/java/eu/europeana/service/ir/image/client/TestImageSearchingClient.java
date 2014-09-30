package eu.europeana.service.ir.image.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import eu.europeana.service.ir.image.client.exception.ImageSearchApiException;
import eu.europeana.service.ir.image.client.impl.ImageSearchingClientImpl;
import eu.europeana.service.ir.image.client.impl.result.ImageSearchResults;
import eu.europeana.service.ir.image.client.impl.result.SearchResultItem;

public class TestImageSearchingClient {

	ImageSearchingClient imageSearchingClient = new ImageSearchingClientImpl();
	String TEST_EU_ID = "/10106/2C2A2B381740CC28B01445B9256E11AF9EFCEECA";
	String TEST_IMG_URL = "http://upload.wikimedia.org/wikipedia/commons/3/39/Singing_bird_box_by_Fr%C3%A8res_Rochat,_circa_1810.jpg"; 
	
//	@Before
//	public void initSearchClient(){
//		
//	}
	
	@Test
	public void testGetComponentFromRest() {
		String componentName = imageSearchingClient.getComponentNameFromRest();
		assertEquals("image-similarity", componentName);
	}
	
	@Test
	public void testSearchById() throws ImageSearchApiException {
		
		final int rows = 13;
		ImageSearchResults results = imageSearchingClient.searchById(TEST_EU_ID, 0, rows);
		assertTrue(results.getItemsCount() > 0);
		assertTrue(results.getTotalResults() > results.getItemsCount());
		assertEquals(rows, results.getTotalResults());
		
		for (SearchResultItem result : results.getItems()) {
			System.out.println(result.getResourceId());
			System.out.println(result.getThmbUrl());
			System.out.println(result.getCachedThmbUrl());
		}
		
	}
	
	@Test
	public void testSearchByUrl() throws ImageSearchApiException, UnsupportedEncodingException {
		
		final int rows = 13;
		ImageSearchResults results = imageSearchingClient.searchByUrl(TEST_IMG_URL, 0, rows);
		assertTrue(results.getItemsCount() > 0);
		assertTrue(results.getTotalResults() > results.getItemsCount());
		assertEquals(rows, results.getTotalResults());
		
		for (SearchResultItem result : results.getItems()) {
			System.out.println(result.getResourceId());
			System.out.println(result.getThmbUrl());
			System.out.println(result.getCachedThmbUrl());
		}
		
	}
	
}
