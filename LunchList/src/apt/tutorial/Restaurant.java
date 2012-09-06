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
	private int lastVisitDay = -1;
	private int lastVisitMonth = -1;
	private int lastVisitYear = -1;
	
	public int getLastVisitDay() {
		return lastVisitDay;
	}

	public void setLastVisitDay(int lastVisitDay) {
		this.lastVisitDay = lastVisitDay;
	}

	public int getLastVisitMonth() {
		return lastVisitMonth;
	}

	public void setLastVisitMonth(int lastVisitMonth) {
		this.lastVisitMonth = lastVisitMonth;
	}

	public int getLastVisitYear() {
		return lastVisitYear;
	}

	public void setLastVisitYear(int lastVisitYear) {
		this.lastVisitYear = lastVisitYear;
	}

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
	
	public String toString() {
		return getName();
	}
}
