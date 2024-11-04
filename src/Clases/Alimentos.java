package Clases;

import GestionBasesDatos.BasesDatos;

import java.math.BigDecimal;

public class Alimentos {
    private int id;
    private String nombre;
    private BigDecimal precio;
    private int cantidad_disponible;

    public Alimentos() {

    }

    public Alimentos( String nombre, BigDecimal precio, int cantidad_disponible) {

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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad_disponible;
    }

    public void setCantidad(int cantidad) {
        this.cantidad_disponible = cantidad_disponible;
    }
}
