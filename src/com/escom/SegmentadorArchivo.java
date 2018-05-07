package com.escom;

class SegmentadorArchivo {
    private Archivo archivo;
    private  int numArchivos;
    private  long rango;
    private  long sizeBytesArchivo;
    public SegmentadorArchivo(Archivo archivo, int numArchivos){
        this.archivo = archivo;
        this.numArchivos = numArchivos;
        this.sizeBytesArchivo = archivo.getSize();
        this.rango = archivo.getSize()/numArchivos;
    }
    // tipo trama 0 -> gurada el archivo
    // tipo trama 1 -> pide el texto
    public void enviar(ConexionWorker [] worker){
        System.out.println("tamaño del archivo: "+sizeBytesArchivo);
        Trama trama = new Trama(0,archivo.getNombre());
        long Acopiar = rango;
        int de = 0;
        for (int i = 0; i < numArchivos ; i++) {
            try {
                if((Acopiar + rango) > sizeBytesArchivo){
                    Acopiar = (int)sizeBytesArchivo;
                }
                trama.setArray(archivo.getDatos(de,(int)Acopiar));// los datos del archivo
                //System.out.println(" inferior:  "+de+"  hasta :"+Acopiar);
                worker[i].enviarDatos(trama.setByteArray());
                de+=rango;
                Acopiar+=rango;
            } catch (ArchivoNoExiste archivoNoExiste) {
                archivoNoExiste.printStackTrace();
            }
        }


    }



}
