package core.api.player.entity;

public enum BelongType {
	
	CLUB("C"),
	ELEMENTARY_SCHOOL("E"),
	HIGH_SCHOOL("H"),
	MIDDLE_SCHOOL("M"),
	PROTEAM("P"),
	UNIVERSITY("U");
	
	private String value;
	
	BelongType(String value){
		this.value = value;
	}
	
	public String getKey() {
		return name();
	}
	
	public String getValue() {
		return value;
	}
	
	// value로부터 Key값을 반환하는 메서드
	// 만일 value로 U를 넣으면 UNIVERSITY가 반환된다.
	public static BelongType fromValue(String value) {
		for(BelongType b : BelongType.values()) {
			if(b.value.equalsIgnoreCase(value)) {
				return b;
			}
		}
		return null;
	}
}
