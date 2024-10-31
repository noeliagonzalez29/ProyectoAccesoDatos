package Clases;

import java.io.Serializable;

public class Vacas extends Animales implements Serializable {
    int peso;

    public Vacas() {
    }

    public Vacas(int id, Anim tipoAnimal, String nombre, int dia_insercion, Productos p, Alimentos a, int peso) {
        super(id, tipoAnimal, nombre, dia_insercion, p, a);
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
