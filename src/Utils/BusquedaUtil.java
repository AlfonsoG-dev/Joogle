package Utils;

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
     * da el contexto del método, es decir, el bloque de codigo dentro del método buscado
     * @param filePath: archivo a leer
     * @param sentencia: método buscado
     */
    public String GetMethodContext(String filePath, String sentencia) {
        String[] fileLines = textUtils.GetSentences(filePath).split("\n");
        String buscada = GetLineNumber(filePath, sentencia) + ":" + LocalizarMetodo(filePath, sentencia);
        String conNumLinea = "";
        int inicial = Integer.parseInt(buscada.split(":")[0]);
        int end = 0;
        for(int i=0; i<fileLines.length; ++i) {
            conNumLinea = GetLineNumber(filePath, fileLines[i]) + ":" + fileLines[i];
            if(conNumLinea.equals(buscada) && (i+1) < fileLines.length) {
                end = GetLineNumber(filePath, fileLines[i+1]);
                format.formatoPresentFilename(filePath, inicial);
            } else if(conNumLinea.equals(buscada) && (i+1) >= fileLines.length) {
                format.formatoPresentFilename(filePath, inicial);
                end = -1;
            }
        }
        String respuesta = "";
        String[] fileText = fileUtils.GetCleanTextFromFile(filePath).split("\n");
        if(end != -1) {
            for(int i=inicial-1; i<end-1; ++i) {
                System.out.println(fileText[i]);
            }
        } else {
            for(int i=inicial-1; i<fileText.length; ++i) {
                System.out.println(fileText[i]);
            }
        }
        return respuesta;
    }
    /**
     * genera un string con las sentencias que sean "todos"
     * @param filePath: archivo a leer
     * @return true si el archivo tiene sentencias todo, false de lo contrario
     */
    public boolean GetTodoSentences(String filePath) {
        String[] fileLines = fileUtils.GetTextFromFile(filePath).split("\n");
        boolean exists = false;
        for(String fl: fileLines) {
            String[] numerosFl = fl.split(":");
            for(int i=1; i<numerosFl.length; ++i) {
                String valores = numerosFl[i].trim().replace("*", "").replace("/", "").replace(" ", "").replace("}", "");
                if(valores.toLowerCase().equals("TODO".toLowerCase())) {
                    System.out.println(Colores.ANSI_YELLOW + filePath + Colores.ANSI_RESET + ":" + fl.replace(":", ""));
                    exists = true;
                }
            }
        }
        return exists;
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
            String[] datos = p.split("\\(");
            for(int i=0; i<datos.length-1; ++i) {
                String[] separate = datos[i].split(" ");
                build += separate[separate.length-1].trim() +"\n";
            }
        }
        return build;
    }
    /**
    * genera un String con el tipo de retorno del método
    * @param filePath: ruta del archivo a leer
    * @return String con el tipo de retorno del método
    */
    public String GetReturnType(String filePath) {
        String build = "";
        String[] partition = textUtils.GetSentences(filePath).split("\n");
        for(String p: partition) {
            String rem = p.trim();
            String datos = rem.split("\\(")[0];
            if(datos.contains(";") == false) {
                String[] separate = datos.split(" ");
                if(fileUtils.TokenList().contains(separate[1])) {
                    separate[1] = separate[2];
                }
                if(separate[1].contains(",")) {
                    separate[1] = separate[1].concat(" " + separate[2]);
                }
                build += separate[1] + "\n";
            }
        }
        return build;
    }
    /**
    * genera un String con los argumentos que recibe el método
    * @param filePath: ruta del archivo a leer
    * @return String con los argumentos del método
    */
    public String GetArguments(String filePath) {
        String[] sentences = textUtils.GetSentences(filePath).split("\n");
        String nombres = "", tipos = "";
        for(String s: sentences) {
            String[] separate = s.split("\\(");
            tipos += "(" +  separate[1].trim() + "\n";
        }
        String[] argumentos = tipos.split("\n");
        for(String a: argumentos) {
            if(a.contains(",")) {
                String[] ars = a.split(",");
                String args = "(";
                for(String at: ars) {
                    String tipe = at.replace("(", "").replace(")", "").trim();
                    String[] separate = tipe.split(" ");
                    args += separate[0] + ", ";
                }
                String clean_args = args.substring(0, args.length()-2) + ")";
                nombres += clean_args + "\n";
            } else {
                String[] separate = a.split(" ");
                String args = "";
                if(separate.length > 1) {
                    args += separate[0] + ")";
                } else {
                    args += separate[0];
                }
                nombres += args + "\n";
            }
        }
        String fNombres = nombres.replace("(", "( ").replace(")", " )");
        return fNombres;
    }
    /**
    * genera un string con la sentencia completa según el método buscado
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    * @return String con la sentencia completa del método buscado
    */
    public String LocalizarMetodo(String filePath, String sentencia) {
        String build = "";
        String[] partition = textUtils.GetSentences(filePath).split("\n");

        for(String p: partition) {
            String r = sentencia.trim().replace("::", "");
            if (p.toLowerCase().contains(r.toLowerCase().trim())) {
                build = p;
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
        String[] fileLines = fileUtils.GetTextFromFile(filePath).split("\n");
        String lines = "";
        for(String fl: fileLines) {
            String[] numeros_fl = fl.replace("}", "").split(":");
            if(numeros_fl.length == 2) {
                String valores = numeros_fl[1].trim();
                for(String t: fileUtils.TokenList()) {
                    if(valores.startsWith(t) && valores.contains(")") || valores.endsWith("\n")) {
                        lines += numeros_fl[0] + ":" + valores.replace("{", "").trim() + "\n";
                    }
                }
            }
        }
        int res = 0;
        String[] separate = lines.split("\n");
        for(String fl: separate) {
            String[] numeros_fl = fl.replace("}", "").split(":");
            if(numeros_fl.length == 2) {
                String valores = numeros_fl[1].trim();
                if(LocalizarMetodo(filePath, sentence).equals(valores)) {
                    res = Integer.parseInt(numeros_fl[0]);
                }
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
                result += Colores.ANSI_YELLOW + s + Colores.ANSI_RESET + "\n";
                ++r;
            } else if(s.toLowerCase().replace(" ", "").equals(st) || textUtils.CompareCharToChar(s, st) > 10) {
                result += Colores.GREEN_UNDERLINED + s + Colores.ANSI_RESET + "\n";
                ++r;
            } else if(textUtils.CompareCharToChar(s, st) > 2) {
                result += Colores.ANSI_YELLOW + s + Colores.ANSI_RESET + "\n";
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
                result += Colores.GREEN_UNDERLINED + sentences[i] + Colores.ANSI_RESET + "\n";
                ++r;
            } else if(textUtils.CompareCharToChar(s, st) > 2) {
                result += Colores.ANSI_YELLOW + sentences[i] + Colores.ANSI_RESET + "\n";
                ++r;
            } else {
                if(s.contains(",") && st.contains(",")) {
                    String[] comas = sentences[i].split(",");
                    String[] sComas = st.split(",");
                    String cB = "";
                    for(int c=0; c<comas.length; ++c) {
                        for(int sc=0; sc<sComas.length; ++sc) {
                            if(comas[c].replace(" ", "").replace(")", "").toLowerCase().equals(sComas[sc].replace(")", "")) 
                                    || textUtils.CompareCharToChar(comas[c], sComas[sc]) > 2) {
                                comas[c] = Colores.ANSI_YELLOW + comas[c] + Colores.ANSI_RESET;
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
                            || textUtils.CompareCharToChar(comas, sComa) > 2) {
                        comas = Colores.ANSI_YELLOW + comas + Colores.ANSI_RESET;
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
}
