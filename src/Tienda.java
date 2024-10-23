import java.io.Serializable;
import java.util.*;

public class Tienda implements Serializable {
    private Map<String, List<Semilla>> tiendaEstacion;
    private List<Semilla> semillasDelDia;

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
        while (semillasDelDia.size() < 3 && semillasEstacion.size() > 0) {
            int index = random.nextInt(semillasEstacion.size());
            Semilla semilla = semillasEstacion.get(index);
            if (!semillasDelDia.contains(semilla)) {
                semillasDelDia.add(semilla);
            }
        }
    }

    public int venderSemillas(int dineroDisponible) {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Semillas disponibles hoy:");
        for (int i = 0; i < semillasDelDia.size(); i++) {
            Semilla s = semillasDelDia.get(i);
            System.out.println((i + 1) + ". " + s.getNombre() + " - Precio: " + s.getPrecioCompraSemilla());
        }
        System.out.println("¿Qué semilla quieres comprar? (Ingresa el número)");
        int seleccion = entrada.nextInt();
        Semilla semillaSeleccionada = semillasDelDia.get(seleccion - 1);
        System.out.println("¿Cuántas semillas de " + semillaSeleccionada.getNombre() + " quieres comprar?");
        int cantidad = entrada.nextInt();
        int total = cantidad * semillaSeleccionada.getPrecioCompraSemilla();
        if (dineroDisponible >= total) {
            dineroDisponible -= total;
            System.out.println("Has comprado " + cantidad + " " + semillaSeleccionada.getNombre() +
                    " por un total de " + total + ". Te quedan " + dineroDisponible + " monedas.");
            // Agregar la lógica para añadir las semillas compradas al inventario del jugador
        } else {
            System.out.println("No tienes suficiente dinero. Necesitas " + total +
                    " monedas, pero solo tienes " + dineroDisponible + ".");
        }
        return dineroDisponible;
    }

    public List<Semilla> getSemillasDelDia() {
        return semillasDelDia;
    }
}

