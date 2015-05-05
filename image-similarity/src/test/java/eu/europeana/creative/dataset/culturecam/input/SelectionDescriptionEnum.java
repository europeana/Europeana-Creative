package eu.europeana.creative.dataset.culturecam.input;

public enum SelectionDescriptionEnum {
	//#ID;Title;Portal link;Results;items;selection;dicriminator;Content selection comments (Christina re Culture Cam)
	TITLE(0), PORTAL_LINK(1), RESULT_COUNT(2), SELECTED_COUNT(3), SELECTION_TYPE(4), SELECTION_CRITERION(5), SELECTION_COMMENT(6);  
	
	private final int position;
	
	SelectionDescriptionEnum (int pos){
		this.position = pos;
	}

	public int getPosition() {
		return position;
	}
	
	
}
