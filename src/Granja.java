import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Granja implements Serializable {
    private int diaActual;
    private Estacion estacion;
    private int presupuesto;
    private Tienda t;
    private HuertoGestion h;
    private Almacen a;
    Semilla semilla;
    private List<Semilla> semillasDisponibles;
    private GestionProperties propiedades;
    private int diasEnEstacionActual;

    public List getSemillasDisponibles() {
        return semillasDisponibles;
    }

    public void setSemillasDisponibles(List semillasDisponibles) {
        this.semillasDisponibles = semillasDisponibles;
    }

    public HuertoGestion getH() {
        return h;
    }

    public void setH(HuertoGestion h) {
        this.h = h;
    }

    public Almacen getA() {
        return a;
    }

    public void setA(Almacen a) {
        this.a = a;
    }

    public Granja(int diaActual, Estacion estacion, int presupuesto) {
        this.diaActual = diaActual;
        this.estacion = estacion;
        this.presupuesto = presupuesto;
    }

    public Granja() {
        this.h= new HuertoGestion();
        this.a= new Almacen();
        this.semilla= new Semilla();
        this.semillasDisponibles= new ArrayList<>();
        this.t= new Tienda();
        this.propiedades=new GestionProperties();
        this.diaActual=1;
        this.diasEnEstacionActual=1;

    }
    public void inicializarValores(boolean personalizado) {
        String[] valores;
        if (personalizado) {
            valores = propiedades.crearFicheroPropiedadesPersonalizado();
        } else {
            valores = propiedades.crearFichero();
        }

        // Inicializar valores desde el array
        this.presupuesto = Integer.parseInt(valores[2]);
        this.estacion = Estacion.valueOf(valores[3].toUpperCase());
        // Si necesitas inicializar el huerto con filas y columnas:
        this.h.inicializarHuerto();
    }

    public int getDiaActual() {
        return diaActual;
    }

    public void setDiaActual(int diaActual) {
        this.diaActual = diaActual;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Tienda getT() {
        return t;
    }

    public void setT(Tienda t) {
        this.t = t;
    }


    public void cambiarEstacion() {

        switch (estacion) {
            case PRIMAVERA:
                estacion = Estacion.VERANO;
                break;
            case VERANO:
                estacion = Estacion.OTOÑO;
                break;
            case OTOÑO:
                estacion = Estacion.INVIERNO;
                break;
            case INVIERNO:
                estacion = Estacion.PRIMAVERA;
                break;
        }


    }
    public void iniciarNuevoDia () {
        estacion= Estacion.valueOf(propiedades.getProperty("estacioninicio"));
        Estacion estacionAnterior = estacion;

        // 1. Actualizar el contador de días
        diaActual++;
        diasEnEstacionActual++;

        // 2. Verificar si es necesario cambiar de estación
        int duracionEstacion = Integer.parseInt(propiedades.getProperty("duracionestacion"));
        if (diasEnEstacionActual > duracionEstacion) {
            cambiarEstacion();
            diasEnEstacionActual = 0; // Reiniciar el contador de días en la nueva estación
        }

        // 3. Actualizar cultivos o limpiar huerto
        if (estacion == estacionAnterior) {
            h.actualizarHuertoNuevoDia();
        } else {
            h.inicializarHuerto();
            System.out.println("Dia" + diaActual + "ahora en estacion" + estacion);
            h.cuidarHuerto();  // Eliminar todos los cultivos si cambió la estación
        }

        // 4. Generar nuevas semillas para la tienda
        t.generarSemillasDelDia(String.valueOf(estacion));
        // Opcional: Mostrar información del nuevo día o estación
        System.out.println("Día " + diaActual + " en la estación " + estacion);
    }


    public void cuidarHuerto () {
        h.cuidarHuerto();
    }
    public void plantarCultivoColumna ( int columna){
        // Mostrar las semillas disponibles para plantar
        List<Semilla> semillasDisponibles = semilla.leerSemillas();
        System.out.println("Semillas disponibles para plantar:");
        for (Semilla semilla : semillasDisponibles) {
            System.out.println("ID: " + semilla.getId() + ", Nombre: " + semilla.getNombre() + ", Días de Crecimiento: " + semilla.getDiasCrecimiento());
        }

        // Pedir al usuario que seleccione una semilla
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID de la semilla que desea plantar en la columna " + columna + ":");
        int idSemilla = scanner.nextInt();

        // Intentar plantar la semilla en la columna

        h.plantarSemillaColumna(columna,idSemilla); // Método que deberías tener en Huerto




    }
    public void venderFrutos () {
        // Obtener los frutos del almacén
        Map<Semilla, Integer> frutosEnAlmacen = a.obtenerAlmacen(); // Método que debes implementar en Almacen
        double gananciasTotales = 0.0; // Inicializar ganancias totales

        System.out.println("Ventas de frutos:");

        for (Semilla semi : frutosEnAlmacen.keySet()) {

            int cantidad = frutosEnAlmacen.get(semi); // Cantidad de frutos
            double precioVenta = semilla.getPrecioVentaFruto();
            double totalVenta = precioVenta * cantidad;

            // Mostrar la información de la venta
            System.out.println("Se han vendido " + cantidad + " unidades de " + semilla.getNombre() + " por " + totalVenta + " euros.");

            gananciasTotales += totalVenta;



            // Limpiar los frutos vendidos del almacén
            a.eliminarFrutos(semi.getId()); // Método para eliminar los frutos vendidos

        }

        // Mostrar las ganancias totales
        System.out.println("Ganancias totales: " + gananciasTotales + " euros.");
    }
    public void mostrarGranja() {
        System.out.println("Estado de la granja:");
        System.out.println("Día actual: " + diaActual);
        System.out.println("Estación actual: " + estacion);
        System.out.println("Presupuesto: " + presupuesto);
        System.out.println("Dinero disponible: ");
        h.mostrarHuerto();
    }




}
