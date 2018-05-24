package com.escom;

public class FragmentadorArchivos {

    public static void main(String[] args) throws ArchivoNoExiste {
        final int puerto = 2121;
        Archivo archivo = null;
        String texto = null;
        int numeroWorkers = 3;

        String[]  workers = new String[numeroWorkers];
        workers[0] = "192.168.30.2";
        workers[1] = "192.168.30.3";
        workers[2] = "192.168.30.4";
        String[] espejo = new String[numeroWorkers];
        espejo[0] = "192.168.31.2";
        espejo[1] = "192.168.31.3";
        espejo[2] = "192.168.31.4";
        String ruta = "/home/enrique/Documentos/texto.txt";
        ConexionWorker[] maquinaEspejo = new ConexionWorker[ numeroWorkers];
        ConexionWorker[] worker = new ConexionWorker[numeroWorkers];
        worker[0] = new ConexionWorker("192.168.30.2",puerto);
        worker[1] = new ConexionWorker("192.168.30.3",puerto);
        worker[2] = new ConexionWorker("192.168.30.4",puerto);
        ConexionWorker[] maquinaVirtual = new ConexionWorker[1];
        maquinaVirtual[0] = new ConexionWorker("192.168.1.128",2121);
        Archivo original = new Archivo(ruta);

       SegmentadorArchivo segmentador = new SegmentadorArchivo(original,numeroWorkers,espejo);
       segmentador.enviar( worker);
        try {
            original.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

   /* System.out.println("Reciviendo paqutes !!");
       ReconstruirArchivo reconstruir = new ReconstruirArchivo(original,workers,espejo);
        for (int i = 0; i < numeroWorkers ; i++) {
            reconstruir.getFragmento(i);
        }*/

    }
}

