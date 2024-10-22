import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    /**
     *
     * @param args
     */

    private final static GestionBinario GESTION_BINARIO = new GestionBinario();
    public static void menuJuego(){
        Scanner entrada= new Scanner(System.in);
        GestionProperties gestion= new GestionProperties();
        int columna = Integer.parseInt(gestion.getProperty("columnas"));
        Granja granja = new Granja();
        String[]valores;
        String resp;
        int opc;
        do{
            System.out.println("------STARDAM VALLEY------");
            System.out.println("1. Iniciar Nuevo Día");
            System.out.println("2. Atender Cultivo");
            System.out.println("3. Plantar cultivo en columna");
            System.out.println("4. Vender cosecha");
            System.out.println("5. Mostrar información granja");
            System.out.println("6. Salir");
            opc= entrada.nextInt();
            switch (opc){
                case 1:
                    granja.iniciarNuevoDia();
                    break;
                case 2:
                    granja.cuidarHuerto();
                    break;
                case 3:
                    granja.plantarCultivoColumna(columna);
                    break;
                case 4:
                    granja.venderFrutos();
                    break;
                case 5:
                    granja.mostrarGranja();
                    break;
                case 6:
                    System.out.println("ABANDONANDO EL JUEGO...");
                    GESTION_BINARIO.guardarPartida(granja);
                    break;

            }
        }while(opc!=6);
    }
    public static void nuevaPartida() {
        Scanner entrada= new Scanner(System.in);
        GestionProperties p=new GestionProperties();
        Granja granja = new Granja();
        String resp;
        String[] valores;
        boolean personalizado;
        //si hay fichero binario es por que habia partida previa y hay que borrar el bin
        GESTION_BINARIO.eliminarBinarioPartida();
        //preguntamos si quiere personalizar el fichero properties
        System.out.println("¿Quieres personalizar la partida?");
        resp= entrada.nextLine();
        personalizado=resp.equalsIgnoreCase("si");
        if (personalizado){
            System.out.println("Vamos a personalizar los datos");
           //metodo gestion crearfichero personalizado
            granja.inicializarValores(personalizado);
           valores= p.crearFicheroPropiedadesPersonalizado();
           HuertoGestion h=new HuertoGestion();
           h.crearFicheroHuerto();

                //llamar al metodo de la clase Tienda que maneja la generacion de semillas

        } else {
            //llamar a metodo crearfichero clase gestionproperties y al metodo iniciar componentes
            valores=p.crearFichero();

        }
        iniciarComponentes(Integer.parseInt(valores[0]),Integer.parseInt(valores[1]));
        menuJuego();

    }

    public  static void iniciarComponentes(int filas,int columnas){
        try {
            RandomAccessFile raf = new RandomAccessFile("src/resources/huerto.dat", "rw");
            for (int i = 0; i < filas; i++){
                for (int j = 0; j < columnas; j++){
                    raf.writeInt(-1);
                    raf.writeBoolean(false);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void cargarPartida(){
        //cargo el binario si existe y muestro el menu
        if (GESTION_BINARIO.existeFichero()) {

               Granja granja= GESTION_BINARIO.cargarBinarioPartida();
                int diaAc = granja.getDiaActual();
                Estacion estacion= granja.getEstacion();
                int presu = granja.getPresupuesto();
                Tienda ti= granja.getT();
                Almacen al = granja.getA();
                HuertoGestion h= granja.getH();

                menuJuego();


        }else {
            System.out.println("No existe partida guardada");
        }
    }
    public static void main(String[] args) {
        Scanner entrada= new Scanner(System.in);
        int opcion;
        if (GESTION_BINARIO.existeFichero()) {

            do {
                System.out.println("-----BIENVENIDO A STARDAM VALLEY------");
                System.out.println("1. NUEVA PARTIDA");
                System.out.println("2. CARGAR PARTIDA");
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
            } while (opcion != 2);
        }else {
            System.out.println("-----BIENVENIDO A STARDAM VALLEY------");
            System.out.println("1. NUEVA PARTIDA");
            opcion = entrada.nextInt();
            switch (opcion) {
                case 1:
                    nuevaPartida();
                    break;
                default:
                    System.out.println("Opcion no válida");
                    break;

            }

        }
    }
}