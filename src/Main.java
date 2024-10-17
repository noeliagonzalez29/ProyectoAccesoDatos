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

    private final static String RUTA_FICHE_BIN= "resources/stardam_valley.bin";
    private final static String RUTA_FICHE_CONFIG="resources/default_config.properties";
    private final static String RUTA_CONFIG_PERSO="resources/personalized_config.properties";
    public static void menuJuego(){
        Scanner entrada= new Scanner(System.in);
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
                    //tengo que guardar en el archivo bin el estado del juego
                    break;

            }
        }while(opc!=6);
    }
    public static void nuevaPartida() {
        Scanner entrada= new Scanner(System.in);
        GestionProperties p=null;
        String resp,fila,columna,presupuesto,estacion,duracion;
        //si hay fichero binario es por que habia partida previa y hay que borrar el bin
        if (Files.exists(Paths.get(RUTA_FICHE_BIN))){
            try {
                Files.delete(Paths.get(RUTA_FICHE_BIN));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //preguntamos si quiere personalizar el fichero properties
        System.out.println("¿Quieres personalizar la partida?");
        resp= entrada.nextLine();
        if (resp.equalsIgnoreCase("si")){
            System.out.println("Vamos a personalizar los datos");
           //metodo gestion crearfichero personalizado
            p.crearFicheroPropiedadesPersonalizado();
                //iniciamos los componente aacorde a la personalizacion
                iniciarComponentes(Integer.parseInt(fila),Integer.parseInt(columna));
                menuJuego();

                //llamar al metodo de la clase Tienda que maneja la generacion de semillas


            } else {
            //llamar a metodo crearfichero clase gestionproperties y al metodo iniciar componentes
            iniciarComponentes(Integer.parseInt(fila),Integer.parseInt(columna));
            menuJuego();

        }

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
        if (Files.exists(Paths.get(RUTA_FICHE_BIN))) {
            try {
                InputStream ArchivoEntrada= Files.newInputStream(Paths.get(RUTA_FICHE_BIN));
                ObjectInputStream flujoEntrada= new ObjectInputStream(ArchivoEntrada);
                Granja granja = (Granja) flujoEntrada.readObject();
                int diaAc = granja.getDiaActual();
                Estacion estacion= granja.getEstacion();
                int presu = granja.getPresupuesto();
                Tienda ti= granja.getT();
                Almacen al = granja.getA();
                HuertoGestion h= granja.getH();

                menuJuego();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }else {
            System.out.println("No existe partida guardada");
        }
    }
    public static void main(String[] args) {
        Scanner entrada= new Scanner(System.in);
        int opcion;
        if (Files.exists(Paths.get(RUTA_FICHE_BIN))) {

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