package eu.europeana.creative.dataset.design.queries;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.api.client.dataset.DatasetDescriptor;
import eu.europeana.api.client.exception.EuropeanaClientException;
import eu.europeana.api.client.myeuropeana.exception.MyEuropeanaApiException;
import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;
import eu.europeana.api.client.thumbnails.ThumbnailsAccessor;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;

public class V1QueriesTest extends ThumbnailAccessorUtils{

	//private boolean overwriteThumbnails = false;
	
	private IRConfiguration config;

	@Before
	public void init(){
		String dataset = "design_v1";
		setDataset(dataset);
	}
	
	@Test
	public void analyzeTags() throws MyEuropeanaApiException, IOException, EuropeanaClientException{
		String csvFile = "/queries/design/query_responses.csv";
		Set<String> queryResponses = readResponseIds(csvFile); 
		System.out.println("total ids in QueryResponses: " + queryResponses.size());
		
		File datasetFile =  getConfig().getDatasetFile(getDataset());
		Map<String, String> dv1Thumbnails = readThumbnailsMap(datasetFile);
		
		if(queryResponses.removeAll(dv1Thumbnails.keySet()))
			System.out.println("#ids not available in the dataset: " + queryResponses.size());
		else
			System.out.println("no query response Id is available in the dataset!");
		
		Map<String, String> missingObjects = new HashMap<String, String>(queryResponses.size());
		ThumbnailsAccessor ta = new ThumbnailsAccessor();
		String thumbnailUrl = null;
		File imageFolder = getConfig().getImageFolderAsFile(getDataset());
		for (String euId : queryResponses) {
			thumbnailUrl = ta.copyThumbnail(euId, imageFolder);
			missingObjects.put(euId, thumbnailUrl);
		}
		
		String missingItemsFile = "/tmp/queries/design/tmp_missing_items.csv";
		writeThumbnailsToCsvFile(new DatasetDescriptor("queries", getDataset()), missingObjects, new File(missingItemsFile));
		System.out.println("Missing items written to file: " + missingItemsFile);
		
		//Map<String, Integer> normalizedTags = normalizeTags(originalTags);
		//URL outFileUrl = getClass().getResource("/myeuropeana/design/statistics/");
		//File outFile = new File(outFileUrl.getFile(), "normalized_tags.csv");
		//String outFile = getCollectionsCvsFolder() CvsFolder() CvsFolder() "/myeuropeana/design/statistics/tags.csv";
		//File outFile = new File("./src/test/resources/myeuropeana/design/statistics/normalized_tags.csv");
		//writeMapToCsvFile(normalizedTags, outFile);
	
	}

	

	private Set<String> readResponseIds(String csvFile) throws IOException {
		Set<String> res = new HashSet<String>();
		
		BufferedReader reader = null;
		try {
			InputStream in = getClass().getResourceAsStream(csvFile);
			reader = new BufferedReader(new InputStreamReader(in));
			// BufferedReader reader = new BufferedReader(new
			// FileReader("/collection_07501_thumbnails.csv"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				//ignore comments
				if(line.startsWith("#"))
					continue;
				
				res.add(line);
			}
				
			
		} finally {
				if(reader != null)
					reader.close();
		}
		return res;
	}
	
//	private void writeMapToCsvFile(
//			Map<String, Integer> map, File file)
//			throws IOException {
//
//		// create parent dirs
//		file.getParentFile().mkdirs();
//		
//		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//		
//		int count = 0;
//		for (Entry<String, Integer> entry : map.entrySet()) {
//
//			writer.write(entry.getKey());
//			writer.write(";");
//			writer.write(entry.getValue().toString());
//			writer.write("\n");
//			count++;
//			if (count % 1000 == 0)
//				writer.flush();
//		}
//		writer.flush();
//		writer.close();
//	}
	
	protected IRConfiguration getConfig() {
		if(config == null)
			config = new IRConfigurationImpl();

		return config;
	}
}
