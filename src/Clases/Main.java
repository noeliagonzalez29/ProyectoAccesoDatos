package Clases;

import GestionFicheros.GestionBinario;
import GestionFicheros.GestionProperties;
import GestionFicheros.HuertoGestion;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * La clase Clases.Main es la clase principal de la aplicación "Stardam Valley".
 * Maneja la lógica del juego, incluida la interacción con el usuario a través de un menú,
 * el inicio de nuevas partidas, la carga de partidas guardadas y la gestión de cultivos en la granja.
 */
public class Main {

    private final static GestionBinario GESTION_BINARIO = new GestionBinario();

    /**
     * Muestra el menú principal del juego y gestiona la lógica del juego en función de la opción elegida por el usuario.
     *
     * @param granja La instancia de la granja que se está gestionando en el juego.
     */
    public static void menuJuego(Granja granja) {
        Scanner entrada = new Scanner(System.in);
        GestionProperties.getInstancia();
        int opc = 0;
        do {
            System.out.println("------------STARDAM VALLEY------------");
            System.out.println("1. Iniciar Nuevo Día");
            System.out.println("2. Huerto ");
            System.out.println("3. Establos");
            System.out.println("4. Salir");

            try {
                opc = entrada.nextInt();

                switch (opc) {
                    case 1:
                        granja.iniciarNuevoDia();
                        break;
                    case 2:
                        menuHuerto(granja);
                        break;
                    case 3:
                        menuEstablo(granja);
                        break;
                    case 4:
                        System.out.println("ABANDONANDO EL JUEGO...");
                        GESTION_BINARIO.guardarPartida(granja);
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + opc);
                }
            } catch (InputMismatchException e) {
                System.out.println("Debe introducir un número del 1 al 6");
                entrada.nextLine(); //hay que limpiar porque si no no deja de repetirse

            }
        } while (opc != 4);

    }
    public static void menuHuerto(Granja granja){
        Scanner entrada = new Scanner(System.in);
        int opc = 0;
        int columna;
        do{

            System.out.println("1. Atender Cultivo");
            System.out.println("2. Plantar cultivo en columna");
            System.out.println("3. Vender cosecha");
            System.out.println("4. Mostrar información granja");
            System.out.println("5. Volver");
            opc= entrada.nextInt();
            switch (opc){
                case 1:
                    granja.cuidarHuerto();
                    break;
                case 2:
                    boolean columValida = false;
                    do {
                        try {
                            System.out.println("¿En qué columna quiere plantar?");
                            columna = entrada.nextInt();

                            if (granja.getH().isColumnaEnRango(columna)) {
                                if (granja.getH().isColumnaVacia(columna)) {
                                    granja.plantarCultivoColumna(columna);
                                    columValida = true;
                                } else {
                                    System.out.println("NO puedes plantar aquí, columna OCUPADA");
                                }
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Debe introducir un número válido para la columna");
                            entrada.nextLine();
                        }
                    } while (!columValida);
                    break;
                case 3:
                    granja.venderFrutosGranja();
                    break;
                case 4:
                    granja.mostrarGranja();
                    break;
                case 5:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opcion NO valida. Introduce de 1 a 5");
            }

        }while(opc!=5);
    }
    public static void menuEstablo(Granja granja){
        Scanner entrada = new Scanner(System.in);
        int opc = 0;
        do{
            System.out.println("---------ESTABLOS------");
            System.out.println("1. Producir");
            System.out.println("2. Alimentar");
            System.out.println("3. Vender productos");
            System.out.println("4. Rellenar comedero");
            System.out.println("5. Mostrar Animales");
            System.out.println("6. Volver");
            opc= entrada.nextInt();
            switch (opc){
                case 1:
                    granja.produccionAnimales();
                    break;
                case 2:
                   granja.alimentarAnimales();
                    break;
                case 3:
                    granja.venderProductos();

                    break;
                case 4:
                    granja.rellenarComedero();
                    break;
                case 5:
                    //mostrar animales instancia de establo en granja para mostrarlo
                    granja.mostrarEstablo();
                    break;
                case 6:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opcion NO valida. Introduce de 1 a 6");
            }

        }while(opc!=6);
    }

    /**
     * Inicia una nueva partida del juego, permitiendo al usuario personalizar su configuración si lo desea.
     */
    public static void nuevaPartida() {
        Scanner entrada = new Scanner(System.in);
        GestionProperties p = GestionProperties.getInstancia();
        Granja granja;
        String resp;
        String[] valores;
        boolean personalizado;
        //si hay fichero binario es por que habia partida previa y hay que borrar el bin
        GESTION_BINARIO.eliminarBinarioPartida();
        p.eliminarConfiguracionPersonalizada();
        //preguntamos si quiere personalizar el fichero properties
        System.out.println("¿Quieres personalizar la partida?");
        resp = entrada.nextLine();
        personalizado = resp.equalsIgnoreCase("si");
        HuertoGestion h = new HuertoGestion();
        if (personalizado) {
            System.out.println("Vamos a personalizar los datos");
            //metodo gestion crearfichero personalizado
            valores = p.crearFicheroPropiedadesPersonalizado();
            h.crearFicheroHuerto();
        } else {
            valores = p.cargarPorDefecto();
        }
        h.iniciarComponentes(Integer.parseInt(valores[0]), Integer.parseInt(valores[1]));
        granja = new Granja();
        granja.inicializarValores(personalizado);
        menuJuego(granja);
        granja.getH().cerrarConexion();
    }

    /**
     * Carga una partida previamente guardada y muestra el menú del juego.
     */
    public static void cargarPartida() {
        Granja granja = null;
        //cargo el binario si existe y muestro el menu
        if (GESTION_BINARIO.existeFichero()) {
            granja = GESTION_BINARIO.cargarBinarioPartida();
            menuJuego(granja);
        } else {
            System.out.println("No existe partida guardada");
        }
        granja.getH().cerrarConexion();

    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        int opcion = 0;
        if (GESTION_BINARIO.existeFichero()) {
            do {
                System.out.println("-------------BIENVENIDO A STARDAM VALLEY------------");
                System.out.println("1. NUEVA PARTIDA");
                System.out.println("2. CARGAR PARTIDA");
                try {
                    opcion = entrada.nextInt();
                    switch (opcion) {
                        case 1:
                            nuevaPartida();
                            break;
                        case 2:
                            cargarPartida();
                            break;
                        default:
                            System.out.println("Opcion no válida");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Debe introducir un número del 1 al 2");
                    entrada.nextLine();
                }
            } while (opcion != 1 && opcion != 2);
        } else {
            do {
                System.out.println("-----------BIENVENIDO A STARDAM VALLEY-----------");
                System.out.println("1. NUEVA PARTIDA");
                try {
                    opcion = entrada.nextInt();
                    switch (opcion) {
                        case 1:
                            nuevaPartida();
                            break;
                        default:
                            System.out.println("Opcion no válida");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Debe introducir el número 1 porque no tienes partida guardada");
                    entrada.nextLine();
                }
            } while (opcion != 1);
        }
    }
}