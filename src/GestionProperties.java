import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
public class GestionProperties {
    private final static String RUTA_FICHE_CONFIG="resources/default_config.properties";
    private final static String RUTA_CONFIG_PERSO="resources/personalized_config.properties";

    public void crearFichero(){
        Properties properties= new Properties();
        try {
            properties.load(new FileInputStream(RUTA_FICHE_CONFIG));
            fila= properties.getProperty("filashuerto");
            columna=properties.getProperty("columnas");
            presupuesto= properties.getProperty("presupuesto");
            estacion= properties.getProperty("estacioninicio");
            duracion= properties.getProperty("duracionestacion");

            //iniciar componentes



            //llamar al metodo de la clase Tienda que maneja la generacion de semillas

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void crearFicheroPropiedadesPersonalizado(){
        Scanner entrada= new Scanner(System.in);
        String fila,columna,presupuesto,estacion,duracion;
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(RUTA_FICHE_CONFIG));
            //ahora modificamos los atributos del properties
            System.out.println("¿Cuántas filas quieres?");
            fila= entrada.nextLine();
            properties.setProperty("filashuerto", fila);
            System.out.println("¿Cuántas columnas quieres?");
            columna= entrada.nextLine();
            properties.setProperty("columnas", columna);
            System.out.println("¿Qué presupuesto tines?");
            presupuesto= entrada.nextLine();
            properties.setProperty("presupuesto", presupuesto);
            System.out.println("¿Estación del año?");
            estacion= entrada.nextLine();
            properties.setProperty("estacioninicio", estacion);
            System.out.println("¿Duración de la estación?");
            duracion= entrada.nextLine();
            properties.setProperty("duracionestacion", duracion);
            //lo guardamos en el personalizado
            properties.store(new FileOutputStream(RUTA_CONFIG_PERSO), "configuracion personalizada");
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
