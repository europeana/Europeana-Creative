package eu.europeana.creative.dataset.culturecam.bl;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;

import eu.europeana.api.client.dataset.DatasetDescriptor;
import eu.europeana.api.client.thumbnails.ThumbnailAccessorUtils;
import eu.europeana.creative.dataset.IRTestConfigurations;

public abstract class BaseBlTest extends ThumbnailAccessorUtils implements
		IRTestConfigurations {

	static Map<String, String> blSets = new HashMap<String, String>();
	protected static final String STEP_FLICKR = "flickr";
	protected static final String STEP_THUMBNAILS = "thumbnails";
	protected static final String STEP_ORDERED = "ordered";
	protected static final String STEP_CLASSIFIED = "classified";
	protected static final String STEP_FILTERED_ORDERED = "filtered-ordered";
	protected static final String STEP_AGGREGATED = "aggregated";
	protected static final String IMAGE_FOLDER_NAME = "/app/eucreative/imagesimilarityhome/culturecam/image/";
	
		
	// String[] flickrSetIds = {"72157639959761466",
	// "72157638733975756", "72157640831988343", "72157638544764936",
	// "72157641857515565", "72157639585298964", "72157638739336254",
	// "72157638821811323"};
	//
	@BeforeClass
	public static void init() {
		blSets.put("72157639959761466", "BL-BookCover");
		blSets.put("72157638733975756", "BL-Letters");
		blSets.put("72157640831988343", "BL-Selection");
		blSets.put("72157638544764936", "BL-Highlights");
		blSets.put("72157641857515565", "BL-Flora");
		blSets.put("72157639585298964", "BL-Decorative");
		blSets.put("72157638739336254", "BL-Fashion");
		blSets.put("72157638821811323", "BL-Xmas");
	}

	protected String getCvsFileForStep(DatasetDescriptor dataset, String step) {
		String folder = getBlCvsFolder(step);
		return folder + dataset.getImageSetName() + "_" + encode(dataset.getCollectionName()) + ".csv";
	}

	protected String getBlCvsFolder(String step) {
		String folder = getCollectionsCvsFolder(getDataset())+ "bl/" + step + "/";
		return folder;
	}
	
	@Override
	public String getCollectionsCvsFolder(String dataset) {
		return COLLECTIONS_FOLDER + dataset + "/";
	}
}
