package com.escom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {
    private static  final  int puerto = 2121;

    public static void main(String[] args){
        boolean servidorActivo = true;
        String ruta = "/home/redes/";
        final int guardarDatos = 0;
        final int enviarDatos = 1;
        boolean isWoker = true;
        int tramaSize;
        ConexionWorker toMaster;
        try {

            ServerSocket worker = new ServerSocket(puerto);
            Socket conexion = worker.accept();
            InputStream flujoEntrada = conexion.getInputStream();
            while(servidorActivo){
                if((tramaSize = flujoEntrada.available()) > 0){
                    System.out.println("SE recibio mensaje");
                    byte[] tramaRaw = new byte[tramaSize];
                    flujoEntrada.read(tramaRaw,0,tramaSize);
                    Trama trama = new Trama(tramaRaw);
                    if(trama.getTipo() == guardarDatos){// guarda los datos de la trama
                        System.out.println("SE guardara un archivo de la direccion "+ String.valueOf(conexion.getInetAddress()));
                        Archivo file  = new Archivo(ruta+trama.getHashCode(),"rw");
                        file.escribir(trama.getArray());
                        file.close();
                        if(isWoker);
                    }else if(trama.getTipo() == enviarDatos){
                        toMaster = new ConexionWorker(String.valueOf(conexion.getInetAddress()),puerto);
                        System.out.println("SE enviaran Datos");
                        Archivo file = new Archivo(ruta+trama.getHashCode());
                        toMaster.enviarDatos(file.getDatos(0,(int)file.getSize()));

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
