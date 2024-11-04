package Clases;

import java.io.Serializable;

public class Cerdos extends Animales implements Serializable {
    Estacion estacion;

    public Cerdos() {
    }



    public Cerdos(int id, Anim tipoAnimal, String nombre) {
        super(id, tipoAnimal, nombre,0);
        this.estacion = estacion;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }
    @Override
    public int producir() {
        if (!isEstaAlimentado()) {
            return 0;
        }

        int cantidadProducida = 0;
        switch (estacion) {
            case PRIMAVERA:
            case VERANO:
                cantidadProducida = (int) (Math.random() * 2) + 2; // Entre 2 y 3
                break;
            case OTOÑO:
                cantidadProducida = (int) (Math.random() * 2); // Entre 0 y 1
                break;
            case INVIERNO:
                cantidadProducida = 0;
                break;
        }

        if (cantidadProducida > 0) {
            setEstaAlimentado(false);
            getP().setCantidad(getP().getCantidad() + cantidadProducida);
        }

        return cantidadProducida;
    }
}
