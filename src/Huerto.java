import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Huerto {
    private static final int LONGITUD= 9;
    private String nombreArchivo;
    private int filas;
    private int columnas;

    public Huerto(String nombreArchivo, int filas, int columnas) {
        this.nombreArchivo = nombreArchivo;
        this.filas = filas;
        this.columnas = columnas;
        inicializarArchivo();
    }

    public void inicializarArchivo(){
        try {
            RandomAccessFile raf = new RandomAccessFile("resources/huerto.dat", "rw");
            for (int i= 0; i<=filas; i++){
                for (int j=0; i<=columnas; j++){
                    raf.writeInt(-1);
                    raf.writeBoolean(false);
                    raf.writeInt(-1);
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }


    }

    public void plantarColumna(int columna){
        if (columna<0 || columna>=columnas){
            System.out.println("esa columna no está");
        }
    }
    public void regar(){}
    public void cosechar(){}
}
