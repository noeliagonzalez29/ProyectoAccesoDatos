package Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * El enum {@code Clases.Estacion} representa las cuatro estaciones del año:
 * PRIMAVERA, VERANO, OTOÑO e INVIERNO.
 * Ofrece métodos para convertir una cadena de estaciones en una lista de valores del enum.
 */
public enum Estacion implements Serializable {
    PRIMAVERA, VERANO, OTOÑO, INVIERNO;

    /**
     * Convierte una cadena de texto con nombres de estaciones separados por guiones
     * en una lista de objetos {@code Clases.Estacion}.
     *
     * @param estaciones Una cadena de texto con los nombres de las estaciones separados por guiones,
     *                   por ejemplo, "primavera-verano".
     * @return Una lista de objetos {@code Clases.Estacion} que corresponde a las estaciones indicadas en la cadena.
     */
    public static List<Estacion> estacionesSeparadas(String estaciones) {
        List<Estacion> listaEstaciones = new ArrayList<>();
        String[] estacionesArray = estaciones.split("-");
        for (String estacion : estacionesArray) {
            listaEstaciones.add(Estacion.valueOf(estacion.toUpperCase()));
        }
        return listaEstaciones;
    }
}
