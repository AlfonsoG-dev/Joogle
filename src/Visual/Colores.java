package Visual;

/**
 * colores utilizados para la mejorar visualmente los mensajes
 */
public record Colores() {
    /**
    * color rojo para los valores numéricos
    */
    public static final String ANSI_RED = "\u001B[41m";
    /**
    * color cyan para el path del archivo
    */
    public static final String ANSI_CYAN = "\u001B[46m";
    /**
    * quitar color para los resultados no exactos
    */
    public static final String ANSI_RESET = "\u001B[0m";
    /**
    * color amarillo para los resultados
    */
    public static final String ANSI_YELLOW = "\u001B[33m";
    /**
     * color con linea baja
     */
    public static final String GREEN_UNDERLINED = "\033[4;32m";
    /**
     * color para link de file
     */
    public static final String RED_UNDERLINED = "\033[4;31m";
}
