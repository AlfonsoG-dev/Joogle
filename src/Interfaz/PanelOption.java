package Interfaz;

import java.io.BufferedReader;
import java.io.IOException;


import Mundo.Busqueda;
public class PanelOption {
    private Busqueda busqueda;
    private BufferedReader miBufferedReader;
    public PanelOption(BufferedReader nBufferedReader) {
        busqueda = new Busqueda();
        miBufferedReader = nBufferedReader;
    }

    public void searchInFileOption() throws IOException {
        System.out.print("[file]: ");
        String f = miBufferedReader.readLine();
        System.out.print("[sentence]: ");
        String s = miBufferedReader.readLine();
        busqueda.searchInFile(f, s);
    }

    public void searcInDirectoryOption() throws IOException {
        System.out.print("[directory]: ");
        String dir = miBufferedReader.readLine();
        System.out.print("[sentence]: ");
        String ds = miBufferedReader.readLine();
        busqueda.searcInDirectory(dir, ds);
    }

    public void searcInDirectoriesOption() throws IOException {
        System.out.print("[directory]: ");
        String dirD = miBufferedReader.readLine();
        System.out.print("[sentence]: ");
        String dsD = miBufferedReader.readLine();
        busqueda.searcInDirectories(dirD, dsD);
    }

    public void searchForFilesOptions() throws IOException {
        System.out.print("[directory]: ");
        String fd = miBufferedReader.readLine();
        busqueda.buscarFiles(fd);
    }

    public void searchForTodoOptions() throws IOException {
        System.out.print("[directory]: ");
        String d = miBufferedReader.readLine();
        busqueda.buscarTODO(d);
    }

    public void searchForMethodsOption() throws IOException {
        System.out.print("[file]: ");
        String fl = miBufferedReader.readLine();
        System.out.print("[sentece]: ");
        String sl = miBufferedReader.readLine();
        busqueda.buscarMethods(fl, sl);
    }
}
