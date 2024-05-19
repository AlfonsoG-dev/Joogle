$buildCommand = "javabuild --build"
$runCommand = "java -jar Joogle.jar -f .\src\testing\Pruebas.java 'void(int)'"
$command = "$buildCommand" + " && " + "$runCommand"
Invoke-Expression $command
