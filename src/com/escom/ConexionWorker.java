package com.escom;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class ConexionWorker {
    private String IP;
    private int puerto;
    private Socket conexion ;

    public ConexionWorker(String IPDestino, int p) {
        IP = IPDestino;
        puerto = p;

    }

    public  String getIP(){return  IP;}
    public  int getPuerto(){return  puerto;}

     public  boolean  enviarDatos(byte[] array) {

         try {

             conexion = new Socket(IP,puerto);
             conexion.setSoTimeout(10);
             if(!conexion.isConnected()){
                 System.out.println("No esta conetado");
             }
             OutputStream fujoSalida = conexion.getOutputStream();
             fujoSalida.write(array);

             fujoSalida.flush();
             try {
                 System.out.println("Cerrando conexion..");
                 conexion.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
            return true;

         }
         catch (UnknownHostException e){
             //System.out.println(e);
             System.out.println("Host no encontrado..." );
             return  false;
         }
         catch (IOException e) {
             System.out.println("Error de conexion con la maquina "+IP +"\nRevisar estado de la maquina");
           //  e.printStackTrace();
             return  false;
         }
     }
}

// System.out.println("Error de conexion con la maquina "+IP +"\nRevisar estado de la maquina");