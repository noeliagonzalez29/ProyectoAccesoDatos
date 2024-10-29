package GestionFicheros;

import Clases.Semilla;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * La clase {@code GestionFicheros.HuertoGestion} se encarga de gestionar un huerto,
 * permitiendo realizar operaciones como plantar semillas, cuidar el huerto,
 * y mostrar el estado de las celdas en un archivo.
 * Esta clase utiliza un archivo de acceso aleatorio para almacenar
 * la información del huerto de manera persistente.
 */
public class HuertoGestion implements Serializable {
    private static final String RUTA_HUERTO = "src/resources/huerto.dat";
    private transient RandomAccessFile raf;
    private static final int LONGITUD_INT = 4;
    private static final int LONGITUD_BOOLEAN = 1;
    private static final int LONGITUD = LONGITUD_INT + LONGITUD_BOOLEAN + LONGITUD_INT;
    private GestionProperties p;
    private Semilla s;


    /**
     * Constructor de la clase {@code GestionFicheros.HuertoGestion}.
     * Inicializa las propiedades del huerto y crea un nuevo objeto {@code Clases.Semilla}.
     */
    public HuertoGestion() {
        this.p = GestionProperties.getInstancia();
        this.s = new Semilla();
        abrirConexion();
        inicializarHuerto();
    }

    /**
     * Abre una conexión con el archivo del huerto.
     *
     * @throws RuntimeException si ocurre un error al abrir el archivo.
     */
    public void abrirConexion() {
        try {
            if (raf == null) {
                raf = new RandomAccessFile(RUTA_HUERTO, "rw");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error al abrir el archivo: " + e.getMessage());
        }
    }

    /**
     * Inicializa los componentes del huerto, creando un archivo de datos con un número especificado de filas y columnas.
     *
     * @param filas    El número de filas del huerto.
     * @param columnas El número de columnas del huerto.
     */
    public static void iniciarComponentes(int filas, int columnas) {
        try {
            RandomAccessFile raf = new RandomAccessFile("src/resources/huerto.dat", "rw");
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    raf.writeInt(-1);
                    raf.writeBoolean(false);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inicializa el huerto creando las celdas en el archivo.
     * Lee el número de filas y columnas desde las propiedades y
     * establece cada celda con valores predeterminados.
     */
    public void inicializarHuerto() {

        String filasStr = p.getProperty("filashuerto");
        String columnasStr = p.getProperty("columnas");
        try {
            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);

            raf.seek(0);
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
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

    /**
     * Cuida el huerto, actualizando el estado de las celdas
     * y recolectando frutos cuando están listos.
     *
     * @return un mapa que asocia semillas con la cantidad cosechada.
     */
    public Map<Semilla, Integer> cuidarHuerto() {
        Map<Semilla, Integer> cosecha = new HashMap<>();
        String filasStr = p.getProperty("filashuerto");
        String columnasStr = p.getProperty("columnas");
        abrirConexion();
        try {
            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);
            s.leerSemillas();
            // Iterar por cada celda del huerto
            raf.seek(0);
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    long posicion = (i * columnas + j) * LONGITUD; // Calcula la posición en el archivo
                    raf.seek(posicion);

                    int id = raf.readInt();           // Lee el id de la semilla
                    boolean regado = raf.readBoolean(); // Lee si la celda ha sido regada
                    int diasPlantado = raf.readInt();   // Lee cuántos días lleva plantado

                    // Si hay una semilla plantada (id != -1)
                    if (id != -1) {
                        Semilla semillaActual = s.buscarSemillaPorId(id);
                        if (semillaActual != null) {

                            // Actualiza la celda para reflejar el nuevo estado
                            raf.seek(posicion);
                            raf.writeInt(id);
                            raf.writeBoolean(true); // Se marca como no regada para el siguiente ciclo

                            if (diasPlantado >= semillaActual.getDiasCrecimiento()) {
                                // La semilla está lista para cosechar
                                int maxFrutos = semillaActual.getMaxFrutos(); //aleatorio entre 1 maxFrutos
                                System.out.println("La semilla en (" + i + ", " + j +
                                        ") está lista para cosechar. Frutos obtenidos: " + maxFrutos);

                                // Almacenar los frutos
                                cosecha.put(semillaActual, cosecha.getOrDefault(semillaActual, 0) + maxFrutos);

                                // Limpiar la celda
                                raf.seek(posicion);
                                raf.writeInt(-1);
                                raf.writeBoolean(false);
                                raf.writeInt(-1);
                            }
                        }
                    }
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cosecha;

    }

    /**
     * Actualiza el estado del huerto para un nuevo día,
     * incrementando los días plantados y actualizando el estado de riego.
     */
    public void actualizarHuertoNuevoDia() {

        String filasStr = p.getProperty("filashuerto");
        String columnasStr = p.getProperty("columnas");
        abrirConexion();
        try {
            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);
            raf.seek(0);
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    long posicion = raf.getFilePointer();
                    int id = raf.readInt();
                    boolean regado = raf.readBoolean();
                    int diasPlantado = raf.readInt();

                    if (regado == true) {
                        diasPlantado++;
                        raf.seek(posicion + LONGITUD_INT);
                        raf.writeBoolean(false);
                        raf.writeInt(diasPlantado);
                    } else if ((id != -1) && (regado == false)) {
                        diasPlantado++;
                        raf.seek(posicion + LONGITUD_INT);
                        raf.writeBoolean(false);
                        raf.writeInt(diasPlantado);
                    }

                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar el huerto para el nuevo día: " + e.getMessage());
        }

    }

    /**
     * Muestra el estado actual del huerto en la consola,
     * imprimiendo los detalles de cada celda.
     */
    public void mostrarHuerto() {

        String filasStr = p.getProperty("filashuerto");
        String columnasStr = p.getProperty("columnas");
        abrirConexion();
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
                        diasNuevo = -1;
                    } else {
                        diasNuevo = diasPlantado;
                    }

                    System.out.print("[" + idNuevo + ", " + regadoNuevo + ", " + diasNuevo + "] ");

                }
                System.out.println();

            }
        } catch (IOException e) {
            throw new RuntimeException("Error al mostrar el huerto: " + e.getMessage());
        }

    }

    /**
     * Verifica si una columna del huerto está vacía.
     *
     * @param columna el índice de la columna a verificar.
     * @return {@code true} si la columna está vacía; {@code false} en caso contrario.
     */
    public boolean isColumnaVacia(int columna) {
        String filasStr = p.getProperty("filashuerto");
        String columnasStr = p.getProperty("columnas");
        abrirConexion();
        try {
            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);
            abrirConexion();

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
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Verifica si el índice de una columna está dentro del rango válido.
     *
     * @param columna el índice de la columna a verificar.
     * @return {@code true} si la columna está en rango; {@code false} en caso contrario.
     */
    public boolean isColumnaEnRango(int columna) {
        String columnasStr = p.getProperty("columnas");
        int columnas = Integer.parseInt(columnasStr);
        abrirConexion();
        if (columna < 0 || columna >= columnas) {
            System.out.println("Columna fuera de rango. Debe estar entre 0 y " + (columnas - 1));
            return false;
        }

        return true;
    }

    /**
     * Planta una semilla en una columna específica del huerto.
     *
     * @param columna   el índice de la columna donde se plantará la semilla.
     * @param idSemilla el ID de la semilla que se va a plantar.
     */
    public void plantarSemillaColumna(int columna, int idSemilla) {

        String filasStr = p.getProperty("filashuerto");
        String columnasStr = p.getProperty("columnas");
        abrirConexion();
        try {
            int filas = Integer.parseInt(filasStr);
            int columnas = Integer.parseInt(columnasStr);
            for (int i = 0; i < filas; i++) {
                long posicion = (i * columnas + columna) * LONGITUD;

                raf.seek(posicion);
                raf.writeInt(idSemilla);
                raf.writeBoolean(false);
                raf.writeInt(0);

            }

        } catch (IOException e) {
            System.out.println("Error al plantar semilla en la columna: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Crea el archivo del huerto si no existe.
     */
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

    /**
     * Cierra la conexión con el archivo del huerto.
     */
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
