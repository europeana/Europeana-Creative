package eu.europeana.creative.dataset;

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

public class HistorianaDatasetBuilderTest extends
		ThumbnailsForCollectionAccessorTest implements IRTestConfigurations {

	public static String CLASS_WW1 = "ww1";

	final String DATASET_HISTORIANA = "historiana";
	private String dataset = null;
	private ImageSearchingService imageSearchingService;

//	@Test
	public void createDemoDataset() throws IOException {
		
		setDataset(DATASET_HISTORIANA);

		//http://www.europeana.eu/portal/search.html?query=*%3A*&start=1&rows=12&qf=PROVIDER%3A%22Europeana+1914+-+1918%22&qf=TYPE%3AIMAGE
//		int objects0 = buildImageSet("Europeana1914-1918",
//				null, new String[] { CLASS_WW1,
//				"ugc1914" }, null, null, null, "IMAGE", "Europeana 1914 - 1918");
//		assertEquals(58723, objects0);

		//http://www.europeana.eu/portal/search.html?query=*:*&qf=1914-1918&qf=PROVIDER:%22EFG+-+The+European+Film+Gateway%22&qf=TYPE:IMAGE&rows=12
//		int objects1 = buildImageSet("European Digital Film Gateway",
//				null, new String[] { CLASS_WW1,
//				"filmgateway" }, "1914-1918", null, null, "IMAGE", "EFG - The European Film Gateway");
//		assertEquals(136, objects1); // 1 object without thumbnail

		//http://www.europeana.eu/portal/search.html?query=europeana_collectionName:9200168*&qf=TYPE:IMAGE&rows=24;
//		int objects2 = buildImageSet("Serbian National Library",
//				"9200168*", new String[] { CLASS_WW1,
//				"serbia" }, null, null, null, "IMAGE");
//		assertEquals(206, objects2); // 1 object without thumbnail

		//http://www.europeana.eu/portal/search.html?query=DATA_PROVIDER%3A%22National+Library+of+the+Netherlands+-+Koninklijke+Bibliotheek%22&rows=12&qf=eerste+wereldoorlog
//		int objects3 = buildImageSet("Netherlands National Library",
//				null, new String[] { CLASS_WW1,
//				"netherlands" }, "eerste wereldoorlog", null, null, "IMAGE", null, "\"National Library of the Netherlands - Koninklijke Bibliotheek\"" );
//		assertEquals(2780, objects3);

		//http://www.europeana.eu/portal/search.html?query=*:*&qf=PROVIDER:%22The+Great+War+Archive%2C+University+of+Oxford%22&qf=TYPE:IMAGE&rows=12
//		int objects4 = buildImageSet("Th Great War Archive_University of Oxford",
//				null, new String[] { CLASS_WW1,
//				"uk-great war archive" }, null, null, null, "IMAGE", "\"The Great War Archive, University of Oxford\"");
//		assertEquals(6342, objects4);

		//http://www.europeana.eu/portal/search.html?query=DATA_PROVIDER%3A%22The+Wellcome+Library%22+Great+War+OR+First+World+War+OR+WW1+OR+1914-1918&rows=12
//		int objects5 = buildImageSet("Welcome",
//				null, new String[] { CLASS_WW1,
//				"welcome" }, "Great War OR First World War OR WW1 OR 1914-1918", null, null, "IMAGE", null, "\"The Wellcome Library\"");
//				assertEquals(133, objects5); //13 videos skipped out

//		http://www.europeana.eu/portal/search.html?query=DATA_PROVIDER%3A%22Istituto+centrale+per+il+catalogo+unico%22&start=1&rows=24&qf=First+World+War
//			int objects6 = buildImageSet("Italy_Istituto_Centrale",
//			null, new String[] { CLASS_WW1,
//			"italy-iccu" }, "First World War", null, null, "IMAGE", null, "\"Istituto centrale per il catalogo unico\"");
//			assertEquals(149878, objects6); //13 videos skipped out
		
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

	
//	private BufferedWriter getDataSetFileWriter(boolean urls)
//			throws IOException {
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

///	@Test
	public void downloadThumbnails() throws FileNotFoundException, IOException {

		setDataset(DATASET_HISTORIANA);
		File datasetFile = getConfig().getDatasetFile(DATASET_HISTORIANA);

		IndexHelper ixHelper = new IndexHelper();
		Map<String, String> thumbnailsMap = ixHelper
				.getThumbnailsMap(datasetFile);

		ThumbnailsAccessor ta = new ThumbnailsAccessor();
		List<String> skippedItems = ta.copyThumbnails(thumbnailsMap,
				getConfig().getImageFolderAsFile(DATASET_HISTORIANA));

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

		setDataset(DATASET_HISTORIANA);
		IRConfiguration config = getConfig();
		File datasetFile = config.getDatasetFile(getDataset());

		IndexHelper ixHelper = new IndexHelper();
		Map<String, String> thumbnailsMap = ixHelper
				.getThumbnailsMap(datasetFile);
		BufferedWriter indexedUrlsWriter = getDataSetFileWriter(true);
		EuropeanaId euId = new EuropeanaId();
		int counter = 0;

		for (Map.Entry<String, String> thumbnail : thumbnailsMap.entrySet()) {

			euId.setNewId(thumbnail.getKey());
			try {
				getImageSearchingService().searchSimilar(euId);

				if (getImageSearchingService().getTotalResults() > 0) {
					// write to file
					indexedUrlsWriter.append(thumbnail.getKey()).append("; ");
					indexedUrlsWriter.append(thumbnail.getValue()).append("\n");
					counter++;
				} else {
					// not indexed yet
					System.out.println("Skipped item: " + euId.getNewId());
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
