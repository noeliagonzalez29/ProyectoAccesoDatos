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
        this.p = new GestionProperties();
        this.s = new Semilla();
        this.a = new Almacen();
        inicializarHuerto();
    }
    public HuertoGestion(GestionProperties p, Semilla s, Almacen a) {
        this.p = p;
        this.s = s;
        this.a =a;
        inicializarHuerto();
    }

    public void inicializarHuerto(){
        String filasStr = p.getProperty("filashuerto");
        String columnasStr = p.getProperty("columnas");


        try {
            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);
            RandomAccessFile raf = new RandomAccessFile(RUTA_HUERTO, "rw");
            for (int i= 0; i<filas; i++){
                for (int j=0; j<columnas; j++){
                    raf.writeInt(-1);
                    raf.writeBoolean(false);
                    raf.writeInt(-1);
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }


    }

    public void cuidarHuerto(){

        ArrayList<Semilla> semillas = s.leerSemillas();
        try {
            RandomAccessFile raf = new RandomAccessFile(RUTA_HUERTO, "rw");
            // Iterar por cada celda del huerto
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    long posicion = (i * columnas + j) * LONGITUD; // Calcula la posición en el archivo
                    raf.seek(posicion);

                    int id = raf.readInt();           // Lee el id de la semilla
                    boolean regado = raf.readBoolean(); // Lee si la celda ha sido regada
                    int diasPlantado = raf.readInt();   // Lee cuántos días lleva plantado

                    // Si hay una semilla plantada (id != -1)
                    if (id != -1) {
                        System.out.println("Cuidando semilla en posición (" + i + ", " + j + "), ID: " + id);

                        // Si la semilla ha sido regada
                        if (regado) {
                            diasPlantado++; // Incrementar los días plantado
                            System.out.println("Semilla ha sido regada. Incrementando días a: " + diasPlantado);

                            // Actualiza la celda para reflejar el nuevo estado
                            raf.seek(posicion);
                            raf.writeInt(id);
                            raf.writeBoolean(false); // Se marca como no regada para el siguiente ciclo
                            raf.writeInt(diasPlantado);
                        }

                        // Obtener información de la semilla desde la clase Semilla
                        Semilla semilla = s.buscarSemillaPorId(id); // Método que devuelve la semilla por su ID

                        // Verificar si la planta ha crecido lo suficiente para cosechar
                        if (diasPlantado >= semilla.getDiasCrecimiento()) {
                            // La semilla está lista para cosechar
                            int maxFrutos = semilla.getMaxFrutos(); // Número máximo de frutos
                            System.out.println("La semilla en (" + i + ", " + j + ") está lista para cosechar. Frutos obtenidos: " + maxFrutos);

                            // Almacenar los frutos en el almacén
                            a.guardarFrutos(semilla, maxFrutos);  // Método para guardar los frutos en el almacén

                            // Restablecer la celda a su estado original (vacío)
                            raf.seek(posicion);
                            raf.writeInt(-1);      // No hay semilla plantada
                            raf.writeBoolean(false); // No regado
                            raf.writeInt(-1);      // No hay días plantados
                        }
                    }
                }
            }
            cerrarConexion();

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
        String filasStr = p.getProperty("filashuerto");
        String columnasStr = p.getProperty("columnas");

        try {
            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);

            RandomAccessFile raf = new RandomAccessFile(RUTA_HUERTO, "rw");
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
            String filasStr = p.getProperty("filashuerto");
            String columnasStr = p.getProperty("columnas");

            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);
            RandomAccessFile raf = new RandomAccessFile(RUTA_HUERTO, "rw");
            for (int i = 0; i < filas; i++) {
                long posicion = (i * columnas + columna) * LONGITUD;
                raf.seek(posicion);
                int idActual= raf.readInt();

                if (idActual==-1) {
                    raf.seek(posicion);
                    raf.writeInt(idSemilla);
                    raf.writeBoolean(false);
                    raf.writeInt(0);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al plantar semilla en la columna: " + e.getMessage());
           e.printStackTrace();
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

    public void venderFrutos() {
        Map<Semilla, Integer> almacen = a.obtenerAlmacen(); // Obtenemos el almacén con id y cantidad
        int gananciasTotales = 0;

        for (Semilla semilla : almacen.keySet()) {
            int cantidad = almacen.get(semilla);  // Obtenemos la cantidad del fruto para esa semilla
            int precioVenta = semilla.getPrecioVentaFruto(); // Obtenemos el precio de venta del fruto
            int ganancias = precioVenta * cantidad;          // Calculamos las ganancias

            System.out.println("Se han vendido " + cantidad + " unidades de " + semilla.getNombre() + " por " + ganancias + " euros.");
            gananciasTotales += ganancias;
        }

        System.out.println("Ganancias totales: " + gananciasTotales + " euros.");
    }


}
