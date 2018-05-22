package com.escom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.RandomAccess;

public class Archivo implements AutoCloseable {
    private String ruta;
    private String modo;
    private int sizebuff;
    private byte []buff;
    private RandomAccessFile in;

    private void ajusta() throws ArchivoNoExiste{
        try {
            if(in.length() > 0 || sizebuff < 0) {
                if (sizebuff > in.length() || sizebuff < 0) {
                    //sizebuff = (int) Files.size(new File(ruta).toPath());
                    sizebuff = (int)in.getChannel().size();
                    //System.err.println("tamaño especificado es mayor al del archivo, se leera solo el tamaño del archivo");
                }
            }
            buff = new byte[sizebuff];
        } catch (IOException e) {
            //e.printStackTrace();
            throw new ArchivoNoExiste("el archivo: " + ruta + " no existe");

        }
    }

    private void abreArchivo() throws ArchivoNoExiste{
        try {
            in = new RandomAccessFile(ruta,modo);

        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            throw new ArchivoNoExiste("el archivo: " + ruta + " no existe");
        }
    }

    public Archivo(String ruta, String modo,int sizebuff) throws ArchivoNoExiste{
        this.ruta = ruta;
        this.modo = modo;
        this.sizebuff = sizebuff;
        //in = new RandomAccessFile(ruta,modo);
        //existe = true;
        abreArchivo();
      //  ajusta();

    }

    public Archivo(String ruta, int sizebuff) throws ArchivoNoExiste{
        this(ruta,"r",sizebuff);
    }

    public Archivo(String ruta) throws ArchivoNoExiste{
        this(ruta,"r",-1);
    }

    public Archivo(String ruta, String modo ) throws ArchivoNoExiste{
        this(ruta,modo,-1);
    }
    public String getNombre(){ return  ruta.substring(ruta.lastIndexOf("/")+1);}


    public String getRuta(){
        return ruta;
    }

    public void setRuta(String ruta) throws ArchivoNoExiste{
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ruta = ruta;
        abreArchivo();
    }
    

    public String getModo() {
        return modo;
    }

    public void setModo(String modo){
        this.modo = modo;
    }

    public int getSizebuff(){
        return sizebuff;
    }

    public void setSizebuff(int sizebuff){
        this.sizebuff = sizebuff;
    }

    public String getDatos()throws ArchivoNoExiste{

            try {
                in.readFully(buff);
                in.seek(0);


            } catch (IOException e) {
                //e.printStackTrace();
                throw new ArchivoNoExiste("el archivo: " + ruta + " ya no existe");
            }
            return new String(buff);



    }

    public byte[] getDatos(int inicio, int fin) throws ArchivoNoExiste{
        byte []aux = new byte[fin-inicio];
        try {
            in.seek(inicio);
            in.read(aux);
            //System.out.println("inicio: " + inicio + ", fin: " + fin + ", tam: " + (fin-inicio) + new String(aux));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aux;
    }

    public long getSize(){
        long temp = -1;
        try {
            temp =  in.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    void escribir(byte[] info){
        try {
            in.write(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escribir(byte[] info,long pos){
        try {
            in.seek(pos);
            in.write(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void escribirFinal(byte[] info, boolean trampa){
        try {
            /*if(in.length() > 0){
                in.seek(in.length()-1);

            }else{
                in.write(info,0,info.length);
            }*/
            in.write(info,0,info.length);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void close() throws Exception {
        in.close();
    }
}
