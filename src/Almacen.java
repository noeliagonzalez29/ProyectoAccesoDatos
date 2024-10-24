import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Almacen implements Serializable {

        // Almacena los frutos y sus cantidades
        private Map<Semilla, Integer> frutos;

        public Almacen() {
            this.frutos = new HashMap<>();
        }


        // Método para añadir nueva cosecha
        public void añadirCosecha(Map<Semilla, Integer> nuevaCosecha) {
            for (Semilla semilla : nuevaCosecha.keySet()) {
                Integer cantidad = nuevaCosecha.get(semilla);

                if (frutos.containsKey(semilla)) {
                    // Si existe, actualiza la cantidad
                    frutos.put(semilla, frutos.get(semilla) + cantidad);
                } else {
                    // Si no existe, añade el fruto con la nueva cantidad
                    frutos.put(semilla, cantidad);
                }
            }
            System.out.println("Cosecha añadida exitosamente.");
        }

        // Método para mostrar el contenido del almacén
        public void mostrarAlmacen() {
            System.out.println("Productos en el almacén:");
            for (Map.Entry<Semilla, Integer> entry : frutos.entrySet()) {
                Semilla semilla = entry.getKey();
                int cantidad = entry.getValue();
                if (cantidad > 0) {
                    System.out.println(semilla.getNombre() + ": " + cantidad + " unidades.");
                }
            }
        }

        // Método de venta
        public void venderFrutos() {
            int gananciasTotal=0;
            for (Map.Entry<Semilla,Integer> entry: frutos.entrySet()){
                Semilla semilla= entry.getKey();
                int cantidadF= entry.getValue();

                if (cantidadF>0){
                    int precioV= semilla.getPrecioVentaFruto();
                    int ganancias= cantidadF* precioV;

                    gananciasTotal+= ganancias;

                    System.out.println("Se han vendido" + cantidadF + "de" + semilla.getNombre()+ "por" + ganancias + "euros.");
                }
            }
        }

}


