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
        Operations operation = new Operations(args);
        if(args.length == 0) {
            operation.OrganizeInputptions();
        } else {
            operation.OrganizeCLIOptions();
        }
    }
}

