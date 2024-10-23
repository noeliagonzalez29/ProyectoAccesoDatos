import java.io.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
public class GestionProperties implements Serializable {
    private final static String RUTA_FICHE_CONFIG="src/resources/default_config.properties";
    private final static String RUTA_CONFIG_PERSO="src/resources/personalized_config.properties";
    private transient Properties properties;
    private static final long serialVersionUID = 1L;

    public GestionProperties() {
        properties = new Properties();

    }


    public String[] crearFichero(){
        Properties properties= new Properties();
        String[] valores= new String[5];
        try {
            properties.load(new FileInputStream(RUTA_FICHE_CONFIG));
            valores[0]= properties.getProperty("filashuerto");
            valores[1]=properties.getProperty("columnas");
            valores[2]= properties.getProperty("presupuesto");
            valores[3]= properties.getProperty("estacioninicio");
            valores[4]= properties.getProperty("duracionestacion");

            //iniciar componentes
            //depuracion
            //System.out.println("Valores cargados: " + Arrays.toString(valores));


            //llamar al metodo de la clase Tienda que maneja la generacion de semillas

        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
        return valores;
    }
    public String[] crearFicheroPropiedadesPersonalizado(){
        Scanner entrada= new Scanner(System.in);
        String[] valorP= new String[5];
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(RUTA_FICHE_CONFIG));
            //ahora modificamos los atributos del properties
            System.out.println("¿Cuántas filas quieres?");
            valorP[0]= entrada.nextLine();
            properties.setProperty("filashuerto",  valorP[0]);
            System.out.println("¿Cuántas columnas quieres?");
            valorP[1]= entrada.nextLine();
            properties.setProperty("columnas",  valorP[1]);
            System.out.println("¿Qué presupuesto tines?");
            valorP[2]= entrada.nextLine();
            properties.setProperty("presupuesto",  valorP[2]);
            System.out.println("¿Estación del año?");
            valorP[3]= entrada.nextLine();
            properties.setProperty("estacioninicio",  valorP[3]);
            System.out.println("¿Duración de la estación?");
            valorP[4]= entrada.nextLine();
            properties.setProperty("duracionestacion",  valorP[4]);
            //lo guardamos en el personalizado
            properties.store(new FileOutputStream(RUTA_CONFIG_PERSO), "configuracion personalizada");
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  valorP;
    }


    public String getProperty(String valorProperty) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(RUTA_FICHE_CONFIG));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.getProperty(valorProperty)  ;
    }
    public void cargarPropiedades(boolean personalizar) {
        if (personalizar) {
            crearFicheroPropiedadesPersonalizado(); // Cargar personalizado si el usuario lo desea
        } else {
            crearFichero(); // Cargar propiedades por defecto
        }

    }
}
