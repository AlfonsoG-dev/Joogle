package Mundo;
import java.io.File;
import java.nio.file.Files;

import java.util.List;

import Utils.BusquedaUtil;
import Utils.FileUtils;

import Visual.BusquedaFormat;
import Visual.Colores;

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
        try {
            String[] 
                methodNames = utils.getMethodName(filePath).split("\n"),
                types       = utils.compareToReturnType(filePath, sentencia).split("\n"),
                arguments   = utils.compareToArguments(filePath, sentencia).split("\n");
            File miFile = new File(filePath);
            if (miFile.exists()) {
                System.out.println(
                        methodNames.length + " | " + types.length + " | " + arguments.length
                );
                for(int i = 0; i < methodNames.length; ++i) {
                    if(types[i].contains(Colores.GREEN_UNDERLINED) || arguments[i].contains(Colores.GREEN_UNDERLINED)) {
                        format.formatoBusquedaSentencia(
                            utils.getLineNumber(filePath, methodNames[i]),
                            filePath,
                            methodNames[i],
                            types[i],
                            arguments[i]
                        );
                    } else if (sentencia.equals("")) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
    * busca la sentencia en el archivo designado
    * @param filePath: ruta del archivo a leer
    * @param searchSentence: sentencia a buscar
    */
    public synchronized void searchInFile(String filePath, String searchSentence) {
        try {
            File miFile = new File(filePath);
            if(miFile.isFile() && miFile.getName().contains(".java")) {
                System.out.println(
                        String.format(
                            "\n%s\n",
                            format.setColorSentence(
                                filePath,
                                Colores.ANSI_CYAN
                            )
                        )
                );
                String sentence = searchSentence;
                searchSentence(filePath, sentence);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
    * busca la sentencia dentro de los archivos del directorio designado
    * @param directory: directory con los archivos
    * @param searchSentence: sentencia a buscar
    */
    public void searchInDirectory(String directory, String searchSentence) {
        try {
            File miFile = new File(directory);
            if(miFile.isFile()) {
                throw new Exception("[ Error ]: ONLY WORKS WITH DIRECTORIES");
            }
            List<File> files = fileUtils.getFilesFromDirectory(miFile);
            if(files != null) {
                files
                .stream()
                    .map(e -> e.getPath())
                    .forEach(e -> {
                        searchInFile(e, searchSentence);
                    });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
    * busca la sentencia dentro de los archivos de los directorios del directorio designado
    * @param directorys: directorys del directory designado
    * @param searchSentence: sentencia a buscar
    */
    public void searchInDirectories(String directorys, String searchSentence) {
        try {
            File miFile = new File(directorys);
            if(miFile.isFile()) {
                throw new Exception("[ Error ]: ONLY WORKS WITH DIRECTORIES");
            } else if(miFile.isDirectory()) {
                fileUtils.getFilesFromDirectories(miFile.toPath())
                    .parallelStream()
                    .map(e -> e.getPath())
                    .forEach(e -> {
                        searchInFile(e, searchSentence);
                    });
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * buscar "todos" en el proyecto
     * @param filePath: archivo a leer las sentencias todo
     */
    public void searchTODO(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.isFile()) {
                utils.getTodoSentences(miFile.getPath());
            } else if(miFile.isDirectory()) {
                fileUtils.getFilesFromDirectories(miFile.toPath())
                    .parallelStream()
                    .map(e -> e.getPath())
                    .filter(e -> !e.isEmpty())
                    .forEach(e -> {
                        searchTODO(e);
                    });
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * buscar la ruta de los archivos dentro del proyecto
     * @param filePath: ruta de los archivos a leer
     */
    public void searchFiles(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.isDirectory()) {
                fileUtils.getFilesFromDirectories(miFile.toPath())
                    .parallelStream()
                    .map(e -> e.getPath())
                    .filter(e -> !e.isEmpty())
                    .forEach(e -> {
                        format.formatoBusquedaFiles(e);
                    });
            } else if(miFile.isFile()) {
                format.formatoBusquedaFiles(miFile.getPath());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * busca los métodos de la ruta especifica
     * @param filePath: ruta a leer
     * @param sentence: sentencia a buscar
     */
    public void searchMethods(String filePath, String sentence) {
        try {
            String cSentence = sentence.replace(File.separator, "");
            File miFile = new File(filePath);
            if(miFile.isFile() && !cSentence.equals("")) {
                utils.getMethodContext(miFile.getPath(), cSentence);
            } else if(miFile.isFile() && cSentence.equals("")) {
                String[] metodos = utils.getMethodName(filePath).split("\n");
                for(String m: metodos) {
                    int lineNumber = utils.getLineNumber(miFile.getPath(), m);
                    format.formatoBusquedaMethod(miFile.getPath(), m, lineNumber);
                }
            } else if(miFile.isDirectory()) {
                fileUtils.getFilesFromDirectories(miFile.toPath())
                    .parallelStream()
                    .map(e -> e.getPath())
                    .filter(e -> !e.isEmpty())
                    .forEach(e -> {
                        searchMethods(e, cSentence);
                    });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

