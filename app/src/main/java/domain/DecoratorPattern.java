package domain;

import java.util.HashMap;

import data_source.Interface;
import data_source.MegaContainer;
import data_source.Relation;

public class DecoratorPattern extends IDetector {
	protected HashMap<String, String> classWithLevels = new HashMap<String, String>(); 

	public DecoratorPattern(MegaContainer MEGA, String color) {
		this.MEGA = MEGA;
		this.color = color;
	}

	@Override
	public boolean check(String className) {
		// Figure out if the bottom class is a decorator
		// Add the class and interface above it to the hashmap so they can easily be checked later
		if (this.classWithLevels.containsKey(className)) return true;
		data_source.Class classToCheck = this.MEGA.classes.get(className);
		if (classToCheck.superclass != null) {
			data_source.Class secondLevelClass = this.MEGA.classes.get(classToCheck.superclass);
			if(secondLevelClass != null) {
				for (String inher : secondLevelClass.inherited) {
					Interface thirdLevelInterface = this.MEGA.interfaces.get(inher);
					if (thirdLevelInterface != null) {
						this.classWithLevels.put(thirdLevelInterface.name, "component");
						this.classWithLevels.put(secondLevelClass.name, "decorator");
						this.classWithLevels.put(classToCheck.name, "decorator");
						return true;
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
		if(this.classWithLevels.containsKey(nameClassFrom) && this.classWithLevels.containsKey(nameClassTo)
				&& !this.MEGA.inheritance.containsValue(relation)) {
			if (this.classWithLevels.get(nameClassTo).equals("component")) {
				return "<<decorates>>";
			}
		}
		return null;
	}

}
