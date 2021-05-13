package data_source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MegaContainer extends IContainer{
	
	public HashMap<String, Interface> interfaces;
	
	public HashMap<String, Class> classes;
	
	public HashMap<String, Relation> relations;
	
	public HashMap<String, Relation> inheritance;
	
	public MegaContainer() {
		this.interfaces = new HashMap<String, Interface>(); //InternalName for Interface, Interface Object;
		this.classes = new HashMap<String, Class>(); //InternalName for Class, Class Object
		this.relations = new HashMap<String, Relation>(); //Relation object itself
		this.inheritance = new HashMap<String, Relation>();
	}
	
	public void resetContainer() {
		this.interfaces.clear();
		this.classes.clear();
		this.relations.clear();
		this.inheritance.clear();
	}
	
	public String getName() {
		return "MegaContainer";
	}
	
	public boolean hasRelation(String classTo, String className) {
		String key = classTo.concat(className);
		return relations.containsKey(key);
	}

	public boolean isDependency(String classTo, String className) {
		String key = classTo.concat(className);
		return getRelationType(key).equals("Dependency");
	}
	
	public boolean isAssociation(String classTo, String className) {
		String key = classTo.concat(className);
		return relations.get(key).type.equals("Association");
	}
	
	private String getRelationType(String key) {
		return relations.get(key).type;
	}

	public IContainer getMegaClass(String key) {
		return classes.get(key);
	}
	
	public ArrayList<Method> getClassMethods(String className) {
		return classes.get(className).methods;
	}
	
	public HashSet<Field> getClassFields(String className) {
		return classes.get(className).fields;
	}
	
	public Set<String> getMegaClassKeys() {
		return classes.keySet();
	}
	
	public Set<String> getMegaInterfaceKeys() {
		return interfaces.keySet();
	}
	
	public IContainer getMegaInterface(String key) {
		return interfaces.get(key);
	}

	public void addRelation(String classTo, String className, Relation newRelation) {
		String key = classTo.concat(className);
		relations.put(key, newRelation);
	}

	public void addInheritance(String classTo, String className, Relation newRelation) {
		String key = classTo.concat(className);
		inheritance.put(key, newRelation);
	}
}
