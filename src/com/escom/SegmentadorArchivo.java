package com.escom;

 class FragmentadorArchivo {
    private Archivo archivo;
    private String[] rutasArchivo;
    private int numerArchivos;
    private  String rutaCopia;
    private  int numArchivos;
    private  long rango;
    private  long sizeBytesArchivo;
    private  ConexionWorker maquina1 = new ConexionWorker("192.168.3.2",2121);

    public FragmentadorArchivo(Archivo archivo, int numArchivos, String rutaCopias){
        this.archivo = archivo;
        this.rutaCopia = rutaCopias;
        this.numArchivos = numArchivos;
        this.sizeBytesArchivo = archivo.getSize();
        this.rango = archivo.getSize()/numArchivos;
    }

    public void framentar(){
        System.out.println("tamaño del archivo: "+sizeBytesArchivo);
        Archivo[] copia = new Archivo[numArchivos];
        rutasArchivo = new String[3];
        long Acopiar = rango;
        int de = 0;
        for (int i = 0; i < numArchivos ; i++) {
            String rutaCopiaAux = rutaCopia+"copia"+i+".txt";
            System.out.println(rutaCopiaAux);
            try {
                copia[i] = new Archivo(rutaCopiaAux,"rw");
                rutasArchivo[i] = new String(rutaCopiaAux);
                if((Acopiar + rango) > sizeBytesArchivo){
                    Acopiar = (int)sizeBytesArchivo;
                }
                copia[i].escribir(archivo.getDatos(de,(int)Acopiar));
                maquina1.enviarDatos(archivo.getDatos(de,(int)Acopiar));
                //archivo.getDatos(0,100);
                System.out.println(" inferior:  "+de+"  hasta :"+Acopiar);
                de+=rango;
                Acopiar+=rango;

                try {
                    copia[i].close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (ArchivoNoExiste archivoNoExiste) {
                archivoNoExiste.printStackTrace();
            }
        }
    }

    public Archivo  arrejuntar(){
        Archivo original = null;
        try {
            original = new Archivo("ruta","rw");
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        }
      //  byte[] info = new byte[(int) sizeBytesArchivo];
        byte[] datos = new byte[0];
        Archivo[] copia = new Archivo[numArchivos];
        for (int i = 0; i < numArchivos; i++) {
            try {
                copia[i] = new Archivo(rutasArchivo[i]);
                //datos += copia[i].getDatos(0, (int) copia[i].getSize());
            } catch (ArchivoNoExiste archivoNoExiste) {
                archivoNoExiste.printStackTrace();
            }
            try {
                copia[i].close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return original;
    }

}
