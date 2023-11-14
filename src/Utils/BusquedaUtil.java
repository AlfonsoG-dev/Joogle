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
    public String GetMethodName(String filePath) {
        String build = "";
        String[] partition = textUtils.GetSentences(filePath).split("\n");
        for(String p: partition) {
            build += MethodModel.getNameOfMethods(p);
        }
        return build;
    }
    /**
    * genera un String con el tipo de retorno del método
    * @param filePath: ruta del archivo a leer
    * @return String con el tipo de retorno del método
    */
    private String GetReturnType(String filePath) {
        String[] sentences = textUtils.GetSentences(filePath).split("\n");
        String tipes = "";
        for(String s: sentences) {
            tipes += MethodModel.getReturnType(s);
        }
        return tipes;
    }
    /**
    * genera un String con los argumentos que recibe el método
    * @param filePath: ruta del archivo a leer
    * @return String con los argumentos del método
    */
    private String GetArguments(String filePath) {
        String[] sentences = textUtils.GetSentences(filePath).split("\n");
        String arguments = "";
        for(String s: sentences) {
            arguments += MethodModel.getArguments(s);
        }
        return arguments;
    }
    /**
    * genera un string con la sentencia completa según el método buscado
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    * @return String con la sentencia completa del método buscado
    */
    public String LocalizarMetodo(String filePath, String sentencia) {
        String build = "";
        ArrayList<MethodModel> partition = textUtils.listMethods(filePath);
        for(MethodModel mt: partition) {
            String r = sentencia.trim().toLowerCase();
            if(mt.GetMethodName().toLowerCase().contains(r)) {
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
    public int GetLineNumber(String filePath, String sentence) {
        ArrayList<MethodModel> separate = textUtils.listMethods(filePath);
        int res = 0;
        for(MethodModel mt: separate) {
            if(mt.GetMethodName().toLowerCase().contains(sentence.toLowerCase())) {
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
    public String CompareToReturnType(String filePath, String sentence) {
        String st = sentence.split("=>")[0].replace(" ", "").toLowerCase();
        String[] sentences = GetReturnType(filePath).split("\n");
        String result = "";
        int r = 0;
        for(String s: sentences) {
            if(st.equals("")) {
                result += format.SetColorSentence(s, Colores.ANSI_YELLOW) + "\n";
                ++r;
            } else if(s.toLowerCase().replace(" ", "").equals(st) ||
                    textUtils.CompareCharToChar(s, st) == st.length()) {
                result += format.SetColorSentence(s, Colores.GREEN_UNDERLINED) + "\n";
                ++r;
            } else if(textUtils.CompareCharToChar(s, st) > st.length()) {
                result += format.SetColorSentence(s, Colores.ANSI_YELLOW) + "\n";
                ++r;
            } else {
                result += s + "\n";
            }
        }
        format.ConcurrencyFormat(r, "ReturnType");
        return result;
    }
    /**
    * modifica los datos para que solo aquellos que tengan el mismo valor se pinten de cierto color
    * @param filePath: archivo a leer
    * @param sentence: sentencia buscada
    * @return lista de datos modificados con el color
    */
    public String CompareToArguments(String filePath, String sentence) {
        String st = "";
        if(sentence.contains("=>")) {
            st = sentence.split("=>")[1].replace(" ", "").toLowerCase();
        }
        String[] sentences = GetArguments(filePath).split("\n");
        String result = "";
        int r = 0;
        for(int i=0; i<sentences.length; ++i) {
            String s = sentences[i].replace(" ", "").toLowerCase();
            if(st.equals("")) {
                result += sentences[i] + "\n";
                ++r;
            } else if(s.equals(st)) {
                result += format.SetColorSentence(sentences[i], Colores.GREEN_UNDERLINED) + "\n";
                ++r;
            } else if(textUtils.CompareCharToChar(s, st) > st.length()) {
                result += format.SetColorSentence(sentences[i], Colores.ANSI_YELLOW) + "\n";
                ++r;
            } else {
                if(s.contains(",") && st.contains(",")) {
                    String[] comas = sentences[i].split(",");
                    String[] sComas = st.split(",");
                    String cB = "";
                    for(int c=0; c<comas.length; ++c) {
                        for(int sc=0; sc<sComas.length; ++sc) {
                            if(comas[c].replace(" ", "").replace(")", "").toLowerCase().equals(sComas[sc].replace(")", "")) 
                                    || textUtils.CompareCharToChar(comas[c], sComas[sc]) > st.length()) {
                                comas[c] = format.SetColorSentence(comas[c], Colores.ANSI_YELLOW);
                            }
                        }
                        cB += comas[c] + ", ";
                    }
                    sentences[i] = cB.substring(0, cB.length()-2);
                } else if(st.contains(",")) {
                    String sComa = sentence.split(",")[0];
                    String comas = sentences[i].split(",")[0];
                    String cB = "";
                    if(comas.replace(" ", "").toLowerCase().equals(sComa.replace(" ", "").toLowerCase() + ")") 
                            || textUtils.CompareCharToChar(comas, sComa) > st.length()) {
                        comas = format.SetColorSentence(comas, Colores.ANSI_YELLOW);
                    }
                    cB += comas + ", ";
                    sentences[i] = cB.substring(0, cB.length()-2);
                }
                result += sentences[i] + "\n";
            }
        }
        format.ConcurrencyFormat(r, "Arguments");
        return result;
    }
    /**
     * da el contexto del método, es decir, el bloque de codigo dentro del método buscado
     * @param filePath: archivo a leer
     * @param sentencia: método buscado
     */
    public String GetMethodContext(String filePath, String sentencia) {
        String[] fileLines = textUtils.GetSentences(filePath).split("\n");
        int inicial = GetLineNumber(filePath, sentencia);
        String buscada = inicial + ":" + LocalizarMetodo(filePath, sentencia);
        String conNumLinea = "";
        int end = 0;
        for(int i=0; i<fileLines.length; ++i) {
            conNumLinea = GetLineNumber(filePath, MethodModel.getNameOfMethods(fileLines[i])) + ":" + fileLines[i];
            if(conNumLinea.equals(buscada) && (i+1) < fileLines.length) {
                end = GetLineNumber(filePath, MethodModel.getNameOfMethods(fileLines[i+1]));
                format.formatoPresentFilename(filePath, inicial);
            } else if(conNumLinea.equals(buscada) && (i+1) >= fileLines.length) {
                format.formatoPresentFilename(filePath, inicial);
                end = -1;
            }
        }
        String respuesta = "";
        String[] fileText = fileUtils.GetCleanTextFromFile(filePath).split("\n");
        if(end > 0 &&
                textUtils.DeleteComments(fileText, inicial, end).isEmpty() == false) {
            System.out.println(textUtils.DeleteComments(fileText, inicial, end));
        } else if(end < 0 &&
                textUtils.DeleteComments(fileText, inicial, fileText.length).isEmpty() == false) {
            System.out.println(textUtils.DeleteComments(fileText, inicial, fileText.length));
        }
        return respuesta;
    }
    /**
     * genera un string con las sentencias que sean "todos"
     * @param filePath: archivo a leer
     * @return true si el archivo tiene sentencias todo, false de lo contrario
     */
    public void GetTodoSentences(String filePath) {
        String[] fileLines = fileUtils.GetCleanTextFromFile(filePath).split("\n");
        boolean existe = false;
        for(int i=0; i<fileLines.length; ++i) {
            String valores = fileLines[i].replace(" ", "");
            if(valores.startsWith("//TODO:") || valores.startsWith("*TODO:") ||
                    valores.startsWith("//TODO") || valores.startsWith("/*TODO:")) {
                System.out.println(format.SetColorSentence(filePath, Colores.ANSI_YELLOW) + ":" + i + fileLines[i]);
                existe = true;
            }
        }
        if(existe == false) {
            System.err.println("\n\t NO TIENE TODO's POR EL MOMENTO \n");
        }
    }
}
