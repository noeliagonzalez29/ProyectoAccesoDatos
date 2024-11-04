package Clases;

import GestionBasesDatos.BasesDatos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Establo implements Serializable {
    //lista donde guardar los animales y metodos
    private List<Animales> lAnimales;
    private BasesDatos basesDatos;
    Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

    public Establo() {
        this.lAnimales = new ArrayList<>();
        basesDatos = new BasesDatos();
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
                int cantidadConsumida = animal.calcularCantidadConsumida(); // Llama al método
                int cantidadDisponible = animal.getA().getCantidad();
                System.out.println(animal.getNombre() + "con una cantidad de: " + cantidadDisponible + animal.isEstaAlimentado());
                if (cantidadDisponible >= cantidadConsumida) {
                    int nuevaCantidad = cantidadDisponible - cantidadConsumida;
                    basesDatos.actualizarAlimentos(animal.getA().getId(), nuevaCantidad);
                    //modificar la cantidad
                    animal.getA().setCantidad(nuevaCantidad);
                    //animal.actualizarCantidadAlimento(nuevaCantidad);
                    animal.setEstaAlimentado(true); // Marca al animal como alimentado
                    System.out.println("Animal " + animal.getNombre() + " alimentado. Nueva cantidad disponible: " + nuevaCantidad);

                    basesDatos.inssertarTablaConsumo(animal.getId(), cantidadConsumida, fechaActual);

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
            int produccion = animal.producir();

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

        // Recorremos los animales y accedemos a sus productos
        for (Animales animal : lAnimales) {
            Productos producto = animal.getP();  // Usando el getter que ya tienes

            if (producto != null && producto.getCantidad() > 0) {
                // Calcular el importe de la venta de este producto
                double precio = producto.getCantidad() * producto.getPrecio();

                System.out.println("Se han vendido " + producto.getCantidad() +
                        " unidades de " + producto.getNombre() +
                        " por " + precio + "€");

                // Registrar la transacción
                Timestamp fechaVenta = new Timestamp(System.currentTimeMillis());
                basesDatos.registrarTablaTransacciones(
                        Tipo_transaccion.VENTA,
                        producto.getTipo_elemento(),
                        precio,
                        fechaVenta
                );

                // Actualizar la cantidad en la base de datos
                basesDatos.actualizarTablaProductos(producto.getId(), 0);  // Ponemos la cantidad a 0

                // Actualizar la cantidad en el objeto
                producto.setCantidad(0);

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

