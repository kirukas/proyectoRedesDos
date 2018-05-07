package com.escom;

import java.nio.ByteBuffer;

public class Trama {
    private final static  int sizeInt = Integer.BYTES;
    private final static int sizeCabecera = 2*sizeInt;
    private int tipoTrama;
    private int hashCode;
    private  String archivo;
    private byte[] Array;
    // tipo trama 0 -> gurada el archivo
    // tipo trama 1 -> pide el texto
    public Trama(int tipo, String nombreArchivo ){
        tipoTrama = tipo;
        hashCode = nombreArchivo.hashCode();
        archivo = nombreArchivo;
    }
    public Trama(int tipo, String nombreArchivo , byte[] array){
        tipoTrama = tipo;
        hashCode = nombreArchivo.hashCode();
        Array = array;
        archivo = nombreArchivo;
    }
    public Trama (byte[] tramaRaw){
        int tamDatos = tramaRaw.length-sizeCabecera;
        tipoTrama = ByteBuffer.wrap(tramaRaw,0,sizeInt).getInt();
        hashCode = ByteBuffer.wrap(tramaRaw,sizeInt,2*sizeInt).getInt();
        Array = new byte[tamDatos];
        System.arraycopy(tramaRaw,sizeCabecera,Array,0,tamDatos);
    }
    public int getTipo(){return  tipoTrama;}
    public String getNombreArchivo(){return  archivo;}
    public int getHashCode(){return hashCode;}
    public byte[] getArray() { return Array; }
    public void setArchivo(String archivo) { this.archivo = archivo; }
    public void setArray(byte[] array) { Array = array; }
    public void setHashCode(int hashCode) { this.hashCode = hashCode; }
    public void setTipoTrama(int tipoTrama) { this.tipoTrama = tipoTrama; }

    public byte[]setByteArray(){

        byte[] tramaByteArray = new byte[sizeCabecera+Array.length];
        ByteBuffer.wrap(tramaByteArray,0,sizeInt).putInt(tipoTrama);
        ByteBuffer.wrap(tramaByteArray,sizeInt,2*sizeInt).putInt(hashCode);
        System.arraycopy(Array,0,tramaByteArray,sizeCabecera,Array.length);
        return tramaByteArray;
    }


}