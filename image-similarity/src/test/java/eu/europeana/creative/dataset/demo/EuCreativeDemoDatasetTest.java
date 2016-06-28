package eu.europeana.creative.dataset.demo;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.junit.Test;

import eu.europeana.api.client.exception.EuropeanaApiProblem;
import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;
import eu.europeana.creative.dataset.IRTestConfigurations;

/**
 * ticket:
 * <code>https://europeanadev.assembla.com/spaces/europeana-creative/tickets/2?comment=450587433#comment:450587433</code>
 * 
 * Gallica:http://www.europeana.eu/portal/search.html?query=*:*&qf=DATA_PROVIDER
 * : %22French+National+Library+-+Biblioth%C3%A8que+Nationale+de+France%22&qf=
 * TYPE:IMAGE&rows=24 Amsterdam museum:http://www.europeana.eu/portal/search.
 * html?query=*:*&qf=TYPE:IMAGE&
 * qf=PROVIDER:%22Digitale+Collectie%22&qf=DATA_PROVIDER
 * :%22Amsterdam+Museum%22&rows=12 Bildarchiv Foto Marburg:http://www.europeana
 * .eu/portal/search.html?query=*:*&qf=TYPE:IMAGE
 * &qf=DATA_PROVIDER:%22Bildarchiv+Foto+Marburg%22&rows=12 National Library of
 * Netherlands:http://www.europeana.eu/portal/search.html?query=*:*&qf=TYPE
 * :IMAGE &qf=DATA_PROVIDER:%22National+Library+of+the+Netherlands+-+Koninklijke
 * +Bibliotheek%22&rows=12 Austrian National Library:http://www.europeana.eu/
 * portal/search.html?query=*:*&qf=TYPE:IMAGE
 * &qf=DATA_PROVIDER:%22%C3%96sterreichische
 * +Nationalbibliothek+-+Austrian+National+Library%22&rows=12 Progetto Art Past
 * :http://www.europeana.eu/portal/search.html?query=*%3A*&start=13&rows= 48&qf
 * =TYPE%3AIMAGE&qf=DATA_PROVIDER%3A%22Progetto+ArtPast-+CulturaItalia%22
 **/
public class EuCreativeDemoDatasetTest extends ThumbnailAccessorUtils implements
		IRTestConfigurations {

	// public static String CLASS_WW1 = "ww1";

	// @Test
	public void createGallicaSubset() throws MalformedURLException,
			UnsupportedEncodingException, IOException, EuropeanaApiProblem {

		// Gallica:http://www.europeana.eu/portal/search.html?query=*:*&qf=DATA_PROVIDER:%22French+National+Library+-+Biblioth%C3%A8que+Nationale+de+France%22&qf=TYPE:IMAGE&rows=24
		// assertEquals(246683, objects1);
		String portalUrl = "http://www.europeana.eu/portal/search.html?query=*:*&qf=DATA_PROVIDER:%22French+National+Library+-+Biblioth%C3%A8que+Nationale+de+France%22&qf=TYPE:IMAGE&rows=24";
		createSubset("fullcollection", "Gallica", portalUrl, 1, 246683);
	}

	// @Test
	public void createAmsterdamMuseumSubset() throws MalformedURLException,
			UnsupportedEncodingException, IOException, EuropeanaApiProblem {

		// Amsterdam
		// museum:http://www.europeana.eu/portal/search.html?query=*:*&qf=TYPE:IMAGE&qf=PROVIDER:%22Digitale+Collectie%22&qf=DATA_PROVIDER:%22Amsterdam+Museum%22&rows=12
		// //objects assertEquals(78631, objects2);
		// //objects with thumbnail
		// assertEquals(60413, objects2);

		String portalUrl = "http://www.europeana.eu/portal/search.html?query=*:*&qf=TYPE:IMAGE&qf=PROVIDER:%22Digitale+Collectie%22&qf=DATA_PROVIDER:%22Amsterdam+Museum%22&rows=12";
		createSubset("digitalcollection", "Amsterdam_Museum", portalUrl, 1,
				60413);
	}

	@Test
	public void createKbSubset() throws MalformedURLException,
			UnsupportedEncodingException, IOException, EuropeanaApiProblem {

		// National Library of
		// Netherlands:http://www.europeana.eu/portal/search.html?query=*:*&qf=TYPE:IMAGE&qf=DATA_PROVIDER:%22National+Library+of+the+Netherlands+-+Koninklijke+Bibliotheek%22&rows=12
		String portalUrl = "http://www.europeana.eu/portal/search.html?query=*:*&qf=TYPE:IMAGE&qf=DATA_PROVIDER:%22National+Library+of+the+Netherlands+-+Koninklijke+Bibliotheek%22&rows=12";
		createSubset("fullcollection", "KB", portalUrl, 120501, 810589);
	}

	@Test
	public void createOnbSubset() throws MalformedURLException,
			UnsupportedEncodingException, IOException, EuropeanaApiProblem {

		// Austrian National
		// Library:http://www.europeana.eu/portal/search.html?query=*:*&qf=TYPE:IMAGE&qf=DATA_PROVIDER:%22%C3%96sterreichische+Nationalbibliothek+-+Austrian+National+Library%22&rows=12
		// assertEquals(264,167, objects5);
		String portalUrl = "http://www.europeana.eu/portal/search.html?query=*:*&qf=TYPE:IMAGE&qf=DATA_PROVIDER:%22%C3%96sterreichische+Nationalbibliothek+-+Austrian+National+Library%22&rows=12";
		createSubset("fullcollection", "ONB", portalUrl, 1, 264167);
	}

	@Test
	public void createCulturitaliaSubset() throws MalformedURLException,
			UnsupportedEncodingException, IOException, EuropeanaApiProblem {

		// Progetto Art
		// Past:http://www.europeana.eu/portal/search.html?query=*%3A*&start=13&rows=48&qf=TYPE%3AIMAGE&qf=DATA_PROVIDER%3A%22Progetto+ArtPast-+CulturaItalia%22
		// assertEquals(474071, objects6);

		String portalUrl = "http://www.europeana.eu/portal/search.html?query=*%3A*&qf=TYPE%3AIMAGE&qf=DATA_PROVIDER%3A%22Progetto+ArtPast-+CulturaItalia%22&start=13&rows=48";
		createSubset("culturitalia", "Progetto_art", portalUrl, 0, 474071);
	}

	@Test
	public void performDatasetAggregation() throws IOException {
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

	@Override
	public String getCollectionsCvsFolder(String dataset) {
		return null;
	}

}
