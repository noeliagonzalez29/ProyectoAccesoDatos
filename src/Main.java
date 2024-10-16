import java.nio.file.Files;
import java.util.Scanner;

public class Main {
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner entrada= new Scanner(System.in);
        int opcion;
        if (Files.exists()) {

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