import Mundo.Operations;
/**
 * clase main de ejecución
 */
class Joogle {
    /**
     * metodo ejecutor de la clase
     * @param args: argumentos de la consola
     */
    public static void main(String[] args) {
        Operations miOperation = new Operations(args);
        miOperation.OrganizeSearchOptions();
    }
}

