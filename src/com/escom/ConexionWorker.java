package com.escom;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ConexionWorker {
    private String IP;
    private int puerto;
    public ConexionWorker(String IPDestino, int p) {
        IP = IPDestino;
        puerto = p;
    }

     public  void enviarDatos(byte[] array) {
         try {
             Socket conexion = new Socket(IP,puerto);
             OutputStream fujoSalida = conexion.getOutputStream();
             fujoSalida.write(array);
             fujoSalida.flush();
             try {
                 conexion.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         } catch (IOException e) {
             System.out.println("Hola !!!!");
             e.printStackTrace();
         }


     }

}
