package eu.europeana.assets.ir.image.api.evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @deprecated - please replace with melampo-indexbuilder (/#)IndexHelper
 * @author Sergiu Gordea 
 *
 */
public class IndexHelperOld {

	public static void main(String... args) throws IOException {
		IndexHelperOld helper = new IndexHelperOld();
		helper.convertCvsToUriFilenamesFile();
	}

	public void convertCvsToUriFilenamesFile() throws IOException {
		Map<String, String> thumbs = getThumbnailsMap("/dataset.csv");
		writeToUriFileNames(thumbs, "/europeanaUriFilenames.sub.txt");
	}

	public Map<String, String> getThumbnailsMap(String filePath)
			throws IOException {
		final File file = new File(filePath);
		return getThumbnailsMap(file);
	}

	public Map<String, String> getThumbnailsMap(final File file)
			throws FileNotFoundException, IOException {
		Map<String, String> thumbnails = new HashMap<String, String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			// BufferedReader reader = new BufferedReader(new
			// FileReader("/collection_07501_thumbnails.csv"));

			String europeanaUriAndObject = null;
			String[] values;

			while ((europeanaUriAndObject = reader.readLine()) != null) {
				//ignore comments
				if(europeanaUriAndObject.startsWith("#"))
					continue;
				
				//parse values
				values = europeanaUriAndObject.split(";");
				thumbnails.put(values[0], values[1]);
			}
		} finally {
				closeReader(reader); 
		}
		
		return thumbnails;
	}

	protected void closeReader(BufferedReader reader) {
		try{
			if(reader != null)
				reader.close();
		} catch (Exception e){
			System.out.println("cannot close reader" + e); 
		}
	}

	void writeToUriFileNames(Map<String, String> thumbnails, String outFile)
			throws IOException {

		int index = 0;
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		for (Entry<String, String> thumbnail : thumbnails.entrySet()) {

			writer.write("id");
			writer.write("" + index);
			writer.write("=");
			writer.write(thumbnail.getKey());
			writer.newLine();

			writer.write("photo");
			writer.write("" + index);
			writer.write("=");
			writer.write(thumbnail.getValue());
			writer.newLine();
			writer.newLine();

			index++;
			if (index % 1000 == 0)
				writer.flush();
		}
		writer.flush();
		writer.close();

	}
}
