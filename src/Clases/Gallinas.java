package Clases;

import java.io.Serializable;

public class Gallinas extends Animales implements Serializable {
    int numDias;

    public Gallinas() {
    }

    public Gallinas(int id, Anim tipoAnimal, String nombre, int dia_insercion, Productos p, Alimentos a, int numDias) {
        super(id, tipoAnimal, nombre, dia_insercion, p, a);
        this.numDias = numDias;
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }
}
