import java.io.Serializable;
import java.util.List;

public class Granja implements Serializable {
    private int diaActual;
    private Estacion estacion;
    private int presupuesto;
    private Tienda t;
    private HuertoGestion h;
    private Almacen a;
    private List semillasDisponibles;

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


    public void avanzarDia(){
        diaActual++;

        //llamo al get duracion del properties para pasar de estacion

    }
}
