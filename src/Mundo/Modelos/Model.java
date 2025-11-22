package mundo.modelos;
import utils.FileUtils;

/**
 * representa los methodos
 */
public final class Model {

    private static final FileUtils fileUtils = new FileUtils();
    private String sentence;
    private int lineNumber;
    public Model(String nMetodo, int nLineNumber) {
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
        StringBuilder build = new StringBuilder();
        String[] partition = fileSentence.split("\n");
        for(String p: partition) {
            String[] datos = p.split("\\(");
            for(int i=0; i<datos.length-1; ++i) {
                String[] separate = datos[i].split(" ");
                build.append(separate[separate.length-1].trim());
                build.append("\n");
            }
        }
        return build.toString();
    }
    /**
     * nombre del metodo
     */
    public String getMethodName() {
        return Model.getNameOfMethods(sentence);
    }
    /**
     * utilidad para hallar el tipo de retorno de las sentencias del texto
     */
    public static String getReturnType(String fileSentence) {
        String[] sentences = fileSentence.split("\n");
        StringBuilder types = new StringBuilder();
        for(String s: sentences) {
            String[] methods = s.split("\\(");
            String[] spaces  = methods[0].split(" ");
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
            types.append(spaces[0]);
            types.append("\n");
        }
        return types.toString();
    }
    /**
     * utilidad para separar los argumentos que tienen coma
     */
    private static String containsComa(String arguments) {
        StringBuilder args = new StringBuilder();
        String[] ars = arguments.split(",");
        args.append("(");
        for(int i=0; i<ars.length; ++i) {
            String type = ars[i].replace("(", "").replace(")", "").trim();
            String[] separate = type.split(" ");
            args.append(separate[0]);
            args.append(", ");
        }
        return args.substring(0, args.length()-2) + ")\n";
    }
    /**
     * utilidad para hallar los argumentos de las sentencias del texto
     */
    public static String getArguments(String fileSentence) {
        String[] sentences = fileSentence.split("\n");
        StringBuilder nombres = new StringBuilder();
        StringBuilder tipos   = new StringBuilder();
        for(String s: sentences) {
            String[] separate  = s.split("\\(");
            String[] partition = separate[1].split("\\)");
            String args = "";
            if(partition.length == 0) {
                args  = ")";
            } else {
                args = partition[0] + ")";
            }
            tipos.append("(");
            tipos.append(args.trim());
            tipos.append("\n");
        }
        String[] argumentos = tipos.toString().split("\n");
        for(String a: argumentos) {
            if(a.contains(",")) {
                nombres.append(Model.containsComa(a));
            } else {
                String[] separate = a.split(" ");
                String args = "";
                if(separate.length > 1) {
                    args += separate[0] + ")";
                } else {
                    args += separate[0];
                }
                nombres.append(args);
                nombres.append("\n");
            }
        }
        return nombres.toString().replace("(", "( ").replace(")", " )");
    }
}
