package domain;

import java.util.HashSet;

import data_source.MegaContainer;
import data_source.Relation;

public abstract class IDetector {
	protected MegaContainer MEGA;
	protected String color;
	protected HashSet<String> classWithLevels = new HashSet<String>(); 
	
	
	public abstract boolean check(String className);
	
	public String getColor() {
		return this.color;
	}
	
	public boolean checkRelation(Relation relation) {
		return false;
	}
	
	public boolean checkInterface(String className) {
		return false;
	}
	
	public String getLabel(String unnecessary) {
		return null;
	}

	public String getRelationLabel(Relation relation) {
		return null;
	}
}
