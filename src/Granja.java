import java.io.Serializable;
import java.util.List;

public class Granja implements Serializable {
    private int diaActual;
    private Estacion estacion;
    private int presupuesto;
    private Tienda t;
    private HuertoGestion h;
    private Almacen a;
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

        public void iniciarNuevoDia () {
            Estacion estacionAnterior = estacion;

            // 1. Actualizar el contador de días
            diaActual++;
            diasEnEstacionActual++;

            // 2. Verificar si es necesario cambiar de estación
            int duracionEstacion = Integer.parseInt(propiedades.getProperty("duracionestacion"));
            if (diasEnEstacionActual > duracionEstacion) {
                cambiarEstacion();
                diasEnEstacionActual = 1; // Reiniciar el contador de días en la nueva estación
            }

            // 3. Actualizar cultivos o limpiar huerto
            if (estacion == estacionAnterior) {
                h.actualizarHuertoNuevoDia();
            } else {
                h.cuidarHuerto();  // Eliminar todos los cultivos si cambió la estación
            }

            // 4. Generar nuevas semillas para la tienda
            t.generarSemillasDelDia(estacion.toString());
        }


        public void cuidarHuerto () {
        }
        public void plantarCultivoColumna ( int columna){
        }
        public void venderFrutos () {
        }
        public void mostrarGranja() {
            System.out.println("Estado de la granja:");
            System.out.println("Día actual: " + diaActual);
            System.out.println("Estación actual: " + estacion);
            System.out.println("Presupuesto: " + presupuesto);
            h.mostrarHuerto();
        }
    }
}
