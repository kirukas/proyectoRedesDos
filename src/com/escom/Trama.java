package com.escom;

import java.nio.ByteBuffer;

public class Trama {
    private final static  int sizeInt = Integer.BYTES;
    private final static int sizeCabecera = 3*sizeInt;
    private int tipoTrama;
    private int hashCode;
    private  String archivo;
    private byte[] Array;
    int[] cabecera = new int[3];
    private int numeroWorker;
    // tipo trama 0 -> gurada el archivo
    // tipo trama 1 -> pide el texto
    public void inicalizaCabecera(){
        cabecera[0] = tipoTrama;
        cabecera[1] = hashCode;
        cabecera[2] = numeroWorker;
    }
    public Trama(int tipo, String nombreArchivo){
        tipoTrama = tipo;
        hashCode = nombreArchivo.hashCode();
        archivo = nombreArchivo;
    }
    // respuesta de la trama
    public Trama(int tipo ,byte[] array){
        tipoTrama = tipo;
        Array = array;
    }
    public Trama(int tipo, String nombreArchivo , byte[] array){
        tipoTrama = tipo;
        hashCode = nombreArchivo.hashCode();
        Array = array;
        archivo = nombreArchivo;
    }
    public Trama(int tipo, String nombreArchivo ,int numeroWorker, byte[] array){
        tipoTrama = tipo;
        hashCode = nombreArchivo.hashCode();
        Array = array;
        archivo = nombreArchivo;
        this.numeroWorker = numeroWorker;
    }
    public Trama (byte[] tramaRaw){
        System.out.println("Tamaño del byte array "+tramaRaw.length);
        int tamDatos = tramaRaw.length-sizeCabecera;
        tipoTrama = ByteBuffer.wrap(tramaRaw,0,sizeInt).getInt();
        hashCode = ByteBuffer.wrap(tramaRaw,sizeInt,2*sizeInt).getInt();
        numeroWorker = ByteBuffer.wrap(tramaRaw,2*sizeInt,3*sizeInt).getInt();
        Array = new byte[tamDatos];
        System.arraycopy(tramaRaw,sizeCabecera,Array,0,tamDatos-1);
    }
    public void setNumeroWorker(int nw){numeroWorker = nw;}
    public int getTipo(){return  tipoTrama;}
    public String getNombreArchivo(){return  archivo;}
    public int getHashCode(){return hashCode;}
    public byte[] getArray() { return Array; }
    public void setArchivo(String archivo) { this.archivo = archivo; }
    public void setArray(byte[] array) { Array = array; }
    public void setHashCode(int hashCode) { this.hashCode = hashCode; }
    public void setTipoTrama(int tipoTrama) { this.tipoTrama = tipoTrama; }
    public int getNumeroWorker(){return  numeroWorker;}

    private byte[] casToByteArray(int entero){
        byte [] bytes = new byte[sizeInt];
        ByteBuffer.wrap(bytes).putInt(entero);
        return  bytes;
    }
    public byte[]setByteArray(){
        byte[] tramaByteArray = new byte[sizeCabecera + Array.length];
        System.out.println("tamaño de bytes"+tramaByteArray.length);
        //Array[0] = '$';
        //Array[Array.length-1] = '$';
        //inicalizaCabecera();
        //System.out.println("tamaño de bytes"+tramaByteArray.length);
        /*for (int i = 0; i < 3; i++) {
            byte[] aux = casToByteArray(cabecera[i]);
            System.arraycopy(aux,0,tramaByteArray,i*sizeInt,aux.length);
            sdfkekfeḱrĺerĺelrṕlerl
        }*/
       ByteBuffer.wrap(tramaByteArray,0,sizeInt).putInt(tipoTrama);
       ByteBuffer.wrap(tramaByteArray,sizeInt,2*sizeInt).putInt(hashCode);
       ByteBuffer.wrap(tramaByteArray,2*sizeInt,3*sizeInt).putInt(numeroWorker);
        System.arraycopy(Array,0,tramaByteArray,sizeCabecera,Array.length);
        return tramaByteArray;
    }
    public void   toStringTrama() {
        System.out.println("tipo de trama: "+getTipo()+"\n hasCode:"+getHashCode()+"\n numero Worker " +getNumeroWorker());
        System.out.print("Datos");
        for (int i = 0; i < Array.length; i++) {
            System.out.print(" "+Array[i]);
        }
    }

}
/// RPC Y COMO HACER UN HOLA MUNDO CON RPC