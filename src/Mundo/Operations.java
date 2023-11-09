package Mundo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Visual.Colores;
import Utils.OperationUtils;

/**
 * clase para organizar las operaciones según las opciones dadas por el usuario
 */
public final class Operations {
    /**
    * CLI options
    */
    private String[] options;
    /**
     */
    private OperationUtils opUtils;
    /**
     * constructor
     */
    public Operations(String[] nOptions) {
        options = nOptions;
        opUtils = null;
    }
    /**
    * organiza la forma en la que se ejecutan los argumentos de la consola;
    *  -f es para buscar la sentencia dentro de un archivo 
    *  -d es para buscar la sentencia dentro de los archivos del directory designado,
    *  solo se tienen en cuenta los archivos no directorios
    *  -D es para buscar la sentencia dentro de los archivos del directory designado,
    *  si el directorio tiene más directorios se busca tambien dentro de ellos
    *  -lf listar los archivos que tienen como extensión .java
    *  -lm listar los métodos del archivo
    *  -lt listart todos los todos en el proyecto
    */
    public void OrganizeCLIOptions() {
        try {
            outter: for(int i=0; i<options.length; ++i) {
                opUtils = new OperationUtils(options, i);
                switch(options[i]) {
                    case "-f":
                        opUtils.SearchInFileOperation();
                        break;
                    case "-d":
                        opUtils.SearcInDirectoryOperation();
                        break;
                    case "-D":
                        opUtils.SearcInDirectoriesOperation();
                        break;
                    case "-lf":
                        opUtils.SearchForFilesOperation();
                        break;
                    case "-lm":
                        opUtils.SearchForMethodsOperation();
                        break;
                    case "-lt":
                        opUtils.SearchForTODOKeyOperation();
                        break;
                    case "--h":
                        opUtils.GetHelpOperation();
                        break;
                    default: 
                        System.out.println("utilize el comando --h para mas informacion");
                        break outter;
                }
            }
        } catch(Exception e) {
            System.err.println(Colores.RED_UNDERLINED + e.toString().toUpperCase() + Colores.ANSI_RESET);
        }
    }
    /**
     * panel de opciones cuando no se da una por CLI
     */
    public void OrganizeInputptions() {
        try {
            BufferedReader mio = new BufferedReader(new InputStreamReader(System.in));
            System.out.print(Colores.GREEN_UNDERLINED + "!press enter¡" + Colores.ANSI_RESET);
            while(mio.readLine().equals("-q") == false) {
                Busqueda mia = new Busqueda();
                System.out.print("[operation]: ");
                switch(mio.readLine()) {
                    case "-f":
                        System.out.print("[file]: ");
                        String f = mio.readLine();
                        System.out.print("[sentence]: ");
                        String s = mio.readLine();
                        mia.SearchInFile(f, s);
                        break;
                    case "-d":
                        System.out.print("[directory]: ");
                        String dir = mio.readLine();
                        System.out.print("[sentence]: ");
                        String ds = mio.readLine();
                        mia.SearcInDirectory(dir, ds);
                        break;
                    case "-D":
                        System.out.print("[directory]: ");
                        String dirD = mio.readLine();
                        System.out.print("[sentence]: ");
                        String dsD = mio.readLine();
                        mia.SearcInDirectory(dirD, dsD);
                        break;
                    case "-lf":
                        System.out.print("[directory]: ");
                        String fd = mio.readLine();
                        mia.BuscarFiles(fd);
                        break;
                    case "-lt":
                        System.out.print("[directory]: ");
                        String d = mio.readLine();
                        mia.BuscarTODO(d);
                        break;
                    case "-lm":
                        System.out.print("[file]: ");
                        String fl = mio.readLine();
                        System.out.print("[sentece]: ");
                        String sl = mio.readLine();
                        mia.BuscarMethods(fl, sl);
                        break;
                    case "--h":
                        System.out.println("Opciones para joogle");
                        System.out.println("-f para buscar dentro de un archivo:" + 
                                "\t seguido de /\"\"/ para buscar una sentencia");
                        System.out.println("-d para buscar dentro de un directorio:" + 
                                "\t seguido de /\"\"/ para buscar una sentencia");
                        System.out.println("-D para buscar dentro de todos los directorios:" + 
                                "\t seguido de /\"\"/ para buscar una sentencia");
                        System.out.println("-lf para listar todos los archivos .java:\t seguido del directorio");
                        System.out.println("-lm para listar todos los métodos del proyecto:\t seguido del directorio");
                        System.out.println("\t si seleccionas un archivo y adicionas el nombre" + 
                                " del metodo se retorna el bloque de codigo de ese metodo");
                        System.out.println("-lt para listar todos los TODO del proyecto:\t seguido del directorio");
                        break;
                }
                System.out.println(Colores.GREEN_UNDERLINED + "use -q to quit or --h for help" + Colores.ANSI_RESET);
            }
        } catch(Exception e) {
        }
    }
}
