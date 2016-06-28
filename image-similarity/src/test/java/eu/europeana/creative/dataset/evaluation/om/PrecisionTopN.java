package eu.europeana.creative.dataset.evaluation.om;


public class PrecisionTopN {
	private static String PRECISION_AT = "P@";
	public static String TYPE_CATEGORY = "c";
	public static String TYPE_SUBCATEGORY = "sc";

	String type;
	private int n;
	int summ;
	int count;
	
	public PrecisionTopN(String type, int n) {
		this.type = type;
		this.n = n;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getSumm() {
		return summ;
	}

	public void addToSumm(int summ) {
		this.summ += summ;
		incrementCount();		
	}

	private int incrementCount() {
		return count++;
	}
	
	int getCount() {
		return count;
	}
	
	

	public String getMetricName() {
		return PRECISION_AT + getN() + getType();
	}


	public double getMetricValue() {
		return (double)getSumm()/ getN() / getCount();
	}
	
	public String toString(){
		return getMetricName() + ";" + getMetricValue();
	}
}
