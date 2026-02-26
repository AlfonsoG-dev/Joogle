package interfaz;

import java.io.BufferedReader;
import java.io.IOException;


import mundo.Busqueda;
public class PanelOption {
    private static final String CONSOLE_FORMAT = "[%s]: ";
    private static final String DIR_STRING = "directory";
    private static final String INPUT_STRING = "sentence";
    private Busqueda busqueda;
    private BufferedReader miBufferedReader;
    public PanelOption(BufferedReader nBufferedReader) {
        busqueda = new Busqueda();
        miBufferedReader = nBufferedReader;
    }

    public void searchInFileOption() throws IOException {
        System.console().printf(CONSOLE_FORMAT, "file");
        String f = miBufferedReader.readLine();
        System.console().printf(CONSOLE_FORMAT, INPUT_STRING);
        String s = miBufferedReader.readLine();
        busqueda.searchInFile(f, s);
    }

    public void searcInDirectoryOption() throws IOException {
        System.console().printf(CONSOLE_FORMAT, DIR_STRING);
        String dir = miBufferedReader.readLine();
        System.console().printf(CONSOLE_FORMAT, INPUT_STRING);
        String ds = miBufferedReader.readLine();
        busqueda.searchInDirectory(dir, ds);
    }

    public void searcInDirectoriesOption() throws IOException {
        System.console().printf(CONSOLE_FORMAT, DIR_STRING);
        String dirD = miBufferedReader.readLine();
        System.console().printf(CONSOLE_FORMAT, INPUT_STRING);
        String dsD = miBufferedReader.readLine();
        busqueda.searchInDirectories(dirD, dsD);
    }

    public void searchForFilesOptions() throws IOException {
        System.console().printf(CONSOLE_FORMAT, DIR_STRING);
        String fd = miBufferedReader.readLine();
        busqueda.searchFiles(fd);
    }

    public void searchForTodoOptions() throws IOException {
        System.console().printf(CONSOLE_FORMAT, DIR_STRING);
        String d = miBufferedReader.readLine();
        busqueda.searchTODO(d);
    }

    public void searchForMethodsOption() throws IOException {
        System.console().printf(CONSOLE_FORMAT, "file");
        String fl = miBufferedReader.readLine();
        System.console().printf(CONSOLE_FORMAT, INPUT_STRING);
        String sl = miBufferedReader.readLine();
        busqueda.searchMethods(fl, sl);
    }
}
