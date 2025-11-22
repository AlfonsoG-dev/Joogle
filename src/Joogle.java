import mundo.Operations;
import interfaz.Options;
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
        Options mOptions = new Options();
        if(args.length == 0) {
            mOptions.organizeInputptions();
        } else {
            operation.organizeCLIOptions();
        }
    }
}

