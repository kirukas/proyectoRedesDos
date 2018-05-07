package com.escom;

public class FragmentadorArchivos {

    public static void main(String[] args) throws ArchivoNoExiste {
        final int puerto = 2121;
        Archivo archivo = null;
        String texto = null;
        int numeroWorkers = 3;
        String ruta = "/home/enrique/Documentos/texto.txt";
        ConexionWorker[] maquinaEspejo = new ConexionWorker[ numeroWorkers];
        ConexionWorker[] worker = new ConexionWorker[numeroWorkers];
        worker[0] = new ConexionWorker("192.168.30.2",puerto);
        worker[1] = new ConexionWorker("192.168.30.2",puerto);
        worker[2] = new ConexionWorker("192.168.30.2",puerto);
        ConexionWorker maquina1 = new ConexionWorker("192.168.3.2",2121);
        Archivo original = new Archivo(ruta);
        SegmentadorArchivo segmentador = new SegmentadorArchivo(original,numeroWorkers);
        segmentador.enviar( worker);

        try {
            original.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
