package data_source;

import java.util.ArrayList;

public class MethodCalled extends IContainer {
	public String functionName;
	public ArrayList<String> args = new ArrayList<>();
	public String owner;	
	public String returnType;
	
	public MethodCalled(String name, String owner) {
		functionName = name;
		this.owner = owner;
	}

	public String getName() {
		return null;
	}

	@Override
	public String toString() {
		return ("	Class: " + owner + "'s " + functionName + " 	Args: " + args.toString() + "  Returns: " + returnType);
	}
}
