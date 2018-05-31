package com.escom;

class SegmentadorArchivo {
    private Archivo archivo;
    private  int numArchivos;
    private  long rango;
    private  long sizeBytesArchivo;
    private String[] espejo;
    public SegmentadorArchivo(Archivo archivo, int numArchivos, String [] e){
        this.archivo = archivo;
        this.numArchivos = numArchivos;
        this.sizeBytesArchivo = archivo.getSize();
        this.rango = archivo.getSize()/numArchivos;
       espejo = e;
    }
    // tipo trama 0 -> gurada el archivo
    // tipo trama 1 -> pide el texto
    public void enviar(ConexionWorker [] worker){
        System.out.println("Archivo a segmentar  "+archivo.getNombre() +"  tamaño del archivo: "+sizeBytesArchivo);
        Trama trama = new Trama(0,archivo.getNombre());
        long Acopiar = rango;
        int de = 0;
        for (int i = 0; i < numArchivos ; i++) {
            try {
                if((Acopiar + rango) > sizeBytesArchivo){
                    Acopiar = (int)sizeBytesArchivo;
                }
                trama.setNumeroWorker(i);
                trama.setArray(archivo.getDatos(de,(int)Acopiar));// los datos del archivo
                 //System.out.println(" inferior:  "+de+"  hasta :"+Acopiar);
                 int longitudPaquete = ((int)Acopiar - de) + trama.getSizeCabecera();
                 // trama.toStringTrama();
                trama.setLongitudPaquete(longitudPaquete);
                System.out.println("Enviando paquete de longitud "+longitudPaquete + "  bytes") ;
                 if(!(worker[i].enviarDatos(trama.getByteArray()))){// si la conexion no fue exitosa se envia a su espejo

                     ConexionWorker Espejo = new ConexionWorker(espejo[i],2121);
                    if(!(Espejo.enviarDatos(trama.getByteArray()))) System.out.println("Los datos no se pudieron guardar!!");
                }
                de+=rango;
                Acopiar+=rango;
            } catch (ArchivoNoExiste archivoNoExiste) {
                archivoNoExiste.printStackTrace();
            }
        }


    }



}
