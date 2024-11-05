package Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Animales implements Serializable {
    private int id;
    private Anim tipoAnimal;
    private String nombre;
    private int dia_insercion;
    private boolean estaAlimentado;
    private Alimentos alimento;
    private Productos producto;
    private int peso;
    private int diasEnJuego;
    public Animales() {
    }

    public Animales( int id, Anim tipoAnimal, String nombre, int dia_insercion) {
        this.id = id;
        this.tipoAnimal = tipoAnimal;
        this.nombre = nombre;
        this.peso= peso;
        this.dia_insercion = dia_insercion;
        this.estaAlimentado= false;

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
        return producto;
    }

    public void setP(Productos  p) {
        this.producto = p;
    }

    public Alimentos getA() {
        return alimento;
    }

    public void setA(Alimentos  a) {
        this.alimento = a;
    }

    public boolean isEstaAlimentado() {
        return estaAlimentado;
    }

    public void setEstaAlimentado(boolean estaAlimentado) {
        this.estaAlimentado = estaAlimentado;
    }


    public abstract int producir(Estacion estacion);
    public int calcularCantidadConsumida() {
        switch (tipoAnimal) {
            case VACA:
                if (diasEnJuego < 10) {
                    return 1;
                } else if (diasEnJuego < 40) {
                    return 3;
                } else {
                    return 2;
                }
            case CERDO:
            case OVEJA:
            case GALLINA:
                return 1;
            default:
                return 0;
        }
    }


    public void setAlimento(Alimentos alimento) {
    }

    public void setProducto(Productos producto) {
    }

    //metodo abstracto producir
}
