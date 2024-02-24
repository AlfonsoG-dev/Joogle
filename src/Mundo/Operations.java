package Mundo;

import Utils.OperationUtils;
import Visual.Colores;

/**
 * clase para organizar las operaciones según las opciones dadas por el usuario
 */
public final class Operations {
    /**
    * CLI options
    */
    private String[] options;
    /**
     */
    private OperationUtils opUtils;
    /**
     * constructor
     */
    public Operations(String[] nOptions) {
        options = nOptions;
        opUtils = null;
    }
    /**
    * organiza la forma en la que se ejecutan los argumentos de la consola;
    *  -f es para buscar la sentencia dentro de un archivo 
    *  -d es para buscar la sentencia dentro de los archivos del directory designado,
    *  solo se tienen en cuenta los archivos no directorios
    *  -D es para buscar la sentencia dentro de los archivos del directory designado,
    *  si el directorio tiene más directorios se busca tambien dentro de ellos
    *  -lf listar los archivos que tienen como extensión .java
    *  -lm listar los métodos del archivo
    *  -lt listart todos los todos en el proyecto
    */
    public void organizeCLIOptions() {
        try {
            outter: for(int i=0; i<options.length; ++i) {
                opUtils = new OperationUtils(options, i);
                switch(options[i]) {
                    case "-f":
                        opUtils.searchInFileOperation();
                        break;
                    case "-d":
                        opUtils.searcInDirectoryOperation();
                        break;
                    case "-D":
                        opUtils.searcInDirectoriesOperation();
                        break;
                    case "-lf":
                        opUtils.searchForFilesOperation();
                        break;
                    case "-lm":
                        opUtils.searchForMethodsOperation();
                        break;
                    case "-lt":
                        opUtils.searchForTODOKeyOperation();
                        break;
                    case "--h":
                        opUtils.getHelpOperation();
                        break;
                    default: 
                        System.out.println("utilize el comando --h para mas informacion");
                        break outter;
                }
            }
        } catch(Exception e) {
            System.err.println(Colores.RED_UNDERLINED + e.getLocalizedMessage() + Colores.ANSI_RESET);
        }
    }
}
