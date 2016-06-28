package eu.europeana.creative.dataset.smk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.creative.dataset.BaseCreativeDatasetUtil;
import eu.europeana.service.ir.image.api.ImageSearchingService;
import eu.europeana.service.ir.image.api.SmkImageSearchingServiceImpl;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;
import eu.europeana.service.ir.image.web.model.json.SearchResultItem;

public class SmkImageSearchingServiceTest extends BaseCreativeDatasetUtil{
	
	SmkImageSearchingServiceImpl imageSearchingService;
	
	@Before
	public void init(){
		String dataset = "smk";
		setDataset(dataset);
	}
	
	@Test
	public void testSearchById() throws ImageIndexingException, IOException, ImageSearchingException{
		
		String euId = "/05811/3975303D_9B98_47A3_9999_7BBAC8660137";
		
		getImageSearchingService().searchSimilar(euId);
		List<SearchResultItem> results = getImageSearchingService().getResults(0, 5);
		
		for (SearchResultItem resultItem : results) {
			System.out.println(resultItem.getResourceId());
		}
		assertTrue(results.size() > 0);
		assertEquals(euId, results.get(0).getResourceId());
	}
	
	@Test
	public void testSearchByUrl() throws ImageIndexingException, IOException, ImageSearchingException{
		
		URL url = new URL("http://image-similarity.ait.ac.at/culturecam-web/datasets/culturecam/image/05811/3975303D_9B98_47A3_9999_7BBAC8660137.jpg");
		String euId = "/05811/3975303D_9B98_47A3_9999_7BBAC8660137";
		//File imageFile = getConfig().getImageFile(getDataset(), euId);
		
		getImageSearchingService().searchSimilar(url,null);
		List<SearchResultItem> results = getImageSearchingService().getResults(0, 5);
		
		for (SearchResultItem resultItem : results) {
			System.out.println(resultItem.getResourceId());
		}
		assertTrue(results.size() > 0);
		assertEquals(euId, results.get(0).getResourceId());
	}
	
	@Test
	public void testSearchByIdDC() throws ImageIndexingException, IOException, ImageSearchingException{
		
		String euId = "/05811/3975303D_9B98_47A3_9999_7BBAC8660137";
		
		getImageSearchingService().searchSimilar(euId, ImageSearchingService.QUERY_TYPE_DC);
		List<SearchResultItem> results = getImageSearchingService().getResults(0, 5);
		
		for (SearchResultItem resultItem : results) {
			System.out.println(resultItem.getResourceId());
		}
		assertTrue(results.size() > 0);
		assertEquals(euId, results.get(0).getResourceId());
	}
	
	@Test
	public void testSearchByUrlDC() throws ImageIndexingException, IOException, ImageSearchingException{
		
		URL url = new URL("http://image-similarity.ait.ac.at/culturecam-web/datasets/culturecam/image/05811/3975303D_9B98_47A3_9999_7BBAC8660137.jpg");
		String euId = "/05811/3975303D_9B98_47A3_9999_7BBAC8660137";
		//File imageFile = getConfig().getImageFile(getDataset(), euId);
		
		getImageSearchingService().searchSimilar(url, ImageSearchingService.QUERY_TYPE_DC);
		List<SearchResultItem> results = getImageSearchingService().getResults(0, 5);
		
		for (SearchResultItem resultItem : results) {
			System.out.println(resultItem.getResourceId());
		}
		assertTrue(results.size() > 0);
		assertEquals(euId, results.get(0).getResourceId());
	}

	protected SmkImageSearchingServiceImpl getImageSearchingService() {
		//create index searcher for test dataset
		if(imageSearchingService == null){
			imageSearchingService = new SmkImageSearchingServiceImpl(getDataset(), getConfig());
			//open index
			imageSearchingService.init();
		}
		return imageSearchingService;
	}

}
