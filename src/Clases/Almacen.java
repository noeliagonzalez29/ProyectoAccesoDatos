package Clases;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * La clase {@code Clases.Almacen} representa un almacén que gestiona los frutos cosechados
 * y permite la venta de estos frutos, evitando que un fruto se venda más de una vez.
 */
public class Almacen implements Serializable {

    /**
     * Mapa que almacena los frutos disponibles y sus cantidades.
     */
    private Map<Semilla, Integer> frutos;
    /**
     * Mapa que registra las ventas acumuladas de cada fruto para evitar múltiples ventas.
     */
    private Map<Semilla, Integer> ventasAcumuladas;

    /**
     * Crea una nueva instancia de {@code Clases.Almacen} con colecciones vacías para almacenar
     * frutos y controlar ventas acumuladas.
     */
    public Almacen() {
        this.frutos = new HashMap<>();
        this.ventasAcumuladas = new HashMap<>();
    }

    /**
     * Añade una nueva cosecha de frutos al almacén.
     * Si la semilla ya existe, su cantidad se incrementa.
     *
     * @param nuevaCosecha Un mapa con las nuevas semillas y sus cantidades a añadir.
     * @return Un mapa con la nueva cosecha añadida al almacén.
     */
    public Map<Semilla, Integer> aniadirCosecha(Map<Semilla, Integer> nuevaCosecha) {
        for (Semilla semilla : nuevaCosecha.keySet()) {
            Integer cantidad = nuevaCosecha.get(semilla);
            frutos.put(semilla, frutos.getOrDefault(semilla, 0) + cantidad);
        }
        return nuevaCosecha;
    }

    /**
     * Muestra el contenido actual del almacén, incluyendo cada fruto y su cantidad.
     * Indica si el almacén está vacío.
     */
    public void mostrarAlmacen() {
        boolean estaVacio = false;
        System.out.println("----------- PRODUCTOS ALMACÉN ----------");
        for (Map.Entry<Semilla, Integer> entry : frutos.entrySet()) {
            Semilla semilla = entry.getKey();
            int cantidad = entry.getValue();
            if (cantidad >= 0) {
                System.out.println("[" + semilla.getNombre() + " - " + cantidad + " ]");
                estaVacio = true;
            } else {
                System.out.println("NO hay productos en el almacén ya han sido vendidos");
                estaVacio = false;
            }
        }
        if (!estaVacio) {
            System.out.println("No hay productos aun en el almacén");
        }
    }

    /**
     * Vende los frutos disponibles en el almacén, calculando el total de ganancias.
     * Controla que los frutos no sean vendidos nuevamente si ya fueron vendidos antes.
     *
     * @return El total de ganancias obtenidas por la venta de los frutos.
     */
    public int venderFrutos() {
        int gananciasTotal = 0;
        for (Map.Entry<Semilla, Integer> entry : frutos.entrySet()) {
            Semilla semilla = entry.getKey();
            int cantidadF = entry.getValue();
            //ya he vendido
            int yaVendido = ventasAcumuladas.getOrDefault(semilla, 0);
            int cantVender = cantidadF - yaVendido;
            if (cantVender > 0) {
                int precioV = semilla.getPrecioVentaFruto();
                int ganancias = cantidadF * precioV;

                gananciasTotal += ganancias;
                //actualizar lo vendido
                ventasAcumuladas.put(semilla, yaVendido + cantVender);

                System.out.println("Se han vendido " + cantVender + " de " + semilla.getNombre() + " por " + ganancias + " euros.");
                frutos.remove(semilla);
            }
        }
        return gananciasTotal;
    }

}


