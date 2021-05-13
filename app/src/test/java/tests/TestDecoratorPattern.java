package tests;
import static org.junit.Assert.*;

import org.junit.Test;

import data_source.MegaContainer;
import data_source.Class;
import data_source.Interface;
import domain.DecoratorPattern;

public class TestDecoratorPattern {
	
	public TestDecoratorPattern() {
	}
	
	@Test
	public void IdentifiesDecoratorPattern() {
		MegaContainer xyz = new MegaContainer();
		Class c1 = new Class();
		Class c2 = new Class();
		c1.name = "c1";
		c2.name = "c2";
		
		c1.superclass = c2.name;
		
		Interface I1 = new Interface();
		I1.name = "I1";
		
		xyz.classes.put(c1.name, c1);
		xyz.classes.put(c2.name, c2);
		xyz.interfaces.put(I1.name, I1);
		
		c2.inherited.add(I1.name);
		
		
		DecoratorPattern decoratorPattern = new DecoratorPattern(xyz, "red");
		assertEquals(decoratorPattern.check(c1.name),true);
	}
	
}
