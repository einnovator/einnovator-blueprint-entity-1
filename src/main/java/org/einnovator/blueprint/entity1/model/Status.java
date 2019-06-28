/**
 * 
 */
package org.einnovator.blueprint.entity1.model;

/**
 * A enumerate for {@code Status}.
 *
 */
public enum Status {
	OK("Ok"),
	KO("KO");
	

	private final String displayValue;
	
	Status(String displayValue) {
		this.displayValue = displayValue;
	}

	
	public String getDisplayValue() {
		return displayValue;
	}

	public String getLabel() {
		return displayValue;
	}

	public static Status parse(String s) {
		for (Status e: Status.class.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}
	
	public static Status parse(String s, Status defaultValue) {
		Status value = parse(s);
		return value!=null ? value: defaultValue;
	}


}
