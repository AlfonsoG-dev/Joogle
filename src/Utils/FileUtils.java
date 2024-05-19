package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * record con los métodos para utilizar los archivos
 */
public record FileUtils() {
    /**
    * tokens a ignorar en la busqueda
    * @return lista de tokens a ignorar
    */
    public List<String> declarationTokenList() {
        String[] tokens = {
            "abstract",
            "class",
            "default",
            "enum",
            "final",
            "interface",
            "public",
            "private",
            "protected",
            "record",
            "synchronized",
            "static"
        };
        return Arrays.asList(tokens);
    }
    /**
     * genera un string con la ruta de los archivos dentro del directorio
     * @param files: los archivos dentro del directorio
     * @return string con la ruta de los archivos
     */
    public List<File> getFilesFromDirectory(DirectoryStream<Path> myFiles) throws IOException {
        List<File> files = new ArrayList<>();
        myFiles
            .forEach(e -> {
                File f = e.toFile();
                if(f.isFile() && f.getName().contains(".java")) {
                    files.add(f);
                }
            });
        return files;
    }
    /**
    * genera un String con la ruta de los archivos dentro de los directorios
    * <br> pre: </br> busca dentro de los directorios un archivo; si el hijo es directorio ingresa y busca los archivos
    * @param miFiles: los archivos dentro del directorio designado
    * @throws IOException: error al buscar los archivos del directorio
    * @return String con la ruta de los archivos
    */
    public List<File> getFilesFromDirectories(DirectoryStream<Path> myFiles) {
        List<File> files = new ArrayList<>();
        myFiles
            .forEach(e -> {
                File f = e.toFile();
                if(f.isFile() && f.getName().contains(".java")) {
                    files.add(f);
                }
                if(f.isDirectory()) {
                    try {
                        files.addAll(
                                getFilesFromDirectories(
                                    Files.newDirectoryStream(f.toPath())
                            )
                        );
                    } catch(Exception err) {
                        err.printStackTrace();
                    }
                }
            });

        return files;
    }
    /**
    * Genera un String con los valores del archivo con numero de linea
    * @param filePath: ruta del archivo a leer
    * @return String con los datos del archivo con numero de linea
    */
    public String getTextFromFile(String filePath) {
        StringBuffer build = new StringBuffer();
        FileReader miReader = null;
        BufferedReader miBufferReader = null;
        try {
            miReader = new FileReader(filePath);
            miBufferReader = new BufferedReader(miReader);
            int i=1;
            while(miBufferReader.ready()) {
                build.append(i);
                build.append(":");
                build.append(miBufferReader.readLine());
                build.append("\n");
                ++i;
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if(miReader != null) {
                try {
                    miReader.close();
                } catch(Exception e) {
                    System.err.println(e);
                }
            }
            if(miBufferReader != null) {
                try {
                    miBufferReader.close();
                } catch(Exception e) {
                    System.err.println(e);
                } finally {
                    miBufferReader = null;
                }
            }
        }
        return build.toString();
    }
    /**
     * genera un String con los valores del archivo sin numero de linea
     * @param filePath: ruta del archivo a leer
     * @return String con los datos del archivo sin numero de linea
     */
    public String getCleanTextFromFile(String filePath) {
        StringBuffer build = new StringBuffer();
        FileReader miReader = null;
        BufferedReader miBufferReader = null;
        try {
            miReader = new FileReader(filePath);
            miBufferReader = new BufferedReader(miReader);
            while(miBufferReader.ready()) {
                build.append(miBufferReader.readLine());
                build.append("\n");
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if(miReader != null) {
                try {
                    miReader.close();
                } catch(Exception e) {
                    System.err.println(e);
                }
            }
            if(miBufferReader != null) {
                try {
                    miBufferReader.close();
                } catch(Exception e) {
                    System.err.println(e);
                } finally {
                    miBufferReader = null;
                }
            }
        }
        return build.toString();
    }
}
