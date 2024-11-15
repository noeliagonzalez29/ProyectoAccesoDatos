package Clases;

import java.io.Serializable;
import java.sql.Date;

public class Ovejas extends Animales implements Serializable {
    Date fechaEsquilado;

    public Ovejas() {
    }

    public Ovejas(int id, Anim tipoAnimal, String nombre) {
        super(id, tipoAnimal, nombre, 0);
        this.fechaEsquilado = fechaEsquilado;
    }

    public Date getFechaEsquilado() {
        return fechaEsquilado;
    }

    public void setFechaEsquilado(Date fechaEsquilado) {
        this.fechaEsquilado = fechaEsquilado;
    }

    /**
     * Metodo que sobreescribe de Animal y determina como produce las ovejas acorde a la fecha de esquilado (dias reales)
     */
    @Override
    public int producir(Estacion estacion, int diaJuego) {
        if (!isEstaAlimentado()) {
            return 0;
        }

        // Verificar si han pasado 2 días desde el último esquilado
        Date fechaActual = new Date(System.currentTimeMillis());
        if (fechaEsquilado != null) {
            long diferenciaDias = (fechaActual.getTime() - fechaEsquilado.getTime()) / (24 * 60 * 60 * 1000);
            if (diferenciaDias < 2) {
                System.out.println(getNombre() + "No puede producir lana no han pasado 2 días");
            }
        }

        // Producir lana
        int cantidadProducida = 5;
        setEstaAlimentado(false);
        setFechaEsquilado(fechaActual);
        getP().setCantidad(getP().getCantidad() + cantidadProducida);

        return cantidadProducida;
    }


}
