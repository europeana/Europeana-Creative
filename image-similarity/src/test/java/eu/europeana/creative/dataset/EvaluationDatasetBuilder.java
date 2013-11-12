package eu.europeana.creative.dataset;

import static org.junit.Assert.assertEquals;
import it.cnr.isti.indexer.IndexHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.europeana.api.client.thumbnails.ThumbnailsAccessor;
import eu.europeana.api.client.thumbnails.ThumbnailsForCollectionAccessorTest;
import eu.europeana.corelib.tools.lookuptable.EuropeanaId;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.ImageSearchingService;
import eu.europeana.service.ir.image.api.ImageSearchingServiceImpl;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;

public class EvaluationDatasetBuilder extends
		ThumbnailsForCollectionAccessorTest implements IRTestConfigurations {

	public static String CLASS_PAINTINGS = "paintings";
	public static String CLASS_OBJECTS = "objects";
	public static String CLASS_DRAWINGS = "drawings";
	public static String CLASS_BIRDS = "birds";
	public static String CLASS_INSECTS = "insects";
	public static String CLASS_BUILDINGS = "buildings";

	public static String SUB_CLASS_PORTRAINTS = "portraits";
	public static String SUB_CLASS_DECOR_MINIATURS = "decor miniaturs";
	public static String SUB_CLASS_LANDSCAPES = "landscapes";
	public static String SUB_CLASS_BOTTLES = "bottles";
	public static String SUB_CLASS_PORCELAIN = "porcelain";
	public static String SUB_CLASS_PARROTS = "parrots";
	public static String SUB_CLASS_DUCKS = "ducks";
	public static String SUB_CLASS_WOODPECKERS = "woodpeckers";
	public static String SUB_CLASS_HAWKS_EAGLES = "hawks and eagles";
	public static String SUB_CLASS_ELECTRICS = "electrical engineering";
	public static String SUB_CLASS_OPTICS = "optical engineering";
	public static String SUB_CLASS_BUTTERFLIES = "butterflies";
	public static String SUB_CLASS_ICONS = "icons";
	public static String SUB_CLASS_MURALPAINTINGS = "mural paintings";
	public static String SUB_CLASS_CIVILS = "civils";
	public static String SUB_CLASS_PEASANT_HOUSES = "peasent houses";
	public static String SUB_CLASS_INTERIORS = "interiors";
	public static String SUB_CLASS_CHURCHES = "churches";
	public static String SUB_CLASS_TRUMPETS = "musical trumpets";
	public static String SUB_CLASS_CLOCK_TOWERS = "clock towers";
	
	final String DATASET_DEMO = "demo";
	private String dataset = null;
	private ImageSearchingService imageSearchingService;

	//@Test
	public void createDemoDataset() throws IOException {
		
		setDataset(DATASET_DEMO);
		
		int objects0 = buildImageSet("Rijksmuseum-portrets",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_PAINTINGS,
						SUB_CLASS_PORTRAINTS }, "portret", "schilderij");
		assertEquals(1243, objects0);

		int objects = buildImageSet("Rijksmuseum-miniatuur",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_OBJECTS,
						SUB_CLASS_DECOR_MINIATURS }, "miniatuur beeld", null);
		assertEquals(68, objects);

		int objects1 = buildImageSet("Rijksmuseum-landschap",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_PAINTINGS,
						SUB_CLASS_LANDSCAPES }, "landschap", "schilderij");
		assertEquals(424, objects1);

		int objects2 = buildImageSet("Rijksmuseum-fles",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_OBJECTS,
						SUB_CLASS_BOTTLES }, null, "fles");
		assertEquals(139, objects2);

		int objects3 = buildImageSet("Rijksmuseum-drawing-lanschap",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_DRAWINGS,
						SUB_CLASS_LANDSCAPES }, "landschap", "tekening");
		assertEquals(701, objects3);

		int objects4 = buildImageSet("Rijksmuseum-porselein",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_OBJECTS,
						SUB_CLASS_PORCELAIN }, "Hollands porselein", null);
		assertEquals(145, objects4);

		int objects5 = buildImageSet("Teylers-parrot", "10106_Ag_EU_STERNA_48",
				new String[] { CLASS_BIRDS, SUB_CLASS_PARROTS }, "parrot", null);
		assertEquals(105, objects5);

		int objects6 = buildImageSet("Teylers-duck", "10106_Ag_EU_STERNA_48",
				new String[] { CLASS_BIRDS, SUB_CLASS_DUCKS }, "duck", null);
		assertEquals(120, objects6);

		int objects7 = buildImageSet("Teylers-woodpecker",
				"10106_Ag_EU_STERNA_48", new String[] { CLASS_BIRDS,
						SUB_CLASS_WOODPECKERS }, "woodpecker", null);
		assertEquals(210, objects7);

		int objects8 = buildImageSet("Teylers-falco", "10106_Ag_EU_STERNA_48",
				new String[] { CLASS_BIRDS, SUB_CLASS_HAWKS_EAGLES }, "falco",
				null);
		assertEquals(146, objects8);

		int objects9 = buildImageSet("Galileo-elettrica",
				"02301_Ag_IT_MG_catalogue", new String[] { CLASS_OBJECTS,
						SUB_CLASS_ELECTRICS }, "ingegneria elettrica", null);
		assertEquals(231, objects9);

		int objects10 = buildImageSet("Galileo-optics",
				"02301_Ag_IT_MG_catalogue", new String[] { CLASS_OBJECTS,
						SUB_CLASS_OPTICS }, "optics", null, "IMAGE");
		assertEquals(195, objects10);

		int objects11 = buildImageSet("MIMO-trompe", "09102_Ag_EU_MIMO_ESE",
				new String[] { CLASS_OBJECTS, SUB_CLASS_TRUMPETS }, "trompe",
				null);
		assertEquals(1194, objects11);

		int objects12 = buildImageSet("NHM-LISABON-butterflies",
				"2023901_Ag_EU_NaturalEurope_all", new String[] {
						CLASS_INSECTS, SUB_CLASS_BUTTERFLIES }, "butterflies",
				null, "IMAGE");
		assertEquals(376, objects12);

		int objects13 = buildImageSet("Athena-icon",
				"08515_Ag_EU_ATHENA_ChouvashiaStateArtMuseum",
				// "2023901_Ag_EU_NaturalEurope_all",
				new String[] { CLASS_PAINTINGS, SUB_CLASS_ICONS }, null, // "butterflies",
				null, // what
				"Неизвестный иконописец", // who
				null);
		assertEquals(117, objects13);

		int objects14 = buildImageSet("Athena-icon",
				"08559_Ag_EU_ATHENA_The_State_Tretyakov_Gallery",
				// "2023901_Ag_EU_NaturalEurope_all",
				new String[] { CLASS_PAINTINGS, SUB_CLASS_ICONS }, null, // "butterflies",
				null, // what
				"Неизвестный иконописец", // who
				null);
		assertEquals(33, objects14);

		int objects15 = buildImageSet("Cimec-icoana", "05812_L_RO_CIMEC_ese", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_PAINTINGS, SUB_CLASS_ICONS},
				"icoana", // "butterflies",
				null, // what
				null, // who
				null, "cIMeC - Institutul de Memorie Culturală");
		assertEquals(244, objects15);

		int objects16 = buildImageSet("Cimec-icoana", "05811_L_RO_CIMEC_ese", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_PAINTINGS, SUB_CLASS_ICONS},
				"icoana", // "butterflies",
				null, // what
				null, // who
				null, "cIMeC - Institutul de Memorie Culturală");
		assertEquals(57, objects16);

		int objects17 = buildImageSet("Tel-muralpainting",
				"9200170_Ag_EU_TEL_a1019d_EU_Libraries_Vienna", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_PAINTINGS, SUB_CLASS_MURALPAINTINGS},
				"church", // "butterflies",
				"mural paintings", // what
				null, // who
				null);// object type
		assertEquals(1088, objects17);

		int objects18 = buildImageSet("Romania-building",
				"2022404_Ag_RO_Elocal_clujulin", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_CIVILS},
				null, // "butterflies",
				"building", // what
				null, // who
				null);// object type
		assertEquals(134, objects18);

		int objects19 = buildImageSet(
				"Romania-building",
				"08511_Ag_EU_ATHENA_InstituteforCulturalMemory*", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_PEASANT_HOUSES},
				null, // "butterflies",
				"building", // what
				null, // who
				null, null, // provider
				new String[] { "NOT gips", "NOT capitel" });// object type
		assertEquals(192, objects19);

		int objects20 = buildImageSet("Judaica-spicetower",
				"09307_Ag_EU_Judaica_Jewish_Museum_London", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_OBJECTS, SUB_CLASS_DECOR_MINIATURS},
				"\"spice tower\"", // "butterflies",
				null, // what
				null, // who
				null);// object type
		assertEquals(121, objects20);

		int objects21 = buildImageSet("CultureGrid-clocktower", "2022*", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_CLOCK_TOWERS},
				null, // "butterflies",
				"\"clock tower\"", // what
				null, // who
				"IMAGE", "CultureGrid", new String[] { "NOT bridge",
						"NOT square" });// object type
		assertEquals(296, objects21);

		int objects22 = buildImageSet("TEL-palace",
				"92037_Ag_EU_TEL_a0444_BritishLibrary", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_INTERIORS},
				null, // "butterflies",
				"palace", // what
				null, // who
				null, null, new String[] { "room", "interior", "NOT Mary",
						"NOT \"Veliko Tarnovo\"" });// object type
		assertEquals(36, objects22);

		int objects23 = buildImageSet("TEL-palace", "2022*", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_INTERIORS},
				null, // "butterflies",
				"palace", // what
				null, // who
				null, null, new String[] { "room", "interior", "NOT Mary",
						"NOT \"Veliko Tarnovo\"" });// object type
		assertEquals(95, objects23);

		int objects24 = buildImageSet("EU_LOCAL_Durhamcathedral",
				"2022316_Ag_UK_ELocal_DurhamCountyCouncil", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_CHURCHES},
				null, // "butterflies",
				"\"Durham Cathedral\"", // what
				null, // who
				"IMAGE", null, new String[] { "NOT interior", "NOT Cloister",
						"NOT Sanctuary", "NOT roof", "NOT exhibit",
						"NOT \"vibration tests\"" });// object type
		assertEquals(154, objects24);

		int objects25 = buildImageSet("OpenUp-butterfly",
				"11617_Ag_EU_OpenUp*", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_INSECTS, SUB_CLASS_BUTTERFLIES},
				400, 500, 
				"\"Zoological collections of the University of Tartu\"", // "butterflies",
				null, // what
				null, // who
				null, null, null);
		assertEquals(500, objects25);
		
		performDatasetAggregation();

	}

	private void performDatasetAggregation() throws IOException {
		File cvsFolder = new File(getCollectionsCvsFolder());
		File[] collectionFiles = cvsFolder.listFiles();
		BufferedReader reader = null;
		//String headerLine = null;
		String line = null;
		BufferedWriter datasetWriter = getDataSetFileWriter(false);
		
		
		for (int i = 0; i < collectionFiles.length; i++) {
			reader = new BufferedReader(new FileReader(collectionFiles[i]));
			boolean firstLine = true;
			while ((line = reader.readLine()) != null) {
				//write headers to sysout
				if(firstLine){
					System.out.println(line);
					firstLine = false;
				}
				//write all data to dataset
				datasetWriter.write(line);
				datasetWriter.write("\n");

			}
			datasetWriter.flush();
			//close reader
			try{
				reader.close();
			}catch(IOException e){
				System.out.println("cannot close reader for: " + collectionFiles[i]);
				e.printStackTrace();
			}
		}
		datasetWriter.close();
	}

	private BufferedWriter getDataSetFileWriter(boolean urls) throws IOException {
		File datasetFile = getDataSetFile(urls);
		datasetFile.getParentFile().mkdirs();
		
		return new BufferedWriter(new FileWriter(datasetFile));  
	}
		
	private File getDataSetFile(boolean urls) {
		IRConfiguration config = getConfig();
		if(urls)
			return config.getDatasetUrlsFile(getDataset());
		else
			return config.getDatasetFile(getDataset());		
	}

	
	public void testGetThumbnailsForCollectionLimit() {
		// avoid execution
	}

	public void testGetThumbnailsForCollectionAll() {
		// avoid execution
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	
	//@Test
	public void downloadThumbnails() throws FileNotFoundException, IOException{
		
		setDataset(DATASET_DEMO);
		IRConfiguration config = getConfig();
		File datasetFile = config.getDatasetFile(DATASET_DEMO);
		
		IndexHelper ixHelper = new IndexHelper();
		Map<String, String> thumbnailsMap = ixHelper.getThumbnailsMap(datasetFile);
		
		
		
		ThumbnailsAccessor ta = new ThumbnailsAccessor();
		List<String> skippedItems = ta.copyThumbnails(thumbnailsMap, config.getIndexImagesFolder(DATASET_DEMO));
		
		System.out.println("Skipped items: " + skippedItems.size());
		for (String itemId : skippedItems) {
			System.out.println(itemId);
		}
	}

	protected IRConfiguration getConfig() {
		IRConfiguration config = new IRConfigurationImpl();
		return config;
	}
	
	@Test
	public void buildIndexedUrlsFile() throws FileNotFoundException, IOException, ImageSearchingException{
		
		setDataset(DATASET_DEMO);
		IRConfiguration config = getConfig();
		File datasetFile = config.getDatasetFile(getDataset());
		
		IndexHelper ixHelper = new IndexHelper();
		Map<String, String> thumbnailsMap = ixHelper.getThumbnailsMap(datasetFile);
		BufferedWriter indexedUrlsWriter = getDataSetFileWriter(true);
		EuropeanaId euId = new EuropeanaId();
		int counter = 0;
		
		for (Map.Entry<String, String> thumbnail : thumbnailsMap.entrySet()) {
			
			
			euId.setNewId(thumbnail.getKey());
			try{
				getImageSearchingService().searchSimilar(euId);
			
				if(getImageSearchingService().getTotalResults() > 0){
					//write to file
					indexedUrlsWriter.append(thumbnail.getKey()).append("; ");
					indexedUrlsWriter.append(thumbnail.getValue()).append("\n");
					counter++;
				}else{
					//not indexed yet
					System.out.println("Skipped item: " + euId.getNewId());
				}
				
			}catch(ImageSearchingException e){
				System.out.println(e.getMessage());
			}
		}
		
		System.out.println("correct items: " + counter);
	}
	
	public ImageSearchingService getImageSearchingService() {
		if (imageSearchingService == null){
			imageSearchingService = new ImageSearchingServiceImpl(getDataset(),
					getConfig());
			imageSearchingService.init();
		}
		return imageSearchingService;
	}

	@Override
	public String getCollectionsCvsFolder(String dataset) {
		return COLLECTIONS_FOLDER + dataset +"/";
	}
	
	@Override
	protected String getCollectionsCvsFolder() {
		return getCollectionsCvsFolder(getDataset());
	}

}
