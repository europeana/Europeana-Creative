package eu.europeana.service.ir.image.features;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class DominantColorDescriptor {

	SortedSet<ColorBin> bins = new TreeSet<ColorBin>();
	//int imageSize;
	int width, height;
	private String image;
	
	public DominantColorDescriptor(Collection<ColorBin> binCollection, int width, int heigth){
		this.width = width;
		this.height = heigth;
		ColorBin binCopy;
		for (ColorBin colorBin : binCollection) {
			binCopy = colorBin.clone();
			binCopy.setPercentageAndScore(binCopy.count*100/(width * heigth));
			bins.add(binCopy);
		}
	}

	public SortedSet<ColorBin> getBins() {
		return bins;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DominantColorDescriptor for image: ").append(getImage());
		builder.append("\n image width: ").append(getWidth());
		builder.append("\n image height: ").append(getHeight());
		builder.append("\n Bins: ");
		for (ColorBin bin : bins) {
			builder.append("\n").append(bin.toString());
		}
		
		return builder.toString();
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
}
