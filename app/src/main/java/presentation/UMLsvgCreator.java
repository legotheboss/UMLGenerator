package presentation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import data_source.LogWriter;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class UMLsvgCreator {

	private String diagramLocation;
	
	private String UMLtextLocation;
	
	public UMLsvgCreator(String UMLtextLocation, String diagramLocation) {
		this.diagramLocation = diagramLocation;
		this.UMLtextLocation = UMLtextLocation;
		if(diagramLocation.length() < 5) diagramLocation = "diagram.svg";
	}
	
	@SuppressWarnings("deprecation")
	public void createSVGfromUML() {

		try {
			String content = Files.readString(Paths.get(UMLtextLocation), StandardCharsets.US_ASCII);
			SourceStringReader reader = new SourceStringReader(content);
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
			os.close();
			final String svg = new String(os.toByteArray(), Charset.forName("UTF-8"));
			
			this.writeToFile(svg, diagramLocation);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writeToFile(String str, String fileOutput) throws IOException {
		LogWriter x = new LogWriter(fileOutput);
		x.writeText(str);
		
	}

}
