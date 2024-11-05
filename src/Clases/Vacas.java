package Clases;

import java.io.Serializable;
import java.util.List;

public class Vacas extends Animales implements Serializable {
    int peso;

    public Vacas() {
    }

    public Vacas(int id, Anim tipoAnimal, String nombre, int peso) {
        super(id, tipoAnimal, nombre,0);

        this.peso = peso;
    }



    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    @Override
    public int producir(Estacion estacion) {
        if (!isEstaAlimentado()) {
            return 0;
        }

        // Calcular 1% del peso
        int cantidadProducida = (int) (peso * 0.01);
        setEstaAlimentado(false);
        getP().setCantidad(getP().getCantidad() + cantidadProducida);

        return cantidadProducida;
    }

}
