package Clases;

import java.math.BigDecimal;

public class Productos {
    private int id;
    private String nombre;
    private double precio;
    private int cantidadProducida;

    public Tipo_elemento getTipo_elemento() {
        return tipo_elemento;
    }

    public void setTipo_elemento(Tipo_elemento tipo_elemento) {
        this.tipo_elemento = tipo_elemento;
    }

    private Tipo_elemento tipo_elemento;
    public Productos() {
    }

    public Productos( String nombre, double precio) {

        this.nombre = nombre;
        this.precio = precio;
        this.cantidadProducida =cantidadProducida;
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

    public int getCantidadProducida() {
        return cantidadProducida;
    }

    public void setCantidadProducida(int cantidadProducida) {
        this.cantidadProducida = cantidadProducida;
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
        return cantidadProducida;
    }

    public void setCantidad(int cantidad) {
        this.cantidadProducida = cantidad;
    }
}
