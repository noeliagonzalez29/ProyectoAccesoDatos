package Clases;

import java.io.Serializable;
import java.util.*;

/**
 * La clase Clases.Tienda representa una tienda que ofrece semillas agrupadas por estaciones.
 * Permite agregar semillas a la tienda y generar una lista de semillas disponibles para el día
 * en función de la estación actual.
 */
public class Tienda implements Serializable {

    private static final long serialVersionUID = 5970610966714932501L;
    /**
     * Mapa que relaciona cada estación con una lista de semillas disponibles en esa estación.
     */
    private Map<String, List<Semilla>> tiendaEstacion;
    /**
     * Lista de semillas disponibles para el día actual.
     */
    private List<Semilla> semillasDelDia;

    /**
     * Constructor que inicializa la tienda con estaciones vacías y una lista de semillas del día.
     * Se crean cuatro estaciones: PRIMAVERA, VERANO, OTOÑO e INVIERNO.
     */
    public Tienda() {
        this.tiendaEstacion = new HashMap<>();
        this.semillasDelDia = new ArrayList<>();
        tiendaEstacion.put("PRIMAVERA", new ArrayList<>());
        tiendaEstacion.put("VERANO", new ArrayList<>());
        tiendaEstacion.put("OTOÑO", new ArrayList<>());
        tiendaEstacion.put("INVIERNO", new ArrayList<>());
    }

    /**
     * Agrega una lista de semillas a la estación especificada.
     *
     * @param estacion la estación a la que se agregarán las semillas .
     * @param semillas la lista de semillas a agregar a la estación especificada.
     */
    public void agregarSemillasPorEstacion(String estacion, List<Semilla> semillas) {
        estacion = estacion.toUpperCase();
        if (tiendaEstacion.containsKey(estacion)) {
            tiendaEstacion.get(estacion).addAll(semillas);
        }
    }

    /**
     * Genera una lista de semillas del día en función de la estación actual.
     * Selecciona aleatoriamente hasta tres semillas únicas de la lista de semillas de la estación actual.
     *
     * @param estacionActual la estación actual.
     */
    public void generarSemillasDelDia(String estacionActual) {
        estacionActual = estacionActual.toUpperCase();
        // Limpiar las semillas del día anterior
        semillasDelDia.clear();
        // Obtener las semillas de la estación actual
        List<Semilla> semillasEstacion = tiendaEstacion.get(estacionActual);

        Random random = new Random();
        Set<Semilla> sUnicas = new HashSet<>();
        while (sUnicas.size() < 3 && semillasEstacion.size() > 0) {
            int index = random.nextInt(semillasEstacion.size());
            Semilla semilla = semillasEstacion.get(index);
            sUnicas.add(semilla);
        }
        semillasDelDia.addAll(sUnicas);
    }

    /**
     * Devuelve la lista de semillas disponibles para el día actual.
     *
     * @return la lista de semillas del día.
     */
    public List<Semilla> getSemillasDelDia() {
        return semillasDelDia;
    }
}

