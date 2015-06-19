package eu.europeana.service.ir.image.features;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.imageanalysis.ScalableColor;
import net.semanticmetadata.lire.utils.ConversionUtils;
import net.semanticmetadata.lire.utils.SerializationUtils;

public class DominantColor extends ScalableColor {

	// <index, centroidValue>
	public static Map<Integer, Integer> hCentroidMap = new HashMap<Integer, Integer>();
	public static List<Integer> svCentroidValues = new ArrayList<Integer>();
	public static List<Integer> grayCentroidValues = new ArrayList<Integer>();
	public Map<String, ColorBin> binMap = new HashMap<String, ColorBin>();
	DominantColorDescriptor descriptor;
	
	// public static Set svBins();

	public void initBins() {
		initHCentroidMap();
		initSVCentroids();
		initGrayCentroids();
	}

	private void initBinMap() {
		if (!binMap.isEmpty()) {
			// clean
			for (Map.Entry<String, ColorBin> binEntry : binMap.entrySet()) {
				binEntry.getValue().reset();
			}
		} else {
			// init
			initBins();

			// fill
			Integer vLevel;
			ColorBin bin;
			String binName;
			for (Iterator<Integer> iterator = grayCentroidValues.iterator(); iterator
					.hasNext();) {
				vLevel = iterator.next();
				binName = buildGrayScaleBinName(vLevel);
				bin = new ColorBin(binName, 0, 16, vLevel);
				setGrayScaleBinSize(bin, vLevel);
				
				binMap.put(bin.getName(), bin);
			}

			for (Map.Entry<Integer, Integer> hCentroid : hCentroidMap
					.entrySet()) {
				for (Integer sCentroid : svCentroidValues) {
					for (Integer vCentroid : svCentroidValues) {
						binName = buildBinNameForCentroid(hCentroid.getValue(),
								sCentroid, vCentroid);
						bin = new ColorBin(binName, hCentroid.getValue(),
								sCentroid, vCentroid);
						
						setColorBinSize(bin, hCentroid.getValue(),
								sCentroid, vCentroid);
						binMap.put(binName, bin);
					}
				}
			}
		}

	}

	private void setColorBinSize(ColorBin bin, Integer value,
			Integer sCentroid, Integer vCentroid) {
		bin.sethSize(43);// 6 bins
		bin.setsSize(getSvBinSize(sCentroid));
		bin.setvSize(getSvBinSize(vCentroid));
	}

	private int getSvBinSize(int svCentroid) {
		if(svCentroid == 144)
			return 96;
		else
			return 64;
	}

	protected void setGrayScaleBinSize(ColorBin bin, Integer vLevel) {
		bin.sethSize(86);//any hue, still not uniformly distributed, we use 2 avg bin size
		if(vLevel < 32){
			bin.setsSize(255); //if v < 32, everything is black
			bin.setvSize(64);
		}else if(vLevel == 144){
			bin.setsSize(32);
			bin.setvSize(96);
		}else {
			bin.setsSize(32);
			bin.setvSize(64);
		}
	}

	protected String buildGrayScaleBinName(Integer vLevel) {
		String binName;
		binName = buildBinNameForCentroid(0, 16, vLevel);
		return binName;
	}

	private String buildBinNameForCentroid(int hCentroid, int sCentroid,
			int vCentroid) {
		return toHexString(hCentroid) + toHexString(sCentroid)
				+ toHexString(vCentroid);
	}

	private String toHexString(int b) {
		String ret = Integer.toHexString(b);
		if(ret.length() == 1)
			ret = "0"+ret;
		return ret;
	}
	
	private static void initGrayCentroids() {
		if (!grayCentroidValues.isEmpty())
			return; // the static set was already initialized
		grayCentroidValues.add(computeGrayCentroidValue(31));
		grayCentroidValues.add(computeGrayCentroidValue(95));
		grayCentroidValues.add(computeGrayCentroidValue(191));
		grayCentroidValues.add(computeGrayCentroidValue(255));

	}

	private static void initSVCentroids() {
		if (!svCentroidValues.isEmpty())
			return; // the static set was already initialized

		// grayscale is threated different
		// svCentroidValues.add(computeSVCentroidValue(31));
		svCentroidValues.add(computeSVCentroidValue(95));
		svCentroidValues.add(computeSVCentroidValue(191));
		svCentroidValues.add(computeSVCentroidValue(255));

	}

	protected static void initHCentroidMap() {
		if (!hCentroidMap.isEmpty())
			return; // the static map was already initialized
		// the bins have the names of the H value of the Centroids
		// using highest values in the bins for computing the index and
		// hardcoded centroid value
		hCentroidMap.put(computeHCentroidIndex(21), 0);
		hCentroidMap.put(computeHCentroidIndex(64), 43);
		hCentroidMap.put(computeHCentroidIndex(107), 85);
		hCentroidMap.put(computeHCentroidIndex(150), 128);
		hCentroidMap.put(computeHCentroidIndex(193), 170);
		hCentroidMap.put(computeHCentroidIndex(236), 213);
		hCentroidMap.put(computeHCentroidIndex(237), 0);// her the lowest
														// version
	}

	public static int computeHCentroidIndex(int hValue) {
		// 43 = Math.round(255/6=42,5);//splitting the H value in 6 bins.
		// Bin0=Bin5 = [0...21, 213...255]
		return Math.round((float) hValue / 43);
	}

	public static int computeHCentroidValue(int hValue) {
		return hCentroidMap.get(computeHCentroidIndex(hValue));
	}

	public static int computeSVCentroidValue(int svValue) {
		if (svValue < 32)// first bin 32 values
			// return 16;//centroid
			// not used as this is gray scale
			throw new IllegalArgumentException(
					"Input value must be in range [32 ...255]:" + svValue);
		if (svValue < 96) // second bin 64 values (darker if V or lighter if S )
			return 64;
		if (svValue < 192) // third bin 96 (faded colors)
			return 144;
		if (svValue < 256) // forth bin 64 values (full colors)
			return 224;

		throw new IllegalArgumentException(
				"Input value must be in range [32 ...255]:" + svValue);
	}

	public static int computeGrayCentroidValue(int vValue) {
		if (vValue < 32)// first bin 32 values = black
			return 16;// centroid
		if (vValue < 96) // second bin 64 values = dark
			return 64;
		if (vValue < 192) // third bin 96 = gray
			return 144;
		if (vValue < 256) // forth bin 64 values = light
			return 224;

		throw new IllegalArgumentException(
				"Input value must be in range [0...255]:" + vValue);
	}

	boolean isGrayScale(int h, int s, int v) {
		return s < 32 || v < 32;
	}

	public DominantColor() {
		super();
		// init();
		// throw new RuntimeException("To test!");
	}

	@Override
	protected void init() {
		super.init();
		initBinMap();
		//reset descriptor
		descriptor = null;
	}

	@Override
	public void extract(BufferedImage image) {
		super.extract(image);
		// initBinMap();
		descriptor = new DominantColorDescriptor(binMap.values(), _xSize, _ySize);
		//descriptor.setImage(image.)
	}

	@Override
	protected void _Quant(int H, int S, int V, int m, int n) {
		super._Quant(H, S, V, m, n);
		// compute ColorBin
		String binName;

		if (isGrayScale(H, S, V)) {
			binName = buildGrayScaleBinName(computeGrayCentroidValue(V));
		} else {
			binName = buildBinNameForCentroid(computeHCentroidValue(H),
					computeSVCentroidValue(S), computeSVCentroidValue(V));
		}

		binMap.get(binName).addPixel(H, S, V);
	}

	protected int[] computeOrderedDistribution(int[] histogram) {
		int[] distribution = Arrays.copyOf(histogram, histogram.length);
		Arrays.sort(distribution);
		float size = _ySize * _xSize;
		for (int i = 0; i < distribution.length; i++) {
			distribution[i] = (int) (distribution[i] * 100 / size);
		}
		return distribution;
	}

	protected int computeDominantValueCount(int[] distribution, int percentage) {
		int sum = 0;
		int count = 0;
		for (int i = 0; i < distribution.length; i++) {
			sum += distribution[distribution.length - (i + 1)];// distribution
																// is ordered in
																// ascending
																// order
			count++;
			if (sum >= percentage)
				return count;
		}
		return count;
	}

	protected LireFeature extractFeatures(String extractorClassName,
			File imageFile) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {

		InputStream in = new FileInputStream(imageFile);

		BufferedImage image = ImageIO.read(in);
		extract(image);
		return this;
	}

	public Map<String, ColorBin> getBinMap() {
		return binMap;
	}

	public DominantColorDescriptor getDescriptor() {
		return descriptor;
	}
	
	 public byte[] getByteArrayRepresentation() {
	       
//		 out.writeByte(version);
//			out.writeByte(length);
//			for (int i = 0; i < length; i++) {
//				out.writeByte(h[i]);
//				out.writeByte(s[i]);
//				out.writeByte(v[i]);
//				out.writeShort(score[i]);
//			}
//		 
//	      return SerializationUtils.toByteArray(null);
		 throw new RuntimeException("Not suported yet");
	    }

	    public void setByteArrayRepresentation(byte[] in) {
	    	 throw new RuntimeException("Not suported yet");
	    }

	    public void setByteArrayRepresentation(byte[] in, int offset, int length) {
	    	 throw new RuntimeException("Not suported yet");
	    }

	    public double[] getDoubleHistogram() {
	    	 throw new RuntimeException("Not suported yet");
	    }

	    @Override
	    public String getStringRepresentation() {
	    	
	    	StringBuilder builder = new StringBuilder();
	    	Collection<ColorBin> bins = getDescriptor().getBins();
	    	int length = 10;
	    	int i = 0;
	    	for (ColorBin colorBin : bins) {
	    			
	    		builder.append(colorBin.hCentroid).append(' ');
				builder.append(colorBin.sCentroid).append(' ');
				builder.append(colorBin.vCentroid).append(' ');
				builder.append(colorBin.getNormalizedScore());
				
				//keep top 10 only and do not append last empty space
	    		if(++i >= length)
	    			break;
	    		
	    		builder.append(' ');
			}
	    	return builder.toString();
	    	
	    }
}
