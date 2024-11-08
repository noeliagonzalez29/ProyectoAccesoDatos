package Clases;

import GestionBasesDatos.BasesDatos;

import java.io.Serializable;
import java.math.BigDecimal;

public class Alimentos implements Serializable {
    private int id;
    private String nombre;
    private double precio;
    private int cantidad_disponible;

    public Alimentos() {

    }

    public Alimentos( int id,String nombre,double precio, int cantidad_disponible) {
        this.id= id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad_disponible = cantidad_disponible;
    }
    public Alimentos(String nombre){
        this.nombre= nombre;

    }


 public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }



    public int getCantidad() {
        return cantidad_disponible;
    }

    public void setCantidad(int cantidad) {
        this.cantidad_disponible = cantidad_disponible;
    }
}
