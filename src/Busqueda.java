import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

/**
 * clase para buscar métodos dentro de clases de java
 * EJM: String => (int, boolean)
 */
public class Busqueda {
    /**
    * CLI options
    */
    private String[] options;
    /**
    * color amarillo para los resultados
    */
    private final String ANSI_YELLOW = "\u001B[33m";
    /**
    * color cyan para el path del archivo
    */
    private final String ANSI_CYAN = "\u001B[46m";
    /**
    * color rojo para los valores numéricos
    */
    private final String ANSI_RED = "\u001B[41m";
    /**
    * quitar color para los resultados no exactos
    */
    private final String ANSI_RESET = "\u001B[0m";
    /**
    * constructor
    */
    public Busqueda(String[] nOptions) {
        this.options = nOptions;
    }
    /**
    * tokens a ignorar en la busqueda
    * @return lista de tokens a ignorar
    */
    private ArrayList<String> TokenAccesorList() {
        String[] tokens = new String[]{"public", "private", "protected", "final", "abstract", "static", "record", "class", "interface", "extends", "implements"};
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
    private String GetTextFromFile(String filePath) {
        String sentencia = "";
        String build = "";
        FileReader miReader = null;
        BufferedReader miBufferReader = null;
        try {
            miReader = new FileReader(filePath);
            miBufferReader = new BufferedReader(miReader);
            while(miBufferReader.ready()) {
                sentencia += miBufferReader.readLine() + "\n";
            }
            String[] datos = sentencia.split("\n");
            for(String d: datos) {
                if(d.contains(")") || d.contains("{") || d.endsWith("\n")) {
                    build += d.trim() + "\n";
                }
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
    private String GetSentences(String filePath) {
        String build = "";
        String[] partition = this.GetTextFromFile(filePath).split("\n");
        for(String p: partition) {
            for(String t: this.TokenAccesorList()) {
                if(p.startsWith(t)) {
                    build += p.replace("{", "").trim() +"\n";
                }
            }
        }
        return build;
    }
    /**
    * genera un String con los métodos del archivo
    * @param filePath: ruta del archivo a leer
    * @return String con los métodos del archivo
    */
    private String GetMethodName(String filePath) {
        String build = "";
        String[] partition = this.GetSentences(filePath).split("\n");
        for(String p: partition) {
            String rem = p.replace(";", "").trim();
            String[] datos = rem.split("\\(");
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
    private String GetReturnType(String filePath) {
        String build = "";
        String[] partition = this.GetSentences(filePath).split("\n");
        for(String p: partition) {
            String rem = p.trim() + ";";
            String datos = rem.split("\\(")[0];
            if(datos.contains(";") == false) {
                String[] separate = datos.split(" ");
                if(this.TokenAccesorList().contains(separate[1])) {
                    separate[1] = separate[2];
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
    private String GetArguments(String filePath) {
        String build = "";
        String method_names = "";
        String[] partition = this.GetSentences(filePath).split("\n");
        for(String p: partition) {
            String[] rem = p.split("\\(");
            if(rem.length > 1) {
                method_names += "(" + rem[1] + ";";
            }
        }

        String[] validate = method_names.split(";");
        for(int i=0; i<validate.length; ++i) {
            if(validate[i].contains(",")) {
                String[] coma = validate[i].split(",");
                String[] p = coma[0].split(" ");
                String[] s = coma[1].split(" ");
                String l = "";
                if(s[1].contains(")") == false) {
                    l += p[0]+ ", " + s[1] + ")";
                }
                validate[i] = l;
            } else {
                String[] d = validate[i].split(" ");
                String l = "";
                if(d[0].contains(")") == true) {
                    d[0] = "()";
                    l += d[0];
                }
                else if(d[0].contains(")") == false) {
                    d[0] = d[0] + ")";
                    l += d[0];
                }
                validate[i] = l;
            }
            build += validate[i].replace("(", "( ").replace(")", " )") + "\n";
        }
        return build;
    }
    /**
    * genera un string con la sentencia completa según el método buscado
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    * @return String con la sentencia completa del método buscado
    */
    private String LocalizarMetodo(String filePath, String sentencia) {
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
    private int GetLineNumber(String filePath, String sentence) {
        FileReader miReader = null;
        LineNumberReader miLineReader = null;
        int resultado = -1;

        try {
            miReader = new FileReader(filePath);
            miLineReader = new LineNumberReader(miReader);
            String localizar = this.LocalizarMetodo(filePath, sentence).replace(";", "");

            while(miLineReader.read() != -1) {
                if (miLineReader.readLine().contains(localizar)) {
                    resultado = miLineReader.getLineNumber();
                }
            }
        } catch (Exception var19) {
            //System.err.println(var19);
        } finally {
            if (miReader != null) {
                try {
                    miReader.close();
                } catch (Exception var18) {
                    System.err.println(var18);
                } finally {
                    miReader = null;
                }
            }

            if (miLineReader != null) {
                try {
                    miLineReader.close();
                } catch (Exception var17) {
                    System.err.println(var17);
                } finally {
                    miLineReader = null;
                }
            }

        }

        return resultado;
    }
    /**
    * da el formato de respuesta a la busqueda
    * @param filePath: ruta del archivo a leer
    * @param method_name: nombre del método a mostrar como respuesta
    * @param type: tipo de retorno del método
    * @param argument: argumentos del método
    */
    private void BusquedaFormat(String filePath, String method_name, String type, String argument) {
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
    private void ConcurrencyFormat(int cantidad, String tipo) {
        System.out.println(String.format("%s : %s", tipo, ANSI_RED + cantidad + ANSI_RESET) + "\n");
    }
    /**
    * modifica los datos para que solo aquellos que tengan el mismo valor se pinten de cierto color
    * @param filePath: archivo a leer
    * @param sentence: sentencia buscada
    * @return lista de datos modificados con el color
    */
    private String CompareToReturnType(String filePath, String sentence) {
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
    private String CompareToArguments(String filePath, String sentence) {
        String st = sentence;
        if(sentence.equals("") == false) {
            st = sentence.split("=>")[1].replace(" ", "").toLowerCase();
        }
        String[] sentences = this.GetArguments(filePath).split("\n");
        String result = "";
        int r = 0;
        for(String s: sentences) {
            if(st.equals("")) {
                result += ANSI_YELLOW + s + ANSI_RESET + "\n";
                ++r;
            } else if(s.replace(" ", "").toLowerCase().equals(st)) {
                result += ANSI_YELLOW + s + ANSI_RESET + "\n";
                ++r;
            } else {
                result += s + "\n";
            }
        }
        this.ConcurrencyFormat(r, "Arguments");
        return result;
    }
    /**
    * busca la sentencia según el tipo de retorno y los argumentos
    * @param filePath: ruta del archivo a leer
    * @param sentencia: sentencia a buscar
    */
    private void BuscarSentencia(String filePath, String sentencia) {
        String[] method_names = this.GetMethodName(filePath).split("\n");
        String[] types = this.CompareToReturnType(filePath, sentencia).split("\n");
        String[] arguments = this.CompareToArguments(filePath, sentencia).split("\n");
        try {
            File miFile = new File(filePath);
            if (miFile.exists()) {
                for(int i = 0; i < method_names.length; ++i) {
                    if (sentencia.equals("")) {
                        this.BusquedaFormat(
                        filePath,
                        method_names[i],
                        types[i],
                        arguments[i]
                        );
                    } else {
                        this.BusquedaFormat(
                        filePath,
                        method_names[i],
                        types[i],
                        arguments[i]
                        );
                    }
                }
            } else {
                System.out.println("el archivo no existe");
            }
        } catch (Exception var11) {
            //System.err.println(var11);
        }

    }
    /**
    * genera un String con la ruta de los archivos dentro de los directorios
    * <br> pre: </br> busca dentro de los directorios un archivo; si el hijo es directorio ingresa y busca los archivos
    * @param miFiles: los archivos dentro del directorio designado
    * @throws IOException: error al buscar los archivos del directorio
    * @return String con la ruta de los archivos
    */
    private String GetFilesFromDirectory(File[] miFiles) throws IOException {
        String fileName = "";
        for(File f: miFiles) {
            if(f.isFile()) {
                fileName += f.getCanonicalPath() + "\n";
            }
            if(f.isDirectory()) {
                fileName += this.GetFilesFromDirectory(f.listFiles()) + "\n";
            }
        }
        return fileName;
    }
    /**
    * busca la sentencia en el archivo designado
    * @param filePath: ruta del archivo a leer
    */
    public void SearchInFile(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.isFile() ) {
                System.out.println(String.format("\n%s\n", ANSI_CYAN + filePath + ANSI_RESET));
                for(int i=0; i<options.length; ++i) {
                    if(options[i].contains("/") && options[i].endsWith("/")) {
                        String sentence = options[i].replace("/", "");
                        this.BuscarSentencia(filePath, sentence);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    /**
    * busca la sentencia dentro de los archivos del directorio designado
    * @param directory: directory con los archivos
    */
    public void SearcInDirectory(String directory) {
        try {
            File miFile = new File(directory);
            if(miFile.isDirectory()) {
                File[] files = miFile.listFiles();
                for(File f: files) {
                    if(f.getName().contains(".java")) {
                        this.SearchInFile(f.getCanonicalPath());
                    }
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
    * busca la sentencia dentro de los archivos de los directorios del directorio designado
    * @param directorys: directorys del directory designado
    */
    public void SearcInDirectorys(String directorys) {
        try {
            String filesName = "";
            File miFile = new File(directorys);
            if(miFile.isDirectory()) {
                filesName = this.GetFilesFromDirectory(miFile.listFiles());
                String[] partition = filesName.split("\n");
                for(String p: partition) {
                    if(p.contains(".java")) {
                        this.SearchInFile(p);
                    }
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
    * organiza la forma en la que se ejecutan los argumentos de la consola;
    *  -f es para buscar la sentencia dentro de un archivo 
    *  -d es para buscar la sentencia dentro de los archivos del directory designado, solo se tienen en cuenta los archivos no directorios
    *  -D es para buscar la sentencia dentro de los archivos del directory designado si el directorio tiene más directorios se busca tambien dentro de ellos
    */
    public void GetFilesFromPath() {
        try {
            String directory = "";
            String fileName = "";
            String directorys = "";
            for(int i=0; i<options.length; ++i) {
                if(options[i].contains("-d")) {
                    directory = options[i+1];
                    this.SearcInDirectory(directory);
                }
                if(options[i].contains("-f")) {
                    fileName = options[i+1];
                    this.SearchInFile(fileName);
                }
                if(options[i].contains("-D")) {
                    directorys = options[i+1];
                    this.SearcInDirectorys(directorys);
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
}

