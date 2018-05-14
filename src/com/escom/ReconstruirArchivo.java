package com.escom;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ReconstruirArchivo {
    private  static final int puerto = 2121;
    private String ruta ="/home/enrique/Documentos/Redes2/ARCHIVOSRECONSTRUIDOS";
    private String[] espejo;
    private ConexionWorker [] worker;
    private Archivo archivo;
    private Trama peticion;
    private  String[] rutasTemporales;
    private  boolean [] respuestaWorker;
    private ServerSocket master ;
    public ReconstruirArchivo(Archivo a, ConexionWorker[] w, String[] e){
        worker = w;
        espejo = e;
        archivo = a;
        ConstruirPeticion();
        rutasTemporales = new String[worker.length];
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
        rutasTemporales[t.getNumeroWorker()] = fileAux;//va guardando las rutas temporales
        try {
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void EnviarPeticionArchivoWorkers(){
        System.out.println("Enviando petcion  a los workers");

        for (int i = 0; i < worker.length; i++) {
            if(respuestaWorker[i] == false ){
                //peticion.setNumeroWorker(i);
                worker[i].enviarDatos(peticion.setByteArray());
            }
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

    public void getFragmentos(){
        int t = 0;

        //EnviarPeticionArchivoWorkers();
        try {
            Socket conexion = new Socket("192.168.30.2",puerto);
            InputStream flujoEntrada = conexion.getInputStream();
            OutputStream flujoSalida = conexion.getOutputStream();
            flujoSalida.write(peticion.setByteArray());
            flujoSalida.flush();
            System.out.println("Mensaje"+flujoEntrada.readAllBytes().toString());

            conexion.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

        /* EnviarPeticionArchivoWorkers();
        try {
            ServerSocket  servidor =  new ServerSocket(puerto);
            System.out.println("holas");
            Socket conexion =  servidor.accept();
            InputStream flujoEntrada = conexion.getInputStream();
            if(( t = flujoEntrada.available())> 0){
                System.out.println("llego mensaje ");
            }

        }
        catch (UnknownHostException e){
            //System.out.println(e);
            System.out.println("Host no encontrado..." );
        }
        catch (IOException e) {
            System.out.println("Error de conexion con la maquina "+"\nRevisar estado de la maquina");
            e.printStackTrace();
        }*/

    }

}
