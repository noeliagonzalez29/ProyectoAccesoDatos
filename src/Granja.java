import java.util.ArrayList;
import java.util.HashMap;

public class Granja {
    private int diaActual;
    private Estacion estacion;
    private int presupuesto;
    private Tienda t;
    private HashMap<String,Integer>almacen;


    public Granja() {
        this.diaActual = 1;
        this.estacion= Estacion.PRIMAVERA;
        this.presupuesto=1000;
        this.almacen= new HashMap<>();
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

    public HashMap<String, Integer> getAlmacen() {
        return almacen;
    }

    public void setAlmacen(HashMap<String, Integer> almacen) {
        this.almacen = almacen;
    }

    public void avanzarDia(){
        diaActual++;
    }
}
