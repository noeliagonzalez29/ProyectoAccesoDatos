import java.io.Serializable;
import java.util.*;

public class Tienda implements Serializable {
    private static final long serialVersionUID = 5970610966714932501L;
    private Map<String, List<Semilla>> tiendaEstacion;
    private  List<Semilla> semillasDelDia;


    public Tienda() {
        this.tiendaEstacion = new HashMap<>();
        this.semillasDelDia = new ArrayList<>();
        tiendaEstacion.put("PRIMAVERA", new ArrayList<>());
        tiendaEstacion.put("VERANO", new ArrayList<>());
        tiendaEstacion.put("OTOÑO", new ArrayList<>());
        tiendaEstacion.put("INVIERNO", new ArrayList<>());
    }
/*
public Map<String,List<Semilla>>generarNuevaTienda(List<Semilla>semillasDisponibles){
        Map<String, List<Semilla>> tiendaEstacion = new HashMap<>();

        tiendaEstacion.put("Primavera", new ArrayList<>());
        tiendaEstacion.put("Otoño", new ArrayList<>());
        tiendaEstacion.put("Verano", new ArrayList<>());
        tiendaEstacion.put("Invierno", new ArrayList<>());

        for (Semilla s: semillasDisponibles){
           if (tiendaEstacion.containsKey(s.getEstaciones())){
               tiendaEstacion.get(s.getEstaciones()).add(s);
           }else {
               System.out.println("no es ninguna");
           }

        }
        return tiendaEstacion;
    }
 */

    public void agregarSemillasPorEstacion(String estacion, List<Semilla> semillas) {
        estacion = estacion.toUpperCase();
        if (tiendaEstacion.containsKey(estacion)) {
            tiendaEstacion.get(estacion).addAll(semillas);
        }
    }
    public void generarSemillasDelDia(String estacionActual) {
        estacionActual=estacionActual.toUpperCase();
        // Limpiar las semillas del día anterior
        semillasDelDia.clear();
        // Obtener las semillas de la estación actual
        List<Semilla> semillasEstacion = tiendaEstacion.get(estacionActual);

        Random random = new Random();
        Set<Semilla>sUnicas= new HashSet<>();
        while (sUnicas.size() < 3 && semillasEstacion.size() > 0) {
            int index = random.nextInt(semillasEstacion.size());
            Semilla semilla = semillasEstacion.get(index);
            //if (!semillasDelDia.contains(semilla)) {
                sUnicas.add(semilla);
           // }
        }
        semillasDelDia.addAll(sUnicas);
    }


    public List<Semilla> getSemillasDelDia() {

        return semillasDelDia;
    }
}

