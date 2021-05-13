package domain;

import data_source.*;
import data_source.Class;

public class SingletonPattern extends IDetector{
	
	public SingletonPattern(MegaContainer MEGA, String color) {
		this.color = color;
		this.MEGA = MEGA;
	}

	@Override
	public boolean check(String className) {
		Class cl = this.MEGA.classes.get(className);
		
		boolean x = checkClassForPrivateConstructor(cl);
		boolean y = checkForSelfInstance(cl);
		boolean z = checkForPublicMethodSingleton(cl);
		if(x && y && z) {
			classWithLevels.add(className);
			return true;
		} else return false;
	}

	private String getActualClassName(String className) {
		String[] x = className.split("/");
		return x[x.length-1];
	}

	@Override
	public String getLabel(String classOrInterfaceName) {
		if(this.classWithLevels.contains(classOrInterfaceName)) return "singleton";
		else return "";
	}
	
	private boolean checkClassForPrivateConstructor(Class cl) {
		for(Method m : cl.methods) {
			if(m.name.equals("<clinit>") && m.accessLevel == 3) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkForSelfInstance(Class cl) {
		String className = getActualClassName(cl.name);
		for(Field f : cl.fields) {
			if(f.type.equals(className)) return true;
		}
		return false;
	}

	private boolean checkForPublicMethodSingleton(Class cl) {
		for(Method m : cl.methods) {
			if(m.returnType.equals(cl.name) && m.accessLevel == 1) return true;
		}
		return false;
	}

}
