package presentation;
import java.util.HashSet;
import domain.ConstructParser;

public class Main {

	public static void main(String[] args) {
		if(args.length%2==0) {
			HashSet<String[]> inputArgs = new HashSet<>(); 
			for(int i = 0; i<args.length; i+=2) {
				String[] argPair = new String[2];
				if(args[i].charAt(0)=='-' &&args[i].length()>1) {
					argPair[0] = args[i].substring(1);
					argPair[1] = args[i+1];
				}
				inputArgs.add(argPair);
			}
			ConstructParser conpar = new ConstructParser(inputArgs);
			conpar.beginParsing();
			
			
			UMLsvgCreator output = new UMLsvgCreator(conpar.getOutputFileName(), "diagram.svg");
			output.createSVGfromUML();
		}
		else System.out.println("Error: Need even number of arguments");
	}

}
