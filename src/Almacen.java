import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Almacen implements Serializable {

        // Almacena los frutos y sus cantidades
        private Map<Semilla, Integer> frutos;
        //quiero evitar que si ya he vendido una vez se vuelva a vender y se vuelva a sumar al presupuesto
        private Map<Semilla, Integer> ventasAcumuladas;

        public Almacen() {

            this.frutos = new HashMap<>();
            this.ventasAcumuladas= new HashMap<>();
        }


        // Método para añadir nueva cosecha
        public Map<Semilla, Integer> aniadirCosecha(Map<Semilla, Integer> nuevaCosecha) {
            for (Semilla semilla : nuevaCosecha.keySet()) {
                Integer cantidad = nuevaCosecha.get(semilla);

                // Si existe, actualiza la cantidad
                frutos.put(semilla, frutos.getOrDefault(semilla,0) + cantidad);
            }
            return nuevaCosecha;
        }

        // Método para mostrar el contenido del almacén
        public void mostrarAlmacen() {
            boolean estaVacio= false;
            System.out.println("----- PRODUCTOS ALMACÉN -------");
            for (Map.Entry<Semilla, Integer> entry : frutos.entrySet()) {
                Semilla semilla = entry.getKey();
                int cantidad = entry.getValue();
                if (cantidad > 0) {
                    System.out.println("[" + semilla.getNombre() + " - "+ cantidad + " ]");
                    estaVacio=true;
                }
            }
            if (!estaVacio){
                System.out.println("No hay productos aun en el almacén");
            }
        }

        // Método de venta
        public int venderFrutos() {
            int gananciasTotal=0;
            for (Map.Entry<Semilla,Integer> entry: frutos.entrySet()){
                Semilla semilla= entry.getKey();
                int cantidadF= entry.getValue();
                //ya he vendido
                int yaVendido= ventasAcumuladas.getOrDefault(semilla,0);
                int cantVender= cantidadF - yaVendido;
                if (cantVender>0){
                    int precioV= semilla.getPrecioVentaFruto();
                    int ganancias= cantidadF * precioV;

                    gananciasTotal+= ganancias;
                    //actualizar lo vendido
                    ventasAcumuladas.put(semilla, yaVendido + cantVender );

                    System.out.println("Se han vendido " + cantVender + " de " + semilla.getNombre()+ " por " + ganancias + " euros.");
                }
            }
            return  gananciasTotal;
        }

}


