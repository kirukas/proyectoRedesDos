package com.escom;

public class Main {

    public static void main(String[] args) throws ArchivoNoExiste {
        Archivo archivo = null;
        String texto = null;
        String ruta = "/home/enrique/Documentos/texto.txt";
        String rutaCopia = "/home/enrique/Documentos/";
        int numeroArchivos = 3;
        Archivo original = new Archivo(ruta);
        fragmentoArchivo fragmentar = new fragmentoArchivo(original,numeroArchivos,rutaCopia);
        fragmentar.framentar();
        try {
            original.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
