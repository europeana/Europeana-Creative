package eu.europeana.service.ir.image.features;

public class ColorBin implements Comparable<ColorBin>, Cloneable{

	private static final int avgBinSize = 43 * 64 * 64;
	String name;
	int count;
	long distSumm;
	int hCentroid, sCentorid, vCentroid;
	int hSize, sSize, vSize;
	
	private int percentage = -1;
	private float normalizedScore = -1;
	

	public ColorBin(String name, int h, int s, int v){
		this(name, h, s, v, -1, -1, -1);
	}
	
	public ColorBin(String name, int h, int s, int v, int hSize, int sSize, int vSize) {
		this.name = name;
		this.hCentroid = h;
		this.sCentorid = s;
		this.vCentroid = v;
		this.sSize = sSize;
		this.hSize = hSize;
		this.sSize = sSize;
		this.vSize = vSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getDistSumm() {
		return distSumm;
	}

	public void setDistSumm(long distSumm) {
		this.distSumm = distSumm;
	}

	public float getAvgDist() {
		// max dist 144 = 3 (dimensions)* 48 (max dist/dim ... but not
		// average...)
		return (float) getDistSumm() / (3 * getCount());
	}

	// public float getScore(){
	// return 1F- get;
	// }

	public void reset() {
		count = 0;
		distSumm = 0;
	}

	public void addPixel(int h, int s, int v) {
		count++;
		distSumm += computeDist(h, s, v);
	}

	private int computeDist(int h, int s, int v) {
		return Math.abs(h - hCentroid) + Math.abs(s - sCentorid)
				+ Math.abs(v - vCentroid);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name: ").append(name);
		builder.append("\ncount: ").append(count);
		builder.append("\ndist: ").append(distSumm);
		builder.append("\navgDist: ").append(getAvgDist());
		if(percentage >= 0 )
			builder.append("\npercentage: ").append(percentage);
		
		return builder.toString();
	}

	@Override
	public int compareTo(ColorBin o) {
		int res = compareByNormalizedScore(o); 
		if(res != 0)
			return res;
		
		res = compareByCount(o);
		if(res != 0)
			return res;
		
		return this.name.compareTo(o.name);
	}

	protected int compareByNormalizedScore(ColorBin o) {
		return (int) ((o.normalizedScore - this.normalizedScore) * 100);
	}
	
	protected int compareByCount(ColorBin o) {
		return o.count - this.count;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.name.equals(((ColorBin)obj).name);
	}
	
	@Override
	public ColorBin clone(){
		try {
			return (ColorBin) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentageAndScore(int percentage) {
		this.percentage = percentage;
		float factor = (float) avgBinSize / (hSize * sSize * vSize);
		//System.out.println("factor: " + factor);
		this.normalizedScore = (int) (percentage * factor) ;
	}

	public void sethSize(int hSize) {
		this.hSize = hSize;
	}

	public void setsSize(int sSize) {
		this.sSize = sSize;
	}

	public void setvSize(int vSize) {
		this.vSize = vSize;
	}

	public int getNormalizedScore() {
		return (int)normalizedScore;
	}
}
