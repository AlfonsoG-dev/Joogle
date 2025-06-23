$srcClases = "src\*.java src\Interfaz\*.java src\Mundo\*.java src\Mundo\Modelos\*.java src\Utils\*.java src\Visual\*.java "
$libFiles = ""
$compile = "javac --release 23 -Werror -Xlint:all -d .\bin\ $srcClases"
$createJar = "jar -cfm Joogle.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar Joogle.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
