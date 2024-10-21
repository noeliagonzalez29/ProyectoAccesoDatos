import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HuertoGestion implements Serializable {
    private static final String RUTA_HUERTO = "src/resources/huerto.dat";
    private RandomAccessFile raf;
    private int filas;
    private int columnas;
    private static final int LONGITUD_INT= 4;
    private  static  final int LONGITUD_BOOLEAN=1;
    private static final int LONGITUD= LONGITUD_INT + LONGITUD_BOOLEAN +LONGITUD_INT;
    private GestionProperties p= new GestionProperties();
    private Semilla s;
    private Granja g;
    private Almacen a;

    public HuertoGestion() {

        inicializarHuerto();
    }
    public HuertoGestion(GestionProperties p, Semilla s, Almacen a) {
        this.p = p;
        this.s = s;
        this.a = a;
        this.filas = Integer.parseInt(p.getProperty("filashuerto"));
        this.columnas = Integer.parseInt(p.getProperty("columnashuerto"));
    }

    public void inicializarHuerto(){
        String filasStr = p.getProperty("filashuerto");
        String columnasStr = p.getProperty("columnas");
        //NO ESTÁ LEYENDO BIEN LO QUE HAY EN EL ARCHIVO PROPERTY
        System.out.println("Valor de filashuerto: '" + filas + "'");
        System.out.println("Valor de columnas: '" + columnas+ "'");

        try {
            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);
            RandomAccessFile raf = new RandomAccessFile("resources/huerto.dat", "rw");
            for (int i= 0; i<filas; i++){
                for (int j=0; j<=columnas; j++){
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

    public void cuidarHuerto(){

        ArrayList<Semilla> semillas = s.leerSemillas();
        try {
            RandomAccessFile raf = new RandomAccessFile(RUTA_HUERTO, "rw");
            raf.seek(0);
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    long posicion = (i * columnas + j) * LONGITUD;
                    raf.seek(posicion);

                    int id = raf.readInt();
                    boolean regado = raf.readBoolean();
                    int diasPlantado = raf.readInt();

                    if (id != -1) {
                        // Hay una semilla plantada
                        if (!regado) {
                            // Regar la planta
                            raf.seek(posicion + LONGITUD_INT);
                            raf.writeBoolean(true);
                        }

                        // Incrementar los días plantados
                        diasPlantado++;
                        raf.seek(posicion + LONGITUD_BOOLEAN+ LONGITUD_INT);
                        raf.writeInt(diasPlantado);

                        // Buscar la semilla correspondiente
                        boolean encontrado = false;

                        while (!encontrado && i < semillas.size()) {
                            Semilla semilla = semillas.get(i);
                            if (semilla.getId().equals(String.valueOf(id))) {
                                encontrado = true;
                                if (diasPlantado >= semilla.getDiasCrecimiento()) {
                                    // Cosechar
                                    Map<String, Integer> nuevaCosecha = new HashMap<>();
                                    nuevaCosecha.put(semilla.getNombre(), semilla.getMaxFrutos());
                                    a.añadirCosecha(nuevaCosecha);

                                    // Reiniciar la celda
                                    raf.seek(posicion);
                                    raf.writeInt(-1);
                                    raf.writeBoolean(false);
                                    raf.writeInt(-1);
                                }
                            }
                            i++;
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public void actualizarHuertoNuevoDia() {

        try {
            RandomAccessFile raf = new RandomAccessFile(RUTA_HUERTO, "rw");

            raf.seek(0);
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    long posicion = raf.getFilePointer();
                    int id = raf.readInt();
                    boolean regado = raf.readBoolean();
                    int diasPlantado = raf.readInt();

                    if (id != -1) {
                        raf.seek(posicion + LONGITUD_INT);
                        raf.writeBoolean(false);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar el huerto para el nuevo día: " + e.getMessage());
        }

    }
    public void mostrarHuerto() {
        try {
            raf.seek(0);
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    int id = raf.readInt();
                    boolean regado = raf.readBoolean();
                    int diasPlantado = raf.readInt();


                    String idNuevo;
                    if (id == -1) {
                        idNuevo = "SS";
                    } else {
                        idNuevo = Integer.toString(id);
                    }

                    String regadoNuevo = Boolean.toString(regado);

                    int diasNuevo;
                    if (diasPlantado == -1) {
                        diasNuevo = 0;
                    } else {
                        diasNuevo = diasPlantado;
                    }

                    // Formatear la salida sin utilizar especificadores
                    System.out.print("[" + idNuevo + ", " + regadoNuevo + ", " + diasNuevo + "] ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al mostrar el huerto: " + e.getMessage());
        }
    }


    public boolean isColumnaVacia(int columna) {
        try {
            RandomAccessFile raf = new RandomAccessFile(RUTA_HUERTO, "r");
            raf.seek(0);

            for (int i = 0; i < filas; i++) {
                long posicion = (i * columnas + columna) * LONGITUD;
                raf.seek(posicion);
                int id = raf.readInt();

                // Si encuentro un id diferente de -1, la columna no está vacía
                if (id != -1) {
                    return false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al verificar si la columna está vacía: " + e.getMessage());
        }

        return true;
    }
    public void plantarSemillaColumna(int columna, int idSemilla) {
        if (!isColumnaVacia(columna)) {
            throw new IllegalArgumentException("La columna no está vacía");
        }
        try {
            for (int i = 0; i < filas; i++) {
                long posicion = (i * columnas + columna) * LONGITUD;
                raf.seek(posicion);
                raf.writeInt(idSemilla);
                raf.writeBoolean(false);
                raf.writeInt(0);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al plantar semilla en la columna: " + e.getMessage());
        }
    }

    public void crearFicheroHuerto() {
        File file = new File(RUTA_HUERTO);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error al crear el fichero del huerto: " + e.getMessage());
            }
        }
    }
    public void eliminarFicheroHuerto() {
        File file = new File(RUTA_HUERTO);
        if (file.exists()) {
            file.delete();
        }
    }
    public void cerrarConexion() {
        try {
            if (raf != null) {
                raf.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cerrar la conexión del huerto: " + e.getMessage());
        }
    }
}
