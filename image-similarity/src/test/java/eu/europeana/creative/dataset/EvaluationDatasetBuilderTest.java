package eu.europeana.creative.dataset;

import static org.junit.Assert.assertEquals;
import it.cnr.isti.indexer.IndexHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.europeana.api.client.dataset.DatasetDescriptor;
import eu.europeana.api.client.search.query.Api2QueryInterface;
import eu.europeana.api.client.thumbnails.ThumbnailsAccessor;
import eu.europeana.api.client.thumbnails.ThumbnailsForCollectionAccessorTest;
import eu.europeana.service.ir.image.IRConfiguration;
import eu.europeana.service.ir.image.IRConfigurationImpl;
import eu.europeana.service.ir.image.api.ImageSearchingService;
import eu.europeana.service.ir.image.api.ImageSearchingServiceImpl;
import eu.europeana.service.ir.image.exceptions.ImageSearchingException;

public class EvaluationDatasetBuilderTest extends
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

	// @Test
	public void createDemoDataset() throws IOException {
		
		setDataset(DATASET_DEMO);
		
		//0
		DatasetDescriptor dataset = new DatasetDescriptor("Rijksmuseum-portrets",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_PAINTINGS,
				SUB_CLASS_PORTRAINTS });
		Api2QueryInterface query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "portret", "schilderij");
		
		int objects0 = buildImageSet(dataset, query);
		assertEquals(1243, objects0);

		//
		dataset = new DatasetDescriptor("Rijksmuseum-miniatuur",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_OBJECTS,
				SUB_CLASS_DECOR_MINIATURS });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "miniatuur beeld", null);
		
		int objects = buildImageSet(dataset, query);
		assertEquals(68, objects);

		//1
		dataset = new DatasetDescriptor("Rijksmuseum-landschap",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_PAINTINGS,
				SUB_CLASS_LANDSCAPES });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "landschap", "schilderij");
		int objects1 = buildImageSet(dataset, query);
		assertEquals(424, objects1);

		//2
		dataset = new DatasetDescriptor("Rijksmuseum-fles",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_OBJECTS,
				SUB_CLASS_BOTTLES });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), null, "fles");
		int objects2 = buildImageSet(dataset, query);
		assertEquals(139, objects2);

		//3
		dataset = new DatasetDescriptor("Rijksmuseum-drawing-lanschap",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_DRAWINGS,
				SUB_CLASS_LANDSCAPES });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "landschap", "tekening");
		int objects3 = buildImageSet(dataset, query);
		assertEquals(701, objects3);

		//4
		dataset = new DatasetDescriptor("Rijksmuseum-porselein",
				"90402_M_NL_Rijksmuseum", new String[] { CLASS_OBJECTS,
				SUB_CLASS_PORCELAIN });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "Hollands porselein", null);
		int objects4 = buildImageSet(dataset, query);
		assertEquals(145, objects4);

		//5
		dataset = new DatasetDescriptor("Teylers-parrot", "10106_Ag_EU_STERNA_48",
				new String[] { CLASS_BIRDS, SUB_CLASS_PARROTS });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "parrot", null);
		
		int objects5 = buildImageSet(dataset, query);
		assertEquals(105, objects5);

		//6
		dataset = new DatasetDescriptor("Teylers-duck", "10106_Ag_EU_STERNA_48",
				new String[] { CLASS_BIRDS, SUB_CLASS_DUCKS });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "duck", null);
		
		int objects6 = buildImageSet(dataset, query);
		assertEquals(120, objects6);

		//7
		dataset = new DatasetDescriptor("Teylers-woodpecker",
				"10106_Ag_EU_STERNA_48", new String[] { CLASS_BIRDS,
				SUB_CLASS_WOODPECKERS });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "woodpecker", null);
		
		int objects7 = buildImageSet(dataset, query);
		assertEquals(210, objects7);

		//8
		dataset = new DatasetDescriptor("Teylers-falco", "10106_Ag_EU_STERNA_48",
				new String[] { CLASS_BIRDS, SUB_CLASS_HAWKS_EAGLES });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "falco",
				null);
		
		int objects8 = buildImageSet(dataset, query);
		assertEquals(146, objects8);

		//9
		dataset = new DatasetDescriptor("Galileo-elettrica",
				"02301_Ag_IT_MG_catalogue", new String[] { CLASS_OBJECTS,
				SUB_CLASS_ELECTRICS });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "ingegneria elettrica", null);
		int objects9 = buildImageSet(dataset, query);
		assertEquals(231, objects9);

		//10
		dataset = new DatasetDescriptor("Galileo-optics",
				"02301_Ag_IT_MG_catalogue", new String[] { CLASS_OBJECTS,
				SUB_CLASS_OPTICS });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "optics", null, "IMAGE");
		
		int objects10 = buildImageSet(dataset, query);
		assertEquals(195, objects10);

		//11
		dataset = new DatasetDescriptor("MIMO-trompe", "09102_Ag_EU_MIMO_ESE",
				new String[] { CLASS_OBJECTS, SUB_CLASS_TRUMPETS });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "trompe", null);
		int objects11 = buildImageSet(dataset, query);
		assertEquals(1194, objects11);

		//12
		dataset = new DatasetDescriptor("NHM-LISABON-butterflies",
				"2023901_Ag_EU_NaturalEurope_all", new String[] {
				CLASS_INSECTS, SUB_CLASS_BUTTERFLIES });
		
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "butterflies", null, "IMAGE");
		int objects12 = buildImageSet(dataset, query);
		assertEquals(376, objects12);

		//13
		dataset = new DatasetDescriptor("Athena-icon",
				"08515_Ag_EU_ATHENA_ChouvashiaStateArtMuseum",
				// "2023901_Ag_EU_NaturalEurope_all",
				new String[] { CLASS_PAINTINGS, SUB_CLASS_ICONS });
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), null, // "butterflies",
				null, // what
				"Неизвестный иконописец", // who
				null);
		int objects13 = buildImageSet(dataset, query);
		assertEquals(117, objects13);

		//14
		dataset = new DatasetDescriptor("Athena-icon",
				"08559_Ag_EU_ATHENA_The_State_Tretyakov_Gallery",
				// "2023901_Ag_EU_NaturalEurope_all",
				new String[] { CLASS_PAINTINGS, SUB_CLASS_ICONS });
		
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), null, // "butterflies",
				null, // what
				"Неизвестный иконописец", // who
				null);
		int objects14 = buildImageSet(dataset, query);
		assertEquals(33, objects14);

		//15
		dataset = new DatasetDescriptor("Cimec-icoana", "05812_L_RO_CIMEC_ese", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_PAINTINGS, SUB_CLASS_ICONS});
		
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), 
				"icoana", // "butterflies",
				null, // what
				null, // who
				null, "cIMeC - Institutul de Memorie Culturală");
		int objects15 = buildImageSet(dataset, query);
		assertEquals(244, objects15);

		//16
		dataset = new DatasetDescriptor("Cimec-icoana", "05811_L_RO_CIMEC_ese", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_PAINTINGS, SUB_CLASS_ICONS});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), 
				"icoana", // "butterflies",
				null, // what
				null, // who
				null, "cIMeC - Institutul de Memorie Culturală"); 
		int objects16 = buildImageSet(dataset, query);
		assertEquals(57, objects16);

		//17
		dataset = new DatasetDescriptor("Tel-muralpainting",
				"9200170_Ag_EU_TEL_a1019d_EU_Libraries_Vienna", 
				new String[]{CLASS_PAINTINGS, SUB_CLASS_MURALPAINTINGS});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), 
				"church", // "butterflies",
				"mural paintings");
				
		int objects17 = buildImageSet(dataset, query);
		assertEquals(1088, objects17);

		//18
		dataset = new DatasetDescriptor("Romania-building",
				"2022404_Ag_RO_Elocal_clujulin", 
				new String[]{CLASS_BUILDINGS, SUB_CLASS_CIVILS});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(),null, 
				"building");
				
		int objects18 = buildImageSet(dataset, query);// object type
		assertEquals(134, objects18);

		//19
		dataset = new DatasetDescriptor("Romania-building",
				"08511_Ag_EU_ATHENA_InstituteforCulturalMemory*", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_PEASANT_HOUSES});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), null, // "butterflies",
				"building", // what
				null, // who
				null, null, null, // provider
				new String[] { "NOT gips", "NOT capitel" });
		
		int objects19 = buildImageSet(dataset, query);
		assertEquals(192, objects19);

		//20
		dataset = new DatasetDescriptor("Judaica-spicetower",
				"09307_Ag_EU_Judaica_Jewish_Museum_London", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_OBJECTS, SUB_CLASS_DECOR_MINIATURS});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), "\"spice tower\"", null); 
				
		int objects20 = buildImageSet(dataset, query);
		assertEquals(121, objects20);

		//21
		dataset = new DatasetDescriptor("CultureGrid-clocktower", "2022*", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_CLOCK_TOWERS});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(),
				null, "\"clock tower\"", null,	"IMAGE", "CultureGrid", null, 
				new String[] { "NOT bridge","NOT square" });
				
		int objects21 = buildImageSet(dataset, query);// object type
		assertEquals(296, objects21);

		//22
		dataset = new DatasetDescriptor("TEL-palace",
				"92037_Ag_EU_TEL_a0444_BritishLibrary", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_INTERIORS});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(),
				null, // "butterflies",
				"palace", // what
				null, // who
				null, null, null, new String[] { "room", "interior", "NOT Mary",
						"NOT \"Veliko Tarnovo\"" });
				
		int objects22 = buildImageSet(dataset, query);
		assertEquals(36, objects22);

		//23
		dataset = new DatasetDescriptor("TEL-palace", "2022*", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_INTERIORS});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(),
				null, // "butterflies",
				"palace", // what
				null, // who
				null, null, null, new String[] { "room", "interior", "NOT Mary",
						"NOT \"Veliko Tarnovo\"" });
		
		int objects23 = buildImageSet(dataset, query);// object type
		assertEquals(95, objects23);

		//24
		dataset = new DatasetDescriptor("EU_LOCAL_Durhamcathedral",
				"2022316_Ag_UK_ELocal_DurhamCountyCouncil", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_BUILDINGS, SUB_CLASS_CHURCHES});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(),
				null, // "butterflies",
				"\"Durham Cathedral\"", // what
				null, // who
				"IMAGE", null, null, new String[] { "NOT interior", "NOT Cloister",
						"NOT Sanctuary", "NOT roof", "NOT exhibit",
						"NOT \"vibration tests\"" });
				
		int objects24 = buildImageSet(dataset, query);// object type
		assertEquals(154, objects24);

		//25
		dataset = new DatasetDescriptor("OpenUp-butterfly",
				"11617_Ag_EU_OpenUp*", // "2023901_Ag_EU_NaturalEurope_all",
				new String[]{CLASS_INSECTS, SUB_CLASS_BUTTERFLIES});
		query = getQueryBuilder().buildQuery(dataset.getCollectionName(), 
				"\"Zoological collections of the University of Tartu\"", null);
				
		int objects25 = buildImageSet(dataset, query, 400, 500, ThumbnailsAccessor.ERROR_POLICY_RETHROW);
		assertEquals(500, objects25);
		
		performDatasetAggregation();

	}

	private void performDatasetAggregation() throws IOException {
		File cvsFolder = new File(getCollectionsCvsFolder());
		File[] collectionFiles = cvsFolder.listFiles();
		BufferedReader reader = null;
		// String headerLine = null;
		String line = null;
		BufferedWriter datasetWriter = getDataSetFileWriter(false);

		for (int i = 0; i < collectionFiles.length; i++) {
			reader = new BufferedReader(new FileReader(collectionFiles[i]));
			boolean firstLine = true;
			while ((line = reader.readLine()) != null) {
				// write headers to sysout
				if (firstLine) {
					System.out.println(line);
					firstLine = false;
				}
				// write all data to dataset
				datasetWriter.write(line);
				datasetWriter.write("\n");

			}
			datasetWriter.flush();
			// close reader
			try {
				reader.close();
			} catch (IOException e) {
				System.out.println("cannot close reader for: "
						+ collectionFiles[i]);
				e.printStackTrace();
			}
		}
		datasetWriter.close();
	}

//	BufferedWriter getDataSetFileWriter(boolean urls)
//			throws IOException {
//		super.getDataSetFileWriter(urls)();
//		File datasetFile = getDataSetFile(urls);
//		datasetFile.getParentFile().mkdirs();
//
//		return new BufferedWriter(new FileWriter(datasetFile));
//	}

	public File getDataSetFile(boolean urls) {
		IRConfiguration config = getConfig();
		if (urls)
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

	// @Test
	public void downloadThumbnails() throws FileNotFoundException, IOException {

		setDataset(DATASET_DEMO);
		File datasetFile = getConfig().getDatasetFile(DATASET_DEMO);

		IndexHelper ixHelper = new IndexHelper();
		Map<String, String> thumbnailsMap = ixHelper
				.getThumbnailsMap(datasetFile);

		ThumbnailsAccessor ta = new ThumbnailsAccessor();
		List<String> skippedItems = ta.copyThumbnails(thumbnailsMap,
				getConfig().getImageFolderAsFile(DATASET_DEMO));

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
	public void buildIndexedUrlsFile() throws FileNotFoundException,
			IOException, ImageSearchingException {

		setDataset(DATASET_DEMO);
		IRConfiguration config = getConfig();
		File datasetFile = config.getDatasetFile(getDataset());

		IndexHelper ixHelper = new IndexHelper();
		Map<String, String> thumbnailsMap = ixHelper
				.getThumbnailsMap(datasetFile);
		BufferedWriter indexedUrlsWriter = getDataSetFileWriter(true);
		//EuropeanaId euId = new EuropeanaId();
		int counter = 0;

		for (Map.Entry<String, String> thumbnail : thumbnailsMap.entrySet()) {

			//euId.setNewId(thumbnail.getKey());
			try {
				getImageSearchingService().searchSimilar(thumbnail.getKey());

				if (getImageSearchingService().getTotalResults() > 0) {
					// write to file
					indexedUrlsWriter.append(thumbnail.getKey()).append("; ");
					indexedUrlsWriter.append(thumbnail.getValue()).append("\n");
					counter++;
				} else {
					// not indexed yet
					System.out.println("Skipped item: " + thumbnail.getKey());
				}

			} catch (ImageSearchingException e) {
				System.out.println(e.getMessage());
			}
		}

		System.out.println("correct items: " + counter);
	}

	public ImageSearchingService getImageSearchingService() {
		if (imageSearchingService == null) {
			imageSearchingService = new ImageSearchingServiceImpl(getDataset(),
					getConfig());
			imageSearchingService.init();
		}
		return imageSearchingService;
	}

	@Override
	public String getCollectionsCvsFolder(String dataset) {
		return COLLECTIONS_FOLDER + dataset + "/";
	}

	@Override
	protected String getCollectionsCvsFolder() {
		return getCollectionsCvsFolder(getDataset());
	}

}
