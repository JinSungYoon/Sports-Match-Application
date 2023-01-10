package core.api.join.entity;

public enum StatusType {
	PROPOSAL("P"),
	REJECT("RJ"),
	APPROVAL("A"),
	WITHDRAW("W"),
	RETURN("RT"),
	CONFIRMATION("C");
	

	private String value;
		
	StatusType(String value) {
		this.value = value;
	}
	
	public String getKey() {
		return name();
	}
	
	public String getValue() {
		return value;
	}
	
	public static StatusType fromValue(String value) {
		for(StatusType s : StatusType.values()) {
			if(s.value.equalsIgnoreCase(value)) {
				return s;
			}
		}
		return null;
	}
}
