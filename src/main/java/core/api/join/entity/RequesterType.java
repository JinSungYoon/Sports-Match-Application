package core.api.join.entity;

public enum RequesterType {
	PLAYER("P"),
	TEAM("T");

	private String value;
	
	RequesterType(String value) {
		this.value = value;
	}
	
	public String getKey() {
		return name();
	}
	
	public String getValue() {
		return value;
	}
	
	public static RequesterType fromValue(String value) {
		for(RequesterType r : RequesterType.values()) {
			if(r.value.equalsIgnoreCase(value)) {
				return r;
			}
		}
		return null;
	}
	
}
