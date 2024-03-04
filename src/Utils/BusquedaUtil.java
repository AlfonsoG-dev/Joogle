package Utils;

import java.util.ArrayList;

import Mundo.Modelos.MethodModel;

import Visual.BusquedaFormat;
import Visual.Colores;

/**
 * métodos especiales para ayudar a crear la busqueda
 */
public class BusquedaUtil {
    /**
     * formato visual para la busqueda
     */
    private BusquedaFormat format;
    /**
     * manipulación del archivo
     */
    private FileUtils fileUtils;
    /**
     * manipulación de texto
     */
    private TextUtils textUtils;
    /**
     * constructor
     */
    public BusquedaUtil() {
        format = new BusquedaFormat();
        fileUtils = new FileUtils();
        textUtils = new TextUtils();
    }
    /**
    * genera un String con los métodos del archivo
    * @param filePath: ruta del archivo a leer
    * @return String con los métodos del archivo
    */
    public String getMethodName(String filePath) {
        StringBuffer build = new StringBuffer();
        String[] partition = textUtils.getSentences(filePath).split("\n");
        for(String p: partition) {
            build.append(MethodModel.getNameOfMethods(p));
        }
        return build.toString();
    }
    /**
    * genera un String con el tipo de retorno del método
    * @param filePath: ruta del archivo a leer
    * @return String con el tipo de retorno del método
    */
    private String getReturnType(String filePath) {
        String[] sentences = textUtils.getSentences(filePath).split("\n");
        StringBuffer types = new StringBuffer();
        for(String s: sentences) {
            types.append(MethodModel.getReturnType(s));
        }
        return types.toString();
    }
    /**
    * genera un String con los argumentos que recibe el método
    * @param filePath: ruta del archivo a leer
    * @return String con los argumentos del método
    */
    private String getArguments(String filePath) {
        String[] sentences = textUtils.getSentences(filePath).split("\n");
        StringBuffer arguments = new StringBuffer();
        for(String s: sentences) {
            arguments.append(MethodModel.getArguments(s));
        }
        return arguments.toString();
    }
    /**
    * genera un string con la sentencia completa según el método buscado
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    * @return String con la sentencia completa del método buscado
    */
    public String localizarMetodo(String filePath, String sentencia) {
        String 
            r     = sentencia.trim().toLowerCase(),
            build = "";
        ArrayList<MethodModel> partition = textUtils.listMethods(filePath);
        for(MethodModel mt: partition) {
            if(mt.getMethodName().toLowerCase().contains(r)) {
                build = mt.getSentences();
            }
        }
        return build;
    }
    /**
    * da el número de linea del método buscado
    * @param filePath: ruta del archivo a leer
    * @param sentence: sentencia a buscar
    * @return número de linea del método buscado
    */
    public int getLineNumber(String filePath, String sentence) {
        ArrayList<MethodModel> separate = textUtils.listMethods(filePath);
        int res = 0;
        for(MethodModel mt: separate) {
            if(mt.getMethodName().toLowerCase().contains(sentence.toLowerCase())) {
                res = mt.getLineNumber();
            }
        }
        return res;
    }
    /**
    * modifica los datos para que solo aquellos que tengan el mismo valor se pinten de cierto color
    * @param filePath: archivo a leer
    * @param sentence: sentencia buscada
    * @return lista de datos modificados con el color
    */
    public String compareToReturnType(String filePath, String sentence) {
        String st = sentence.split("=>")[0].replace(" ", "").toLowerCase();
        StringBuffer result = new StringBuffer();
        String[] sentences = getReturnType(filePath).split("\n");
        int r = 0;
        for(String s: sentences) {
            if(st.equals("")) {
                result.append(format.setColorSentence(s, Colores.ANSI_YELLOW) + "\n");
                ++r;
            } else if(s.toLowerCase().replace(" ", "").equals(st) ||
                    textUtils.compareCharToChar(s, st) == st.length()) {
                result.append(format.setColorSentence(s, Colores.GREEN_UNDERLINED) + "\n");
                ++r;
            } else if(textUtils.compareCharToChar(s, st) > st.length()) {
                result.append(format.setColorSentence(s, Colores.ANSI_YELLOW) + "\n");
                ++r;
            } else {
                result.append(s + "\n");
            }
        }
        format.concurrencyFormat(r, "ReturnType");
        return result.toString();
    }
    /**
    * modifica los datos para que solo aquellos que tengan el mismo valor se pinten de cierto color
    * @param filePath: archivo a leer
    * @param sentence: sentencia buscada
    * @return lista de datos modificados con el color
    */
    public String compareToArguments(String filePath, String sentence) {
        String st = sentence.split("=>")[1].replace(" ", "").toLowerCase();
        StringBuffer result = new StringBuffer();
        if(sentence.contains("=>")) {
        }
        String[] sentences = getArguments(filePath).split("\n");
        int r = 0;
        for(int i=0; i<sentences.length; ++i) {
            String s = sentences[i].replace(" ", "").toLowerCase();
            if(st.equals("")) {
                result.append(sentences[i] + "\n");
                ++r;
            } else if(s.equals(st)) {
                result.append(
                        format.setColorSentence(sentences[i], Colores.GREEN_UNDERLINED) + "\n"
                );
                ++r;
            } else if(textUtils.compareCharToChar(s, st) > st.length()) {
                result.append(
                        format.setColorSentence(sentences[i], Colores.ANSI_YELLOW) + "\n"
                );
                ++r;
            } else {
                if(s.contains(",") && st.contains(",")) {
                    String[] 
                        comas  = sentences[i].split(","),
                        sComas = st.split(",");
                    StringBuffer cB = new StringBuffer();
                    for(int c=0; c<comas.length; ++c) {
                        for(int sc=0; sc<sComas.length; ++sc) {
                            String 
                                conditionA = comas[c].replace(" ", "").replace(")", "").toLowerCase(),
                                conditionB = sComas[sc].replace(")", "");
                            int conditionC = textUtils.compareCharToChar(comas[c], sComas[sc]);
                            if(conditionA.equals(conditionB) ||  conditionC > st.length()) {
                                comas[c] = format.setColorSentence(comas[c], Colores.ANSI_YELLOW);
                            }
                        }
                        cB.append(comas[c] + ", ");
                    }
                    sentences[i] = cB.substring(0, cB.length()-2);
                } else if(st.contains(",")) {
                    String 
                        comas      = sentences[i].split(",")[0],
                        sComa      = sentence.split(",")[0],
                        conditionA = comas.replace(" ", "").toLowerCase(),
                        conditionB = sComa.replace(" ", "").toLowerCase() + ")";

                    StringBuffer cB = new StringBuffer();
                    int conditionC = textUtils.compareCharToChar(comas, sComa);
                    if(conditionA.equals(conditionB) || conditionC > st.length()) {
                        comas = format.setColorSentence(comas, Colores.ANSI_YELLOW);
                    }
                    cB.append(comas + ", ");
                    sentences[i] = cB.substring(0, cB.length()-2);
                }
                result.append(sentences[i] + "\n");
            }
        }
        format.concurrencyFormat(r, "Arguments");
        return result.toString();
    }
    private void contextMessage(String s) {
        if(!s.isEmpty()) {
            System.out.println(s);
        }
    }
    /**
     * da el contexto del método, es decir, el bloque de codigo dentro del método buscado
     * @param filePath: archivo a leer
     * @param sentencia: método buscado
     */
    public synchronized String getMethodContext(String filePath, String sentencia) {
        String[] fileLines = textUtils.getSentences(filePath).split("\n");
        int inicial = getLineNumber(filePath, sentencia);
        String 
            buscada     = inicial + ":" + localizarMetodo(filePath, sentencia),
            conNumLinea = "",
            respuesta   = "";
        int end = 0;
        for(int i=0; i<fileLines.length; ++i) {
            conNumLinea = getLineNumber(
                    filePath,
                    MethodModel.getNameOfMethods(fileLines[i])
            ) + ":" + fileLines[i];
            if(conNumLinea.equals(buscada) && (i+1) < fileLines.length) {
                end = getLineNumber(
                        filePath,
                        MethodModel.getNameOfMethods(fileLines[i+1])
                );
                format.formatoPresentFilename(filePath, inicial);
            } else if(conNumLinea.equals(buscada) && (i+1) >= fileLines.length) {
                format.formatoPresentFilename(filePath, inicial);
                end = -1;
            }
        }
        String[] fileText = fileUtils.getCleanTextFromFile(filePath).split("\n");
        String 
            s = "",
            e = "";
        if(end > 0) {
            s = textUtils.deleteComments(
                    fileText,
                    inicial,
                    end
            );
            contextMessage(s);
        } else if(end < 0) {
            e = textUtils.deleteComments(
                    fileText,
                    inicial,
                    fileText.length
            );
            contextMessage(e);
        }
        return respuesta;
    }
    /**
     * genera un string con las sentencias que sean "todos"
     * @param filePath: archivo a leer
     * @return true si el archivo tiene sentencias todo, false de lo contrario
     */
    public synchronized void getTodoSentences(String filePath) {
        String[] fileLines = fileUtils.getCleanTextFromFile(filePath).split("\n");
        boolean existe = false;

        for(int i=0; i<fileLines.length; ++i) {
            String valores = fileLines[i].replace(" ", "");
            boolean
                conditionA = valores.startsWith("*TODO:"),
                conditionB = valores.startsWith("/*TODO:") && valores.endsWith("*/"),
                conditionC = valores.startsWith("//TODO:");
            if(conditionA) {
                int 
                    line  = i+1,
                    count = line;
                String 
                    s = fileLines[i],
                    e = "",
                    b = "\n";
                while(count < fileLines.length) {
                    if(fileLines[count].contains("*/")) {
                        b += s + "\n" + e; 
                        break;
                    } else if(fileLines[count].contains("*")) {
                        e += fileLines[count] + "\n";
                        ++count;
                    }
                }
                System.out.println(
                        format.setColorSentence(
                            filePath,
                            Colores.ANSI_YELLOW
                        ) + ":" + line + b
                );
            } else if(conditionB || conditionC) {
                int line = i+1;
                System.out.println(
                        format.setColorSentence(
                            filePath,
                            Colores.ANSI_YELLOW
                        ) + ":" +  line + fileLines[i]
                );
                existe = true;
            }
        }
        if(existe == false) {
            System.err.println("\n\t NO TIENE TODO's POR EL MOMENTO \n");
        }
    }
}
