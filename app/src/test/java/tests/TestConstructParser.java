package tests;

import domain.ASMController;
import domain.ConstructParser;
import domain.DiagramUMLforPlantUML;
import domain.IController;
import domain.IDiagram;
import domain.OptionsSingleton;
import org.junit.Test;

import data_source.MegaContainer;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestConstructParser {
	public HashSet<String[]> inputArgs = new HashSet<>(); 
	public String argument = "-c presentation.Main -r true -hp skyblue -ct ASM -dt ClassPlantUML -sp blue -ap red -dp blue";
	
	public String[] args = argument.split("\\s+");
	
	ConstructParser conparASM;
	
	public TestConstructParser() {
		this.populateInputArgs(this.args);
		this.conparASM =  new ConstructParser(inputArgs);
	}
	
	@Test
	public void testMakeASMControllerValid() {
		conparASM.controllerType = "ASM";
		ASMController asm = new ASMController();
		assertEquals(asm.getClass(), conparASM.makeController().getClass());
	}
	
	@Test
	public void testMakeASMControllerInvalid() {
		conparASM.controllerType = "AXM";
		IController result = conparASM.makeController();
		assertEquals(null, result);
	}
	
	@Test
	public void testMakeDiagramValid() {
		this.conparASM =  new ConstructParser(inputArgs);
		this.conparASM.populateParameters();
		conparASM.controllerType = "ASM";
		conparASM.diagramType = "ClassPlantUML";
		
		IController controller = this.conparASM.makeController();
		controller.execute();
		MegaContainer xyz = controller.retrieve();
		IDiagram result = this.conparASM.makeDiagram(xyz);
	
		DiagramUMLforPlantUML plantUML = new DiagramUMLforPlantUML(new MegaContainer());
		assertEquals(result.getClass(), plantUML.getClass());
		
	}
	
	@Test
	public void testMakeDiagramInvalid() {
		this.conparASM =  new ConstructParser(inputArgs);
		this.conparASM.populateParameters();
		conparASM.controllerType = "ASM";
		conparASM.diagramType = "ClassPlantUMLInvalid";
		
		IController controller = this.conparASM.makeController();
		controller.execute();
		MegaContainer xyz = controller.retrieve();
		IDiagram result = this.conparASM.makeDiagram(xyz);
	
		assertEquals(null, result);
		
	}
	
	@Test
	public void testOptionsSingletonInitialization() {
		OptionsSingleton os = OptionsSingleton.getInstance();
		HashMap<String, String> opt = os.getInstance().options;
		opt.put("TestOption", "Test");
		assertTrue(true);
	}
	
	@Test
	public void testPopulateParametersValid() {
		this.conparASM.populateParameters();
		assertEquals(8, OptionsSingleton.getInstance().options.size());
	}
	
	
	@Test
	public void testBeginParsing() {
		this.conparASM.diagramType = "ClassPlantUML";
		this.conparASM.controllerType = "ASM";
		this.conparASM.beginParsing();
		assertTrue(true);
	}
	
	@Test
	public void testBeginParsingInvalid() {
		this.conparASM = new ConstructParser(new HashSet<String[]>());
		this.conparASM.diagramType =  null;
		this.conparASM.controllerType = null;
		this.conparASM.beginParsing();
		
		this.conparASM = new ConstructParser(new HashSet<String[]>());
		this.conparASM.diagramType = null;
		this.conparASM.controllerType = "test";
		this.conparASM.beginParsing();
		
		this.conparASM = new ConstructParser(new HashSet<String[]>());
		this.conparASM.diagramType = "test";
		this.conparASM.controllerType = null;
		this.conparASM.beginParsing();
	}
	
	public void populateInputArgs(String[] args) {
		for(int i = 0; i<args.length; i+=2) {
			String[] argPair = new String[2];
			if(args[i].charAt(0)=='-' &&args[i].length()>1) {
				argPair[0] = args[i].substring(1);
				argPair[1] = args[i+1];
			}
			inputArgs.add(argPair);
		}
	}
	
	
	

}
