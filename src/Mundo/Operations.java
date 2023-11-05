package Mundo;

import Visual.Colores;
import Utils.OperationUtils;
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
    public void OrganizeSearchOptions() {
        try {
            outter: for(int i=0; i<options.length; ++i) {
                opUtils = new OperationUtils(options, i);
                switch(options[i]) {
                    case "-f":
                        opUtils.SearchInFileOperation();
                        break;
                    case "-d":
                        opUtils.SearcInDirectoryOperation();
                        break;
                    case "-D":
                        opUtils.SearcInDirectoriesOperation();
                        break;
                    case "-lf":
                        opUtils.SearchForFilesOperation();
                        break;
                    case "-lm":
                        opUtils.SearchForMethodsOperation();
                        break;
                    case "-lt":
                        opUtils.SearchForTODOKeyOperation();
                        break;
                    case "--h":
                        opUtils.GetHelpOperation();
                        break;
                    default: 
                        System.out.println("utilize el comando --h para mas informacion");
                        break outter;
                }
            }
        } catch(Exception e) {
            System.err.println(Colores.RED_UNDERLINED + e.toString().toUpperCase() + Colores.ANSI_RESET);
        }
    }
}
