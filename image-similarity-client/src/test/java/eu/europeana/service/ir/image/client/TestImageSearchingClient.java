package eu.europeana.service.ir.image.client;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.service.ir.image.client.abstracts.ImageSearchingClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/image-similarity-client-context.xml" })
public class TestImageSearchingClient {

	@Autowired
	ImageSearchingClient imageSearchingClient;
	
	@Test
	public void testGetComponentFromRest(){
		String componentName = imageSearchingClient.getComponentNameFromRest();
		assertEquals("ir-image", componentName);
	}
}


