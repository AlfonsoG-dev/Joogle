import java.io.File;
import java.io.IOException;

import Utils.BusquedaUtil;

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
    * color cyan para el path del archivo
    */
    private final String ANSI_CYAN = "\u001B[46m";
    /**
    * quitar color para los resultados no exactos
    */
    private final String ANSI_RESET = "\u001B[0m";
    /**
     */
    private BusquedaUtil utils;
    /**
    * constructor
    */
    public Busqueda(String[] nOptions) {
        this.options = nOptions;
        utils = new BusquedaUtil();
    }
    /**
    * busca la sentencia según el tipo de retorno y los argumentos
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    */
    private void BuscarSentencia(String filePath, String sentencia) {
        try {
            String[] method_names = utils.GetMethodName(filePath).split("\n");
            String[] types = utils.CompareToReturnType(filePath, sentencia).split("\n");
            String[] arguments = utils.CompareToArguments(filePath, sentencia).split("\n");
            File miFile = new File(filePath);
            if (miFile.exists()) {
                for(int i = 0; i < method_names.length; ++i) {
                    if (sentencia.equals("")) {
                        utils.BusquedaFormat(
                        filePath,
                        method_names[i],
                        types[i],
                        arguments[i]
                        );
                    } else {
                        utils.BusquedaFormat(
                        filePath,
                        method_names[i],
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
    * genera un String con la ruta de los archivos dentro de los directorios
    * <br> pre: </br> busca dentro de los directorios un archivo; si el hijo es directorio ingresa y busca los archivos
    * @param miFiles: los archivos dentro del directorio designado
    * @throws IOException: error al buscar los archivos del directorio
    * @return String con la ruta de los archivos
    */
    private String GetFilesFromDirectory(File[] miFiles) throws IOException {
        String fileName = "";
        for(File f: miFiles) {
            if(f.isFile()) {
                fileName += f.getCanonicalPath() + "\n";
            }
            if(f.isDirectory()) {
                fileName += this.GetFilesFromDirectory(f.listFiles()) + "\n";
            }
        }
        return fileName;
    }
    /**
    * busca la sentencia en el archivo designado
    * @param filePath: ruta del archivo a leer
    */
    public void SearchInFile(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.isFile() && miFile.getName().contains(".java")) {
                System.out.println(String.format("\n%s\n", ANSI_CYAN + filePath + ANSI_RESET));
                for(int i=0; i<options.length; ++i) {
                    if(options[i].contains("/") && options[i].endsWith("/")) {
                        String sentence = options[i].replace("/", "");
                        this.BuscarSentencia(filePath, sentence);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    /**
    * busca la sentencia dentro de los archivos del directorio designado
    * @param directory: directory con los archivos
    */
    public void SearcInDirectory(String directory) {
        try {
            File miFile = new File(directory);
            if(miFile.isDirectory()) {
                File[] files = miFile.listFiles();
                for(File f: files) {
                    this.SearchInFile(f.getCanonicalPath());
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
    * busca la sentencia dentro de los archivos de los directorios del directorio designado
    * @param directorys: directorys del directory designado
    */
    public void SearcInDirectorys(String directorys) {
        try {
            String filesName = "";
            File miFile = new File(directorys);
            if(miFile.isDirectory()) {
                filesName = this.GetFilesFromDirectory(miFile.listFiles());
                String[] partition = filesName.split("\n");
                for(String p: partition) {
                    this.SearchInFile(p);
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
    */
    public void GetFilesFromPath() {
        try {
            String directory = "";
            String fileName = "";
            String directorys = "";
            for(int i=0; i<options.length; ++i) {
                if(options[i].contains("-d")) {
                    directory = options[i+1];
                    this.SearcInDirectory(directory);
                }
                if(options[i].contains("-f")) {
                    fileName = options[i+1];
                    this.SearchInFile(fileName);
                }
                if(options[i].contains("-D")) {
                    directorys = options[i+1];
                    this.SearcInDirectorys(directorys);
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
}

