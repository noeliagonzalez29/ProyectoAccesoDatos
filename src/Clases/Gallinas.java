package Clases;

import java.io.Serializable;

public class Gallinas extends Animales implements Serializable {

    public Gallinas() {
    }

    public Gallinas(int id, Anim tipoAnimal, String nombre) {
        super(id, tipoAnimal, nombre, 0);
    }

    /**
     * Metodo que sobreescribe de Animal y determina como produce las gallinas acorde a los dias que lleva en el juego
     */
    @Override
    public int producir(Estacion estacion, int diaJuego) {
        if (!isEstaAlimentado()) {
            return 0;
        }

        int cantidadProducida = 0;
        int diasVida = diaJuego - dia_insercion;
        if (diasVida > 40) {
            cantidadProducida = 1;
        } else if (diasVida > 3) {
            cantidadProducida = 2;
        } else if (diasVida < 3) {
            System.out.println(getNombre() + "No puede producir lleva menos de 3 días");
        }

        if (cantidadProducida > 0) {
            setEstaAlimentado(false); // Necesita volver a ser alimentada

            // Actualizar la cantidad de huevos en el producto
            getP().setCantidad(getP().getCantidad() + cantidadProducida);
        }

        return cantidadProducida;
    }


}
