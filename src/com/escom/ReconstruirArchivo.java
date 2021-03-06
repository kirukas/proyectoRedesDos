package com.escom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.net.UnknownHostException;

public class ReconstruirArchivo {
    private  static final int puerto = 2121;
    final int guardarDatos = 0;
    private String ruta ="/home/enrique/Documentos/Redes2/ARCHIVOSRECONSTRUIDOS";// ruta donde se guardara el archivo
    private String[] espejo;// ips de los espejos
    private Archivo archivo;
    private Trama peticion;
    private  String[] rutasTemporales ;
    private String [] worker;
    private Archivo reconstruido = null;
    private  boolean [] respuestaWorker;

    public ReconstruirArchivo(Archivo a, String[] w, String[] e){
        worker = w;
        espejo = e;
        archivo = a;// archivo a recuperar
        ConstruirPeticion();
        try {
            reconstruido = new Archivo(ruta+"/"+a.getNombre(),"rw");
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        }
        respuestaWorker = new boolean[worker.length];
    }
   private void incializaDatos(){
       for (int i = 0; i < worker.length ; i++) {
           respuestaWorker[i] = false;
       }
   }
    private void ConstruirPeticion(){
        int tipoPetecion = 1; /// para obtener archivo;
        int numeroWorker = 10000;
        byte [] relleno = new byte[10];
        for (int i = 0; i < 10; i++) { relleno[i] = 0; }
        peticion = new Trama(tipoPetecion,archivo.getNombre(),numeroWorker,relleno);
    }

    public void guardaArchivosTemporal(Trama t) {
        String fileAux = ruta+"/"+t.getHashCode()+"_parte_"+t.getNumeroWorker();
        Archivo file  = null;
        try {
            file = new Archivo(fileAux,"rw");
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        }
        file.escribir(t.getArray());
       // rutasTemporales[t.getNumeroWorker()] = fileAux;//va guardando las rutas temporales
        try {
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Archivo  reconstruir(Archivo archivo) {
        Archivo reconstruido = null;
        try {
             reconstruido = new Archivo(ruta+archivo.getNombre(),"rw");
            for (int i = 0; i < worker.length ; i++) {

                Archivo auxiliar = new Archivo(rutasTemporales[i],"rw");
                reconstruido.escribir(auxiliar.getDatos(0,(int)auxiliar.getSize()));

                auxiliar.close();
            }
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reconstruido.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  reconstruido;

    }
    public void getRespaldoEspejo(int i){
        try {
            Socket conexionEspejo = new Socket(espejo[i],puerto);
            InputStream entradaEspejo = conexionEspejo.getInputStream();
            OutputStream salidaAEspejo = conexionEspejo.getOutputStream();
            salidaAEspejo.write(peticion.setByteArray());
            salidaAEspejo.flush();
            int t = 0;
            while(t < 1 ){
                if((t = entradaEspejo.available()) > 0){
                    System.out.println("longitud de la trama: "+t);
                    byte [] Array = new byte[t];
                    entradaEspejo.read(Array);
                    Trama trama = new Trama(Array);
                    if(trama.getTipo() == guardarDatos) {// guarda los datos de la trama
                        System.out.println("Se guardaran los datos en un archivo temporal");
                        if(i == 2){
                            reconstruido.escribir(trama.getArray());
                        }
                        else{
                            reconstruido.escribir(trama.getArray());
                        }
                    }
                }

            }

        }catch (UnknownHostException e){
            //System.out.println(e);
            System.out.println("Host no encontrado..." );
            System.out.print("Error al recuperar el archivo !!");
            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("Error de conexion con la maquina "+"\nRevisar estado de la maquina");
            e.printStackTrace();
            System.out.print("Error al recuperar el archivo !!");
            System.exit(0);
        }

    }
    public void getFragmento(int i){
        try {
            Socket conexion = new Socket(worker[i],puerto);
            InputStream flujoEntrada = conexion.getInputStream();
            OutputStream flujoSalida = conexion.getOutputStream();
            flujoSalida.write(peticion.setByteArray());
            flujoSalida.flush();
            int t = 0;
             while(t < 1){
                if((t = flujoEntrada.available()) > 0){
                    System.out.println("longitud de la trama: "+t);
                    byte [] Array = new byte[t];
                    flujoEntrada.read(Array);
                    Trama trama = new Trama(Array);
                    if(trama.getTipo() == guardarDatos) {// guarda los datos de la trama
                        System.out.println("Se guardaran los datos en un archivo temporal");
                            reconstruido.escribir(trama.getArray());


                    }
                }
             }

           flujoEntrada.close();
             conexion.close();

        }   catch (UnknownHostException e){
            //System.out.println(e);
            System.out.println("Host no encontrado..." );
        }
        catch (IOException e) {
            System.out.println("Error de conexion con la maquina "+"\nRevisar estado de la maquina");
            e.printStackTrace();
        }

    }

}
