package com.escom;

public class ArchivoNoExiste extends Exception{


    public ArchivoNoExiste(String msg){
        super(msg);

    }

    public static void ThrowArchivoNoExiste(String causa) throws ArchivoNoExiste{
        ArchivoNoExiste e = new ArchivoNoExiste(causa);
        throw e;
    }
}
