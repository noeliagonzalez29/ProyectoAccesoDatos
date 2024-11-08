package Clases;

import GestionFicheros.GestionProperties;
import GestionFicheros.HuertoGestion;

import java.io.Serializable;
import java.util.*;

/**
 * La clase Clases.Granja representa una granja que gestiona un huerto, un almacén,
 * un presupuesto,una tienda y las estaciones del año. Permite realizar diversas
 * acciones relacionadas con la siembra y la cosecha de semillas.
 */
public class Granja implements Serializable {
    private int diaActual;
    private Estacion estacion;
    private int presupuesto;
    private int gananciasAcumuladas;
    private Tienda t;
    private HuertoGestion h;
    private Almacen a;
    Semilla semilla;
    private List<Semilla> semillasDisponibles;
    private GestionProperties propiedades;
    private int diasEnEstacionActual;
    private Establo establo;


    public HuertoGestion getH() {
        return h;
    }

    /**
     * Crea una nueva instancia de la clase Clases.Granja, inicializando sus atributos.
     * Se crean instancias de GestionFicheros.HuertoGestion, Clases.Almacen, Clases.Semilla, Clases.Tienda y
     * se cargan las propiedades de configuración.
     */
    public Granja() {
        this.h = new HuertoGestion();
        this.a = new Almacen();
        this.semilla = new Semilla();
        this.semillasDisponibles = new ArrayList<>();
        this.t = new Tienda();
        this.propiedades = GestionProperties.getInstancia();
        this.diaActual = 0;
        this.diasEnEstacionActual = 1;
        this.establo = new Establo();
        // h.abrirConexion();
        inicializarValores(false);

    }


    /**
     * Inicializa los valores de la granja según las propiedades cargadas.
     * Se puede elegir entre cargar valores predeterminados o personalizados.
     *
     * @param personalizado indica si se deben cargar valores personalizados
     */
    public void inicializarValores(boolean personalizado) {
        String[] valores;

        if (personalizado) {
            valores = propiedades.cargarFicheroPersonalizado();
        } else {
            valores = propiedades.cargarPorDefecto();
        }

        this.presupuesto = Integer.parseInt(valores[2]);
        this.estacion = Estacion.valueOf(valores[3].toUpperCase());

        List<Semilla> todasLasSemillas = semilla.leerSemillas();
        for (Semilla s : todasLasSemillas) {
            if (s.getEstaciones().contains(this.estacion)) {
                semillasDisponibles.add(s);
            }
        }

        t.agregarSemillasPorEstacion(estacion.toString(), semillasDisponibles);
        t.generarSemillasDelDia(estacion.toString());
        h.inicializarHuerto();

    }

    /**
     * Cambia la estación actual de la granja a la siguiente.
     */
    public void cambiarEstacion() {
        switch (estacion) {
            case PRIMAVERA:
                estacion = Estacion.VERANO;
                break;
            case VERANO:
                estacion = Estacion.OTOÑO;
                break;
            case OTOÑO:
                estacion = Estacion.INVIERNO;
                break;
            case INVIERNO:
                estacion = Estacion.PRIMAVERA;
                break;
        }
    }

    /**
     * Inicia un nuevo día en la granja, actualizando la estación si es necesario
     * y generando nuevas semillas para la tienda.
     */
    public void iniciarNuevoDia() {

        if (diaActual == 0) {
            estacion = Estacion.valueOf(propiedades.getProperty("estacioninicio"));
        }

        Estacion estacionAnterior = estacion;

        diaActual++;
        diasEnEstacionActual++;
        //reiniciar alimento establo
        establo.reiniciarAlimentacion();

        //establo.resetearAlimentado(); //para cambiar si ha sido alimentado
        int duracionEstacion = Integer.parseInt(propiedades.getProperty("duracionestacion"));
        if (diasEnEstacionActual > (duracionEstacion + 1)) {
            diaActual = 1;
            diasEnEstacionActual = 1; // Reiniciar el contador de días en la nueva estación
            cambiarEstacion();
            h.inicializarHuerto();

        } else {
            h.actualizarHuertoNuevoDia();
        }

        t.generarSemillasDelDia(String.valueOf(estacion));

        System.out.println("Día " + diaActual + " en la estación " + estacion);

    }

    /**
     * Cuida el huerto, gestionando la cosecha de las plantas sembradas.
     */
    public void cuidarHuerto() {
        Map<Semilla, Integer> cosecha = h.cuidarHuerto();
        a.aniadirCosecha(cosecha);
    }

    /**
     * Planta una semilla en toda una columna del huerto, verificando si hay
     * suficiente presupuesto y mostrando las opciones disponibles.
     *
     * @param columna la columna donde se desea plantar la semilla
     * @return true si la siembra fue exitosa, false en caso contrario
     */
    public boolean plantarCultivoColumna(int columna) {
        boolean exito = false;
        String filasStr = propiedades.getProperty("filashuerto");
        int filas = Integer.parseInt(filasStr);
        // Obtener semillas disponibles del día
        List<Semilla> semillasDiarias = t.getSemillasDelDia();
        List<Semilla> semillasComprables = new ArrayList<>();

        // Calcular costo total por semilla para toda la columna
        System.out.println("Semillas disponibles para plantar:");
        for (Semilla semilla : semillasDiarias) {
            int costoTotal = semilla.getPrecioCompraSemilla() * filas;
            if (costoTotal <= presupuesto) {
                semillasComprables.add(semilla);
                System.out.println("ID: " + semilla.getId() +
                        ", Nombre: " + semilla.getNombre() +
                        ", Costo total para la columna: " + costoTotal +
                        ", Días de crecimiento: " + semilla.getDiasCrecimiento());

            }
        }
        if (semillasComprables.isEmpty()) {
            System.out.println("No tienes suficiente presupuesto para comprar ninguna semilla.");
            return exito;
        }
        // Pedir al usuario que seleccione una semilla
        Scanner entrada = new Scanner(System.in);
        boolean correcto = false;
        int idSemilla = 0;
        do {
            try {
                System.out.println("Ingrese el ID de la semilla que desea plantar en la columna " + columna + ":");
                idSemilla = entrada.nextInt();
                entrada.nextLine();
                // Verificar que la semilla seleccionada existe y es comprable
                Semilla semillaSeleccionada = null;
                for (Semilla s : semillasComprables) {
                    if (s.getId() == idSemilla) {
                        semillaSeleccionada = s;
                    }
                }
                if (semillaSeleccionada != null) {
                    // Calcular costo total y actualizar presupuesto
                    int costoTotal = semillaSeleccionada.getPrecioCompraSemilla() * filas;
                    actualizarPresupuesto(-costoTotal);

                    // Plantar la semilla en toda la columna
                    h.plantarSemillaColumna(columna, idSemilla);
                    System.out.println("Se ha plantado " + semillaSeleccionada.getNombre() +
                            " en toda la columna " + columna +
                            ". Costo total: " + costoTotal +
                            ". Presupuesto restante: " + presupuesto);
                    correcto = true;
                    exito = true;
                } else {
                    System.out.println("Error: ID Clases.Semilla no válida ");
                }
            } catch (InputMismatchException e) {
                System.out.println("TIENES que introducir el NUMERO id de la semilla");
                entrada.nextInt();

            }
        } while (!correcto);

        return exito;
    }

    /**
     * Vende los frutos cosechados en la granja, actualizando el presupuesto y
     * las ganancias acumuladas.
     */
    public void venderFrutosGranja() {
        a.mostrarAlmacen();
        int gananciasVenta = a.venderFrutos();
        presupuesto += gananciasVenta;
        gananciasAcumuladas += gananciasVenta;
        System.out.println("Tras la venta tengo: " + presupuesto + " € gracias a las ventas");
        h.mostrarHuerto();
    }

    /**
     * Actualiza el presupuesto de la granja, ya sea incrementándolo por ventas
     * o decrementándolo por la compra de semillas.
     *
     * @param cantidad cantidad a sumar o restar del presupuesto
     */
    private void actualizarPresupuesto(int cantidad) {
        presupuesto += cantidad;
    } //incluir las ganancias de la venta de la produccion de animales

    /**
     * Muestra el estado actual de la granja, incluyendo el día, la estación,
     * el presupuesto, el huerto y el almacén.
     */
    public void mostrarGranja() {
        System.out.println("----------- ESTADO DE LA GRANJA ---------");
        System.out.println("Día actual: " + diaActual);
        System.out.println("Estación actual: " + estacion);
        System.out.println("Presupuesto: " + presupuesto + " € ");
        h.mostrarHuerto();
        a.mostrarAlmacen();

    }

    /**
     * Muestra el estado actual del establo, incluyendo los animales, los alimentos
     * disponibles y los productos que se pueden vender.
     */
    public void mostrarEstablo() {
        establo.mostrarAnimales();
        establo.mostrarAlimentos();
        establo.mostrarProductos();
    }

    /**
     * Alimenta a los animales del establo si el día actual está dentro de la duración de la estación.
     */
    public void alimentarAnimales() {
        int duracionEstacion = Integer.parseInt(propiedades.getProperty("duracionestacion"));

        if (diaActual < duracionEstacion) {
            establo.alimentar();
        }

    }

    /**
     * Realiza la producción de productos de los animales según la estación actual.
     * Esta acción depende de la estación en la que se encuentre la granja.
     */
    public void produccionAnimales() {
        establo.produccion(this.estacion, diaActual);
    }

    /**
     * Vende los productos de los animales del establo, actualizando el presupuesto
     * de la granja con las ganancias obtenidas.
     */
    public void venderProductos() {
        double ingresoTotal = establo.venderProductos();
        actualizarPresupuesto((int) ingresoTotal);
    }

    /**
     * Rellena el comedero de los animales en el establo y actualiza el presupuesto
     * con el gasto total realizado para ello.
     */
    public void rellenarComedero() {
        double gastoTotal = establo.rellenarComedero();
        actualizarPresupuesto((int) -gastoTotal);
        System.out.println("El GASTO TOTAL por rellenar el comedero ha sido de " + gastoTotal + "€.");
    }

    /**
     * metodo que me sirve para restear la base de datos al iniciar partida
     */
    public void resetearBasesDatos() {
        establo.resetearBasesDatos();
    }

    /**
     * metodo que tengo para restablecer la conexion a la base de datos. Enlaza al establo y este tiene otro metodo que en laza
     * con la base de datos que contiene el método en sí
     */

    public void restablecerConexionG() {
        establo.resstablecerConexionE();
    }
}
