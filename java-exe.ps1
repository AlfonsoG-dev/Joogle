#class to compile
$Clases = " ./src/*.java"
#compile program and save the .class data in bin
$Compile = "javac -d ./bin" + "$Clases"
#create jar file
$CreateJarFile = "jar -cfm joogle.jar Manifesto.txt -C bin ."
#just for testing purposes
$JavaCommand = "java -cp ./bin ./src/App.java -f ./src/App.java " + '/""/'
#execute the commands
$RunCommand = "$Compile" + " && " + "$JavaCommand" + " && " + "$CreateJarFile"

Invoke-Expression $RunCommand
