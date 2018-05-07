package com.escom;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {
    private static  final  int puerto = 2121;

    public static void main(String[] args){
        boolean servidorActivo = true;
        String ruta = "/home/redes/";
        final int numeroEspejos = 3;
        final int guardarDatos = 0;
        final int enviarDatos = 1;
        boolean isWoker = true;
        int tramaSize;

        ConexionWorker toMaster;
        ConexionWorker[] espejo = new ConexionWorker[numeroEspejos];
        espejo[0] = new ConexionWorker("192.168.10.2",puerto);
        espejo[1] = new ConexionWorker("192.168.10.3",puerto);
        espejo[2] = new ConexionWorker("192.168.10.4",puerto);

        try {
            ServerSocket worker = new ServerSocket(puerto);
            Socket conexion = worker.accept();
            InputStream flujoEntrada = conexion.getInputStream();
            while(servidorActivo){
                System.out.println("Servidor activo...");
                if((tramaSize = flujoEntrada.available()) > 0){
                    System.out.println("SE recibio mensaje");
                    byte[] tramaRaw = new byte[tramaSize];
                    flujoEntrada.read(tramaRaw,0,tramaSize);
                    Trama trama = new Trama(tramaRaw);
                    if(trama.getTipo() == guardarDatos){// guarda los datos de la trama
                        System.out.println("La direccion "+ String.valueOf(conexion.getInetAddress())+" Mando datos");
                        Archivo file  = new Archivo(ruta+trama.getHashCode(),"rw");
                        file.escribir(trama.getArray());
                        file.close();
                        if(isWoker){// si es umn worker le manda el byte[] array a su espejo
                            espejo[trama.getNumeroWorker()].enviarDatos(tramaRaw);
                        }
                    }else if(trama.getTipo() == enviarDatos){

                        toMaster = new ConexionWorker("127.0.0.1",puerto);
                        System.out.println("Buscando Archivo");
                        Archivo file = new Archivo(ruta+trama.getHashCode());
                        if(!toMaster.enviarDatos(file.getDatos(0,(int)file.getSize()))){
                            System.out.println("la maquina "+String.valueOf(conexion.getLocalAddress())+"No pudo enviar los datos");
                        }
                        file.close();
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
