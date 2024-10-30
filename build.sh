#/bin/sh

srcClases="./src/*.java ./src/Interfaz/*.java ./src/Mundo/*.java ./src/Mundo/Modelos/*.java ./src/Utils/*.java ./src/Visual/*.java "

javac -Werror -Xlint:all -d ./bin/ $srcClases
jar -cfm Joogle.jar Manifesto.txt -C ./bin/ .
java -jar Joogle.jar
