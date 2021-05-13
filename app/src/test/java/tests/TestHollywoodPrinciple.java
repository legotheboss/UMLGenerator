package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import data_source.Argument;
import data_source.Class;
import data_source.Field;
import data_source.MegaContainer;
import data_source.Method;
import data_source.MethodCalled;
import domain.HollywoodPrinciple;



public class TestHollywoodPrinciple {
	
	MegaContainer mc;
	HollywoodPrinciple hp;
	
	@Test
	public void InstantiateMEGA() {
		mc = new MegaContainer();
		
		Class hollywood = new Class();
		hollywood.name = "hollywood";
		
		Class cl2 = new Class();
		cl2.name = "method_called_class";
		
		mc.classes.put("hollywood", hollywood);
		mc.classes.put("cl2", cl2);
		
		this.instanitateFields(hollywood);
		this.instaniateMethods(hollywood);
		
	}
	
	@Test
	public void testHollyWood() {
		InstantiateMEGA();
		hp = new HollywoodPrinciple(mc, "red");
		assertTrue(hp.check("hollywood"));
	}
	
	@Test
	public void checkLabel() {
		testHollyWood();
		assertEquals(null, hp.getLabel("hollywood"));
	}
	
	@Test
	public void checkColor() {
		testHollyWood();
		assertEquals("red", hp.getColor());
	}
	
	public Method instantiateConstructor() {
		Method constructor = new Method();
		constructor.name = "<clinit>";
		constructor.returnType = "null";
		constructor.accessLevel = 3;
		constructor.methodsCalled.add(new MethodCalled("test", "method_called_class"));
		Argument packageTypeArg = new Argument();
		packageTypeArg.packageType = "package_type";
		constructor.parameters.add(new Argument());
		return constructor;
	}
	
	public Method instaniateMethods(Class hollywood) {
		Method getter = new Method();
		getter.name = "getter";
		getter.returnType = "hollywood";
		getter.accessLevel = 1;
		hollywood.methods.add(getter);
		return getter;
	}
	
	public Field instanitateFields(Class hollywood) {
		Field fi = new Field();
		fi.type = "field_type";
		fi.packageType = "sample_inherited";
		hollywood.fields.add(fi);
		hollywood.inherited.add("sample_inherited");
		
		Field instance = new Field();
		instance.type = "hollywood";
		return fi;
	}
		
		
}
