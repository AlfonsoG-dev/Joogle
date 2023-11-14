$clases = " ./src/Joogle.java ./src/Interfaz/*.java ./src/Mundo/*.java ./src/Mundo/Modelos/*.java ./src/Utils/*.java ./src/Visual/*.java"
$compile = "javac -d ./bin/ " + "$clases"
$CreateJarFile = "jar -cfm Joogle.jar Manifesto.txt -C ./bin/ ."
$JavaCommand = "java -jar Joogle.jar -d .\src\ "
$RunCommand = "$compile" + " && " + "$CreateJarFile" + " && " + "$JavaCommand"

Invoke-Expression $RunCommand
