package Utils;

import Mundo.Busqueda;
import Visual.Colores;

public record OperationUtils(String[] options, int i) {
    /**
     * busqueda options
     */
    private final static Busqueda busqueda = new Busqueda();
    /**
     */
    public void SearchInFileOperation() {
        String fileName = options[i+1];
        if((i+2) >= options.length) {
            busqueda.SearchInFile(fileName, "");
        } else {
            busqueda.SearchInFile(fileName, options[i+2]);
        }
    }
    /**
     */
    public void SearcInDirectoryOperation() {
        String directory = options[i+1];
        if((i+2) >= options.length) {
            busqueda.SearcInDirectory(directory, "");
        } else {

            busqueda.SearcInDirectory(directory, options[i+2]);
        }
    }
    /**
     */
    public void SearcInDirectoriesOperation() {
        String directorys = options[i+1];
        if((i+2) >= options.length) {
            busqueda.SearcInDirectories(directorys, "");
        } else {
            String searchSentence = options[i+2];
            busqueda.SearcInDirectories(directorys, searchSentence);
        }
    }
    /**
     */
    public void SearchForFilesOperation() {
        String directorys = options[i+1];
        busqueda.BuscarFiles(directorys);
    }
    /**
     */
    public void SearchForMethodsOperation() {
        String directorys = options[i+1];
        if((i+2) >= options.length) {
            busqueda.BuscarMethods(directorys, "");
        } else {
            busqueda.BuscarMethods(directorys, options[i+2]);
        }
    }
    /**
     */
    public void SearchForTODOKeyOperation() {
        busqueda.BuscarTODO(options[i+1]);
    }
    /**
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
