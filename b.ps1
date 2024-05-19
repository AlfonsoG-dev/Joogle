$buildCommand = "javabuild --build"
$runCommand = "java -jar Joogle.jar -f .\src\testing\Pruebas.java 'Pruebas()'"
$command = "$buildCommand" + " && " + "$runCommand"
Invoke-Expression $command
