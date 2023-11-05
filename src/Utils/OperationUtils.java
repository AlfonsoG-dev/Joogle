package Utils;

import Mundo.Busqueda;

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
    public String VerificarOptionFile() {
        String res = "";
        if((i+1) >= options.length) {
            res = "./";
        } else {
            res = options[i+1];
        }
        return res;
    }
    /**
     * verifica si la opcion [2] se ha digitado
     * @return la opcion[i+2], de lo contrario ""
     */
    public String VerificarOptionSentence() {
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
    public void SearchInFileOperation() throws Exception {
        if(VerificarOptionFile().equals("./")) {
            throw new Exception("debe proporcionar un archivo");
        }
        String fileName = VerificarOptionFile();
        String sentence = VerificarOptionSentence();
        busqueda.SearchInFile(fileName, sentence);
    }
    /**
     * operacion para buscar dentro de un directorio
     * si se da una sentencia, se colorea el resultado buscado
     */
    public void SearcInDirectoryOperation() {
        String directory = VerificarOptionFile();
        String sentence = VerificarOptionSentence();
        busqueda.SearcInDirectory(directory, sentence);
    }
    /**
     * operacion para buscar dentro de directorios
     * si se da una sentencia, se colorea el resultado buscado
     */
    public void SearcInDirectoriesOperation() {
        String directorys = VerificarOptionFile();
        String sentence = VerificarOptionSentence();
        busqueda.SearcInDirectories(directorys, sentence);
    }
    /**
     * operacion para buscar por el nombre de los archivos, 
     * los archivos tienen como extencion .java
     */
    public void SearchForFilesOperation() {
        String directorys = VerificarOptionFile();
        busqueda.BuscarFiles(directorys);
    }
    /**
     * operacion para buscar por los metodos del proyecto
     * si se da una sentencia, se buscara por el bloque de codigo de ese método
     */
    public void SearchForMethodsOperation() {
        String directorys = VerificarOptionFile();
        String sentence = VerificarOptionSentence();
        busqueda.BuscarMethods(directorys, sentence);
    }
    /**
     * operacion para buscar los T\ODO's del proyecto
     */
    public void SearchForTODOKeyOperation() {
        busqueda.BuscarTODO(VerificarOptionFile());
    }
    /**
     * mensaje con todas las operaciones disponibles
     */
    public void GetHelpOperation() {
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
