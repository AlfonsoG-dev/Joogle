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

    public void SearchInFileOption() throws IOException {
        System.out.print("[file]: ");
        String f = miBufferedReader.readLine();
        System.out.print("[sentence]: ");
        String s = miBufferedReader.readLine();
        busqueda.SearchInFile(f, s);
    }

    public void SearcInDirectoryOption() throws IOException {
        System.out.print("[directory]: ");
        String dir = miBufferedReader.readLine();
        System.out.print("[sentence]: ");
        String ds = miBufferedReader.readLine();
        busqueda.SearcInDirectory(dir, ds);
    }

    public void SearcInDirectoriesOption() throws IOException {
        System.out.print("[directory]: ");
        String dirD = miBufferedReader.readLine();
        System.out.print("[sentence]: ");
        String dsD = miBufferedReader.readLine();
        busqueda.SearcInDirectories(dirD, dsD);
    }

    public void SearchForFilesOptions() throws IOException {
        System.out.print("[directory]: ");
        String fd = miBufferedReader.readLine();
        busqueda.BuscarFiles(fd);
    }

    public void SearchForTodoOptions() throws IOException {
        System.out.print("[directory]: ");
        String d = miBufferedReader.readLine();
        busqueda.BuscarTODO(d);
    }

    public void SearchForMethodsOption() throws IOException {
        System.out.print("[file]: ");
        String fl = miBufferedReader.readLine();
        System.out.print("[sentece]: ");
        String sl = miBufferedReader.readLine();
        busqueda.BuscarMethods(fl, sl);
    }
}
