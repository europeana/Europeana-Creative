package eu.europeana.service.ir.image.features;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ScalableAndDominatColorExtractorTest {

	@Test
	public void testDCDExtraction() throws IOException {

		File testImagesFolder = new File("/tmp/dcd");
		String[] fileNames = testImagesFolder.list();
		File imageFile;
		ScalableAndDominantColorExtractor extractor = new ScalableAndDominantColorExtractor();

		for (int i = 0; i < fileNames.length; i++) {
			//skip non image files
			if(!fileNames[i].endsWith(".jpg"))
				continue;
			
			imageFile = new File(testImagesFolder, fileNames[i]);
			BufferedImage image = ImageIO.read(imageFile);

			extractor.extract(image);
			DominantColorDescriptor descriptor = extractor.getDescriptor();
			descriptor.setImage(imageFile.toString());
			System.out.println(descriptor);
			Color rgb;
			System.out.println("<table>");
			for (ColorBin bin : descriptor.bins) {
				float h = (float) bin.hCentroid / 255;
				float s = (float) bin.sCentorid / 255;
				float v = (float) bin.vCentroid / 255;

				int rgbVal = Color.HSBtoRGB(h, s, v);
				rgb = new Color(rgbVal);
				String styleRgb = rgb.getRed() + "," + rgb.getGreen() + ","
						+ rgb.getBlue();
				String htmlRgb = "#" + Integer.toHexString(rgb.getRed())
						+ Integer.toHexString(rgb.getGreen())
						+ Integer.toHexString(rgb.getBlue());

				System.out.print("\n<tr><td>" + bin.getPercentage());
				System.out.print("</td><td>" + bin.getNormalizedScore() +
										"</td><td style='background-color:rgb(");
				System.out.print(styleRgb);
				System.out.print(")'>"+styleRgb +"</td><td bgcolor='" + htmlRgb);

				System.out.print("'>"+htmlRgb+ "</td></tr>");
			}

			System.out.println("</table>");

			// for(Map.Entry<String, ColorBin> entry :
			// extractor.binMap.entrySet()){
			// System.out.println("Bin: " + entry.getKey());
			// System.out.println("Values: " + entry.getValue());
			// }
			// values = extractor.extractDescriptor(imageFile);
			// System.out.println(imageFile);
			// System.out.println(values);
		}

	}
}
