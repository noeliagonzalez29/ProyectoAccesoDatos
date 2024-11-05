package Clases;

import java.io.Serializable;

public class Gallinas extends Animales implements Serializable {
    int numDias;

    public Gallinas() {
    }



    public Gallinas(int id, Anim tipoAnimal, String nombre) {
        super(id, tipoAnimal, nombre,0);
        this.numDias = numDias;
    }

    @Override
    public int producir(Estacion estacion) {
        if (!isEstaAlimentado()) {
            return 0;
        }

        int cantidadProducida = 0;
        if (numDias > 40) {
            cantidadProducida = 1;
        } else if (numDias > 3) {
            cantidadProducida = 2;
        }

        if (cantidadProducida > 0) {
            setEstaAlimentado(false); // Necesita volver a ser alimentada
            // Actualizar la cantidad de huevos en el producto
            getP().setCantidad(getP().getCantidad() + cantidadProducida);
        }

        return cantidadProducida;
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }
}
