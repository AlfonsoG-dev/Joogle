package Utils;

/**
 * clase para manipular texto
 */
public class TextUtils {
    /**
     * metodos de manipulación del archivo
     */
    private FileUtils fileUtils;
    /**
     * constructor de la clase
     */
    public TextUtils() {
        fileUtils = new FileUtils();
    }
    /**
    * genera un String con las sentencias que indican un método
    * @param filePath: ruta del archivo a leer
    * @return String con las lineas en donde hay métodos
    */
    public String GetSentences(String filePath) {
        String[] fileLines = fileUtils.GetCleanTextFromFile(filePath).split("\n");
        String lines = "";
        for(String fl: fileLines) {
            String valores = fl.trim();
            for(String t: fileUtils.TokenList()) {
                if(valores.startsWith(t) && valores.contains(")")
                        || valores.endsWith("\n")) {
                    lines += valores.replace("{", "").trim() + "\n";
                }
            }
        }
        return lines;
    }
    /**
     * comparar letra por letra para distinguir la más parecida
     * @param first: primera letra a comparar
     * @param second: letra para comparar
     * @return el número de coincidencias entre las letras
     */
    public int CompareCharToChar(String first, String second) {
        int r = 0;
        String f = first.replace(" ", "").toLowerCase();
        String s = second.replace(" ", "").toLowerCase();
        try {
            for(int i=0; i<s.length(); ++i) {
                for(int j=s.length()-1; j>0; --j) {
                    if(f.charAt(i) == s.charAt(i) || f.charAt(j) == s.charAt(j)) {
                        ++r;
                    }
                    if(s.charAt(i) == f.charAt(i) || s.charAt(j) == f.charAt(j)) {
                        ++r;
                    }
                }
            }
        } catch(Exception e) {
            //
        }
        int resultado = r/s.length();
        return resultado;
    }
}
