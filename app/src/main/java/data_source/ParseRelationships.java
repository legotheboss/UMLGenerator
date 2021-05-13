package data_source;

public class ParseRelationships {
	public MegaContainer MEGA;

	public ParseRelationships(MegaContainer MEGA) {
		this.MEGA = MEGA;
	}
	
	public void buildRelationships() {
		buildInheritances();
		buildDependencies();
		buildAssociations();
	}
	
	public void buildDependencies() {
		for(String className : MEGA.getMegaClassKeys()) {
			Class currClass = (Class) MEGA.getMegaClass(className);	
			processClass(currClass, className);
		}
	}
	
	private void processClass(Class currClass, String className) {
		for(Method method : MEGA.getClassMethods(className)) {
			processLVTypes(currClass, className, method);
			processParameters(currClass, className, method);
			IContainer classTo = getReturnSuperClass(method);
			if(classTo == null)	continue;
			else
				updateMegaRelations(currClass, classTo, className);
		}
	}
	
	private void updateMegaRelations(Class currClass, IContainer classTo, String className) {
		if(MEGA.hasRelation(classTo.getName(), className)) {
			if (classTo.getName().equals(className)) return;
			if (MEGA.isDependency(classTo.getName(), className)) {
				Relation newRelation = new Relation(currClass.accessLevel, "BidirectionalDep", currClass, classTo, " ", " ");
				MEGA.addRelation(classTo.getName(), className, newRelation);
			}
		} else {
			Relation newRelation = new Relation(currClass.accessLevel, "Dependency", currClass, classTo, " ", " ");
			MEGA.addRelation(currClass.name, classTo.getName(), newRelation);
		}
	
	}

	private IContainer getReturnSuperClass(Method method) {
		IContainer classTo = MEGA.getMegaClass(method.returnType);
		if(classTo==null) {
			classTo = MEGA.getMegaInterface(method.returnType);
		} 
		return classTo;
	}

	private void processParameters(Class currClass, String className, Method method) {
		for(Argument parameter : method.parameters) {
			IContainer classTo = getParameterSuperClass(parameter);
			
			if(classTo == null) continue;
			else {
				if(isDependentRelationship(classTo, className)){
					Relation newRelation = new Relation(currClass.accessLevel, "BidirectionalDep", currClass, classTo, " ", " ");
					MEGA.addRelation(classTo.getName(), className, newRelation);
				} else {
					Relation newRelation = new Relation(currClass.accessLevel, "Dependency", currClass, classTo, " ", " ");
					MEGA.addRelation(className, classTo.getName(), newRelation);
				}
			
				processParameterHoldingTypes(parameter, className, currClass);
			}
		}
	}

	private boolean isDependentRelationship(IContainer classTo, String className) {
		return MEGA.hasRelation(classTo.getName(), className) && 
				MEGA.isDependency(classTo.getName(), className);
	}

	private void processParameterHoldingTypes(Argument parameter, String className, Class currClass) {
		for(int i = 0; i < parameter.holdingTypes.size(); i++) {
			String classToName = parameter.holdingTypes.get(i);
			if(!classToName.contains(";")) continue;
			if(classToName.charAt(0)=='[') classToName = classToName.substring(2);
			classToName = classToName.split(";")[0];
			
			IContainer classTo = getHoldingTypeSuperClass(classToName);
			
			if(classTo == null) continue;
			else {
				if(MEGA.hasRelation(classTo.getName(), className)) {
					if (classTo.getName().equals(className)) continue;
					if (MEGA.isDependency(classTo.getName(), className)) {
						Relation newRelation = new Relation(currClass.accessLevel, "BidirectionalDep", currClass, classTo, "\"*\"", "\"*\"");
						MEGA.addRelation(classTo.getName(), className, newRelation);
					}
				} else {
					Relation newRelation = new Relation(currClass.accessLevel, "Dependency", currClass, classTo, " ", "\"*\"");
					MEGA.addRelation(currClass.name, classTo.getName(), newRelation);
				}
			}
		}
	}

	private IContainer getHoldingTypeSuperClass(String classToName) {
		IContainer classTo = MEGA.getMegaClass(classToName);
		if(classTo==null) {
			classTo = MEGA.getMegaInterface(classToName);
		}
		
		return classTo;
	}

	private IContainer getParameterSuperClass(Argument parameter) {
		IContainer classTo = MEGA.getMegaClass(parameter.packageType);
		if(classTo==null) {
			classTo = MEGA.getMegaInterface(parameter.packageType);
		}
		
		return classTo; 
	}

	private void processLVTypes(Class currClass, String className, Method method) {
		for(String lvType : method.localVariableTypes) {
			IContainer classTo = MEGA.getMegaClass(lvType);
			if(classTo==null) {
				classTo = MEGA.getMegaInterface(lvType);
				if(classTo==null) continue;
			} 
			
			if(MEGA.hasRelation(className, classTo.getName())) {
				if(classTo.getName().equals(className)) continue;
			}
			
			if(isDependentRelationship(classTo, className)){
				Relation newRelation = new Relation(currClass.accessLevel, "BidirectionalDep", currClass, classTo, " ", " ");
				MEGA.addRelation(classTo.getName(), className, newRelation);
			} else {
				Relation newRelation = new Relation(currClass.accessLevel, "Dependency", currClass, classTo, " ", " ");
				MEGA.addRelation(currClass.name, classTo.getName(), newRelation);
			}
		
			processMethodHoldingTypes(method, className, currClass);
		}
	}
	
	private void processMethodHoldingTypes(Method method, String className, Class currClass) {
		for(int i = 0; i < method.holdingTypes.size(); i++) {
			String classToName = method.holdingTypes.get(i);
			if(!classToName.contains(";")) continue;
			if(classToName.charAt(0)=='[') classToName = classToName.substring(2);
			classToName = classToName.split(";")[0];
			
			IContainer classTo = getHoldingTypeSuperClass(classToName);
			
			if(classTo == null) continue;
			else {
				if(MEGA.hasRelation(classTo.getName(), className)) {
					if (classTo.getName().equals(className)) continue;
					if (MEGA.isDependency(classTo.getName(), className)) {
						Relation newRelation = new Relation(currClass.accessLevel, "BidirectionalDep", currClass, classTo, "\"*\"", "\"*\"");
						MEGA.addRelation(classTo.getName(), className, newRelation);
					}
				} else {
					Relation newRelation = new Relation(currClass.accessLevel, "Dependency", currClass, classTo, " ", "\"*\"");
					MEGA.addRelation(currClass.name, classTo.getName(), newRelation);				
				}
			}
		}
	}

	public void buildAssociations() {
		for(String className : MEGA.getMegaClassKeys()) {
			Class currClass = (Class) MEGA.getMegaClass(className);	
			for(Field field : MEGA.getClassFields(className)) {
				IContainer classTo = MEGA.getMegaClass(field.packageType);
				if(classTo==null) classTo = MEGA.getMegaInterface(field.packageType);
				if(classTo!=null) {
					if(MEGA.hasRelation(classTo.getName(), className)){
						if (classTo.getName().equals(className)) continue;
						if (MEGA.isAssociation(classTo.getName(), className)) {
							Relation newRelation = new Relation(currClass.accessLevel, "BidirectionalAsso", currClass, classTo, " ", " ");
							MEGA.addRelation(classTo.getName(), className, newRelation);
						}
					} else {
						Relation newRelation = new Relation(currClass.accessLevel, "Association", currClass, classTo, " "," ");
						MEGA.addRelation(className, classTo.getName(), newRelation);
					}
				} 
								
				for(int i = 0; i < field.holdingTypes.size(); i++) {
					classTo = getAppropriateClass(field.holdingTypes.get(i));
					if(classTo == null) continue;
					if(MEGA.hasRelation(classTo.getName(), className)){
						if (classTo.getName().equals(className)) continue;
						if (MEGA.isAssociation(classTo.getName(), className)) {
							Relation newRelation = new Relation(currClass.accessLevel, "BidirectionalAsso", currClass, classTo, "\"*\"","\"*\"");
							MEGA.addRelation(classTo.getName(), className, newRelation);
						}
					} else {
						Relation newRelation = new Relation(currClass.accessLevel, "Association", currClass, classTo, " ","\"*\"");
						MEGA.addRelation(className, classTo.getName(), newRelation);
					}
				}
			}
		}
	}

	public void buildInheritances() {
		for (String className : MEGA.getMegaClassKeys()) {
			Class cClass = (Class) MEGA.getMegaClass(className);

			if (cClass.superclass != null) {
				IContainer classTo = MEGA.getMegaClass(cClass.superclass);
				if (classTo != null) {
					Relation newRelation = new Relation(cClass.accessLevel, "Inheritance", cClass, classTo, " ", " ");
					MEGA.addInheritance(className, classTo.getName(), newRelation);
				}
			}

			for (String iName : cClass.inherited) {
				IContainer classTo = MEGA.getMegaInterface(iName);
				if (classTo != null) {
					Relation newRelation = new Relation(cClass.accessLevel, "Inheritance", cClass, classTo, " ", " ");
					MEGA.addInheritance(className, classTo.getName(), newRelation);
				}
			}
		}

		for (String className : MEGA.getMegaInterfaceKeys()) {
			Interface cInterface = (Interface) MEGA.getMegaInterface(className);
			for (String iName : cInterface.inherited) {
				IContainer classTo = MEGA.getMegaInterface(iName);
				if (classTo != null) {
					Relation newRelation = new Relation(cInterface.accessLevel, "Inheritance", cInterface, classTo, " ", " ");
					MEGA.addInheritance(className, classTo.getName(), newRelation);
				}
			}
		}
	}

	private IContainer getAppropriateClass(String classToName) {
		IContainer classTo;
		if(!classToName.contains(";"));
		if(classToName.charAt(0)=='[') classToName = classToName.substring(2);
		classToName = classToName.split(";")[0];

		classTo = MEGA.getMegaClass(classToName);
		if(classTo==null) {
			classTo = MEGA.getMegaInterface(classToName);
		}
		return classTo;
	}
}