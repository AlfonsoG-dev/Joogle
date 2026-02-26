package utils;

import java.io.File;

import mundo.Busqueda;

/**
 * record para organizar las operaciones CLI
 */
public class OperationUtils {

    private static final String CONSOLE_FORMAT = "%s%n";

    private String[] options;
    private int i;

    public OperationUtils(String[] options, int i) {
        this.options = options;
        this.i = i;
    }
    /**
     * busqueda options
     */
    private static final Busqueda busqueda = new Busqueda();
    /**
     * verifica si las opcion [1] se ha digitado
     * @return la opcion[i+1], de lo contrario "./"
     */
    public String verificarOptionFile() {
        String res = "";
        if((i+1) >= options.length) {
            res = "." + File.separator;
        } else {
            res = options[i+1];
        }
        return res;
    }
    /**
     * verifica si la opcion [2] se ha digitado
     * @return la opcion[i+2], de lo contrario ""
     */
    public String verificarOptionSentence() {
        String res = "";
        if((i+2) >= options.length) {
            res = "";
        } else {
            res = options[i+2];
        }
        return res;
    }
    /**
     * operacion para buscar dentro de un archivo
     * si se da una sentencia, se colorea el resultado buscado
     */
    public void searchInFileOperation() {
        if(verificarOptionFile().equals("." + File.separator)) {
            System.console().printf("%s%n", "[ Error ]: Falta proporcionar un archivo para su lectura");
            return;
        }
        String fileName = verificarOptionFile();
        String sentence = verificarOptionSentence();
        busqueda.searchInFile(fileName, sentence);
    }
    /**
     * operacion para buscar dentro de un directorio
     * si se da una sentencia, se colorea el resultado buscado
     */
    public void searcInDirectoryOperation() {
        String directory = verificarOptionFile();
        String sentence = verificarOptionSentence();
        busqueda.searchInDirectory(directory, sentence);
    }
    /**
     * operacion para buscar dentro de directorios
     * si se da una sentencia, se colorea el resultado buscado
     */
    public void searcInDirectoriesOperation() {
        String directorys = verificarOptionFile();
        String sentence = verificarOptionSentence();
        busqueda.searchInDirectories(directorys, sentence);
    }
    /**
     * operacion para buscar por el nombre de los archivos, 
     * los archivos tienen como extencion .java
     */
    public void searchForFilesOperation() {
        String directorys = verificarOptionFile();
        busqueda.searchFiles(directorys);
    }
    /**
     * operacion para buscar por los metodos del proyecto
     * si se da una sentencia, se buscara por el bloque de codigo de ese método
     */
    public void searchForMethodsOperation() {
        String directorys = verificarOptionFile();
        String sentence = verificarOptionSentence();
        busqueda.searchMethods(directorys, sentence);
    }
    /**
     * operacion para buscar los T\ODO's del proyecto
     */
    public void searchForTODOKeyOperation() {
        busqueda.searchTODO(verificarOptionFile());
    }
    /**
     * mensaje con todas las operaciones disponibles
     */
    public void getHelpOperation() {
        System.console().printf(CONSOLE_FORMAT, "Opciones para joogle");
        System.console().printf(CONSOLE_FORMAT, "-f para buscar dentro de un archivo:" + 
                "\t seguido del valor entre \"\" para buscar la sentencia");
        System.console().printf(CONSOLE_FORMAT, "-d para buscar dentro de un directorio:" + 
                "\t seguido de /\"\"/ para buscar una sentencia");
        System.console().printf(CONSOLE_FORMAT, "-D para buscar dentro de todos los directorios:" + 
                "\t seguido de /\"\"/ para buscar una sentencia");
        System.console().printf(CONSOLE_FORMAT, "-lf para listar todos los archivos .java:\t seguido del directorio");
        System.console().printf(CONSOLE_FORMAT, "-lm para listar todos los métodos del proyecto:\t seguido del directorio");
        System.console().printf(CONSOLE_FORMAT, "\t si seleccionas un archivo y adicionas el nombre" + 
                " del metodo se retorna el bloque de codigo de ese metodo");
        System.console().printf(CONSOLE_FORMAT, "-lt para listar todos los TODO del proyecto:\t seguido del directorio");
    }
}
