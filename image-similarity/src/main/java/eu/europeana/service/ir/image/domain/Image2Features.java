package eu.europeana.service.ir.image.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.LireFeature;

import eu.europeana.service.ir.image.IRImageConfiguration;
import eu.europeana.service.ir.image.exceptions.FeaturesExtractionException;

public class Image2Features {

	private String[][] extractorsImpl;
	private LireFeature[] lireExtractors;
	
	private IRImageConfiguration configuration;

	public Image2Features(String dataset, IRImageConfiguration configuration) throws FeaturesExtractionException {
		this.configuration = configuration;
		try {
			extractorsImpl = Tools
					.getAllOrderedProperties(this.configuration.getImageFxFile(dataset));
		} catch (IOException e) {
			throw new FeaturesExtractionException(e);
		}
		lireExtractors = new LireFeature[extractorsImpl.length];
		for (int i = 0; i < extractorsImpl.length; i++) {
			String extractorImpl = extractorsImpl[i][1];
			try {
				lireExtractors[i] = (LireFeature) Class.forName(extractorImpl)
						.newInstance();
			} catch (InstantiationException e) {
				throw new FeaturesExtractionException(e);
			} catch (IllegalAccessException e) {
				throw new FeaturesExtractionException(e);
			} catch (ClassNotFoundException e) {
				throw new FeaturesExtractionException(e);
			}
		}
	}

	public String image2Features(InputStream imgStream)
			throws FeaturesExtractionException {
		BufferedImage img;
		try {
			img = ImageIO.read(imgStream);
		} catch (IOException e) {
			throw new FeaturesExtractionException(e);
		}
		String res = extractFeatures(img);
		return res;
	}

	public String image2Features(File imgFile)
			throws FeaturesExtractionException {
		BufferedImage img;
		try {
			img = ImageIO.read(new FileInputStream(imgFile));
		} catch (IOException e) {
			throw new FeaturesExtractionException(e);
		}
		String res = extractFeatures(img);
		return res;
	}

	public String image2Features(URL imgURL)
			throws FeaturesExtractionException {
		BufferedImage img;
		try {
			img = ImageIO.read(imgURL);
		} catch (IOException e) {
			throw new FeaturesExtractionException(e);
		}
		String res = extractFeatures(img);
		return res;
	}

	private String extractFeatures(BufferedImage buffImg)
			throws FeaturesExtractionException {
		StringBuilder features = new StringBuilder();
		features.append("<IRImage>\n<lire>\n");
		for (int i = 0; i < extractorsImpl.length; i++) {
			String extractorName = extractorsImpl[i][0];
			try {
				lireExtractors[i] = lireExtractors[i].getClass().newInstance();
				lireExtractors[i].extract(buffImg);
				features.append("<").append(extractorName).append(">");
				features.append(lireExtractors[i].getStringRepresentation());
				features.append("</").append(extractorName).append(">\n");
			} catch (Exception e) {
				new FeaturesExtractionException("error, unable to extract " + extractorName);
			}
		}
		features.append("</lire>\n</IRImage>");
		//System.out.println("features:" + features.toString());
		return features.toString();
	}
}