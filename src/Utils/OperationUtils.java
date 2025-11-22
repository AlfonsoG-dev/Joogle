package utils;

import java.io.File;

import mundo.Busqueda;

/**
 * record para organizar las operaciones CLI
 */
public record OperationUtils(String[] options, int i) {
    /**
     * busqueda options
     */
    private final static Busqueda busqueda = new Busqueda();
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
    public void searchInFileOperation() throws Exception {
        if(verificarOptionFile().equals("." + File.separator)) {
            throw new Exception("[ Error ]: Falta proporcionar un archivo para su lectura");
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
        System.out.println("Opciones para joogle");
        System.out.println("-f para buscar dentro de un archivo:" + 
                "\t seguido de /\"\"/ para buscar una sentencia");
        System.out.println("-d para buscar dentro de un directorio:" + 
                "\t seguido de /\"\"/ para buscar una sentencia");
        System.out.println("-D para buscar dentro de todos los directorios:" + 
                "\t seguido de /\"\"/ para buscar una sentencia");
        System.out.println("-lf para listar todos los archivos .java:\t seguido del directorio");
        System.out.println("-lm para listar todos los métodos del proyecto:\t seguido del directorio");
        System.out.println("\t si seleccionas un archivo y adicionas el nombre" + 
                " del metodo se retorna el bloque de codigo de ese metodo");
        System.out.println("-lt para listar todos los TODO del proyecto:\t seguido del directorio");
    }
}
