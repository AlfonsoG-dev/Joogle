package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * record con los métodos para utilizar los archivos
 */
public record FileUtils() {
    /**
    * tokens a ignorar en la busqueda
    * @return lista de tokens a ignorar
    */
    public ArrayList<String> TokenList() {
        String[] tokens = new String[] {
            "public", "private", "protected", "final", "abstract", "static", "record", "class", "interface", "enum"
        };
        ArrayList<String> lista = new ArrayList<String>();
        for(String t: tokens) {
            lista.add(t);
        }
        return lista;
    }
    /**
     * genera un string con la ruta de los archivos dentro del directorio
     * @param files: los archivos dentro del directorio
     * @return string con la ruta de los archivos
     */
    public String GetFilesFromDirectory(File[] files) throws IOException {
        String fileNames = "";
        for(File f: files) {
            if(f.isFile() && f.getName().contains(".java")) {
                fileNames += f.getCanonicalPath() + "\n";
            }
        }
        return fileNames;
    }
    /**
    * genera un String con la ruta de los archivos dentro de los directorios
    * <br> pre: </br> busca dentro de los directorios un archivo; si el hijo es directorio ingresa y busca los archivos
    * @param miFiles: los archivos dentro del directorio designado
    * @throws IOException: error al buscar los archivos del directorio
    * @return String con la ruta de los archivos
    */
    public String GetFilesFromDirectories(File[] miFiles) throws IOException {
        String fileName = "";
        for(File f: miFiles) {
            if(f.isFile() && f.getName().contains(".java")) {
                fileName += f.getCanonicalPath() + "\n";
            }
            if(f.isDirectory()) {
                fileName += this.GetFilesFromDirectories(f.listFiles()) + "\n";
            }
        }
        return fileName;
    }
    /**
    * Genera un String con los valores del archivo con numero de linea
    * @param filePath: ruta del archivo a leer
    * @return String con los datos del archivo con numero de linea
    */
    public String GetTextFromFile(String filePath) {
        String build = "";
        FileReader miReader = null;
        BufferedReader miBufferReader = null;
        try {
            miReader = new FileReader(filePath);
            miBufferReader = new BufferedReader(miReader);
            int i=1;
            while(miBufferReader.ready()) {
                build += i + ":" + miBufferReader.readLine() + "\n";
                ++i;
            }
        } catch (Exception var48) {
            System.err.println(var48);
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
        return build;
    }
    /**
     * genera un String con los valores del archivo sin numero de linea
     * @param filePath: ruta del archivo a leer
     * @return String con los datos del archivo sin numero de linea
     */
    public String GetCleanTextFromFile(String filePath) {
        String build = "";
        FileReader miReader = null;
        BufferedReader miBufferReader = null;
        try {
            miReader = new FileReader(filePath);
            miBufferReader = new BufferedReader(miReader);
            while(miBufferReader.ready()) {
                build += miBufferReader.readLine() + "\n";
            }
        } catch (Exception var48) {
            System.err.println(var48);
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
        return build;
    }
}
