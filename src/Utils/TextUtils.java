package Utils;

import java.util.ArrayList;
import java.util.List;

import Mundo.Modelos.Model;

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
        StringBuffer lines = new StringBuffer();
        for(int i=0; i<fileLines.length; ++i) {
            String fileData = fileLines[i].trim();
            String[] spaces = fileData.split(" ");
            boolean
                conditionA = fileUtils.declarationTokenList().contains(spaces[0]),
                conditionB = fileData.endsWith(","),
                conditionC = fileData.contains("("),
                conditionD = fileData.endsWith(")") || fileData.endsWith("{");
            if(conditionA && conditionC && conditionD) {
                lines.append(fileData.replace("{", "").trim() + "\n");
            } else if(conditionB) {
                int next = i+1;
                StringBuffer buildMultiLine = new StringBuffer(fileData);
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
                    lines.append(buildMultiLine.toString().replace("{", "").trim() + "\n");
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
                if(fileData != "") {
                    String[] spaces = fileData.split(" ");
                    boolean
                        conditionA = fileUtils.declarationTokenList().contains(spaces[0]),
                        conditionB = fileData.endsWith(",");
                    if(conditionA && !conditionB) {
                        methods.add(
                                new Model(
                                    fileData.trim().replace("{", "").trim(),
                                    Integer.parseInt(lineNumbers[0])
                                )
                        );
                    }
                    if(conditionB) {
                        int next = i+1;
                        String buildMultiLine = fileData;
                        while(next < fileLines.length) {
                            if(fileLines[next].trim().endsWith("{") && !fileLines[next].trim().contains("(")) {
                                buildMultiLine = buildMultiLine.concat(
                                        " " + fileLines[next].trim().split(":")[1].trim().replace("{", "").trim()
                                );
                                break;
                            }
                            ++next;
                        }
                        methods.add(
                                new Model(
                                    buildMultiLine,
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
        StringBuffer res = new StringBuffer();
        if(end == 0) {
            res.append(fileText[inicial-1] + "\n");
        }
        for(int i=inicial-1; i<end-1; ++i) {
            boolean 
                conditionA = fileText[i].trim().startsWith("/*"),
                conditionB = fileText[i].trim().startsWith("/**"),
                conditionC = fileText[i].trim().startsWith("*"),
                conditionD = fileText[i].trim().startsWith("//");
            if(conditionA || conditionB || conditionC || conditionD) {
                fileText[i] = "";
            }
            if(!fileText[i].equals("")) {
                res.append(fileText[i] + "\n");
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
        String 
            n1 = first.toLowerCase(),
            n2 = second.toLowerCase();
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
