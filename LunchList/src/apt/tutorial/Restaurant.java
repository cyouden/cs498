package apt.tutorial;

public class Restaurant {
	public enum Type {
		NULL,
		SIT_DOWN,
		TAKE_OUT,
		DELIVERY;
	}
	
	private String name = "";
	private String address = "";
	private Type type = Type.NULL;
	private String notes="";
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String toString() {
		return getName();
	}
}
