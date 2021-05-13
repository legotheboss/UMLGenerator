package domain;

import data_source.Class;
import data_source.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ASMController implements IController {
	private List<String> classNames;
	private String blackListFile;
	private String whiteListFile;
	private HashSet<String> blacklist = new HashSet<String>();
	private HashSet<String> whitelist = new HashSet<String>();
	public HashMap<String, String> os;
	private boolean recursion = false;
	private MegaContainer MEGA;

	public ASMController() {
		this.MEGA = new MegaContainer();
		this.os = OptionsSingleton.getInstance().options;
		this.classNames = new ArrayList<>();
		this.blackListFile = "blacklist.txt";
		this.whiteListFile = "whitelist.txt";
		//Goes through decorators to understand run configuration

		for(String key : os.keySet()) {
			switch (key) {
			case "RecursiveParser":
				this.recursion = Boolean.getBoolean(os.get(key));
				break;
			case "ClassParser":
				this.classNames.add(os.get(key));
				break;
			case "BlackListParser":
				if(!os.get(key).isBlank()) this.blackListFile = os.get(key);
				break;
			case "WhiteListParser":
				if(!os.get(key).isBlank()) this.whiteListFile = os.get(key);
				break;
			case "MethodParser":
				this.classNames.add(os.get(key));
			default:
				break;
			}
		}
		
		

		FileReader getBlackList = new FileReader(this.blackListFile);
		this.blacklist = getBlackList.blockEm();
		FileReader getWhiteList = new FileReader(this.whiteListFile);
		this.whitelist = getWhiteList.blockEm();
	}

	public void execute() {
		if (!this.classNames.isEmpty()) {
			try {
				for (String className : classNames) {
					if (this.recursion) recursiveMethod(className);
					else nonRecursiveMethod(className);
				}
			} catch(IOException e) {
				System.out.println(e);
				//this.MEGA.classes.put(key, value)
			}
		} else System.exit(0);

		parseRelations();
	}

	private void nonRecursiveMethod(String currentClass) throws IOException {
		parseClass(currentClass);
	}

	private void recursiveMethod(String currentClass) throws IOException {
		ClassReader tempReader = new ClassReader(currentClass);
		ClassNode tempNode = new ClassNode();
		tempReader.accept(tempNode, ClassReader.EXPAND_FRAMES);
		if (tempNode.superName != null && !tempNode.superName.isEmpty() && !containedInBlacklist(currentClass)) {
			recursiveMethod(tempNode.superName);
		}
		parseClass(tempNode.name);
	}

	private String createType(String type) {
		switch (type) {
		case "Z":
			return "boolean";
		case "C":
			return "char";
		case "B":
			return "byte";
		case "S":
			return "short";
		case "I":
			return "int";
		case "F":
			return "float";
		case "J":
			return "long";
		case "D":
			return "double";
		case "Ljava/lang/Object;":
			return "Object";
		case "[I":
			return "int[]";
		case "[[Ljava/lang/Object;":
			return "Object[][]";
		default:
			if(type.charAt(0) == '[' && type.length() == 1) {
				return "";
			} else if (type.charAt(0) == '[') {
				return createType(type.substring(1)) + "[]";
			} else if (type.charAt(0) == 'L'){
				String[] className = type.replace(";","").substring(1).split("\\/");
				return className[className.length-1];
			}
			else return type;
		}
	}
	
	private ArrayList<String> processArchaicTypes(String input) {
		ArrayList<String> output = new ArrayList<>();
		int x = input.length();
		
		for(int i = 0; i < x; i++) {
			String current = input.substring(i, i+1);
			if(current.charAt(0) == 'L') {
				output.add(input.substring(i));
				break;
			}
			else output.add(createType(current));
		}
		return output;
	}

	private int createAccessLevel(int accessLevel) {
		if ((accessLevel & Opcodes.ACC_PUBLIC) != 0) return 1;
		if ((accessLevel & Opcodes.ACC_PROTECTED) != 0) return 2;
		if ((accessLevel & Opcodes.ACC_PRIVATE) != 0) return 4;
		return 3;
	}

	private void parseInterfaces(String givenClass) throws IOException {
		ClassReader reader = new ClassReader(givenClass);
		ClassNode classNode = new ClassNode();
		reader.accept(classNode, ClassReader.EXPAND_FRAMES);

		for (String current : classNode.interfaces) {
			parseInterface(current);
		}
	}
	
	private void parseInterface(String current) throws IOException {
		current = current.replace('.', '/');
		if (containedInBlacklist(current)) return;
		if(current.contains("[]")) current = current.split("\\[")[0];
		
		ClassReader reader = new ClassReader(current);
		ClassNode tempNode = new ClassNode();
		reader.accept(tempNode, ClassReader.EXPAND_FRAMES);

		this.blacklist.add(current);
		parseInterfaces(current);
		Interface currentInterface = new Interface();
		currentInterface.name = current;		
		currentInterface.methods = methodArgumentGetter(tempNode.methods, currentInterface.name);
		
		for(String inher : tempNode.interfaces) {
			currentInterface.inherited.add(inher);
		}
		
		this.MEGA.interfaces.put(currentInterface.name, currentInterface);
	}

	private void parseClass(String givenClass) throws IOException {
		givenClass = givenClass.replace('.', '/');
		if(containedInBlacklist(givenClass)) return;
		if(givenClass.contains("[]")) givenClass = givenClass.split("\\[")[0];
		
		ClassNode classNode = null;
		try {
			ClassReader reader = new ClassReader(givenClass);
			classNode = new ClassNode();
			reader.accept(classNode, ClassReader.EXPAND_FRAMES);
		} catch(Exception e) {
			return;
		}
		parseInterfaces(givenClass);
		
		if((classNode.access & Opcodes.ACC_INTERFACE) != 0) parseInterface(givenClass);
		else {
			Class curClass = new Class();
			
			if((classNode.access & Opcodes.ACC_ABSTRACT) != 0) curClass.isAbstract = true;
	
			curClass.name = createType(Type.getObjectType(classNode.name).getClassName().replace(".", "/"));
			this.blacklist.add(givenClass);
			
			curClass.accessLevel = createAccessLevel(classNode.access);
			curClass.superclass = classNode.superName;
			curClass.methods = methodArgumentGetter(classNode.methods, curClass.name);
			curClass.fields = fieldBuilder(classNode.fields);
			
			for(String inher : classNode.interfaces) {
				curClass.inherited.add(inher);
			}
			if(curClass.superclass!=null) curClass.inherited.add(curClass.superclass);
			
			this.MEGA.classes.put(classNode.name, curClass);
		}
	}
	
	private HashSet<Field> fieldBuilder(List<FieldNode> fNodes) {
		HashSet<Field> fields = new HashSet<>();
		for (FieldNode fNode : fNodes) {
			Field newField = new Field();
			newField.name = fNode.name;
			newField.accessLevel = createAccessLevel(fNode.access);
			newField.type = createType(
					Type.getType(fNode.desc).toString().split("/")[Type.getType(fNode.desc).toString().split("/").length - 1]
					.split(";")[0]);
			newField.holdingTypes.addAll(deconstructSignature(fNode.signature));
			String objType = Type.getType(fNode.desc).toString();
			String type = typeCheck(objType);
			newField.packageType = type;
			if(objType.charAt(0) == '[') newField.holdingTypes.add(type);
			
			fields.add(newField);
			try { parseClass(type); } 
			catch (IOException e) { e.printStackTrace(); }
		}
		
		return fields;
	}
	
	private String typeCheck(String objType) {
		if(objType.charAt(0) == 'L') return objType.substring(1).split(";")[0];
		else if(objType.charAt(0) == '['){
			if(objType.charAt(1)=='L') return createType(objType.substring(2).split(";")[0]);
			else return createType(objType.substring(1).split(";")[0]);
		}else return createType(objType.split(";")[0]);
	}

	private ArrayList<Method> methodArgumentGetter(List<MethodNode> methods, String className) throws IOException {
		ArrayList<Method> argSet = new ArrayList<Method>();
		for (MethodNode currNode : methods) {
			Method currentMethod = new Method();
			currentMethod.name = currNode.name;
			if(currentMethod.name.equals("<init>")) continue;
			
			currentMethod.accessLevel = createAccessLevel(currNode.access);
			currentMethod.returnType = Type.getReturnType(currNode.desc).getClassName().replace(".", "/");
			
			if(currNode.signature != null) {
				String output = currNode.signature.split("\\)")[1];
				if(output != null) currentMethod.holdingTypes.addAll(deconstructSignature(output));
			}
			
			if(!currentMethod.returnType.toLowerCase().contains("void")) 
				parseClass(currentMethod.returnType);
			
			Type[] argumentTypes = Type.getArgumentTypes(currNode.desc);
			List<LocalVariableNode> localVariables = currNode.localVariables;
			InsnList ins = currNode.instructions;

			resolveMethodArguments(localVariables, argumentTypes, currentMethod);
			
			manageLocalVariables(localVariables, argumentTypes, currentMethod);

            buildArguments(ins, currentMethod);

			argSet.add(currentMethod);
		}
		return argSet;
	}

	private void manageLocalVariables(List<LocalVariableNode> localVariables, Type[] argumentTypes, Method currentMethod) throws IOException {
		if (localVariables != null && !localVariables.isEmpty()) {
			for (int k = argumentTypes.length; k < localVariables.size(); k++) {
				if (localVariables.get(k).name.equals("this")) continue;
				String objType = Type.getType(localVariables.get(k).desc).toString();
				if(objType.charAt(0) == 'L') {
					String type = createType(objType.substring(1).split(";")[0].replace(".", "/"));
					currentMethod.localVariableTypes.add(type);
					parseClass(type);
				}
			}
		}
	}

	private void buildArguments(InsnList ins, Method currentMethod) throws IOException {

		int insSize = ins.size();

		for(int k = 0; k < insSize ; k++) {
			AbstractInsnNode yolo = ins.get(k);
			if(yolo.getType() == 5) {
				MethodInsnNode methodInst = (MethodInsnNode) yolo;
				if(methodInst.name.equals("<init>")) continue;
				parseClass(methodInst.owner);
				MethodCalled internal = new MethodCalled(methodInst.name, methodInst.owner);

				String variable_type = methodInst.desc.split("\\(")[1].split("\\)")[0];

				if(!variable_type.isBlank()) {
					String[] splitArgs = variable_type.split(";");
					for(String x : splitArgs) {
						boolean generic = x.charAt(0) == 'L' || x.charAt(0) == 'T';
						if(!x.contains(">")) {
							if(generic) {
								internal.args.add(x.substring(1));
							}
							else if(x.charAt(0) == '[') {
								internal.args.add("[] "+ x.substring(1));
							}
							else if(!createType(""+x.charAt(0)).equals(""+x.charAt(0))) {
								internal.args.addAll(processArchaicTypes(x));
							}
							else if(x.length()>1 && x.charAt(1) == 'T') {
								internal.args.add(x.substring(2));
							}
							else internal.args.add(x);
						}
						else if(generic) {
							internal.args.add(x.substring(1));
						}
					}
				}

				String returnType =  methodInst.desc.split("\\(")[1].split("\\)")[1];
				if(!returnType.isBlank()) {
					internal.returnType = createType(returnType.substring(0, returnType.length()));
				}
				currentMethod.methodsCalled.add(internal);
			}
		}
	}

	private void resolveMethodArguments(List<LocalVariableNode> localVariables, Type[] argumentTypes, Method currentMethod) throws IOException{
		int i = 0;
		int j = 0;
		while (currentMethod.parameters.size() < argumentTypes.length) {
			if (localVariables == null || localVariables.isEmpty()) break;
			if (localVariables.get(i).name.equals("this")) {
				if (localVariables.size() > i + 1) i++;
				else break;
			}
			Argument newArgument = new Argument();
			newArgument.name = localVariables.get(i).name;
			String[] typeToInterpret = argumentTypes[j].toString().split("/");
			String typeWithSemicolon = typeToInterpret[typeToInterpret.length - 1];
			if (typeWithSemicolon.contains(";")) {
				typeWithSemicolon = typeWithSemicolon.substring(0, typeWithSemicolon.length()-1);
			}
			newArgument.type = createType(typeWithSemicolon);

			if (!newArgument.type.toString().equals(createType(localVariables.get(i).desc.split("/")
					[localVariables.get(i).desc.split("/").length - 1].split(";")[0]))) {
				i++;
				continue;
			}
			newArgument.holdingTypes.addAll(deconstructSignature(localVariables.get(i).signature));
			String objType = Type.getType(localVariables.get(i).desc).toString();
			if(objType.charAt(0) == 'L') {
				String type = createType(objType.substring(1).split(";")[0].replace(".", "/"));
				newArgument.packageType = type;
				parseClass(type);
			}
			i++;
			j++;
			currentMethod.parameters.add(newArgument);
		}
	}

	private void parseRelations() {
		ParseRelationships pr = new ParseRelationships(this.MEGA);
		pr.buildRelationships();
	}

	@Override
	public MegaContainer retrieve() {
		return this.MEGA;
	}
	
	private boolean containedInBlacklist(String className) {
		className = className.split(";")[0];
		if(blacklist.contains(className)) return true;
		if(whitelist.contains(className)) return false;
		if(className.contains("[]")) return containedInBlacklist(className.split("\\[")[0]);
		String[] splitName = className.split("/|\\.");
		if(blacklist.contains(splitName[0] + "/*")) return true;
		if(splitName.length > 1)
			if(blacklist.contains(splitName[0] + "/" + splitName[1] + "/*")) return true;
		return false;
	}
	
	public ArrayList<String> deconstructSignature(String signature) {
		ArrayList<String> result = new ArrayList<String>();
		if (signature == null) return result;
		String[] testing = signature.split("<");
		for(String x : testing) {
			if(x.contains(";")) {
				for(String y : x.split(";")) {
					if(!y.contains(">")) {
						if(y.charAt(0) == 'L' || y.charAt(0) == 'T') result.add(y.substring(1) + ";");
						else if(y.length()>1 && y.charAt(1) == 'T') result.add(y.substring(2)+";");
						else result.add(y + ";");
					}
				}
			} 
			else if(x.charAt(0) == 'L' || x.charAt(0) == 'T') result.add(x.substring(1));
		}
		return result;
	}

}
