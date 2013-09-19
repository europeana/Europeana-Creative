package eu.europeana.corelib.tools.lookuptable;


public class EuropeanaId {

	private ObjectId id;
	private String oldId;
	private String newId;
	private long timestamp;
	private long lastAccess; 
	
	/**
	 * Get the record ID
	 * @return the record ID
	 */
	public ObjectId getId() {
		return id;
	}
	
	/**
	 * Set the recordID
	 * @param id The id to set
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}
	
	/**
	 * Get the oldID field
	 * @return
	 */
	public String getOldId() {
		return oldId;
	}
	
	/**
	 * Set the oldID field
	 * @param oldId
	 */
	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
	
	/**
	 * Get the newID field
	 * @return the newID Field
	 */
	public String getNewId() {
		return newId;
	}
	
	/**
	 * Get a long representing the date the record was imported
	 * @return The date the record was imported
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Set the date the record was imported
	 * @param timestamp The date the record was imported
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * The date the records was last accessed
	 * @return
	 */
	public long getLastAccess() {
		return lastAccess;
	}
	
	/**
	 * Set the date the record was last accessed
	 * @param lastAccess
	 */
	public void setLastAccess(long lastAccess) {
		this.lastAccess = lastAccess;
	}
	
	/**
	 * Set the newId field
	 * @param newId
	 */
	public void setNewId(String newId) {
		this.newId = newId;
	}
}
