package tests;
import static org.junit.Assert.*;

import org.junit.Test;

import data_source.MegaContainer;
import data_source.Method;
import data_source.Class;
import data_source.Field;
import domain.SingletonPattern;

public class TestSingletonPattern {

	MegaContainer mc;
	SingletonPattern sp;
	
	@Test
	public void InstantiateMEGA() {
		mc = new MegaContainer();
		
		Class sing = new Class();
		sing.name = "singleton";
		
		Method constructor = new Method();
		constructor.name = "<clinit>";
		constructor.returnType = "null";
		constructor.accessLevel = 3;
		
		Field instance = new Field();
		instance.type = "singleton";
		
		Method getter = new Method();
		getter.name = "getter";
		getter.returnType = "singleton";
		getter.accessLevel = 1;
		
		sing.methods.add(constructor);
		sing.methods.add(getter);
		sing.fields.add(instance);
		
		mc.classes.put("singleton", sing);
	}
	
	@Test
	public void testSingleton() {
		InstantiateMEGA();
		sp = new SingletonPattern(mc, "red");
		assertTrue(sp.check("singleton"));
	}
	
	@Test
	public void checkLabel() {
		testSingleton();
		assertEquals("singleton", sp.getLabel("singleton"));
	}
	
	@Test
	public void checkColor() {
		testSingleton();
		assertEquals("red", sp.getColor());
	}
	
}
