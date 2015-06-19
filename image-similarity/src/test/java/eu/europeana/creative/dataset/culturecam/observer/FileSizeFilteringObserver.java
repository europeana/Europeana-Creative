package eu.europeana.creative.dataset.culturecam.observer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;


import eu.europeana.api.client.exception.TechnicalRuntimeException;
import eu.europeana.api.client.thumbnails.download.ThumbnailDownloader;
import eu.europeana.api.client.thumbnails.processing.LargeThumbnailsetProcessing;

public class FileSizeFilteringObserver extends ThumbnailDownloader implements
		Observer {

	private int minSize, placeholderSize;
	File filteredImages;

	public FileSizeFilteringObserver(File localImageFolder,
			File filteredImages, int minSize, int placeholderSize) {
		super(localImageFolder);
		this.minSize = minSize;
		this.placeholderSize = placeholderSize;
		this.filteredImages = filteredImages;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof Map))
			throw new TechnicalRuntimeException(
					"Wrong argument type. Expected map but invoked with "
							+ arg.getClass());

		@SuppressWarnings("unchecked")
		Map<String, String> thumbnailMap = (Map<String, String>) arg;
		Map<String, String> fillteredThumbnails = new HashMap<String, String>();

		// File datasetFile = getConfig().getDatasetFile(getDataset());
		File imageFile;
		long fileSize;
		int failureCount = 0;

		for (String thumbnailId : thumbnailMap.keySet()) {
			imageFile = getImageFile(getDownloadFolder(), thumbnailId);
			fileSize = imageFile.length();
			if (!imageFile.exists())
				failureCount++;
			else if (fileSize < minSize
					|| imageFile.length() == placeholderSize)
				fillteredThumbnails.put(thumbnailId, String.valueOf(fileSize));

		}

		((LargeThumbnailsetProcessing) o).increaseFailureCount(failureCount);
		
		if (!fillteredThumbnails.isEmpty())
			writeThumbnailsToCsvFile(fillteredThumbnails, filteredImages);

	}

	protected void writeThumbnailsToCsvFile(Map<String, String> thumbnails,
			File file) {
		try {
			// create parent dirs
			boolean append = true;
			if (!file.exists()){
				//file.getParentFile().mkdirs();
				append = false;
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(file,
					append));

			for (Entry<String, String> thumbnail : thumbnails.entrySet()) {

				writer.write(thumbnail.getKey());
				writer.write(";");
				writer.write(thumbnail.getValue());
				writer.write("\n");
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("cannot write filteredThumbnailsFile", e);
		}

	}

}