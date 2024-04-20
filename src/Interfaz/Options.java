package Interfaz;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Visual.Colores;
public class Options {
    public void organizeInputptions() {
        try {
            BufferedReader mio = new BufferedReader(new InputStreamReader(System.in));
            System.out.print(Colores.GREEN_UNDERLINED + "!press enter¡" + Colores.ANSI_RESET);
            while(mio.readLine().equals("-q") == false) {
                PanelOption miPanel = new PanelOption(mio);
                System.out.print("[ INFO ]: ");
                switch(mio.readLine()) {
                    case "-f":
                        miPanel.searchInFileOption();
                        break;
                    case "-d":
                        miPanel.searcInDirectoryOption();
                        break;
                    case "-D":
                        miPanel.searcInDirectoriesOption();
                        break;
                    case "-lf":
                        miPanel.searchForFilesOptions();
                        break;
                    case "-lt":
                        miPanel.searchForTodoOptions();
                        break;
                    case "-lm":
                        miPanel.searchForMethodsOption();
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
