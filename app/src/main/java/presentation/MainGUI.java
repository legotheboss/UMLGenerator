package presentation;

import domain.ConstructParser;
import domain.OptionsSingleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.HashSet;

public class MainGUI extends JFrame implements ActionListener{
    JFrame f;

    public JButton createUMLbutton;
    public JButton createSVGbutton;
    public JButton HPcolorPickerButton;
    public JButton DPcolorPickerButton;
    public JButton APcolorPickerButton;
    public JButton singletonColorPickerButton;
    public HashMap<String, String> os;

    public JTextField classMethodInput;
    public JTextField recurseLevelInput;
    public JTextField blackListInput;
    public JTextField outputInput;
    public JTextField accessLevelInput;
    public JTextField hpInput;
    public JTextField dpInput;
    public JTextField apInput;
    public JTextField singletonInput;

    public JLabel classMethodInputLabel;
    public JLabel controllerTypeLabel;
    public JLabel diagramTypeLabel;
    public JLabel recurseLevelLabel;
    public JLabel blacklistLabel;
    public JLabel outputLabel;
    public JLabel hwLabel;
    public JLabel dpLabel;
    public JLabel apLabel;
    public JLabel singletonLabel;
    public JLabel accessLevelLabel;
    public JLabel processStatusLabel;

    public JCheckBox recurseCheck;

    public JComboBox controllerCombo;
    public JComboBox diagramCombo;

    private ConstructParser cp;

    public MainGUI() {
        boolean buttonClicked = false;
        this.cp = new ConstructParser(new HashSet<>());
        this.setTitle("UML Converter");
        classMethodInputLabel = new JLabel("Class or Method Name");
        classMethodInputLabel.setBounds(5, 0, 250, 20);

        classMethodInput = new JTextField();
        classMethodInput.setBounds(5,20, 250, 20);

        recurseCheck = new JCheckBox("Recurse Superclasses");
        recurseCheck.setBounds(110,45, 200,20);

        controllerTypeLabel = new JLabel("Controller Type");
        controllerTypeLabel.setBounds(5, 65, 250, 20);

        String controllers[]={"ASM"};
        controllerCombo = new JComboBox(controllers);
        controllerCombo.setBounds(5, 90,90,20);

        diagramTypeLabel = new JLabel("Diagram Type");
        diagramTypeLabel.setBounds(120, 65, 250, 20);

        String diagramTypes[]={"ClassPlantUML"};
        diagramCombo =new JComboBox(diagramTypes);
        diagramCombo.setBounds(100,90,150,20);

        recurseLevelLabel = new JLabel("Recurse Level");
        recurseLevelLabel.setBounds(5, 45, 125, 20);

        recurseLevelInput = new JTextField();
        recurseLevelInput.setBounds(90,45,20,20);

        blacklistLabel = new JLabel("Blacklist File");
        blacklistLabel.setBounds(5, 110, 125, 20);

        blackListInput = new JTextField();
        blackListInput.setBounds(5,130,250,20);

        outputLabel = new JLabel("Output File");
        outputLabel.setBounds(5, 150, 125, 20);

        outputInput = new JTextField();
        outputInput.setBounds(5,170,250,20);

        hwLabel = new JLabel("HollyWood Priciple Identifier (hex color)");
        hwLabel.setBounds(5, 190, 250, 20);

        hpInput = new JTextField();
        hpInput.setBounds(5,210,100,20);

        //button should be 5 away from the outside of the frame
        HPcolorPickerButton = new JButton("Pick Color");
        HPcolorPickerButton.setBounds(105, 210, 140, 20);
        HPcolorPickerButton.addActionListener(this);

        dpLabel = new JLabel("Decorator Priciple Identifier (hex color)");
        dpLabel.setBounds(5, 230, 250, 20);

        dpInput = new JTextField();
        dpInput.setBounds(5,250,100,20);

        DPcolorPickerButton = new JButton("Pick Color");
        DPcolorPickerButton.setBounds(105, 250, 140, 20);
        DPcolorPickerButton.addActionListener(this);

        apLabel = new JLabel("Adapter Priciple Identifier (hex color)");
        apLabel.setBounds(5, 270, 250, 20);

        apInput = new JTextField();
        apInput.setBounds(5,290,100,20);

        APcolorPickerButton = new JButton("Pick Color");
        APcolorPickerButton.setBounds(105, 290, 140, 20);
        APcolorPickerButton.addActionListener(this);
        
        singletonLabel = new JLabel("Singleton Priciple Identifier (hex color)");
        singletonLabel.setBounds(5, 310, 250, 20);

        singletonInput = new JTextField();
        singletonInput.setBounds(5, 330, 100, 20);

        //button should be 5 away from the outside of the frame
        singletonColorPickerButton = new JButton("Pick Color");
        singletonColorPickerButton.setBounds(105, 330, 140, 20);
        singletonColorPickerButton.addActionListener(this);

        accessLevelLabel = new JLabel("Access Level");
        accessLevelLabel.setBounds(5, 360, 125, 20);

        accessLevelInput = new JTextField();
        accessLevelInput.setBounds(90,360,20,20);
        
        processStatusLabel = new JLabel("<html>Ready to go!</html>");
        processStatusLabel.setBounds(5, 400, 125, 50);
        processStatusLabel.setVerticalAlignment(JLabel.TOP);

        createUMLbutton = new JButton("Create UML");
        createUMLbutton.setBounds(130, 360, 120, 20);
        createUMLbutton.addActionListener(this);

        createSVGbutton = new JButton("Create SVG");
        createSVGbutton.setBounds(130, 380, 120, 20);
        createSVGbutton.setEnabled(false);
        createSVGbutton.addActionListener(this);

        add(createUMLbutton);
        add(createSVGbutton);
        add(HPcolorPickerButton);
        add(DPcolorPickerButton);
        add(APcolorPickerButton);
        add(singletonColorPickerButton);
        add(accessLevelLabel);
        add(accessLevelInput);
        add(apLabel);
        add(apInput);
        add(dpLabel);
        add(dpInput);
        add(hwLabel);
        add(hpInput);
        add(singletonLabel);
        add(singletonInput);
        add(outputLabel);
        add(outputInput);
        add(blacklistLabel);
        add(blackListInput);
        add(recurseLevelInput);
        add(recurseLevelLabel);
        add(diagramCombo);
        add(recurseCheck);
        add(diagramTypeLabel);
        add(controllerTypeLabel);
        add(controllerCombo);
        add(classMethodInputLabel);
        add(classMethodInput);
        add(processStatusLabel);

        setSize(275,445);
        setLayout(null);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == HPcolorPickerButton){
            Color c = JColorChooser.showDialog(this,"Choose",Color.CYAN);
            this.hpInput.setText(convertColorToHex(c).substring(1));
        }
        else if (e.getSource() == DPcolorPickerButton){
            Color c = JColorChooser.showDialog(this,"Choose",Color.YELLOW);
            this.dpInput.setText(convertColorToHex(c).substring(1));
        }
        else if (e.getSource() == APcolorPickerButton){
            Color c = JColorChooser.showDialog(this,"Choose",Color.GREEN);
            this.apInput.setText(convertColorToHex(c).substring(1));
        } 
        else if (e.getSource() == singletonColorPickerButton){
            Color c = JColorChooser.showDialog(this,"Choose",Color.RED);
            this.singletonInput.setText(convertColorToHex(c).substring(1));
        }  else if (e.getSource() == createUMLbutton) {
            insertParameters();
            cp.populateParameters();
            cp.beginParsing();
            createSVGbutton.setEnabled(true);
            processStatusLabel.setText("<html>Processing code...</html>");
        } else if (e.getSource() == createSVGbutton) {
            System.out.println("here");
            String outputSVG = ".svg";
            if(outputInput.getText().length() < 4) outputSVG = "diagram.svg";
            else outputSVG = outputInput.getText() + outputSVG;
            UMLsvgCreator output = new UMLsvgCreator(cp.getOutputFileName(), outputSVG);
            output.createSVGfromUML();
            processStatusLabel.setText("<html>UML generated as SVG: diagram.svg</html>");
        }
    }

    private String convertColorToHex(Color c) {
        int R = c.getRed();
        int G = c.getGreen();
        int B = c.getBlue();

        String rgb = "#" + Integer.toHexString(R) + Integer.toHexString(G) + Integer.toHexString(B);
        return rgb;
    }

    public void insertParameters() {
        this.os = OptionsSingleton.getInstance().options;
        os.put("ClassParser", classMethodInput.getText());
        os.put("RecursiveMethodParser", recurseLevelInput.getText());
        os.put("RecursiveParser", Boolean.toString(recurseCheck.isSelected()));
        os.put("diagramType", diagramCombo.getSelectedItem().toString());
        os.put("controllerType", controllerCombo.getSelectedItem().toString());
        os.put("BlackListParser", blackListInput.getText());
        os.put("OutputParser", outputInput.getText());
        os.put("AccessParser", accessLevelInput.getText());
        if(!hpInput.getText().isBlank()) os.put("HollywoodParser", hpInput.getText());
        if(!dpInput.getText().isBlank()) os.put("DecoratorPatternParser", dpInput.getText());
        if(!apInput.getText().isBlank()) os.put("AdapterPatternParser", apInput.getText());
        if(!singletonInput.getText().isBlank()) os.put("SingletonPatternParser", singletonInput.getText());
    }

    public static void main(String[] args) {
        new MainGUI();
    }
}
