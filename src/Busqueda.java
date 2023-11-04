import java.io.File;

import Utils.BusquedaUtil;
import Visual.BusquedaFormat;
import Utils.FileUtils;
import Visual.Colores;

/**
 * clase para buscar métodos dentro de clases de java
 * EJM: String => (int, boolean)
 */
public class Busqueda {
    /**
    * CLI options
    */
    private String[] options;
    /**
     * utiliades para crear la busqueda
     */
    private BusquedaUtil utils;
    /**
     * formato visual para la busqueda
     */
     private BusquedaFormat format;
     /**
      */
     private FileUtils fileUtils;
    /**
    * constructor
    */
    public Busqueda(String[] nOptions) {
        options = nOptions;
        utils = new BusquedaUtil();
        format = new BusquedaFormat();
        fileUtils = new FileUtils();
    }
    /**
     * buscar "todos" en el proyecto
     * @param filePath: archivo a leer las sentencias todo
     */
    public void BuscarTODO(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.isFile()) {
                if(utils.GetTodoSentences(miFile.getCanonicalPath()) == false) {
                    System.err.println("\n\t NO TIENE TODO POR EL MOMENTO \n");
                } else {
                    utils.GetTodoSentences(miFile.getCanonicalPath());
                }
            } else {
                String[] fileNames = fileUtils.GetFilesFromDirectories(miFile.listFiles()).split("\n");
                for(String fn: fileNames) {
                    BuscarTODO(fn);
                }
            }

        } catch(Exception e) {
            //System.err.println(e);
        }
    }
    /**
     * buscar la ruta de los archivos dentro del proyecto
     * @param filePath: ruta de los archivos a leer
     */
    public void BuscarFiles(String filePath) {
        try {
            File miFile = new File(filePath);
            String[] fileNames = fileUtils.GetFilesFromDirectories(miFile.listFiles()).split("\n");
            for(String fn: fileNames) {
                if(fn.equals("") == false) {
                    format.formatoBusquedaFiles(fn);
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * busca los métodos de la ruta especifica
     * @param filePath: ruta a leer
     * @param sentence: sentencia a buscar
     */
    public void BuscarMethods(String filePath, String sentence) {
        try {
            String cSentence = sentence.replace("/", "");
            String filesName = "";
            File miFile = new File(filePath);
            if(miFile.isDirectory() && cSentence.equals("")) {
                filesName = fileUtils.GetFilesFromDirectories(miFile.listFiles());
                String[] partition = filesName.split("\n");
                for(String p: partition) {
                    String[] metodos = utils.GetMethodName(p).split("\n");
                    for(String m: metodos) {
                        int line = utils.GetLineNumber(p, m + "::");
                        format.formatoBusquedaMethod(p, m, line);
                    }
                }
            }
            if(miFile.isFile() && cSentence.equals("") == false) {
                utils.GetMethodContext(miFile.getCanonicalPath(), cSentence);
            } else {
                System.out.println("debes seleccionar un archivo con extension .java");
            }
        } catch(Exception e) {
            //
        }
    }
    /**
    * busca la sentencia según el tipo de retorno y los argumentos
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    */
    public void BuscarSentencia(String filePath, String sentencia) {
        try {
            String[] methodNames = utils.GetMethodName(filePath).split("\n");
            String[] types = utils.CompareToReturnType(filePath, sentencia).split("\n");
            String[] arguments = utils.CompareToArguments(filePath, sentencia).split("\n");
            File miFile = new File(filePath);
            if (miFile.exists()) {
                for(int i = 0; i < methodNames.length; ++i) {
                    if (sentencia.equals("")) {
                        format.formatoBusquedaSentencia(
                        utils.GetLineNumber(filePath, methodNames[i]),
                        filePath,
                        methodNames[i],
                        types[i],
                        arguments[i]
                        );
                    } else {
                        format.formatoBusquedaSentencia(
                        utils.GetLineNumber(filePath, methodNames[i]),
                        filePath,
                        methodNames[i],
                        types[i],
                        arguments[i]
                        );
                    }
                }
            } else {
                System.out.println("el archivo no existe");
            }
        } catch (Exception var11) {
            //System.err.println(var11);
        }

    }
    /**
    * busca la sentencia en el archivo designado
    * @param filePath: ruta del archivo a leer
    * @param searchSentence: sentencia a buscar
    */
    public void SearchInFile(String filePath, String searchSentence) {
        try {
            File miFile = new File(filePath);
            if(miFile.isFile() && miFile.getName().contains(".java")) {
                System.out.println(String.format("\n%s\n", Colores.ANSI_CYAN + filePath + Colores.ANSI_RESET));
                String sentence = searchSentence.replace("/", "");
                BuscarSentencia(filePath, sentence);
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    /**
    * busca la sentencia dentro de los archivos del directorio designado
    * @param directory: directory con los archivos
    * @param searchSentence: sentencia a buscar
    */
    public void SearcInDirectory(String directory, String searchSentence) {
        try {
            File miFile = new File(directory);
            String[] fileNames = fileUtils.GetFilesFromDirectory(miFile.listFiles()).split("\n");
            for(String fn: fileNames) {
                SearchInFile(fn, searchSentence);
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
    * busca la sentencia dentro de los archivos de los directorios del directorio designado
    * @param directorys: directorys del directory designado
    * @param searchSentence: sentencia a buscar
    */
    public void SearcInDirectories(String directorys, String searchSentence) {
        try {
            String filesName = "";
            File miFile = new File(directorys);
            if(miFile.isDirectory()) {
                filesName = fileUtils.GetFilesFromDirectories(miFile.listFiles());
                String[] partition = filesName.split("\n");
                for(String p: partition) {
                    SearchInFile(p, searchSentence);
                }
            }

        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
    * organiza la forma en la que se ejecutan los argumentos de la consola;
    *  -f es para buscar la sentencia dentro de un archivo 
    *  -d es para buscar la sentencia dentro de los archivos del directory designado, solo se tienen en cuenta los archivos no directorios
    *  -D es para buscar la sentencia dentro de los archivos del directory designado si el directorio tiene más directorios se busca tambien dentro de ellos
    *  -lf listar los archivos que tienen como extensión .java
    *  -lm listar los métodos del archivo
    *  -lt listart todos los todos en el proyecto
    */
    public void OrganizeSearchOptions() {
        try {
            String directory = "";
            String fileName = "";
            String directorys = "";
            outter: for(int i=0; i<options.length; ++i) {
                switch(options[i]) {
                    case "-f":
                        fileName = options[i+1];
                        if((i+2) < options.length) {
                            SearchInFile(fileName, options[i+2]);
                        } else {
                            SearchInFile(fileName, "");
                        }
                        break;
                    case "-d":
                        directory = options[i+1];
                        if((i+2) < options.length) {
                            SearcInDirectory(directory, options[i+2]);
                        } else {
                            SearcInDirectory(directory, "");
                        }
                        break;
                    case "-D":
                        directorys = options[i+1];
                        if((i+2) < options.length) {
                            String searchSentence = options[i+2];
                            SearcInDirectories(directorys, searchSentence);
                        } else {
                            SearcInDirectories(directorys, "");
                        }
                        break;
                    case "-lf":
                        directorys = options[i+1];
                        BuscarFiles(directorys);
                        break;
                    case "-lm":
                        directorys = options[i+1];
                        if((i+2) < options.length) {
                            BuscarMethods(directorys, options[i+2]);
                        } else {
                            BuscarMethods(directorys, "");
                        }
                        break;
                    case "-lt":
                        BuscarTODO(options[i+1]);
                        break;
                    case "--h":
                        System.out.println("Opciones para joogle");
                        System.out.println("-f para buscar dentro de un archivo:\t seguido de /\"\"/ para buscar una sentencia");
                        System.out.println("-d para buscar dentro de un directorio:\t seguido de /\"\"/ para buscar una sentencia");
                        System.out.println("-D para buscar dentro de todos los directorios:\t seguido de /\"\"/ para buscar una sentencia");
                        System.out.println("-lf para listar todos los archivos .java:\t seguido del directorio");
                        System.out.println("-lm para listar todos los métodos del proyecto:\t seguido del directorio");
                        System.out.println("\t si seleccionas un archivo y adicionas el nombre del metodo se retorna el bloque de codigo de ese metodo");
                        System.out.println("-lt para listar todos los TODO del proyecto:\t seguido del directorio");
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

