package domain;

import java.util.HashMap;

import data_source.*;
import data_source.Class;

public class AdapterPattern extends IDetector{
	protected HashMap<String, String> classWithLevels = new HashMap<String, String>(); 
	
	public AdapterPattern(MegaContainer MEGA, String color) {
		this.color = color;
		this.MEGA = MEGA;
	}

	@Override
	public boolean check(String className) {
		if(this.classWithLevels.containsKey(className)) return true;
		Class classToCheck = this.MEGA.classes.get(className);
		HashMap<String, String> methodInterface = new HashMap<>();
		for(String i : classToCheck.inherited) {
			if(this.MEGA.interfaces.containsKey(i)) {
				for(Method m : this.MEGA.interfaces.get(i).methods) {
					methodInterface.put(m.name, i);
				}
			} 
		}
		
		if(classToCheck.superclass != null) {
			if(this.MEGA.classes.containsKey(classToCheck.superclass)){
				for(Method m : this.MEGA.classes.get(classToCheck.superclass).methods) {
					methodInterface.put(m.name, classToCheck.superclass);
				}
			}
		}
		
		
		if(!methodInterface.isEmpty()) {	
			for(Field f : classToCheck.fields) {
				for(Method m : classToCheck.methods) {
					if(methodInterface.containsKey(m.name)) {
						for(MethodCalled mc : m.methodsCalled) {
							if(mc.owner.equals(f.packageType)) {
								String mIname = methodInterface.get(m.name);
								if(!(this.MEGA.classes.containsKey(classToCheck.name) && 
								   this.MEGA.classes.containsKey(f.packageType) &&
								   (this.MEGA.classes.containsKey(mIname) || this.MEGA.interfaces.containsKey(mIname)))) break;
								this.classWithLevels.put(classToCheck.name, "adapter");
								this.classWithLevels.put(f.packageType, "adaptee");
								this.classWithLevels.put(mIname, "target");
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkRelation(Relation relation) {
		return this.classWithLevels.containsKey(relation.classFrom.getName()) && this.classWithLevels.containsKey(relation.classTo.getName());
	}

	@Override
	public boolean checkInterface(String interfaceName) {
		return this.classWithLevels.containsKey(interfaceName);
	}

	@Override
	public String getLabel(String classOrInterfaceName) {
		return this.classWithLevels.get(classOrInterfaceName);
	}
	
	@Override
	public String getRelationLabel(Relation relation) {
		String nameClassFrom = relation.classFrom.getName();
		String nameClassTo = relation.classTo.getName();
		if(this.classWithLevels.containsKey(nameClassFrom) && this.classWithLevels.containsKey(nameClassTo)) {
			if (this.classWithLevels.get(nameClassTo).equals("adaptee") && !nameClassFrom.equals(nameClassTo)
					&& !this.MEGA.inheritance.containsValue(relation)) {
				return "<<adapts>>";
			}
		}
		return null;
	}
}
