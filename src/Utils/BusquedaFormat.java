package Utils;

public final class BusquedaFormat {
    /**
    * color rojo para los valores numéricos
    */
    private final String ANSI_RED = "\u001B[41m";
    /**
    * color cyan para el path del archivo
    */
    private final String ANSI_CYAN = "\u001B[46m";
    /**
    * quitar color para los resultados no exactos
    */
    private final String ANSI_RESET = "\u001B[0m";
    /**
    * color amarillo para los resultados
    */
    private final String ANSI_YELLOW = "\u001B[33m";
    /**
     * color para link de file
     */
    public static final String RED_UNDERLINED = "\033[4;31m";
    /**
     * constructor
     */
    public BusquedaFormat() { }
    /**
    * da el formato de respuesta a la busqueda
    * @param lineNumber: linea en la que se encuentra la respuesta
    * @param filePath: ruta del archivo a leer
    * @param method_name: nombre del método a mostrar como respuesta
    * @param type: tipo de retorno del método
    * @param argument: argumentos del método
    */
    public void formatoBusquedaSentencia(int lineNumber, String filePath, String methodName, String type, String argument) {
        String build = "";
        if(lineNumber != -1) {
            build = "| " + ANSI_RED + lineNumber + ANSI_RESET + " | " + methodName + " :: " + type + " => " + argument + "\n";
        } else {
            build = "| " + "unknow" + " | " + methodName + " :: " + type + " => " + argument + "\n";
        }
        System.out.println(build);
    }
    /**
     * da el formato de respuesta a la busqueda de files
     * @param fileName: nombre del archivo a leer
     */
    public void formatoBusquedaFiles(String fileName) {
        String[] name = fileName.split("\\\\");
        name[name.length-1] = ANSI_CYAN + name[name.length-1] + ANSI_RESET;
        String build = "", cBuild = "";
        for(String n:name) {
            build += n +"\\";
        }
        cBuild = build.substring(0, build.length()-2);
        System.out.println(String.format(">- %s", ANSI_YELLOW + cBuild + ANSI_RESET + "\n"));
    }
    /**
     * da el formato de respuesta a la busqueda de métodos
     * @param fileName: nombre del archivo a leer
     * @para lineNumber: numéro de linea en el que se encuentra el método
     */
    public void formatoBusquedaMethod(String fileName, String methodName, int lineNumber) {
        String fLine = RED_UNDERLINED + fileName + ANSI_RESET  + ":" + ANSI_YELLOW + lineNumber + ANSI_RESET;
        System.out.println(String.format("%s >- %s", fLine, ANSI_YELLOW + methodName + ANSI_RESET + "\n"));
    }
    /**
    * da formato a la cantidad de respuestas encontradas
    * @param cantidad: cantidad de veces que se encuentra el valor
    * @param tipo: tipo de valor repetido
    */
    public void ConcurrencyFormat(int cantidad, String tipo) {
        System.out.println(String.format("%s : %s", tipo, ANSI_RED + cantidad + ANSI_RESET) + "\n");
    }
}
