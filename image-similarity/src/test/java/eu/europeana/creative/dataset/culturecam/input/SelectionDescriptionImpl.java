package eu.europeana.creative.dataset.culturecam.input;

public class SelectionDescriptionImpl {
	
	/** the id of the collection */
	private String id;
	
	/** string array of descriptive (meta-) data */
	private String[] values;

	/**
	 * 
	 * @param id - the id of the selection/collection
	 * @param values - string array of descriptive (meta-) data
	 */
	public SelectionDescriptionImpl(String id, String[] values) {
		this.id = id;
		this.values = values;
	}

	public String getFieldValue(SelectionDescriptionEnum e){
		if(values.length <= e.getPosition())
			return null;
		else
			return values[e.getPosition()];
	}
	
	public int getIntFieldValue(SelectionDescriptionEnum e){
		if(values.length <= e.getPosition())
			return -1;
		else
			return Integer.parseInt(getFieldValue(e));
	}

	public String getId() {
		return id;
	}

	protected void setId(String id) {
		this.id = id;
	}
}
