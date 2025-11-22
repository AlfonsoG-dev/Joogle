package utils;

import java.util.ArrayList;
import java.util.List;

import mundo.modelos.Model;

/**
 * helper class for text
 */
public class TextUtils {
    /**
     * helper class for files
     */
    private FileUtils fileUtils;
    /**
     * {@link java.lang.reflect.Constructor}
     */
    public TextUtils() {
        fileUtils = new FileUtils();
    }
    /**
    * creates an String with the java file sentences
    * @param filePath: java file
    * @return the java file sentences
    */
    public String getSentences(String filePath) {
        String[] fileLines = fileUtils.getCleanTextFromFile(filePath).split("\n");
        StringBuilder lines = new StringBuilder();
        for(int i=0; i<fileLines.length; ++i) {
            String fileData = fileLines[i].trim();
            String[] spaces = fileData.split(" ");
            boolean conditionA = fileUtils.declarationTokenList().contains(spaces[0]);
            boolean conditionB = fileData.endsWith(",");
            boolean conditionC = fileData.contains("(");
            boolean conditionD = fileData.endsWith(")") || fileData.endsWith("{");
            if(conditionA && conditionC && conditionD) {
                lines.append(fileData.replace("{", "").trim());
                lines.append("\n");
            } else if(conditionB) {
                int next = i+1;
                StringBuilder buildMultiLine = new StringBuilder(fileData);
                while(next < fileLines.length) {
                    if(fileLines[next].trim().endsWith(",")) {
                        buildMultiLine.append(fileLines[next].trim());
                        next++;
                    }
                    if(fileLines[next].trim().endsWith("{") || fileLines[next].trim().endsWith(")")) {
                        buildMultiLine.append(fileLines[next].trim());
                        break;
                    } else {
                        ++next;
                    }
                }
                if(fileUtils.declarationTokenList().contains(buildMultiLine.toString().split(" ")[0])) {
                    lines.append(buildMultiLine.toString().replace("{", "").trim());
                    lines.append("\n");
                }
            }
        }
        return lines.toString();
    }
    /**
     * genera un {@link String} con las sentencia incluido el número de linea 
     * @param filePath: ruta del archivo a leer
     * @return String con las lineas y su numero de linea
     */
    public List<Model> listMethods(String filePath) {
        String[] fileLines = fileUtils.getTextFromFile(filePath).split("\n");
        List<Model> methods = new ArrayList<>();
        for(int i=0; i<fileLines.length; ++i) {
            String[] lineNumbers = fileLines[i].trim().replace("}", "").split(":");
            if(lineNumbers.length == 2) {
                String fileData = lineNumbers[1].trim();
                if(!fileData.isBlank()) {
                    String[] spaces = fileData.split(" ");
                    boolean conditionA = fileUtils.declarationTokenList().contains(spaces[0]);
                    boolean conditionB = fileData.endsWith(",");
                    boolean conditionC = fileData.contains("(");
                    boolean conditionD = fileData.endsWith(")") || fileData.endsWith("{");
                    if(conditionA && conditionC && conditionD) {
                        methods.add(
                            new Model(
                                fileData.trim().replace("{", "").trim(),
                                Integer.parseInt(lineNumbers[0])
                            )
                        );
                    } else if(conditionB) {
                        int next = i+1;
                        StringBuilder buildMultiLine = new StringBuilder(fileData);
                        while(next < fileLines.length) {
                            if(fileLines[next].trim().endsWith(",")) {
                                buildMultiLine.append(fileLines[next].trim().split(":")[1].trim());
                                next++;
                            }
                            if(fileLines[next].trim().endsWith("{") || fileLines[next].trim().endsWith(")")) {
                                buildMultiLine.append(fileLines[next].trim().split(":")[1].trim());
                                break;
                            } else {
                                ++next;
                            }
                        }
                        methods.add(
                            new Model(
                                buildMultiLine.toString(),
                                Integer.parseInt(lineNumbers[0])
                            )
                        );
                    }
                }
            }
        }
        return methods;
    }
    /**
     * elimina los comentarios del texto
     * @param fileText: texto del archivo
     * @param inicial: inicio de la lectura del archivo
     * @param end: final de la lectura del archivo
     * @return un String con la lectura en el rango deseado
     */
    public String deleteComments(String[] fileText, int inicial, int end) {
        StringBuilder res = new StringBuilder();
        if(end == 0) {
            res.append(fileText[inicial-1] + "\n");
        }
        for(int i=inicial-1; i<end-1; ++i) {
            boolean conditionA = fileText[i].trim().startsWith("/*");
            boolean conditionB = fileText[i].trim().startsWith("/**");
            boolean conditionC = fileText[i].trim().startsWith("*");
            boolean conditionD = fileText[i].trim().startsWith("//");
            if(conditionA || conditionB || conditionC || conditionD) {
                fileText[i] = "";
            }
            if(!fileText[i].equals("")) {
                res.append(fileText[i]);
                res.append("\n");
            }
        }
        return res.toString();
    }
    /**
     * comparar letra por letra para distinguir la más parecida
     * @param first: primera letra a comparar
     * @param second: letra para comparar
     * @return el número de coincidencias entre las letras
     */
    public int compareCharToChar(String first, String second) {
        String n1 = first.toLowerCase();
        String n2 = second.toLowerCase();
        int r = 0;
        try {
            for(char fn: n1.toCharArray()) {
                for(char sn: n2.toCharArray()) {
                    if(fn == sn) {
                        ++r;
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return r;
    }
}
