package eu.europeana.service.ir.image.features;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class DominatColorExtractorTest {

	@Test
	public void testDCDExtraction() throws IOException{
		DominantColorLogic extractor;
		File testImagesFolder = new File("/tmp/dcd");
		String[] fileNames = testImagesFolder.list();
		File imageFile;
		DominantColorValues values;
		for (int i = 0; i < fileNames.length; i++) {
			imageFile = new File(testImagesFolder, fileNames[i]);
			extractor = new DominantColorLogic();
			values = extractor.extractDescriptor(imageFile);
			System.out.println(imageFile);
			System.out.println(values);
		}
		
	}
}
