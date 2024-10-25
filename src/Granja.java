import java.io.Serializable;
import java.util.*;

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

    public List getSemillasDisponibles() {
        return semillasDisponibles;
    }

    public void setSemillasDisponibles(List semillasDisponibles) {
        this.semillasDisponibles = semillasDisponibles;
    }

    public HuertoGestion getH() {
        return h;
    }

    public void setH(HuertoGestion h) {
        this.h = h;
    }

    public Almacen getA() {
        return a;
    }

    public void setA(Almacen a) {
        this.a = a;
    }



    public Granja() {
        this.h= new HuertoGestion();
        this.a= new Almacen();
        this.semilla= new Semilla();
        this.semillasDisponibles= new ArrayList<>();
        this.t= new Tienda();
        this.propiedades=new GestionProperties();
        this.diaActual=0;
        this.diasEnEstacionActual=1;
        inicializarValores(false);

    }
    public void inicializarValores(boolean personalizado) {
        String[] valores;
        if (personalizado) {
            valores = propiedades.crearFicheroPropiedadesPersonalizado();
        } else {
            valores = propiedades.crearFichero();
        }

        // Inicializar valores desde el array
        this.presupuesto = Integer.parseInt(valores[2]);
        this.estacion = Estacion.valueOf(valores[3].toUpperCase());
        // Inicializar la tienda con las semillas de la estación actual
        List<Semilla> todasLasSemillas = semilla.leerSemillas();
        for (Semilla s : todasLasSemillas) {
            if (s.getEstaciones().contains(this.estacion)) {
                semillasDisponibles.add(s);
            }
        }
        // Inicializar la tienda con las semillas disponibles
        t.agregarSemillasPorEstacion(estacion.toString(), semillasDisponibles);
        t.generarSemillasDelDia(estacion.toString());
        h.inicializarHuerto();
    }

    public int getDiaActual() {
        return diaActual;
    }

    public void setDiaActual(int diaActual) {
        this.diaActual = diaActual;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Tienda getT() {
        return t;
    }

    public void setT(Tienda t) {
        this.t = t;
    }


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
    public void iniciarNuevoDia () {

        estacion= Estacion.valueOf(propiedades.getProperty("estacioninicio"));
        Estacion estacionAnterior = estacion;

        // 1. Actualizar el contador de días
       diaActual++;
       diasEnEstacionActual++;

        // 2. Verificar si es necesario cambiar de estación
        int duracionEstacion = Integer.parseInt(propiedades.getProperty("duracionestacion"));
        if (diasEnEstacionActual >= duracionEstacion) {
            cambiarEstacion();
            diasEnEstacionActual = 0; // Reiniciar el contador de días en la nueva estación
           // h.actualizarHuertoNuevoDia();
        }

        // 3. Actualizar cultivos o limpiar huerto
        if (estacion == estacionAnterior) {
           //diaActual++;
            h.actualizarHuertoNuevoDia();

        } else {
           h.inicializarHuerto();

            System.out.println("Dia" + diaActual + "ahora en estacion" + estacion);
            h.cuidarHuerto();  // Eliminar todos los cultivos si cambió la estación
        }

        // 4. Generar nuevas semillas para la tienda
        t.generarSemillasDelDia(String.valueOf(estacion));
        // Opcional: Mostrar información del nuevo día o estación
        System.out.println("Día " + diaActual + " en la estación " + estacion);


    }


    public void cuidarHuerto () {

        Map<Semilla, Integer> cosecha = h.cuidarHuerto();
        a.aniadirCosecha(cosecha);

    }
    public void plantarCultivoColumna ( int columna){
        Scanner entrada= new Scanner(System.in);
        String columnasStr = propiedades.getProperty("columnas");
        String filasStr = propiedades.getProperty("filashuerto");
        int columnas = Integer.parseInt(columnasStr);
        int filas = Integer.parseInt(filasStr);

       // Obtener semillas disponibles del día
        List<Semilla> semillasDiarias = t.getSemillasDelDia();
        List<Semilla> semillasComprables = new ArrayList<>();
        /*
        System.out.println("Semillas disponibles para plantar:");
        for (Semilla semilla : semillasDisponibles) {
            System.out.println("ID: " + semilla.getId() + ", Nombre: " + semilla.getNombre() + ", Días de Crecimiento: " + semilla.getDiasCrecimiento());
        }
         */

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
            return;
        }
        // Pedir al usuario que seleccione una semilla
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID de la semilla que desea plantar en la columna " + columna + ":");
        int idSemilla = scanner.nextInt();
        // Verificar que la semilla seleccionada existe y es comprable
        Semilla semillaSeleccionada = null;
        for (Semilla s : semillasComprables) {
            if (s.getId() == idSemilla) {
                semillaSeleccionada = s;

            }
        }

        if (semillaSeleccionada == null) {
            System.out.println("Error: Semilla no válida o no puedes permitirte comprarla.");
            return;
        }

        // Calcular costo total y actualizar presupuesto
        int costoTotal = semillaSeleccionada.getPrecioCompraSemilla() * filas;
        //contemplar que haya tenido una venta:
       // version antes: presupuesto -= costoTotal;
       // int gananciasVenta= getPresupuesto() + a.venderFrutos();
       // gananciasVenta-= costoTotal;
        actualizarPresupuesto(-costoTotal);

        // Plantar la semilla en toda la columna usando el método de HuertoGestion
        h.plantarSemillaColumna(columna, idSemilla);

        System.out.println("Se ha plantado " + semillaSeleccionada.getNombre() +
                " en toda la columna " + columna +
                ". Costo total: " + costoTotal +
                ". Presupuesto restante: " + presupuesto);
    }


    public void venderFrutosGranja () {
        a.mostrarAlmacen();
       // a.venderFrutos();
        int gananciasVenta= a.venderFrutos();
        presupuesto += gananciasVenta;
        //actualizarPresupuesto(gananciasVenta);
        gananciasAcumuladas += gananciasVenta;
        System.out.println("Tras la venta tengo: " + presupuesto + " € gracias a las ventas");
        h.mostrarHuerto();

    }

    //metodo para actualizar el presupuesto si vendo (gano) o compro semilla(pierdo). Privado para usarlo solo aqui
    private void actualizarPresupuesto(int cantidad){
        presupuesto+=cantidad;
    }
    public void mostrarGranja() {

        System.out.println("----------- ESTADO DE LA GRANJA ---------");
        System.out.println("Día actual: " + diaActual);
        System.out.println("Estación actual: " + estacion);
        System.out.println("Presupuesto: " + presupuesto  + " € ");
        h.mostrarHuerto();
        a.mostrarAlmacen();
    }




}
