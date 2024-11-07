package Clases;

import GestionBasesDatos.BasesDatos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * La clase Establo representa un establo donde se gestionan los animales, su alimentación, producción y la venta de productos.
 * Esta clase interactúa con la base de datos para cargar, actualizar y registrar información relacionada con los animales,
 * los productos y los alimentos.
 */
public class Establo implements Serializable {
    //lista donde guardar los animales y metodos
    private List<Animales> lAnimales;
    //private Granja granja;
    private static final int CANT_MAX =25;
    private BasesDatos basesDatos; //usar el metodo getInstancia de base de datos
    Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
    /**
     * Constructor por defecto de la clase Establo. Inicializa la lista de animales y carga los datos desde la base de datos.
     */
    public Establo() {
        this.lAnimales = new ArrayList<>();
        basesDatos = BasesDatos.getInstancia();
        cargaAnimales();

    }

    /**
     * Carga los animales desde la base de datos y los asigna a la lista de animales.
     */
    public void cargaAnimales() {
        lAnimales = basesDatos.cargarAnimales();
    }
    /**
     * Muestra la información de todos los animales en el establo, incluyendo su id, tipo, estado de alimentación,
     * nombre, alimento y producto.
     */
    public void mostrarAnimales() {

        String alimentado;

        for (Animales animal : lAnimales) {
            if (animal.isEstaAlimentado()) {
                alimentado = "Sí"; // El animal está alimentado
            } else {
                alimentado = "No"; // El animal no está alimentado
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("| ID  |" + animal.getId() + "| TIPO | " + animal.getTipoAnimal() + "| ALIMENTADO |" + alimentado + "| NOMBRE | " + animal.getNombre()
                    + "| ALIMENTO |" + animal.getA().getNombre() + "| PRODUCTO  | " + animal.getP().getNombre() + " |");

        }
    }
    /**
     * Muestra la lista de alimentos disponibles en la base de datos.
     */
    public void mostrarAlimentos() {
        basesDatos.alimentos();
    }
    /**
     * Muestra la lista de productos disponibles en la base de datos.
     */
    public void mostrarProductos() {
        basesDatos.productos();
    }
    /**
     * Alimenta a los animales del establo que aún no han sido alimentados. Actualiza la cantidad de alimento disponible
     * en la base de datos y marca a los animales como alimentados.
     */
    public void alimentar() {
        for (Animales animal : lAnimales) {
            if (!animal.isEstaAlimentado()) { // Verifica si el animal ya ha sido alimentado en el día
                int cantidadConsumida =  animal.calcularCantidadConsumida(); // Llama al método
                int cantidadDisponible = basesDatos.leerCantidadAlimento(animal.getA().getId()); // sacar de bbdd

                if (cantidadDisponible >= cantidadConsumida) {
                    int nuevaCantidad = cantidadDisponible - cantidadConsumida;
                    basesDatos.actualizarAlimentos(animal.getA().getId(), nuevaCantidad);

                    animal.setEstaAlimentado(true); // Marca al animal como alimentado
                    System.out.println("Animal " + animal.getNombre() + " alimentado. Nueva cantidad disponible: " + nuevaCantidad);

                    basesDatos.inssertarTablaConsumo(animal.getId(), nuevaCantidad, fechaActual);

                } else {
                    System.out.println("No todos los animales han podido ser alimentados:  " + animal.getNombre());
                }


            } else {
                System.out.println("El animal " + animal.getNombre() + " ya fue alimentado hoy.");
            }

        }

        //basesDatos.registrarHistorialConsumo(lAnimales);
    }
    /**
     * Inicia la producción de productos por parte de los animales. Actualiza la base de datos con la cantidad de productos
     * producidos y su fecha de producción.
     * @param estacion La estación en la que se encuentra el establo.
     */
    public void produccion(Estacion estacion, int diaJuegoGranja) {
        System.out.println("Comienzo de produccion.....");
        for (Animales animal : lAnimales) {
            int produccion = animal.producir( estacion, diaJuegoGranja);

            if (produccion > 0) {
                System.out.println(animal.getNombre() + " ha producido " + produccion + " unidades de " +
                        animal.getP().getNombre());
                basesDatos.actualizarTablaProductos(animal.getP().getId(), produccion);

                Timestamp fecha_produccion = new Timestamp(System.currentTimeMillis());
                //para ver la fecha
                System.out.println("Fecha de producción: " + fecha_produccion);
                basesDatos.insertarTablaHistorialProduccion(animal.getId(), produccion, fecha_produccion);
            }
            /*
            else { System.out.println(animal.getNombre() + " no pudo producir (necesita ser alimentado primero)");
            }
             */

        }
    }
    /**
     * Vende los productos disponibles en el establo. Calcula el ingreso total de la venta y actualiza la base de datos
     * con la transacción.
     */
    public double venderProductos() {
        System.out.println("Iniciando venta de productos...");
        double ingresoTotal = 0;
        List<Productos> lProductos= basesDatos.cargarProductos();
        // Recorremos los productos que sacamos de mi metodo en bases de datos que me retorna esa lista de productos y me la recorro. NO los animales
        for (Productos p : lProductos) {

            if (p != null && p.getCantidad() > 0) {
                // Calcular el importe de la venta de este producto
                double precio = p.getCantidad() * p.getPrecio();

                System.out.println("Se han vendido " + p.getCantidad() +
                        " unidades de " + p.getNombre() +
                        " por " + precio + "€");



                // Actualizar la cantidad en la base de datos
                basesDatos.actualizarTablaProductos(p.getId(), 0);  // Ponemos la cantidad a 0

                // Actualizar la cantidad en el objeto
                p.setCantidad(0);

                ingresoTotal +=precio;

            }
        }
        // Registrar la transacción con el total de todas las ventas
        Timestamp fechaVenta = new Timestamp(System.currentTimeMillis());
        basesDatos.registrarTablaTransacciones(
                Tipo_transaccion.VENTA,
                Tipo_elemento.PRODUCTO,
                ingresoTotal,
                fechaVenta
        );
        return ingresoTotal;
    }
    /**
     * Reinicia el estado de alimentación de todos los animales del establo, marcándolos como no alimentados.
     */
    public void reiniciarAlimentacion() {
        for (Animales animal : lAnimales) {
            animal.setEstaAlimentado(false); // Reinicia el estado de alimentación
        }
    }
    /**
     * Rellena los comederos de los animales con el alimento necesario, comprando más si es necesario. Registra la transacción
     * de compra en la base de datos y devuelve el costo total de la compra.
     * @return El costo total de la compra de alimentos.
     */
    public double rellenarComedero() {
            List<Alimentos> lAlimentos = basesDatos.cargarAlimentos();
            double totalCompra = 0;

            for (Alimentos a : lAlimentos) {
                if (a != null && a.getCantidad() <= CANT_MAX) {
                    int cantidadNecesaria = CANT_MAX - a.getCantidad();
                    double compra = cantidadNecesaria * a.getPrecio();

                    // Comprar alimento y actualizar cantidad disponible
                    basesDatos.actualizarAlimentos(a.getId(), CANT_MAX);
                    a.setCantidad(CANT_MAX);
                    totalCompra += compra;
                    System.out.println("Se ha rellenado el comedero de " + a.getNombre() + " con " + cantidadNecesaria + " unidades a un costo de " + compra + "€");
                } else if (a.getCantidad() > CANT_MAX) {
                    System.out.println("El alimento " + a.getNombre() + " ya está en su capacidad máxima (" + CANT_MAX + " unidades).");
                } else {
                    System.out.println("No hay suficientes recursos para comprar más " + a.getNombre() + ".");
                }
            }
            // Registrar transacción en la tabla Transacciones
            Timestamp fechaCompra = new Timestamp(System.currentTimeMillis());
            basesDatos.registrarTablaTransacciones(Tipo_transaccion.COMPRA, Tipo_elemento.ALIMENTO, totalCompra, fechaCompra);
            return totalCompra;

    }
    public  void resetearBasesDatos(){
        basesDatos.actualizacionEliminacionProductosComienzo();
        basesDatos.eliminarHistoricoConsumo();
        basesDatos.eliminarHistoricoProduccion();
        basesDatos.eliminarTransacciones();
    }
}


