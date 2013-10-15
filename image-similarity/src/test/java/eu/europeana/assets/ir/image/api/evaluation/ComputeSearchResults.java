package eu.europeana.assets.ir.image.api.evaluation;

import it.cnr.isti.indexer.IndexHelper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.api.ImageSearchingService;
import eu.europeana.service.ir.image.exceptions.ImageIndexingException;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/image-similarity-context.xml" })
public class ComputeSearchResults {
	
	@Autowired
	ImageSearchingService imageSearchingService;
	//image id, image object
	Map<String, EuropeanaImage> europeanaImages = new HashMap<String, EuropeanaImage>(5200);
	//queryId, resultsList
	Map<String, List<EuropeanaId>> allSearchResults = new HashMap<String, List<EuropeanaId>>(5200);
	IndexHelper helper = new IndexHelper();
	
	
	public List<EuropeanaId> searchById(String id) throws ImageIndexingException, IOException, ImageSearchingException{
		EuropeanaId euId = new EuropeanaId();
		//euId.setNewId("/07501/332954D43E8FF3E38D0AED668488886CC91CEDFB");
		euId.setNewId(id);
		
		imageSearchingService.searchSimilar(euId);
		return imageSearchingService.getResults(0, 26);
		
//		for (EuropeanaId europeanaId : results) {
//			System.out.println(europeanaId.getNewId());
//		}
		
	}
	
	
	public void loadEuropeanaImages() throws IOException{
		
		loadEuropeanaImagesFromFile("/Rijksmuseum-miniatur_90402_M_NL_Rijksmuseum.csv", "objects - decor miniaturs");
		loadEuropeanaImagesFromFile("/Rijksmuseum-portrets_90402_M_NL_Rijksmuseum.csv", "paintings - portraits" );
		loadEuropeanaImagesFromFile("/NHM-LISABON-butterfly_2023901_Ag_EU_NaturalEurope_all.csv", "insects - butterflies & mots");
		loadEuropeanaImagesFromFile("/MIMO-trompe_09102_Ag_EU_MIMO_ESE.csv", "objects - music tromps");
		loadEuropeanaImagesFromFile("/Galileo-optic_02301_Ag_IT_MG_catalogue.csv", "objects - optics");
		loadEuropeanaImagesFromFile("/Galileo-electric_02301_Ag_IT_MG_catalogue.csv", "objects - electrical engineering");
		loadEuropeanaImagesFromFile("/Teylers-eagle_10106_Ag_EU_STERNA_48.csv", "birds - eagles");
		loadEuropeanaImagesFromFile("/Teylers-woodpecker_10106_Ag_EU_STERNA_48.csv", "birds - woodpeckers");
		loadEuropeanaImagesFromFile("/Teylers-duck_10106_Ag_EU_STERNA_48.csv", "birds - ducks");
		loadEuropeanaImagesFromFile("/Teylers-parrots_10106_Ag_EU_STERNA_48.csv", "birds - parrots");
		loadEuropeanaImagesFromFile("/Rijksmuseum-porcelain_90402_M_NL_Rijksmuseum.csv", "objects - porcelain");
		loadEuropeanaImagesFromFile("/Rijksmuseum-drawing-lanscape_90402_M_NL_Rijksmuseum.csv", "drawings - landscapes");
		loadEuropeanaImagesFromFile("/Rijksmuseum-bottles_90402_M_NL_Rijksmuseum.csv", "objects - bottles");
		loadEuropeanaImagesFromFile("/Rijksmuseum-landscape_90402_M_NL_Rijksmuseum.csv", "paintings - landscapes");
		
			
		//test.insertImageObjectsFromFile();
		//test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile);
//		test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile();
//		test.insertImageObjectsFromFile();
	}
	
	public void loadEuropeanaImagesFromFile(String fileName, String category) throws IOException{
		//load ids from file
		Map<String, String> thumbnailMap = helper.getThumbnailsMap(fileName);
		//cache europeana images
		EuropeanaImage image = null;
		String[] contentClasses = category.split("-"); 
		
		for (Map.Entry<String, String> thumbnail : thumbnailMap.entrySet()) {
			image = new EuropeanaImage();
			image.setId(thumbnail.getKey());
			image.setImageUrl(thumbnail.getValue());
			
			image.setContentClass(contentClasses[0].trim());
			image.setContentSubClass(contentClasses[1].trim());
			
			europeanaImages.put(image.getId(), image);
		}
		System.out.println("total loaded images: " + europeanaImages.size());
	}
	
	@Test
	public void collectSearchResults() throws IOException, ImageIndexingException, ImageSearchingException{
		//load id from files
		loadEuropeanaImages();
		//search results for each
		List<EuropeanaId> results = null;
		for (String id : europeanaImages.keySet()) {
			results = searchById(id);
			allSearchResults.put(id,  results);
		}
		
		//write results to cvs file
		String outFile = "/SearchResultsAndCategories.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String currentLine;
		int cnt = 0;
		for (String id : allSearchResults.keySet()) {
			currentLine = buildResultsRow(id);
			writer.append(currentLine);
			cnt++;
			
			if(cnt % 1000 == 0){
				writer.flush();
				System.out.println("Results stored :" +  cnt);
			}
			
		}

		writer.close();
		
	}


	protected String buildResultsRow(String id) {
		
		EuropeanaImage currentImage;
		StringBuilder builder = new StringBuilder();
		builder.append(id).append(";");
		EuropeanaImage queryImage = europeanaImages.get(id);
		//set counters (-1 because first result is same as query)
		int hit5=-1, hit10=-1, hit15=-1, hit20=-1, hit25=-1;
		int hit5s=-1, hit10s=-1, hit15s=-1, hit20s=-1, hit25s=-1;
		int cnt = 0;	
		
		
		//get object category
		for (EuropeanaId europeanaId : allSearchResults.get(id)) {
			
			builder.append(europeanaId.getNewId()).append(";");
			//get categories
			currentImage = europeanaImages.get(europeanaId.getNewId());
			if(currentImage == null)
				continue;
			
			builder.append(currentImage.getContentClass()).append(";");
			builder.append(currentImage.getContentSubClass()).append(";");
			//check if same main class
			if(queryImage.getContentClass().equals(currentImage.getContentClass())){
				builder.append("1;");
				if(cnt < 6) hit5++;
				if(cnt < 11) hit10++;
				if(cnt < 16) hit15++;
				if(cnt < 21) hit20++;
				if(cnt < 26) hit25++;
			}else
				builder.append("0;");
			
			//check if same subclass 
			if(queryImage.getContentSubClass().equals(currentImage.getContentSubClass())){
				builder.append("1;");
				if(cnt < 6) hit5s++;
				if(cnt < 11) hit10s++;
				if(cnt < 16) hit15s++;
				if(cnt < 21) hit20s++;
				if(cnt < 26) hit25s++;
			}else
				builder.append("0;");
			
			cnt++;
		}
		builder.append(hit5).append(";");
		builder.append(hit10).append(";");
		builder.append(hit15).append(";");
		builder.append(hit20).append(";");
		builder.append(hit25).append(";");
		
		builder.append(hit5s).append(";");
		builder.append(hit10s).append(";");
		builder.append(hit15s).append(";");
		builder.append(hit20s).append(";");
		builder.append(hit25s).append(";");
		
		builder.append("\n");
		return builder.toString();
	}
}
