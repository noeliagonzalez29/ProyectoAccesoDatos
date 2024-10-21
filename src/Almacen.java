import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Almacen implements Serializable {

        // Almacena los frutos y sus cantidades
        private Map<String, Integer> frutos;

        public Almacen() {
            this.frutos = new HashMap<>();
        }

        // Método para añadir nueva cosecha
        public void añadirCosecha(Map<String, Integer> nuevaCosecha) {
            for (String fruto : nuevaCosecha.keySet()) {
                Integer cantidad = nuevaCosecha.get(fruto);

                if (frutos.containsKey(fruto)) {
                    // Si existe, actualiza la cantidad
                    frutos.put(fruto, frutos.get(fruto) + cantidad);
                } else {
                    // Si no existe, añade el fruto con la nueva cantidad
                    frutos.put(fruto, cantidad);
                }
            }
            System.out.println("Cosecha añadida exitosamente.");
        }

        // Método para mostrar el contenido del almacén
        public void mostrarAlmacen() {
            if (frutos.isEmpty()) {
                System.out.println("El almacén está vacío.");
            } else {
                System.out.println("Contenido del almacén:");
                for (String fruto : frutos.keySet()) {
                    System.out.println("- " + fruto + ": " + frutos.get(fruto) + " unidades");
                }
            }
        }

        // Método para vender frutos
        public int venderFrutos() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("¿Qué fruto deseas vender?");
            String fruto = scanner.nextLine();
            System.out.println("¿Cuántas unidades deseas vender?");
            int cantidadAVender = scanner.nextInt();



            int cantidadDisponible = frutos.get(fruto);
            if (cantidadDisponible < cantidadAVender) {
                System.out.println("No hay suficiente cantidad de " + fruto + " en el almacén. Disponibles: " + cantidadDisponible);
                return 0; // No se ha vendido nada
            }

            // Actualizamos la cantidad en el almacén
            frutos.put(fruto, cantidadDisponible - cantidadAVender);
            System.out.println("Se han vendido " + cantidadAVender + " unidades de " + fruto + ".");
            return cantidadAVender;
        }
    }


