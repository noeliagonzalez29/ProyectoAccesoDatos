package Clases;

import GestionBasesDatos.BasesDatos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Establo implements Serializable {
    //lista donde guardar los animales y metodos
    private List<Animales> lAnimales;
    private BasesDatos basesDatos; //usar el metodo getInstancia de base de datos
    Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
    private Estacion estacion;

    public Establo() {
        this.lAnimales = new ArrayList<>();
        basesDatos = BasesDatos.getInstancia();
        cargaAnimales();

    }

    //metodo agregar animales
    public void aniadirAnimal(Animales a) {
        lAnimales.add(a);
    }

    //metodo que me retorne todos los animales
    public List<Animales> getlAnimales() {
        return lAnimales;
    }

    public void cargaAnimales() {
        lAnimales = basesDatos.cargarAnimales();
    }

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

    public void mostrarAlimentos() {
        basesDatos.alimentos();
    }

    public void mostrarProductos() {
        basesDatos.productos();
    }

    public void alimentar() {
        for (Animales animal : lAnimales) {
            if (!animal.isEstaAlimentado()) { // Verifica si el animal ya ha sido alimentado en el día
                int cantidadConsumida =  animal.calcularCantidadConsumida(); // Llama al método
                int cantidadDisponible = basesDatos.leerCantidadAlimento(animal.getA().getId()); // sacar de bbdd
                System.out.println(animal.getNombre() + "con una cantidad de: " + animal.getA().getCantidad()+ animal.isEstaAlimentado());
                if (cantidadDisponible >= cantidadConsumida) {
                    int nuevaCantidad = cantidadDisponible - cantidadConsumida;
                    basesDatos.actualizarAlimentos(animal.getA().getId(), nuevaCantidad);
                    //modificar la cantidad
                    //animal.getA().setCantidad(nuevaCantidad);
                    //animal.actualizarCantidadAlimento(nuevaCantidad);
                    animal.setEstaAlimentado(true); // Marca al animal como alimentado
                    System.out.println("Animal " + animal.getNombre() + " alimentado. Nueva cantidad disponible: " + nuevaCantidad);

                    basesDatos.inssertarTablaConsumo(animal.getId(), nuevaCantidad, fechaActual);

                } else {
                    System.out.println("No hay suficiente alimento para " + animal.getNombre());
                }
            } else {
                System.out.println("El animal " + animal.getNombre() + " ya fue alimentado hoy.");
            }

        }

        //basesDatos.registrarHistorialConsumo(lAnimales);
    }

    public void produccion() {
        System.out.println("Comienzo de produccion.....");
        for (Animales animal : lAnimales) {
            int produccion = animal.producir( estacion);

            if (produccion > 0) {
                System.out.println(animal.getNombre() + " ha producido " + produccion + " unidades de " +
                        animal.getP().getNombre());
                basesDatos.actualizarTablaProductos(animal.getP().getId(), animal.getP().getCantidad());

                Timestamp fecha_produccion = new Timestamp(System.currentTimeMillis());
                //para ver la fecha
                System.out.println("Timestamp de producción: " + fecha_produccion);
                basesDatos.insertarTablaHistorialProduccion(animal.getId(), produccion, fecha_produccion);
            } else {
                System.out.println(animal.getNombre() + " no pudo producir (necesita ser alimentado primero)");
            }
        }
    }

    public void venderProductos() {
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

                // Registrar la transacción
                Timestamp fechaVenta = new Timestamp(System.currentTimeMillis());
                basesDatos.registrarTablaTransacciones(
                        Tipo_transaccion.VENTA,
                        Tipo_elemento.PRODUCTO,
                        precio,
                        fechaVenta
                );

                // Actualizar la cantidad en la base de datos
                basesDatos.actualizarTablaProductos(p.getId(), 0);  // Ponemos la cantidad a 0

                // Actualizar la cantidad en el objeto
                p.setCantidad(0);

                ingresoTotal +=precio;
            }
        }


    }
    public void reiniciarAlimentacion() {
        for (Animales animal : lAnimales) {
            animal.setEstaAlimentado(false); // Reinicia el estado de alimentación
        }
    }



}

