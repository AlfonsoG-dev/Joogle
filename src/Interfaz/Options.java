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
                        System.out.println("use -f to search within a file");
                        System.out.println("\t you need to provide of a search sentence with 'int(boolean)'");
                        System.out.println("\t the search sentence can be empty just declare with ''");
                        System.out.println("use -d to search within a directory");
                        System.out.println("\t as with the previous one you need to provide the 'search(sentence)'");
                        System.out.println("use -D to search recursive within directories");
                        System.out.println("\t as with the previous one you need to provide the 'search(sentence)'");
                        System.out.println("use -lf to list files of the given directory");
                        System.out.println("use -lm to print the method context");
                        System.out.println("\t you need to provide the method name with 'methodName'");
                        System.out.println("use -lt to search for TODO'S in the files of the given directory");
                        break;
                }
                System.out.println(
                        Colores.GREEN_UNDERLINED + "use -q to quit or --h for help" + Colores.ANSI_RESET
                );
            }
        } catch(Exception e) {
        }
    }
}
