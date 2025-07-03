package Visual;

import java.io.File;

/**
 * clase para dar formato al resultado de la busqueda
 */
public record BusquedaFormat() {
    /**
     */
    public String setColorSentence(String sentence, String color) {
        return String.format("%s", color  + sentence + Colores.ANSI_RESET);
    }
    /**
     * da formato de muestra a la busqueda del contexto de un metodo
     */
    public void formatoPresentFilename(String fileName, int lineNumber) {
        String build = "";
        if(lineNumber > -1) {
            build =
                Colores.YELLOW_UNDERLINED +
                fileName +
                Colores.ANSI_RESET + ":" +
                Colores.RED_UNDERLINED +
                lineNumber + Colores.ANSI_RESET + "\n";
        }
        System.out.println(build);
    }
    /**
    * da el formato de respuesta a la busqueda
    * @param lineNumber: linea en la que se encuentra la respuesta
    * @param filePath: ruta del archivo a leer
    * @param method_name: nombre del método a mostrar como respuesta
    * @param type: tipo de retorno del método
    * @param argument: argumentos del método
    */
    public void formatoBusquedaSentencia(int lineNumber, String filePath, String methodName, String type,
            String argument) {
        String build = "";
        if(lineNumber != -1) {
            build =
                "| " + Colores.RED_UNDERLINED +
                filePath + Colores.ANSI_RESET +
                ":" + Colores.YELLOW_UNDERLINED +
                lineNumber + Colores.ANSI_RESET + " | " + methodName +
                " :: " + type + " => " + argument + "\n";
        } else {
            build = "| " + "unknow" + " | " + methodName + " :: " + type + " => " + argument + "\n";
        }
        System.out.println(build);
    }
    /**
     * da el formato de respuesta a la busqueda de files
     * @param fileName: nombre del archivo a leer
     */
    public synchronized void formatoBusquedaFiles(String fileName) {
        File miFile = new File(fileName);
        String name = Colores.RED_UNDERLINED + miFile.getName() + Colores.ANSI_RESET;
        String build = "";
        build = miFile.getPath().replace(miFile.getName(), name);
        System.out.println(
                String.format(
                    ">- %s",
                    Colores.ANSI_YELLOW +
                    build +
                    Colores.ANSI_RESET + "\n"
                )
        );
    }
    /**
     * da el formato de respuesta a la busqueda de métodos
     * @param fileName: nombre del archivo a leer
     * @para lineNumber: numéro de linea en el que se encuentra el método
     */
    public synchronized void formatoBusquedaMethod(String fileName, String methodName, int lineNumber) {
        String fLine = 
            Colores.RED_UNDERLINED +
            fileName +
            Colores.ANSI_RESET  +
            ":" +
            Colores.ANSI_YELLOW +
            lineNumber +
            Colores.ANSI_RESET;
        System.out.println(
                String.format(
                        "%s >- %s", fLine, Colores.ANSI_YELLOW +
                        methodName + Colores.ANSI_RESET + "\n"
                )
        );
    }
    /**
    * da formato a la cantidad de respuestas encontradas
    * @param cantidad: cantidad de veces que se encuentra el valor
    * @param tipo: tipo de valor repetido
    */
    public void concurrencyFormat(int cantidad, String tipo) {
        System.out.println(
            String.format(
                "%s : %s",
                tipo,
                Colores.ANSI_RED + cantidad + Colores.ANSI_RESET
            )
        );
    }
}
