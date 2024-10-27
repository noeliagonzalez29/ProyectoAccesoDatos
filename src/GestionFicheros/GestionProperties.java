package GestionFicheros;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * La clase {@code GestionFicheros.GestionProperties} maneja la configuración de propiedades de la aplicación
 * mediante el uso de archivos de propiedades, soportando tanto un archivo de configuración por defecto
 * como un archivo personalizado.
 * <p>
 * Implementa el patrón Singleton para garantizar que solo exista una instancia de esta clase.
 */
public class GestionProperties implements Serializable {
    /**
     * Ruta del archivo de configuración por defecto.
     */
    private final static String RUTA_FICHE_CONFIG = "src/resources/default_config.properties";
    /**
     * Ruta del archivo de configuración personalizado.
     */
    private final static String RUTA_CONFIG_PERSO = "src/resources/personalized_config.properties";
    /**
     * Instancia única de la clase para implementar el patrón Singleton.
     */
    private static GestionProperties instancia;
    /**
     * Objeto Properties para manejar la carga y almacenamiento de configuraciones.
     */
    private transient Properties properties;

    private static final long serialVersionUID = 1L;

    /**
     * Constructor privado para evitar la creación de instancias externas.
     */
    private GestionProperties() {
        properties = new Properties();
    }

    /**
     * Obtiene la instancia única de {@code GestionFicheros.GestionProperties}. Si aún no se ha creado,
     * se instancia en este momento.
     *
     * @return La instancia única de {@code GestionFicheros.GestionProperties}.
     */
    public static GestionProperties getInstancia() {
        if (instancia == null) {
            instancia = new GestionProperties();
        }

        return instancia;
    }

    /**
     * Elimina el archivo de configuración personalizado si existe.
     *
     * @throws RuntimeException si ocurre un error durante la eliminación del archivo.
     */
    public void eliminarConfiguracionPersonalizada() {
        File archivoPersonalizado = new File(RUTA_CONFIG_PERSO);
        if (archivoPersonalizado.exists()) {
            archivoPersonalizado.delete();
        }
    }

    /**
     * Carga las propiedades desde el archivo de configuración por defecto.
     *
     * @return Un arreglo de cadenas que contiene los valores de las propiedades.
     * @throws RuntimeException si ocurre un error al cargar el archivo de configuración.
     */
    public String[] cargarPorDefecto() {
        Properties properties = new Properties();
        String[] valores = new String[5];

        try {
            properties.load(new FileInputStream(RUTA_FICHE_CONFIG));
            valores[0] = properties.getProperty("filashuerto");
            valores[1] = properties.getProperty("columnas");
            valores[2] = properties.getProperty("presupuesto");
            valores[3] = properties.getProperty("estacioninicio");
            valores[4] = properties.getProperty("duracionestacion");

        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
        return valores;
    }

    /**
     * Carga las propiedades desde el archivo de configuración personalizado.
     *
     * @return Un arreglo de cadenas que contiene los valores de las propiedades personalizadas.
     * @throws RuntimeException si ocurre un error al cargar el archivo de configuración personalizado.
     */
    public String[] cargarFicheroPersonalizado() {
        Properties properties = new Properties();
        String[] valores = new String[5];

        try {
            properties.load(new FileInputStream(RUTA_CONFIG_PERSO));
            valores[0] = properties.getProperty("filashuerto");
            valores[1] = properties.getProperty("columnas");
            valores[2] = properties.getProperty("presupuesto");
            valores[3] = properties.getProperty("estacioninicio");
            valores[4] = properties.getProperty("duracionestacion");

        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
        return valores;
    }

    /**
     * Crea un archivo de configuración personalizado solicitando al usuario valores para cada propiedad.
     *
     * @return Un arreglo de cadenas que contiene los valores personalizados de las propiedades.
     * @throws RuntimeException si ocurre un error al guardar el archivo de configuración personalizado.
     */
    public String[] crearFicheroPropiedadesPersonalizado() {
        Scanner entrada = new Scanner(System.in);
        File archivoPersonalizado = new File(RUTA_CONFIG_PERSO);
        String[] valorP = new String[5];
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(RUTA_FICHE_CONFIG));
            //ahora modificamos los atributos del properties
            System.out.println("¿Cuántas filas quieres?");
            valorP[0] = entrada.nextLine();
            properties.setProperty("filashuerto", valorP[0]);
            System.out.println("¿Cuántas columnas quieres?");
            valorP[1] = entrada.nextLine();
            properties.setProperty("columnas", valorP[1]);
            System.out.println("¿Qué presupuesto tines?");
            valorP[2] = entrada.nextLine();
            properties.setProperty("presupuesto", valorP[2]);
            System.out.println("¿Estación del año?");
            valorP[3] = entrada.nextLine().toUpperCase();
            properties.setProperty("estacioninicio", valorP[3]);
            System.out.println("¿Duración de la estación?");
            valorP[4] = entrada.nextLine();
            properties.setProperty("duracionestacion", valorP[4]);
            //lo guardamos en el personalizado
            properties.store(new FileOutputStream(RUTA_CONFIG_PERSO), "configuracion personalizada");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return valorP;
    }

    /**
     * Obtiene el valor de una propiedad específica del archivo de configuración.
     * Si el archivo personalizado existe, se toma de allí; de lo contrario, se toma del archivo por defecto.
     *
     * @param valorProperty La clave de la propiedad a obtener.
     * @return El valor de la propiedad especificada o una cadena vacía si no se encuentra.
     */

    public String getProperty(String valorProperty) {
        Properties properties = new Properties();
        File archivoPersonalizado = new File(RUTA_CONFIG_PERSO);
        String valor = "";

        try {
            if (archivoPersonalizado.exists()) {
                properties.load(new FileInputStream(RUTA_CONFIG_PERSO));
                valor = properties.getProperty(valorProperty);
                return valor;
            }
            // Si no existe el archivo personalizado o la propiedad no se encuentra,
            // cargamos el archivo por defecto
            // properties.clear();
            properties.load(new FileInputStream(RUTA_FICHE_CONFIG));
            valor = properties.getProperty(valorProperty);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return valor;
    }
}
