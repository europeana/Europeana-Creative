package eu.europeana.creative.dataset.design.pivots;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.api.client.myeuropeana.exception.MyEuropeanaApiException;
import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;

public class DesignPivotSelectionTest extends ThumbnailAccessorUtils{

	//private boolean overwriteThumbnails = false;
	
	@Before
	public void init(){
		String dataset = "design";
		setDataset(dataset);
	}
	
	@Test
	public void analyzeTags() throws MyEuropeanaApiException, IOException{
		String csvFile = "/myeuropeana/design/statistics/tags.csv";
		Map<String, Integer> originalTags = readTagMap(csvFile); 
		Map<String, Integer> normalizedTags = normalizeTags(originalTags);
		//URL outFileUrl = getClass().getResource("/myeuropeana/design/statistics/");
		//File outFile = new File(outFileUrl.getFile(), "normalized_tags.csv");
		//String outFile = getCollectionsCvsFolder() CvsFolder() CvsFolder() "/myeuropeana/design/statistics/tags.csv";
		File outFile = new File("./src/test/resources/myeuropeana/design/statistics/normalized_tags.csv");
		writeMapToCsvFile(normalizedTags, outFile);
		
//		ThumbnailFromTagsResponseAccessor ta = new ThumbnailFromTagsResponseAccessor();
//		File datasetFile = getDataSetFile(false);
//		if(datasetFile.exists())
//			datasetFile.delete();
//		
//		Map<String, Map<String, String>> thumbnailsByTag = ta.getThumbnailsFromTagsApiResponse(apiResponse);
//		DatasetDescriptor descriptor;
//		
//		int objectCount = 0; 
//		for (Map.Entry<String, Map<String, String>> entry : thumbnailsByTag.entrySet()) {
//			descriptor = new DatasetDescriptor(getDataset(), entry.getKey());
//			descriptor.setClassifications(new String[]{entry.getKey()});
//			writeThumbnailsToCsvFile(descriptor, entry.getValue(), datasetFile, true);
//			objectCount += entry.getValue().size();
//			System.out.println("TAG: " + entry.getKey() + " - " + entry.getValue().size());
//		}	
//		
//		assertEquals(apiResponse.getTotalResults(), objectCount);
	}

	private Map<String, Integer> normalizeTags(Map<String, Integer> originalTags) {
		Map<String, Integer> res = new HashMap<String, Integer>();
		String[] tags = null;
		String tag = null;
		for (Map.Entry<String, Integer> element : originalTags.entrySet()) {
			tags = element.getKey().split(" ");
			for (int i = 0; i < tags.length; i++) {
				tag = tags[i];
				//create map entry
				if(!res.containsKey(tag))
					res.put(tag, 0);
				//update map entry
				res.put(tag, (res.get(tag) + element.getValue()));
			}
		}
		return res;
	}

	private Map<String, Integer> readTagMap(String csvFile) throws IOException {
		Map<String, Integer> res = new HashMap<String, Integer>();
		
		BufferedReader reader = null;
		try {
			InputStream in = getClass().getResourceAsStream(csvFile);
			reader = new BufferedReader(new InputStreamReader(in));
			// BufferedReader reader = new BufferedReader(new
			// FileReader("/collection_07501_thumbnails.csv"));
			String line = null;
			String[] tagCount = null;
			while ((line = reader.readLine()) != null) {
				//ignore comments
				if(line.startsWith("#"))
					continue;
				
				tagCount = line.split(";");
				res.put(tagCount[0], Integer.valueOf(tagCount[1]));
			}
				
			
		} finally {
				if(reader != null)
					reader.close();
		}
		return res;
	}
	
	private void writeMapToCsvFile(
			Map<String, Integer> map, File file)
			throws IOException {

		// create parent dirs
		file.getParentFile().mkdirs();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		int count = 0;
		for (Entry<String, Integer> entry : map.entrySet()) {

			writer.write(entry.getKey());
			writer.write(";");
			writer.write(entry.getValue().toString());
			writer.write("\n");
			count++;
			if (count % 1000 == 0)
				writer.flush();
		}
		writer.flush();
		writer.close();
	}

}
