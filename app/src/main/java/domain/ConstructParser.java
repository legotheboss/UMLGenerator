package domain;

import data_source.LogWriter;
import data_source.MegaContainer;

import java.util.HashMap;
import java.util.HashSet;

public class ConstructParser {

	private HashSet<String[]> inputArgs;
	private String outputFilename;
	public String diagramType;
	public String controllerType;

	public ConstructParser(HashSet<String[]> inputArgs) {
		this.inputArgs = inputArgs;
		this.outputFilename = "diagram.txt";
		this.diagramType = null;
		this.controllerType = null;
	}

	public IController makeController() {
		switch(this.controllerType) {
			case "ASM": 
				ASMController asm = new ASMController();
				return asm;
			default:
				return null;
		}
	}
	
	public IDiagram makeDiagram(MegaContainer MEGA) {
		switch(this.diagramType) {
			case "ClassPlantUML":
				DiagramUMLforPlantUML plantUML = new DiagramUMLforPlantUML(MEGA);
				return plantUML;
//			case "SequencePlantUML":
//				SequenceUMLforPlantUML sequenceUML = new SequenceUMLforPlantUML(MEGA);
//				return sequenceUML;
			default:
				return null;
		}
	}

	public void beginParsing() {
		this.populateParameters();
		
		if (this.controllerType != null && this.diagramType != null) {
			IController controller = makeController();
			controller.execute();
			MegaContainer xyz = controller.retrieve();
			DiagramUMLforPlantUML x = new DiagramUMLforPlantUML(xyz);
			IDiagram diagram = makeDiagram(xyz);

			LogWriter lg = new LogWriter(this.outputFilename);
			lg.writeText(diagram.concatenateFinalString());
		}
	}
	
	public void populateParameters() {
		HashMap<String, String> os = OptionsSingleton.getInstance().options;
		for (String[] currentArgs : inputArgs) {
			String caseArg = currentArgs[0];
				switch (caseArg) {
					case "c":
						os.put("ClassParser", currentArgs[1]);
						break;
					case "r":
						os.put("RecursiveParser", currentArgs[1]);
						break;
					case "rm":
						os.put("RecursiveMethodParser", currentArgs[1]);
						break;
					case "a":
						os.put("AccessParser", currentArgs[1]);
						break;
					case "o":
						os.put("OutputParser", currentArgs[1]);
						break;
					case "b":
						os.put("BlackListParser", currentArgs[1]);
						break;
					case "w":
						os.put("WhiteListParser", currentArgs[1]);
						break;
					case "m":
						os.put("MethodParser", currentArgs[1]);
						break;
					case "dt":
						os.put("diagramType", currentArgs[1]);
						break;
					case "ct":
						os.put("controllerType", currentArgs[1]);
						break;
					case "hp":
						os.put("HollywoodParser", currentArgs[1]);
						break;
					case "dp":
						os.put("DecoratorPatternParser", currentArgs[1]);
						break;
					case "ap":
						os.put("AdapterPatternParser", currentArgs[1]);
						break;
					case "sp":
						os.put("SingletonPatternParser", currentArgs[1]);
						break;
					default:
						break;
				}
			}
		this.diagramType = os.get("diagramType");
		this.controllerType = os.get("controllerType");
		if(os.containsKey("OutputParser")) {
			if(!os.get("OutputParser").isBlank()) {
				System.out.println("OutputParser is" + os.get("OutputParser"));
				this.outputFilename = os.get("OutputParser");
			}
		} else this.outputFilename = "diagram.txt";
	}

	public String getOutputFileName() {
		return this.outputFilename;
	}
}
