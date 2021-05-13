package domain;

import data_source.Class;
import data_source.*;

import java.util.ArrayList;
import java.util.HashMap;

public class DiagramUMLforPlantUML implements IUMLDiagram {
	private StringBuilder finalString = new StringBuilder();
	// Allows for the user to set how much can be seen in the final diagram
	// VISIBILITY LEVELS
	// Private = 4
	// Protected = 3
	// Package Private = 2
	// Public = 1
	// Default visibility is private (4)
	private int visibilityAccess = 4;
	private MegaContainer MEGA;
	private HashMap<String, String> relationMap;
	private HashMap<IDetector, Integer> principleDetection;
	private HashMap<String, String> os;
	
	public DiagramUMLforPlantUML(MegaContainer MEGA) {
		this.os = OptionsSingleton.getInstance().options;
		this.principleDetection = new HashMap<>();
		this.relationMap = new HashMap<String, String>();
		this.MEGA = MEGA;
		
		populateRelationMap();
		
		for(String key : os.keySet()) {
			switch (key) {
				case "AccessParser":
					if(!os.get(key).isBlank()) this.visibilityAccess = Integer.parseInt(os.get(key));
					break;
				case "HollywoodParser":
					this.principleDetection.put(new HollywoodPrinciple(this.MEGA, os.get(key)),0);
					break;
				case "DecoratorPatternParser":
					this.principleDetection.put(new DecoratorPattern(this.MEGA, os.get(key)),1);
					break;
				case "AdapterPatternParser":
					this.principleDetection.put(new AdapterPattern(this.MEGA, os.get(key)),2);
					break;
				case "SingletonPatternParser":
					this.principleDetection.put(new SingletonPattern(MEGA, os.get(key)), 3);
				default:
					break;
			}
		}
	}
	
	private void populateRelationMap() {
		this.relationMap.put("Association","-->");
		this.relationMap.put("Dependency","..>");
		this.relationMap.put("Inheritance","..|>");
		this.relationMap.put("BidirectionalDep","<..>");
		this.relationMap.put("BidirectionalAsso","<-->");
	}

	public String concatenateFinalString() {
		//Takes in a list of all the containers, and iterates through them in order
		//Sends each container to its corresponding function
		//Interfaces first, then classes, then relations
		finalString.append("@startuml\n");
		
		for(String cls : MEGA.classes.keySet()) {
			for(IDetector dp : this.principleDetection.keySet()) {
				checkDetectorsForClassColor(cls, dp);
			}
		}
		for(String infc : MEGA.interfaces.keySet()) {
			for(IDetector dp : this.principleDetection.keySet()) {
				checkDetectorsForInterfaceColor(infc, dp);
			}
		}		
		for(String rel : MEGA.relations.keySet()) {
			for(IDetector dp : this.principleDetection.keySet()) {
				checkDetectorsForLabel(MEGA.relations.get(rel), dp);
			}
		}
		
		for(Relation rel : MEGA.inheritance.values()) {
			createRelation(rel);
		}
		
		for(String cls : MEGA.classes.keySet()) {
			createClass(MEGA.classes.get(cls));
		}
		for(String infc : MEGA.interfaces.keySet()) {
			createInterface(MEGA.interfaces.get(infc));
		}		
		for(String rel : MEGA.relations.keySet()) {
			createRelation(MEGA.relations.get(rel));
		}
		
		finalString.append("\n@enduml");
		return finalString.toString();
	}

	public void createInterface(Interface interfaceToCreate) {
		if(interfaceToCreate.accessLevel <= visibilityAccess) {
			finalString.append("interface " + interfaceToCreate.name.replace('/', '.'));
			
			String[] colorAndLabel = {null, null};
			String[] test = {null, null};
			int priority = -1;
			for(IDetector dp : this.principleDetection.keySet()) {
				if(this.principleDetection.get(dp)>priority) {
					test = checkDetectorsForInterfaceColor(interfaceToCreate.name, dp);
					if(test[0]!=null || test[1]!=null) {
						priority = this.principleDetection.get(dp);
						colorAndLabel = test;
					}
				}
			}
			// add the label if needed (this is the <<example>> thing)
			if (colorAndLabel[1] != null) finalString.append(" <<" + colorAndLabel[1] + ">>");
			// add the color if needed
			if (colorAndLabel[0] != null) finalString.append(" #" + colorAndLabel[0]);
			
			finalString.append(" {\n");
			fillInterfaceMethods(interfaceToCreate);
			finalString.append("}\n");
		}
	}

	public void fillInterfaceMethods(Interface interfaceToFill) {
		for (Method currMethod : interfaceToFill.methods) {
			if(currMethod.name.contains("<") && currMethod.name.contains(">")) continue;
			if(currMethod.accessLevel <= visibilityAccess) {
				finalString.append(accessLevelSymbol(currMethod.accessLevel));
				finalString.append(currMethod.name + "(");
				fillArgs(currMethod);	
				if(!currMethod.holdingTypes.isEmpty()){
					finalString.append(") : ");
					createCollection(currMethod.holdingTypes);
					
				} else finalString.append(") : " + currMethod.returnType.split("\\/")[currMethod.returnType.split("\\/").length-1]);
				finalString.append("\n");		
			}
		}	
	}

	public void createClass(Class classToCreate) {
		if(classToCreate.accessLevel <= visibilityAccess) {
			if(classToCreate.isAbstract) finalString.append("abstract ");
			finalString.append("class " + classToCreate.name.replace('/', '.'));
			
			String[] colorAndLabel = {null, null};
			String[] test = {null, null};
			int priority = -1;
			for(IDetector dp : this.principleDetection.keySet()) {
				if(this.principleDetection.get(dp)>priority) {
					test = checkDetectorsForClassColor(classToCreate.name, dp);
					if(test[0]!=null || test[1]!=null) {
						priority = this.principleDetection.get(dp);
						colorAndLabel = test;
					}
				}
			}
			// add the label if needed (this is the <<example>> thing)
			if (colorAndLabel[1] != null) finalString.append(" <<" + colorAndLabel[1] + ">>");
			// add the color if needed
			if (colorAndLabel[0] != null) finalString.append(" #" + colorAndLabel[0]);
			
			finalString.append(" { \n");
			fillFields(classToCreate);
			fillClassMethods(classToCreate);
			finalString.append("}\n");
		}
	}

	public void fillFields(Class classToFill) {
		for (Field currField : classToFill.fields) {
			if(currField.accessLevel <= visibilityAccess) {
				finalString.append(accessLevelSymbol(currField.accessLevel));
				
				if(currField.holdingTypes.isEmpty()) finalString.append(currField.name + " : " + currField.type);
				else {
					finalString.append(currField.name + " : " );
					createCollection(currField.holdingTypes);
				}
				
				finalString.append("\n");
			}
		}

	}

	public void fillClassMethods(Class classToFill) {
		boolean isNotInterfaceMethod;
		for (Method currMethod : classToFill.methods) {
			if(currMethod.name.contains("<") && currMethod.name.contains(">")) continue;
			isNotInterfaceMethod = true;
			for(String intf : classToFill.inherited) {
				if (MEGA.interfaces.containsKey(intf)) {
					if (!MEGA.interfaces.get(intf).methods.isEmpty()) {
						for (Method method : MEGA.interfaces.get(intf).methods) {
								if (method.name == currMethod.name) {
									isNotInterfaceMethod = false;
								}
						} 
					} 
				}
			}
			if (isNotInterfaceMethod) {
				if(currMethod.accessLevel <= visibilityAccess) {
					finalString.append(accessLevelSymbol(currMethod.accessLevel));
					finalString.append(currMethod.name + "(");
					fillArgs(currMethod);
					
					if(!currMethod.holdingTypes.isEmpty()){
						finalString.append(") : ");
						createCollection(currMethod.holdingTypes);
						
					} else {
						finalString.append(") : " + currMethod.returnType.split("\\/")[currMethod.returnType.split("\\/").length-1]);
					}
					
					finalString.append("\n");
				}
			}
		}
	}

	public void fillArgs(Method methodToFill) {
		int i = 0;
		for(Argument currArg : methodToFill.parameters) {
			if(i > 0) {
				finalString.append(", ");
			}
			
			if(!currArg.holdingTypes.isEmpty()) {
				finalString.append(currArg.name + " : ");
				createCollection(currArg.holdingTypes);
			} else finalString.append(currArg.name + " : " + currArg.type);
			
			
			i++;
		}
	}

	private String accessLevelSymbol(int accessLevel) {
		switch(accessLevel) {
		case 1:
			return "+";
		case 2:
			return "~";
		case 3:
			return "#";
		case 4:
			return "-";
		default:
			return "";
		}
	}

	public void createRelation(Relation relation) {
		//Appends relations to final string 
		//Example: className1 --> className2
		finalString.append(relation.classFrom.getName().replace('/', '.'));
		finalString.append(relation.classFromCardinal);
		finalString.append(this.relationMap.get(relation.type));
		finalString.append(relation.classToCardinal);
		finalString.append(relation.classTo.getName().replace('/', '.'));
		String label = null;
		String test = null;
		int priority = -1;
		for(IDetector dp : this.principleDetection.keySet()) {
			test = checkDetectorsForLabel(relation,dp);
			if(test!=null && this.principleDetection.get(dp)>priority) {
				priority = this.principleDetection.get(dp);
				label = test;
			}
		}
		if(label != null) {
			finalString.append(" :" + label);
		}
		finalString.append("\n");
	}

	private void createCollection(ArrayList<String> collection) {
		if(!collection.isEmpty()) {
			String starter = "<";
			String ender = ">";
			if(collection.size()==1) {
				starter = "[";
				ender = "]";
			}
			
			String mainType = collection.get(0);
			mainType = mainType.split("/")[mainType.split("/").length-1].split(";")[0];
			int x = 0;
			finalString.append(mainType + starter);
			for(int i = 1; i<collection.size(); i++) {
				String myStr = collection.get(i);
				myStr = myStr.split("/")[myStr.split("/").length-1];
				if(myStr.charAt(myStr.length()-1) != ';') {
						finalString.append(myStr + "<");
						x++;
				}	
				else if(i+1 == collection.size()) finalString.append(myStr.substring(0, myStr.length()-1));
				else finalString.append(myStr.substring(0, myStr.length()-1) + ",");
			}	
			for(int i = 0; i<x+1; i++) {
				finalString.append(ender);
			}
			
		}
	}
	
	private String checkDetectorsForLabel(Relation relation, IDetector currDetector) {
		if(currDetector.checkRelation(relation)) {
			return currDetector.getRelationLabel(relation);
		}
		return null;
	}
	
	private String[] checkDetectorsForClassColor(String className, IDetector currDetector) {
		String[] toReturn = new String[2];
		if(currDetector != null) {
			if(currDetector.check(className)) {
				toReturn[0] = currDetector.getColor();
				toReturn[1] = currDetector.getLabel(className);
			}
		}
		return toReturn;
	}
	
	private String[] checkDetectorsForInterfaceColor(String interfaceName, IDetector currDetector) {
		String[] toReturn = new String[2];
		if(currDetector.checkInterface(interfaceName)) {
			toReturn[0] = currDetector.getColor();
			toReturn[1] = currDetector.getLabel(interfaceName);
		}
		return toReturn;
	}


	
}
