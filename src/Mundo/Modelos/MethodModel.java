package Mundo.Modelos;
import Utils.FileUtils;

/**
 * representa los methodos
 */
public final class MethodModel {

    private final static FileUtils fileUtils = new FileUtils();
    private String sentence;
    private int lineNumber;
    public MethodModel(String nMetodo, int nLineNumber) {
        sentence = nMetodo;
        lineNumber = nLineNumber;
    }

    /**
     * metodos
     */
    public String getSentences() {
        return sentence;
    }
    /**
     * numero de linea 
     */
    public int getLineNumber() {
        return lineNumber;
    }
    /**
     * utilidad para hallar el nombre de un método
     */
    public static String getNameOfMethods(String fileSentence) {
        String build = "";
        String[] partition = fileSentence.split("\n");
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
     * nombre del metodo
     */
    public String getMethodName() {
        return MethodModel.getNameOfMethods(sentence);
    }
    /**
     * utilidad para hallar el tipo de retorno de las sentencias del texto
     */
    public static String getReturnType(String fileSentence) {
        String[] sentences = fileSentence.split("\n");
        String types = "";
        for(String s: sentences) {
            String[] 
                methods = s.split("\\("),
                spaces  = methods[0].split(" ");
            for(int i=0; i<spaces.length; ++i) {
                if(spaces[1].contains(",")) {
                    spaces[0] = spaces[1].concat(" " + spaces[2]);
                }
                if(spaces.length == 2) {
                    spaces[0] = spaces[1];
                } else if(fileUtils.declarationTokenList().contains(spaces[0])) {
                    spaces[0] = spaces[i];
                }
            }
            types += spaces[0] + "\n";
        }
        return types;
    }
    /**
     * utilidad para separar los argumentos que tienen coma
     */
    private static String containsComa(String arguments) {
        String args = "(";
        String[] ars = arguments.split(",");
        for(String at: ars) {
            String type = at.replace("(", "").replace(")", "").trim();
            String[] separate = type.split(" ");
            args += separate[0] + ", ";
        }
        return args.substring(0, args.length()-2) + ")\n";
    }
    /**
     * utilidad para hallar los argumentos de las sentencias del texto
     */
    public static String getArguments(String fileSentence) {
        String[] sentences = fileSentence.split("\n");
        String 
            nombres = "",
            tipos   = "";
        for(String s: sentences) {
            String[] 
                separate  = s.split("\\("),
                partition = separate[1].split("\\)");
            String args = "";
            if(partition.length == 0) {
                args  = ")";
            } else {
                args = partition[0] + ")";
            }
            tipos += "(" +  args.trim() + "\n";
        }
        String[] argumentos = tipos.split("\n");
        for(String a: argumentos) {
            if(a.contains(",")) {
                nombres += MethodModel.containsComa(a);
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
        return nombres.replace("(", "( ").replace(")", " )");
    }
}
