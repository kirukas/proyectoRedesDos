package com.escom;

public class ReconstruirArchivo {
    private String ruta ="/home/enrique/Documentos/Redes2/ARCHIVOSRECONSTRUIDOS";
    private String[] espejo;
    private Worker[] worker;
    private Archivo archivo;
    public ReconstruirArchivo(Archivo a, Worker[] w, String[] e){
        worker = w;
        espejo = e;
        archivo = a;
    }
    void EnviarPeticionArchivo(){
        int tipoPetecion = 1;
        byte [] Array = new byte[1];
        Array[0] = 0;
        Trama peticionArchivo = new Trama(tipoPetecion,archivo.getNombre());
        for (int i = 0; i < worker.length; i++) {
            peticionArchivo.setNumeroWorker(i);

        }
    }

    //public Archivo  reconstruir(Archivo archivo){ }


}
