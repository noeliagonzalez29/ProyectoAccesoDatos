import java.util.ArrayList;
import java.util.List;

public enum Estacion {
    PRIMAVERA, VERANO, OTOÑO, INVIERNO;

    public static List<Estacion> estacionesSeparadas(String estaciones) {
        List<Estacion> listaEstaciones = new ArrayList<>();
        String[] estacionesArray = estaciones.split("-");
        for (String estacion : estacionesArray) {
            listaEstaciones.add(Estacion.valueOf(estacion.toUpperCase()));
        }
        return listaEstaciones;
    }
}
