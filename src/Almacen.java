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
            if (frutos.isEmpty()) {
                System.out.println("El almacén está vacío.");
            } else {
                System.out.println("Contenido del almacén:");
                for (Semilla semilla : frutos.keySet()) {
                    System.out.println("- " + semilla.getNombre() + ": " + frutos.get(semilla) + " unidades");
                }
            }
        }
/*
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

 */


    public Map<Semilla, Integer> obtenerAlmacen() {
        return frutos;
    }
    public void guardarFrutos(Semilla semilla, int cantidadFrutos) {
        // Si la semilla ya existe en el almacén, incrementamos la cantidad
        if (frutos.containsKey(semilla)) {
            frutos.put(semilla, frutos.get(semilla) + cantidadFrutos);
        } else {
            // Si la semilla no existe, la añadimos al almacén con la cantidad inicial
            frutos.put(semilla, cantidadFrutos);
        }
        System.out.println("Se han almacenado " + cantidadFrutos + " frutos de la semilla " + semilla.getNombre() + ".");
    }
    public void eliminarFrutos(int idSemilla) {
        // Elimina los frutos de la semilla especificada del almacén
        if (frutos.containsKey(idSemilla)) {
            frutos.remove(idSemilla); // Eliminar la entrada del almacén para la semilla
        }
    }

}


