package GestionFicheros;

import Clases.Granja;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * La clase {@code GestionFicheros.GestionBinario} se encarga de la gestión de archivos binarios,
 * permitiendo guardar, cargar, verificar y eliminar archivos de partida para la clase {@code Clases.Granja}.
 */
public class GestionBinario {
    /**
     * Ruta del archivo binario donde se guarda la partida.
     */
    private final static String RUTA_FICHE_BIN = "src/resources/stardam_valley.bin";

    /**
     * Elimina el archivo binario de la partida si existe.
     *
     * @throws RuntimeException si ocurre un error durante la eliminación del archivo.
     */
    public void eliminarBinarioPartida() {
        if (Files.exists(Paths.get(RUTA_FICHE_BIN))) {
            try {
                Files.delete(Paths.get(RUTA_FICHE_BIN));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Verifica si el archivo binario de la partida existe.
     *
     * @return {@code true} si el archivo binario existe; {@code false} en caso contrario.
     */
    public boolean existeFichero() {
        return Files.exists(Paths.get(RUTA_FICHE_BIN));
    }

    /**
     * Carga la partida guardada desde el archivo binario.
     *
     * @return Una instancia de {@code Clases.Granja} con los datos de la partida cargados.
     * @throws RuntimeException si ocurre un error durante la carga o si el archivo no se encuentra.
     */
    public Granja cargarBinarioPartida() {
        Granja granja;
        try {
            InputStream ArchivoEntrada = Files.newInputStream(Paths.get(RUTA_FICHE_BIN));
            ObjectInputStream flujoEntrada = new ObjectInputStream(ArchivoEntrada);
            granja = (Granja) flujoEntrada.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Hay un error al cargar la partida");
        }
        return granja;
    }

    /**
     * Guarda el estado de la partida en un archivo binario.
     *
     * @param g La instancia de {@code Clases.Granja} que representa la partida a guardar.
     * @throws RuntimeException si ocurre un error durante el guardado del archivo.
     */
    public void guardarPartida(Granja g) {
        try {
            OutputStream archivoSalida = Files.newOutputStream(Paths.get(RUTA_FICHE_BIN));
            ObjectOutputStream flujoSalida = new ObjectOutputStream(archivoSalida);
            flujoSalida.writeObject(g);
            flujoSalida.close();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la partida en el archivo binario", e);
        }
    }
}
