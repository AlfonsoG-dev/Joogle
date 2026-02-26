package interfaz;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import visual.Colores;
public class Options {

    private static final String CONSOLE_FORMAT = "%s%n";

    public void organizeInputptions() {
        try {
            BufferedReader mio = new BufferedReader(new InputStreamReader(System.in));
            System.console().printf("%s%n", Colores.GREEN_UNDERLINED + "!press enter¡" + Colores.ANSI_RESET);
            while(!mio.readLine().equals("-q")) {
                PanelOption miPanel = new PanelOption(mio);
                System.console().printf("%s%n", "[ Info ]: ");
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
                        System.console().printf(CONSOLE_FORMAT, "use -f to search within a file");
                        System.console().printf(CONSOLE_FORMAT, "\t you need to provide of a search sentence with 'int(boolean)'");
                        System.console().printf(CONSOLE_FORMAT, "\t the search sentence can be empty just declare with ''");
                        System.console().printf(CONSOLE_FORMAT, "use -d to search within a directory");
                        System.console().printf(CONSOLE_FORMAT, "\t as with the previous one you need to provide the 'search(sentence)'");
                        System.console().printf(CONSOLE_FORMAT, "use -D to search recursive within directories");
                        System.console().printf(CONSOLE_FORMAT, "\t as with the previous one you need to provide the 'search(sentence)'");
                        System.console().printf(CONSOLE_FORMAT, "use -lf to list files of the given directory");
                        System.console().printf(CONSOLE_FORMAT, "use -lm to print the method context");
                        System.console().printf(CONSOLE_FORMAT, "\t you need to provide the method name with 'methodName'");
                        System.console().printf(CONSOLE_FORMAT, "use -lt to search for TODO'S in the files of the given directory");
                        break;
                    default:
                        break;
                }
                System.console().printf(CONSOLE_FORMAT,
                        Colores.GREEN_UNDERLINED + "use -q to quit or --h for help" + Colores.ANSI_RESET
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
