package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * métodos especiales para ayudar a crear la busqueda
 */
public class BusquedaUtil {
    /**
    * color amarillo para los resultados
    */
    private final String ANSI_YELLOW = "\u001B[33m";
    /**
    * color rojo para los valores numéricos
    */
    private final String ANSI_RED = "\u001B[41m";
    /**
    * quitar color para los resultados no exactos
    */
    private final String ANSI_RESET = "\u001B[0m";
    /**
     * color con linea baja
     */
    private static final String GREEN_UNDERLINED = "\033[4;32m";
    /**
    * tokens a ignorar en la busqueda
    * @return lista de tokens a ignorar
    */
    public ArrayList<String> TokenAccesorList() {
        String[] tokens = new String[]{"public", "private", "protected", "final", "abstract", "static", "class", "interface"};
        ArrayList<String> lista = new ArrayList<String>();
        for(String t: tokens) {
            lista.add(t);
        }
        return lista;
    }
    /**
    * Genera un String con los valores del archivo
    * @param filePath: ruta del archivo a leer
    * @return String con los datos del archivo
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
    * genera un String con las sentencias que indican un método
    * @param filePath: ruta del archivo a leer
    * @return String con las lineas en donde hay métodos
    */
    public String GetSentences(String filePath) {
        String[] fileLines = this.GetTextFromFile(filePath).split("\n");
        String lines = "";
        for(String fl: fileLines) {
            String[] numeros_fl = fl.replace("}", "").split(":");
            if(numeros_fl.length == 2) {
                String valores = numeros_fl[1].trim();
                for(String t: this.TokenAccesorList()) {
                    if(valores.startsWith(t) && valores.contains(")") || valores.endsWith("\n")) {
                        lines += valores.replace("{", "").trim() + "\n";
                    }
                }
            }
        }
        return lines;
    }
    /**
    * genera un String con los métodos del archivo
    * @param filePath: ruta del archivo a leer
    * @return String con los métodos del archivo
    */
    public String GetMethodName(String filePath) {
        String[] sentences = this.GetSentences(filePath).split("\n");
        String nombres = "";
        for(String s: sentences) {
            String[] separate = s.split("\\(");
            String[] methods = separate[0].split(" ");
            nombres += methods[methods.length-1].trim() + "\n";
        }
        return nombres;
    }
    /**
    * genera un String con el tipo de retorno del método
    * @param filePath: ruta del archivo a leer
    * @return String con el tipo de retorno del método
    */
    public String GetReturnType(String filePath) {
        String build = "";
        String[] partition = this.GetSentences(filePath).split("\n");
        for(String p: partition) {
            String rem = p.trim() + ";";
            String datos = rem.split("\\(")[0];
            if(datos.contains(";") == false) {
                String[] separate = datos.split(" ");
                for(int s=1; s<separate.length-1; ++s) {
                    if(this.TokenAccesorList().contains(separate[s]) == false) {
                        if(separate[s].contains(",")) {
                            separate[s] = separate[s].concat(" " + separate[s+1]);
                            separate[s+1] = ";";
                        }
                        if(separate[s] != ";") {
                            build += separate[s] +"\n";
                        }
                    }
                }
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
        String[] sentences = this.GetSentences(filePath).split("\n");
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
        String[] partition = this.GetSentences(filePath).split("\n");

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
        String[] fileLines = this.GetTextFromFile(filePath).split("\n");
        String lines = "";
        for(String fl: fileLines) {
            String[] numeros_fl = fl.replace("}", "").split(":");
            if(numeros_fl.length == 2) {
                String valores = numeros_fl[1].trim();
                for(String t: this.TokenAccesorList()) {
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
                if(this.LocalizarMetodo(filePath, sentence).equals(valores)) {
                    res = Integer.parseInt(numeros_fl[0]);
                }
            }
        }
        return res;
    }
    /**
    * da el formato de respuesta a la busqueda
    * @param filePath: ruta del archivo a leer
    * @param method_name: nombre del método a mostrar como respuesta
    * @param type: tipo de retorno del método
    * @param argument: argumentos del método
    */
    public void BusquedaFormat(String filePath, String method_name, String type, String argument) {
        String build = "";
        int lineNumber = this.GetLineNumber(filePath, method_name);
        if(lineNumber != -1) {
            build = build + "| " + ANSI_RED + lineNumber + ANSI_RESET + " | " + method_name + " :: " + type + " => " + argument + "\n";
        } else {
            build = build + "| " + "unknow" + " | " + method_name + " :: " + type + " => " + argument + "\n";
        }
        System.out.println(build);
    }
    /**
    * da formato a la cantidad de respuestas encontradas
    * @param cantidad: cantidad de veces que se encuentra el valor
    * @param tipo: tipo de valor repetido
    */
    public void ConcurrencyFormat(int cantidad, String tipo) {
        System.out.println(String.format("%s : %s", tipo, ANSI_RED + cantidad + ANSI_RESET) + "\n");
    }
    /**
    * modifica los datos para que solo aquellos que tengan el mismo valor se pinten de cierto color
    * @param filePath: archivo a leer
    * @param sentence: sentencia buscada
    * @return lista de datos modificados con el color
    */
    public String CompareToReturnType(String filePath, String sentence) {
        String st = sentence.split("=>")[0].trim().toLowerCase();
        String[] sentences = this.GetReturnType(filePath).split("\n");
        String result = "";
        int r = 0;
        for(String s: sentences) {
            if(st.equals("")) {
                result += ANSI_YELLOW + s + ANSI_RESET + "\n";
                ++r;
            } else if(s.toLowerCase().equals(st)) {
                result += ANSI_YELLOW + s + ANSI_RESET + "\n";
                ++r;
            } else {
                result += s + "\n";
            }
        }
        ConcurrencyFormat(r, "ReturnType");
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
        String[] sentences = this.GetArguments(filePath).split("\n");
        String result = "";
        int r = 0;
        for(int i=0; i<sentences.length; ++i) {
            String s = sentences[i].replace(" ", "").toLowerCase();
            if(st.equals("")) {
                result += sentences[i] + "\n";
                ++r;
            } else if(s.equals(st)) {
                result += GREEN_UNDERLINED + sentences[i] + ANSI_RESET + "\n";
                ++r;
            } else {
                if(s.contains(",") && st.contains(",")) {
                    String[] comas = sentences[i].split(",");
                    String[] sComas = st.split(",");
                    String cB = "";
                    for(int c=0; c<comas.length; ++c) {
                        for(int sc=0; sc<sComas.length; ++sc) {
                            if(comas[c].replace(" ", "").replace(")", "").toLowerCase().equals(sComas[sc].replace(")", ""))) {
                                comas[c] = ANSI_YELLOW + comas[c] + ANSI_RESET;
                            }
                        }
                        cB += comas[c] + ", ";
                    }
                    sentences[i] = cB.substring(0, cB.length()-2);
                } else if(st.contains(",")) {
                    String sComa = st.split(",")[0];
                    String comas = s.split(",")[0];
                    String cB = "";
                    if(comas.equals(sComa + ")")) {
                        comas = ANSI_YELLOW + comas + ANSI_RESET;
                    }
                    cB += comas + ", ";
                    sentences[i] = cB.substring(0, cB.length()-2);
                }
                result += sentences[i] + "\n";
            }
        }
        this.ConcurrencyFormat(r, "Arguments");
        return result;
    }
}
