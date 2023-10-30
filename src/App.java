/**
 * clase main de ejecución
 */
class App {
    /**
     * metodo ejecutor de la clase
     * @param args: argumentos de la consola
     */
    public static void main(String[] args) {
        Busqueda miBusqueda = new Busqueda(args);
        miBusqueda.GetFilesFromPath();
    }
}

