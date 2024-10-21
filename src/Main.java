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
        Granja granja = null;
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
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    System.out.println("ABANDONANDO EL JUEGO...");
                    System.out.println("La partida fue personalizada?");
                    resp= entrada.nextLine();

                    if (resp.equalsIgnoreCase("si")){
                        valores=gestion.crearFicheroPropiedadesPersonalizado();
                    }else {
                        valores=gestion.crearFichero();
                    }
                    int presu= Integer.parseInt(valores[2]);
                    Estacion e= Estacion.valueOf(valores[3].toUpperCase());
                    int diaAct= granja.getDiaActual();
                    //tengo que guardar en el archivo bin el estado del juego
                     granja = new Granja(diaAct,e,presu);
                    GESTION_BINARIO.guardarPartida(granja);
                    break;

            }
        }while(opc!=6);
    }
    public static void nuevaPartida() {
        Scanner entrada= new Scanner(System.in);
        GestionProperties p=new GestionProperties();;
        String resp;
        String[] valores;
        //si hay fichero binario es por que habia partida previa y hay que borrar el bin
        GESTION_BINARIO.eliminarBinarioPartida();
        //preguntamos si quiere personalizar el fichero properties
        System.out.println("¿Quieres personalizar la partida?");
        resp= entrada.nextLine();
        if (resp.equalsIgnoreCase("si")){
            System.out.println("Vamos a personalizar los datos");
           //metodo gestion crearfichero personalizado
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
            RandomAccessFile raf = new RandomAccessFile("resources/huerto.dat", "rw");
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