package domain;

import data_source.Class;
import data_source.Interface;
import data_source.Method;
import data_source.Relation;

public interface IUMLDiagram extends IDiagram{
	public void createClass(Class classToCreate);
	public void createInterface(Interface interfaceToCreate);
	public void fillInterfaceMethods(Interface interfaceToFill);
	public void fillFields(Class classToFill);
	public void fillClassMethods(Class classToFill);
	public void fillArgs(Method currMethod);
	public void createRelation(Relation relation);
}
