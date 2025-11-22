#/bin/sh
$srcClasses="src/*.java src/interfaz/*.java src/mundo/*.java src/mundo/modelos/*.java src/utils/*.java src/visual/*.java "
$libFiles=""
javac --release 23 -Xlint:all -Xdiags:verbose -d .\bin\ $srcClasses
jar -cfm Joogle.jar Manifesto.txt -C .\bin\ .
java -jar Joogle.jar
