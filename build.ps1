$srcClasses = "src\*.java src\interfaz\*.java src\mundo\*.java src\mundo\modelos\*.java src\utils\*.java src\visual\*.java "
$libFiles = ""
$compile = "javac --release 23 -Xlint:all -Xdiags:verbose -d .\bin\ $srcClasses"
$createJar = "jar -cfm Joogle.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar Joogle.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
