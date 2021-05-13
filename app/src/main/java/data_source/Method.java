package data_source;
import java.util.ArrayList;
import java.util.HashSet;

public class Method extends IContainer{
	public HashSet<Argument> parameters = new HashSet<>();
	public HashSet<String> localVariableTypes = new HashSet<>();
	public String returnType;
	public ArrayList<String> holdingTypes = new ArrayList<String>();
	public String className;
	public ArrayList<MethodCalled> methodsCalled = new ArrayList<MethodCalled>();
}
