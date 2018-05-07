package com.escom;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

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
             OutputStream fujoSalida = conexion.getOutputStream();
             fujoSalida.write(array);
             fujoSalida.flush();
             try {
                 conexion.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
            return true;
         } catch (IOException e) {
             System.out.println("Error de conexion con la maquina "+IP +"Revisar estado de la maquina");
             e.printStackTrace();
             return  false;
         }
     }
}
