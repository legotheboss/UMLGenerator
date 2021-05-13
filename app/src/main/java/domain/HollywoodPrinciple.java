package domain;

import java.util.HashSet;

import data_source.*;
import data_source.Class;

public class HollywoodPrinciple extends IDetector {
	// Don't call us, we'll call you
	
	public HollywoodPrinciple(MegaContainer MEGA, String color) {	
		this.MEGA = MEGA;
		this.color = color;
	}
	
	public boolean check(String className) {
		HashSet<String> usedClasses = new HashSet<>();
		HashSet<String> inherited = new HashSet<>();
		Class x = MEGA.classes.get(className);
		
		if(x != null) {
			addPackageTypes(x ,usedClasses);
			addMethodInfo(x, usedClasses);
			inherited.addAll(x.inherited);
		} else {
			addInterfaceInfo(x, usedClasses, className, inherited);
		}
		return helper(usedClasses, inherited);
	}
	
	private void addPackageTypes(Class x, HashSet<String> usedClasses) {
		for(Field y : x.fields) {
			usedClasses.add(y.packageType);
		}
	}
	
	private void addMethodInfo(Class x ,HashSet<String> usedClasses) {
		for(Method a : x.methods) {
			for(MethodCalled mc : a.methodsCalled) {
				usedClasses.add(mc.owner);
			}
			for(Argument b : a.parameters) {
				usedClasses.add(b.packageType);
			}
			usedClasses.addAll(a.localVariableTypes);
		}
	}
	
	private void addInterfaceInfo(Class x ,HashSet<String> usedClasses, String className, HashSet<String> inherited ) {
		Interface y = MEGA.interfaces.get(className);
		{
			for(Method z : y.methods) {
				for(Argument b : z.parameters) {
					usedClasses.add(b.packageType);
				}
			}
			inherited.addAll(y.inherited);
		}
	}
	
	public boolean helper(HashSet<String> beingUsed, HashSet<String> inherited) {
		if(inherited.isEmpty()) return false;
		boolean result = false;
		for(String i : inherited) {
			if(result) return true;
			if(beingUsed.contains(i)) return true;
			if(MEGA.classes.get(i) != null) result = result || helper(beingUsed, this.MEGA.classes.get(i).inherited);
			else if(MEGA.interfaces.get(i) != null) result = result || helper(beingUsed, this.MEGA.interfaces.get(i).inherited);
		}
		return result;
	}
}
