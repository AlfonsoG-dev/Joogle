package Mundo;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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
    public void buscarSentencia(String filePath, String sentencia) {
        try {
            String[] 
                methodNames = utils.getMethodName(filePath).split("\n"),
                types       = utils.compareToReturnType(filePath, sentencia).split("\n"),
                arguments   = utils.compareToArguments(filePath, sentencia).split("\n");
            File miFile = new File(filePath);
            if (miFile.exists()) {
                for(int i = 0; i < methodNames.length; ++i) {
                    if (sentencia.equals("")) {
                        format.formatoBusquedaSentencia(
                            utils.getLineNumber(filePath, methodNames[i]),
                            filePath,
                            methodNames[i],
                            types[i],
                            arguments[i]
                        );
                    } else {
                        format.formatoBusquedaSentencia(
                            utils.getLineNumber(filePath, methodNames[i]),
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
                String sentence = searchSentence.replace("/", "");
                buscarSentencia(filePath, sentence);
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
    public void searcInDirectory(String directory, String searchSentence) {
        try {
            File miFile = new File(directory);
            if(miFile.isFile()) {
                throw new Exception("ONLY WORKS WITH DIRECTORIES");
            }
            DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
            ArrayList<File> fileNames = fileUtils.getFilesFromDirectory(files);
            fileNames
                .parallelStream()
                .map(e -> e.getPath())
                .forEach(e -> {
                    searchInFile(e, searchSentence);
                });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
    * busca la sentencia dentro de los archivos de los directorios del directorio designado
    * @param directorys: directorys del directory designado
    * @param searchSentence: sentencia a buscar
    */
    public void searcInDirectories(String directorys, String searchSentence) {
        try {
            File miFile = new File(directorys);
            if(miFile.isFile()) {
                throw new Exception("ONLY WORKS WITH DIRECTORIES");
            } else if(miFile.isDirectory()) {
                DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
                ArrayList<File> filesName = fileUtils.getFilesFromDirectories(files);
                filesName
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
    public void buscarTODO(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.isFile()) {
                utils.getTodoSentences(miFile.getPath());
            } else if(miFile.isDirectory()) {
                DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
                ArrayList<File> fileNames = fileUtils.getFilesFromDirectories(files);
                fileNames
                    .parallelStream()
                    .map(e -> e.getPath())
                    .filter(e -> !e.isEmpty())
                    .forEach(e -> {
                        buscarTODO(e);
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
    public void buscarFiles(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.isDirectory()) {
                DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
                ArrayList<File> fileNames = fileUtils.getFilesFromDirectories(files);
                fileNames
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
    public void buscarMethods(String filePath, String sentence) {
        try {
            String cSentence = sentence.replace("/", "");
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
                DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
                ArrayList<File> filesName = fileUtils.getFilesFromDirectories(files);
                filesName
                    .parallelStream()
                    .map(e -> e.getPath())
                    .filter(e -> !e.isEmpty())
                    .forEach(e -> {
                        buscarMethods(e, cSentence);
                    });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

