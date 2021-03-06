package com.escom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Worker {
    private static  final  int puerto = 2121;

    public static void main(String[] args){
        boolean servidorActivo;
        String ruta = "/home/redes/";
        final int numeroEspejos = 3;
        final int guardarDatos = 0;
        final int enviarDatos = 1;
        String isWoker = "worker";
        Socket conexion;
        //boolean isWoker = true;
        int tramaSize;
        boolean aceptarConexion;
        ConexionWorker toMaster;
        ConexionWorker[] espejo = new ConexionWorker[numeroEspejos];
        espejo[0] = new ConexionWorker("192.168.31.2",puerto);
        espejo[1] = new ConexionWorker("192.168.31.3",puerto);
        espejo[2] = new ConexionWorker("192.168.31.4",puerto);
        System.out.println( args[0]+" activo...");
        try {
            ServerSocket worker = new ServerSocket(puerto);
            aceptarConexion = true;

            while(aceptarConexion){
                conexion = worker.accept();
                InputStream flujoEntrada = conexion.getInputStream();// canal comunicasion
                OutputStream flujoSalida = conexion.getOutputStream();// canal de comunicasion
                servidorActivo = true;
                while(servidorActivo){
                    if((tramaSize = flujoEntrada.available()) > 0){
                        System.out.println("Se recibio una trama ..");
                        byte[] tramaRaw = new byte[tramaSize];
                        flujoEntrada.read(tramaRaw,0,tramaSize);
                        Trama trama = new Trama(tramaRaw);
                        System.out.println("Numero de worker "+trama.getNumeroWorker());
                        if(trama.getTipo() == guardarDatos){// guarda los datos de la trama
                            System.out.println("\tLa Maquina "+ String.valueOf(conexion.getInetAddress())+" Mando datos");
                            Archivo file  = new Archivo(ruta+trama.getHashCode(),"rw");
                           // System.out.println("Se reeenvian los datos!");
                            //flujoSalida.write(tramaRaw);
                            //flujoSalida.flush();
                            file.escribir(trama.getArray());
                            file.close();
                            if(isWoker.contains(args[0])){// si es umn worker le manda el byte[] array a su espejo
                                System.out.println("\tEl worker respalda datos en su espejo");
                                System.out.println("el worker "+trama.getNumeroWorker()+"Le manda a la ip "+espejo[trama.getNumeroWorker()].getIP());
                                espejo[trama.getNumeroWorker()].enviarDatos(tramaRaw);
                            }
                        }else if(trama.getTipo() == enviarDatos){
                            System.out.println("\tPeticion de un Archivo ");
                        //toMaster = new ConexionWorker("10.42.0.64",puerto);
                        System.out.println("Buscando Archivo...");
                        Archivo file = new Archivo(ruta+trama.getHashCode());
                        System.out.println("Armando la trama ..");
                        Trama respuesta = new Trama(guardarDatos,file.getDatos(0,(int)file.getSize()));
                        respuesta.setHashCode(trama.getHashCode());
                        System.out.println("Envindo la trama  ..");
                        flujoSalida.write(respuesta.setByteArray());
                        file.close();
                        flujoSalida.flush();// se manda el flujo de salida
                        System.out.println("\tse enviaron los datos con exito!");

                        }
                        servidorActivo  = false;
                    }
                }
                // se cierra el canal de comunicacion
                flujoEntrada.close();
                flujoSalida.close();
                System.out.println("Cerrando la conexion del servidor !");
                // se cierra el socket
                conexion.close();
            }
        }catch (UnknownHostException e){
            //System.out.println(e);
            System.out.println("Host no encontrado..." );

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error de conexion con la maquina "+"10.42.0.64" +"\nRevisar estado de la maquina");
            e.printStackTrace();
        }
    }
}
