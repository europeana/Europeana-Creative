package eu.europeana.service.ir.image.features;

public class DominantColorValues {

	int size;
	int percentages[];
	int colorValues[][];
	int coherency;
	float[][] centroids;
	float[] weights;
	

	public int getSize() {
		return this.size;
	}

	public int getPercentage(int idx) {
		return this.percentages[idx];
	}

	public int getSpatialCoherency() {
		return this.coherency;
	}

	public int[] getColorValue(int idx) {
		return colorValues[idx];
	}

	public void setCoherency(int coherency) {
		this.coherency = coherency;
	}

	public void setColorValues(int[][] colorValues) {
		this.colorValues = colorValues;
	}

	public void setPercentages(int[] percentages) {
		this.percentages = percentages;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("size: ").append(size);
		builder.append("\ncoherency: ").append(coherency);
		builder.append("\npercentages: \n");
		for (int i = 0; i < percentages.length; i++) {
			builder.append(percentages[i]);
			builder.append(", ");
		}
		builder.append("\ncolors:");
		for (int i = 0; i < colorValues.length; i++) {
			builder.append("\n");
			builder.append(colorValues[i][0]).append(" ");
			builder.append(colorValues[i][1]).append(" ");
			builder.append(colorValues[i][2]).append(" ");
		}
		
		builder.append("\nweights:");
		for (int i = 0; i < weights.length; i++) {
			builder.append(weights[i]);
			builder.append(", ");
		}
		builder.append("\ncentroids:");
		for (int i = 0; i < centroids.length; i++) {
			builder.append("\n");
			builder.append(centroids[i][0]).append(" ");
			builder.append(centroids[i][1]).append(" ");
			builder.append(centroids[i][2]).append(" ");
		}
		
		
		return builder.toString();
	

	}
}
