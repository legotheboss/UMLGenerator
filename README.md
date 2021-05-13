![Gradle workflow](https://github.com/legotheboss/UMLGenerator/actions/workflows/gradle.yml/badge.svg)

**SECTION 0: Installation with Run Configurations**
1. Go to the 1CSSE374 repository on the ada 
2. Select "Clone"
3. Select "Copy" under the "Clone with HTTPS" heading
4. Open Eclipse
5. Select "File"
6. Select "Import"
7. Select Git > Projects from Git (with smart import)
8. Select "Clone URL"
9. Paste the URL copied in step 3
10. Enter authentication if necessary
11. Continue selecting "Next" until you can select "Finish"
12. Select "Finish"
13. You should see the project appear in a folder in the package explorer
14. Right click the newly imported project and select "Import..."
15. Select the drop down to see the options within "Run/Debug"
16. Select Launch Configurations
17. Click on browse and browse to the runconfigs directory within the project, click open
18. Select the directory and click finish
19. Note: Make sure [graphviz](https://www.graphviz.org/download/) is installed on your computer!


**SECTION 1: How to Run**
1. Open project in Eclipse
2. Right click on project, then select "Run Configurations"
3. Go to the "Arguments" tab along the top
4. In the first text box, enter appropriate arguments (Section 2: Valid Args)
5. Select "Apply", and then hit "Run"
6. The resulting text can be found in the file name "diagram.txt" unless otherwise specified by the user



**SECTION 2: Valid Arguments**

There must be an even number of command line arguments in order to run this program.
To begin a new argument pair, '-' must be the first character.


*Required Arguments*

`-c packageName.ClassName` OR `-m pathToMethod/classMethodIsIn.MethodName(methodArgs,noSpaces)`
This is a required argument pair for this program.
An interface name or class name is valid for ClassName.
    These can be used for Class Diagrams.
A method in a particular class with particular arguments is valid for the -m argument.
    These can be used for Sequence Diagrams.
In order to analyze multiple, completely unrelated, classes on the same diagram, just provide additional -c argument pairs.
    i.e `-c className1 -c className2 -c interfaceName3`
    
`-ct controllerType`
This is a required argument pair for this program.
The controller type determines which type of API the user desires to use.
The controller type can be one of the following:
    - 'ASM': selects the ASMController

`-dt diagramType`
This is a required argument pair for this program.
The diagram type determines which type of output the user wants returned.
The diagram type can be one of the following:
    - 'ClassPlantUML': selects the class UML diagram using PlantUML
    - 'SequencePlantUML': selects the sequence UML diagram using PlantUML



*Optional Arguments*

These arguments are not required, and they can be used in any combination.

`-rc true` / `-rc false`
This argument can be used to perform recursion when analyzing the class/interface(s) given.
The recursion will go into the superclasses for each class/interface and continue parsing until
the recursions hits a blacklisted class/interface or runs out of classes/interfaces to recurse into.
If a class is on the whitelist and the blacklist, then the class will be parsed as part of the whitelist.
(See Section 3: Altering the Whitelist/Blacklist)

`-rm recursionLevelInteger`
This argument can be used to perform recursion when analyzing the method(s) given.
The recursion will continue parsing methods that are called by the given method until a certain
depth is reached, as is determined by recursionLevelInteger.

`-b blacklistToUse`
This argument can be used to use an alternate blacklist. The blacklistToUse must be a valid
filename, and if it cannot be found, then the default will be used.
DO NOT USE .txt IN THE FILENAME, it will be added for you.

`-a integer`
This argument can be used in order to set the allowed access level.
The integer must be one of the one listed below.
4 - shows only private, protected, package private, and public access levels
3 - shows only protected, package private, and public access levels
2 - shows only package private and public access levels
1 - shows only public access levels

`-o outputFilename`
This argument can be used to change the name of the name of the output file. The default is "diagram.txt".

`-hp legalColor`
This argument can be used to detect violations of the Hollywood design principle in a class diagram.
The second argument is a color entered as a string that is valid for the diagram display type chosen.
For instance, for Plant UML, some of the options can be found on page 142/175 of this pdf: http://plantuml.com/guide

`-dp legalColor`
This argument can be used to detect implementations of the decorator pattern within the class diagram. 
The second argument is a color entered as a string that is valid for the diagram display type chosen.

`-ap legalColor`
This argument can be used to detect implementations of the adapter pattern within the class diagram. 
The second argument is a color entered as a string that is valid for the diagram display type chosen.



**SECTION 3: Altering the Whitelist/Blacklist**

When the -r true argument is given, the program will recurse upward until it hits a blacklisted class/interface or runs out of classes/interfaces to
recurse into. This can be overrode by entering a certain class onto the whitelist.
The default blacklist contains java/lang/Object, so the program will recurse until just before Object.
Classes can be added to the default blacklist by adding each of their names on their own individual lines in blacklist.txt.
Classes can be added to the whitelist by adding each of their names on their own individual lines in whitelist.txt.

If you wish to use a different blacklist, then the argument '-b fileName' can be used. If the filename cannot be found, then the default
file name will be used instead.
