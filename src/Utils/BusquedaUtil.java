package utils;

import java.util.List;

import mundo.modelos.Model;

import visual.BusquedaFormat;
import visual.Colores;

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
        StringBuilder build = new StringBuilder();
        String[] partition = textUtils.getSentences(filePath).split("\n");
        for(String p: partition) {
            build.append(Model.getNameOfMethods(p));
        }
        format.concurrencyFormat(partition.length, "Methods");
        return build.toString();
    }
    /**
    * genera un String con el tipo de retorno del método
    * @param filePath: ruta del archivo a leer
    * @return String con el tipo de retorno del método
    */
    private String getReturnType(String filePath) {
        String[] sentences = textUtils.getSentences(filePath).split("\n");
        StringBuilder types = new StringBuilder();
        for(String s: sentences) {
            types.append(Model.getReturnType(s));
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
        StringBuilder arguments = new StringBuilder();
        for(String s: sentences) {
            arguments.append(Model.getArguments(s));
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
        String r     = sentencia.trim().toLowerCase();
        String build = "";
        List<Model> partition = textUtils.listMethods(filePath);
        for(Model mt: partition) {
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
        List<Model> separate = textUtils.listMethods(filePath);
        int res = 0;
        for(Model mt: separate) {
            if(mt.getMethodName().contains(sentence)) {
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
        String st = sentence.split("\\(")[0].replace(" ", "").toLowerCase();
        StringBuilder result = new StringBuilder();
        String[] sentences = getReturnType(filePath).split("\n");
        int r = 0;
        for(String s: sentences) {
            if(st.equals("") || textUtils.compareCharToChar(s, st) > st.length()) {
                result.append(format.setColorSentence(s, Colores.ANSI_YELLOW));
                result.append("\n");
                ++r;
            } else if(s.toLowerCase().replace(" ", "").equals(st) ||
                    textUtils.compareCharToChar(s, st) == st.length()) {
                result.append(format.setColorSentence(s, Colores.GREEN_UNDERLINED));
                result.append("\n");
                ++r;
            } else {
                result.append(s);
                result.append("\n");
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
        String st = "";
        if(!sentence.isEmpty()) {
            st = "(" + sentence.split("\\(")[1].replace(" ", "").toLowerCase();
        }
        StringBuilder result = new StringBuilder();
        String[] sentences = getArguments(filePath).split("\n");
        int r = 0;
        for(int i=0; i<sentences.length; ++i) {
            String s = sentences[i].replace(" ", "").toLowerCase();
            if(st.equals("")) {
                result.append(sentences[i]);
                result.append("\n");
                ++r;
            } else if(s.equals(st)) {
                result.append(format.setColorSentence(sentences[i], Colores.GREEN_UNDERLINED));
                result.append("\n");
                ++r;
            } else if(textUtils.compareCharToChar(s, st) > st.length()) {
                result.append(format.setColorSentence(sentences[i], Colores.ANSI_YELLOW));
                result.append("\n");
                ++r;
            } else {
                if(s.contains(",") && st.contains(",")) {
                    String[] comas  = sentences[i].split(",");
                    String[] sComas = st.split(",");
                    StringBuilder cB = new StringBuilder();
                    for(int c=0; c<comas.length; ++c) {
                        for(int sc=0; sc<sComas.length; ++sc) {
                            String conditionA = comas[c].replace(" ", "").replace(")", "").toLowerCase();
                            String conditionB = sComas[sc].replace(")", "");
                            int conditionC = textUtils.compareCharToChar(comas[c], sComas[sc]);
                            if(conditionA.equals(conditionB) ||  conditionC > st.length()) {
                                comas[c] = format.setColorSentence(comas[c], Colores.ANSI_YELLOW);
                            }
                        }
                        cB.append(comas[c]);
                        cB.append(", ");
                    }
                    sentences[i] = cB.substring(0, cB.length()-2);
                } else if(st.contains(",")) {
                    String comas      = sentences[i].split(",")[0];
                    String sComa      = sentence.split(",")[0];
                    String conditionA = comas.replace(" ", "").toLowerCase();
                    String conditionB = sComa.replace(" ", "").toLowerCase() + ")";

                    StringBuilder cB = new StringBuilder();
                    int conditionC = textUtils.compareCharToChar(comas, sComa);
                    if(conditionA.equals(conditionB) || conditionC > st.length()) {
                        comas = format.setColorSentence(comas, Colores.ANSI_YELLOW);
                    }
                    cB.append(comas);
                    cB.append(", ");
                    sentences[i] = cB.substring(0, cB.length()-2);
                }
                result.append(sentences[i]);
                result.append("\n");
            }
        }
        format.concurrencyFormat(r, "Arguments");
        return result.toString();
    }
    private void contextMessage(String s) {
        if(!s.isBlank()) {
            System.console().printf("%s", s);
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
        String buscada     = inicial + ":" + localizarMetodo(filePath, sentencia);
        String conNumLinea = "";
        String respuesta   = "";
        int end = 0;
        for(int i=0; i<fileLines.length; ++i) {
            conNumLinea = String.format(
                    "%s:%s",
                    getLineNumber(filePath, Model.getNameOfMethods(fileLines[i])),
                    fileLines[i]
            ); 
            if(conNumLinea.equals(buscada) && (i+1) < fileLines.length) {
                end = getLineNumber(filePath, Model.getNameOfMethods(fileLines[i+1]));
                format.formatoPresentFilename(filePath, inicial);
            } else if(conNumLinea.equals(buscada) && (i+1) >= fileLines.length) {
                format.formatoPresentFilename(filePath, inicial);
                end = -1;
            }
        }
        String[] fileText = fileUtils.getCleanTextFromFile(filePath).split("\n");
        String s = "";
        String e = "";
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
     * genera un string con las sentencias que sean tareas incompletas.
     * @param filePath: archivo a leer
     * @return true si el archivo tiene tareas incompletas, false de lo contrario.
     */
    public synchronized void getTodoSentences(String filePath) {
        String[] fileLines = fileUtils.getCleanTextFromFile(filePath).split("\n");
        boolean existe = false;

        for(int i=0; i<fileLines.length; ++i) {
            String valores = fileLines[i].replace(" ", "");
            boolean conditionA = valores.startsWith("*TODO:") || valores.startsWith("*TODO");
            boolean conditionB = valores.startsWith("/*TODO:") || valores.startsWith("/*TODO");
            boolean conditionC = valores.startsWith("//TODO:") || valores.startsWith("//TODO");
            if(conditionA) {
                int line  = i+1;
                int count = line;
                String s = fileLines[i];
                StringBuilder e = new StringBuilder();
                StringBuilder b = new StringBuilder("\n");
                while(count < fileLines.length) {
                    if(fileLines[count].contains("*/")) {
                        b.append(s);
                        b.append("\n");
                        b.append(e); 
                        break;
                    } else if(fileLines[count].contains("*")) {
                        e.append(fileLines[count]);
                        e.append("\n");
                        ++count;
                    }
                }
                System.out.println(
                        String.format(
                            "%s:%s", 
                            format.setColorSentence( filePath, Colores.ANSI_YELLOW),
                            line + b.toString()
                        )
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
        if(!existe) {
            System.err.println("\n[ Info ]: \t NO TIENE TODO's POR EL MOMENTO \n");
        }
    }
}
