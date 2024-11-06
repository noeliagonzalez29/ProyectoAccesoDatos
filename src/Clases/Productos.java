package Clases;

import java.math.BigDecimal;

public class Productos {
    private int id;
    private String nombre;
    private double precio;
    private int cantidad;
    private Tipo_elemento tipo_elemento;

    public Tipo_elemento getTipo_elemento() {
        return tipo_elemento;
    }

    public void setTipo_elemento(Tipo_elemento tipo_elemento) {
        this.tipo_elemento = tipo_elemento;
    }

    public Productos() {
    }

    public Productos(int id, String nombre, double precio, int cantidad) {
        this.id= id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad= cantidad;

    }
    public Productos(String nombre){
        this.nombre=nombre;
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

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
