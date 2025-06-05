$srcClases = ".\src\*.java .\src\Interfaz\*.java .\src\Mundo\*.java .\src\Mundo\Modelos\*.java .\src\Utils\*.java .\src\Visual\*.java "
$libFiles = ""
$compile = "javac -Werror -Xlint:all -d .\target\ $srcClases"
$createJar = "jar -cfe Joogle.jar Joogle -C .\bin\ ."
$javaCommand = "java -jar Joogle.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
