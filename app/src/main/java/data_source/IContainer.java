package data_source;

public abstract class IContainer {
	public String name;
	public int accessLevel;
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
