package Clases;

import java.io.Serializable;
import java.sql.Date;

public class Ovejas extends Animales implements Serializable {
    Date fechaEsquilado;

    public Ovejas() {
    }

    public Ovejas(int id, Anim tipoAnimal, String nombre, int dia_insercion, Productos p, Alimentos a, Date fechaEsquilado) {
        super(id, tipoAnimal, nombre, dia_insercion, p, a);
        this.fechaEsquilado = fechaEsquilado;
    }

    public Date getFechaEsquilado() {
        return fechaEsquilado;
    }

    public void setFechaEsquilado(Date fechaEsquilado) {
        this.fechaEsquilado = fechaEsquilado;
    }
}
