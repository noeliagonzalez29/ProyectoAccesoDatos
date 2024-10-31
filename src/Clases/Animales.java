package Clases;

import java.io.Serializable;

public class Animales implements Serializable {
    int id;
    Anim tipoAnimal;
    String nombre;
    int dia_insercion;
    Productos p;
    Alimentos a;

    public Animales() {
    }

    public Animales(int id, Anim tipoAnimal, String nombre, int dia_insercion, Productos p, Alimentos a) {
        this.id = id;
        this.tipoAnimal = tipoAnimal;
        this.nombre = nombre;
        this.dia_insercion = dia_insercion;
        this.p = p;
        this.a = a;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Anim getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(Anim tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDia_insercion() {
        return dia_insercion;
    }

    public void setDia_insercion(int dia_insercion) {
        this.dia_insercion = dia_insercion;
    }

    public Productos getP() {
        return p;
    }

    public void setP(Productos p) {
        this.p = p;
    }

    public Alimentos getA() {
        return a;
    }

    public void setA(Alimentos a) {
        this.a = a;
    }
}
