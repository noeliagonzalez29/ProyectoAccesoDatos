package Clases;

import java.io.Serializable;

public class Cerdos extends Animales implements Serializable {
    Estacion estacion;

    public Cerdos() {
    }

    public Cerdos(int id, Anim tipoAnimal, String nombre, int dia_insercion, Productos p, Alimentos a, Estacion estacion) {
        super(id, tipoAnimal, nombre, dia_insercion, p, a);
        this.estacion = estacion;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }
}
