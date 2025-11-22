package mundo;
import java.io.File;
import java.util.List;

import utils.BusquedaUtil;
import utils.FileUtils;

import visual.Colores;
import visual.BusquedaFormat;

/**
 * clase para buscar métodos dentro de clases de java
 * EJM: String => (int, boolean)
 */
public class Busqueda {
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
    public Busqueda() {
        utils = new BusquedaUtil();
        format = new BusquedaFormat();
        fileUtils = new FileUtils();
    }
    /**
    * busca la sentencia según el tipo de retorno y los argumentos
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    */
    public void searchSentence(String filePath, String sentencia) {
        String[] methodNames = utils.getMethodName(filePath).split("\n");
        String[] types       = utils.compareToReturnType(filePath, sentencia).split("\n");
        String[] arguments   = utils.compareToArguments(filePath, sentencia).split("\n");
        File f = new File(filePath);
        if (f.exists()) {
            for(int i = 0; i < methodNames.length; ++i) {
                if(types[i].contains(Colores.GREEN_UNDERLINED) ||
                        arguments[i].contains(Colores.GREEN_UNDERLINED) || sentencia.isBlank()) {
                    format.formatoBusquedaSentencia(
                            utils.getLineNumber(filePath, methodNames[i]),
                            filePath,
                            methodNames[i],
                            types[i],
                            arguments[i]
                    );
                }
            }
        }
    }
    /**
    * busca la sentencia en el archivo designado
    * @param filePath: ruta del archivo a leer
    * @param searchSentence: sentencia a buscar
    */
    public synchronized void searchInFile(String filePath, String searchSentence) {
        File miFile = new File(filePath);
        if(miFile.isFile() && miFile.getName().contains(".java")) {
            System.console().printf(
                    "%s",
                    String.format("%n%s%n", format.setColorSentence(filePath, Colores.ANSI_CYAN))
            );
            String sentence = searchSentence;
            searchSentence(filePath, sentence);
        }
    }
    /**
    * busca la sentencia dentro de los archivos del directorio designado
    * @param directory: directory con los archivos
    * @param searchSentence: sentencia a buscar
    */
    public void searchInDirectory(String directory, String searchSentence) {
        File miFile = new File(directory);
        if(miFile.isFile()) {
            System.console().printf("%s%n", "[ Error ]: ONLY WORKS WITH DIRECTORIES");
        }
        List<File> files = fileUtils.getFilesFromDirectory(miFile);
        if(files != null) {
            for(File f: files) {
                searchInFile(f.getPath(), searchSentence);
            }
        }
    }
    /**
    * busca la sentencia dentro de los archivos de los directorios del directorio designado
    * @param directorys: directorys del directory designado
    * @param searchSentence: sentencia a buscar
    */
    public void searchInDirectories(String directorys, String searchSentence) {
        File miFile = new File(directorys);
        if(miFile.isFile()) {
            System.console().printf("%s%n", "[ Error ]: ONLY WORKS WITH DIRECTORIES");
        } else {
            fileUtils.getFilesFromDirectories(miFile.toPath())
                .parallelStream()
                .map(File::getPath)
                .forEach(e -> searchInFile(e, searchSentence));
        }
    }
    /**
     * Search incomplete tasks in the file.
     * @param filePath - the file to search.
     */
    public void searchTODO(String filePath) {
        File miFile = new File(filePath);
        if(miFile.isFile()) {
            utils.getTodoSentences(miFile.getPath());
        } else if(miFile.isDirectory()) {
            fileUtils.getFilesFromDirectories(miFile.toPath())
                .parallelStream()
                .map(File::getPath)
                .filter(e -> !e.isBlank())
                .forEach(this::searchTODO);
        }
    }
    /**
     * buscar la ruta de los archivos dentro del proyecto
     * @param filePath: ruta de los archivos a leer
     */
    public void searchFiles(String filePath) {
        File miFile = new File(filePath);
        if(miFile.isDirectory()) {
            fileUtils.getFilesFromDirectories(miFile.toPath())
                .parallelStream()
                .map(File::getPath)
                .filter(e -> !e.isBlank())
                .forEach(e -> format.formatoBusquedaFiles(e));
        } else if(miFile.isFile()) {
            format.formatoBusquedaFiles(miFile.getPath());
        }
    }
    /**
     * busca los métodos de la ruta especifica
     * @param filePath: ruta a leer
     * @param sentence: sentencia a buscar
     */
    public void searchMethods(String filePath, String sentence) {
        File miFile = new File(filePath);
        if(miFile.isFile() && !sentence.equals("")) {
            utils.getMethodContext(miFile.getPath(), sentence);
        } else if(miFile.isFile() && sentence.equals("")) {
            String[] metodos = utils.getMethodName(filePath).split("\n");
            for(String m: metodos) {
                int lineNumber = utils.getLineNumber(miFile.getPath(), m);
                format.formatoBusquedaMethod(miFile.getPath(), m, lineNumber);
            }
        } else if(miFile.isDirectory()) {
            fileUtils.getFilesFromDirectories(miFile.toPath())
                .parallelStream()
                .map(File::getPath)
                .filter(e -> !e.isBlank())
                .forEach(e -> searchMethods(e, sentence));
        }
    }
}

