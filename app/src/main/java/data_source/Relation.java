package data_source;

public class Relation extends IContainer{
	public String type;
	public IContainer classFrom;
	public IContainer classTo;
	public String classFromCardinal;
	public String classToCardinal;
	
	public Relation(int accessLevel, String type, IContainer classFrom, IContainer classTo, String classFromCardinal, String classToCardinal) {
		this.accessLevel = accessLevel;
		this.type = type;
		this.classFrom = classFrom;
		this.classTo = classTo;
		this.classFromCardinal = classFromCardinal;
		this.classToCardinal = classToCardinal;
	}
}
