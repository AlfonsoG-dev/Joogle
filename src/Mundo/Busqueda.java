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
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }

    }
    /**
    * busca la sentencia en el archivo designado
    * @param filePath: ruta del archivo a leer
    * @param searchSentence: sentencia a buscar
    */
    public synchronized void SearchInFile(String filePath, String searchSentence) {
        try {
            File miFile = new File(filePath);
            if(miFile.isFile() && miFile.getName().contains(".java")) {
                System.out.println(String.format("\n%s\n", format.SetColorSentence(filePath, Colores.ANSI_CYAN)));
                String sentence = searchSentence.replace("/", "");
                BuscarSentencia(filePath, sentence);
            }
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
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
            DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
            ArrayList<String> fileNames = fileUtils.GetFilesFromDirectory(files);
            fileNames
                .parallelStream()
                .forEach(e -> {
                    SearchInFile(e, searchSentence);
                });
        } catch(Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    /**
    * busca la sentencia dentro de los archivos de los directorios del directorio designado
    * @param directorys: directorys del directory designado
    * @param searchSentence: sentencia a buscar
    */
    public void SearcInDirectories(String directorys, String searchSentence) {
        try {
            File miFile = new File(directorys);
            if(miFile.isDirectory()) {
                DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
                ArrayList<String> filesName = fileUtils.GetFilesFromDirectories(files);
                filesName
                    .parallelStream()
                    .forEach(e -> {
                        SearchInFile(e, searchSentence);
                    });
            }

        } catch(Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    /**
     * buscar "todos" en el proyecto
     * @param filePath: archivo a leer las sentencias todo
     */
    public void BuscarTODO(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.isFile()) {
                utils.GetTodoSentences(miFile.getPath());
            } else {
                DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
                ArrayList<String> fileNames = fileUtils.GetFilesFromDirectories(files);
                fileNames
                    .parallelStream()
                    .forEach(e -> {
                        if(e.isEmpty() == false) {
                            BuscarTODO(e);
                        }
                    });
            }

        } catch(Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    /**
     * buscar la ruta de los archivos dentro del proyecto
     * @param filePath: ruta de los archivos a leer
     */
    public void BuscarFiles(String filePath) {
        try {
            File miFile = new File(filePath);
            DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
            ArrayList<String> fileNames = fileUtils.GetFilesFromDirectories(files);
            fileNames
                .parallelStream()
                .forEach(e -> {
                    if(e.isEmpty() == false) {
                        format.formatoBusquedaFiles(e);
                    }
                });
        } catch(Exception e) {
            System.err.println(e.getLocalizedMessage());
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
            File miFile = new File(filePath);
            if(miFile.isFile() && cSentence.equals("") == false) {
                utils.GetMethodContext(miFile.getPath(), cSentence);
            } else if(miFile.isFile() && cSentence.equals("")) {
                String[] metodos = utils.GetMethodName(filePath).split("\n");
                for(String m: metodos) {
                    int lineNumber = utils.GetLineNumber(miFile.getPath(), m);
                    format.formatoBusquedaMethod(miFile.getPath(), m, lineNumber);
                }
            }
            else if(miFile.isDirectory()) {
                DirectoryStream<Path> files = Files.newDirectoryStream(miFile.toPath());
                ArrayList<String> filesName = fileUtils.GetFilesFromDirectories(files);
                filesName
                    .parallelStream()
                    .forEach(e -> {
                        if(e.isEmpty() == false ) {
                            BuscarMethods(e, cSentence);
                        }
                    });
            }
        } catch(Exception e) {
            //
        }
    }
}

