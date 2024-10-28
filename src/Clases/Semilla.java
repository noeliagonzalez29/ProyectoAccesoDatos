package Clases;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una semilla en el juego, que incluye información sobre su crecimiento,
 * precio y las estaciones en las que puede ser cultivada.
 * <p>
 * La clase permite leer semillas desde un archivo XML y buscar una semilla por su ID.
 */
public class Semilla implements Serializable {
    private final static String RUTA_FICH_XML = "src/resources/semillas.xml";
    private int id;
    private String nombre;
    private List<Estacion> estaciones;
    private int diasCrecimiento;
    private int precioCompraSemilla;
    private int precioVentaFruto;
    private int maxFrutos;
    private ArrayList<Semilla> lSemilla;

    /**
     * Constructor que inicializa una nueva instancia de Clases.Semilla con los parámetros especificados.
     *
     * @param id                  El identificador de la semilla.
     * @param nombre              El nombre de la semilla.
     * @param estaciones          La lista de estaciones en las que se puede cultivar la semilla.
     * @param diasCrecimiento     El número de días que tarda en crecer la semilla.
     * @param precioCompraSemilla El precio de compra de la semilla.
     * @param precioVentaFruto    El precio de venta del fruto de la semilla.
     * @param maxFrutos           El número máximo de frutos que puede producir la semilla.
     */
    public Semilla(int id, String nombre, List<Estacion> estaciones, int diasCrecimiento, int precioCompraSemilla, int precioVentaFruto, int maxFrutos) {
        this.id = id;
        this.nombre = nombre;
        this.estaciones = estaciones;
        this.diasCrecimiento = diasCrecimiento;
        this.precioCompraSemilla = precioCompraSemilla;
        this.precioVentaFruto = precioVentaFruto;
        this.maxFrutos = maxFrutos;
    }

    public Semilla() {
        this.lSemilla = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Estacion> getEstaciones() {
        return estaciones;
    }

    public int getDiasCrecimiento() {
        return diasCrecimiento;
    }

    public int getPrecioCompraSemilla() {
        return precioCompraSemilla;
    }

    public int getPrecioVentaFruto() {
        return precioVentaFruto;
    }

    public int getMaxFrutos() {
        return maxFrutos;
    }

    /**
     * Lee las semillas desde el archivo XML especificado y las almacena en la lista de semillas.
     *
     * @return La lista de semillas leídas del archivo XML.
     */
    public ArrayList<Semilla> leerSemillas() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(RUTA_FICH_XML));

            doc.getDocumentElement().normalize();
            NodeList listaSemillas = doc.getElementsByTagName("semilla");

            for (int i = 0; i < listaSemillas.getLength(); i++) {
                Node nodoSemilla = listaSemillas.item(i);
                nodoSemilla.getAttributes();
                if (nodoSemilla.getNodeType() == Node.ELEMENT_NODE) {
                    Element semilla = (Element) nodoSemilla;

                    int id = Integer.parseInt(semilla.getAttribute("id"));

                    String nombre = semilla.getElementsByTagName("nombre").item(0).getTextContent();
                    String estacion = semilla.getElementsByTagName("estacion").item(0).getTextContent();
                    List<Estacion> estaciones = Estacion.estacionesSeparadas(estacion);

                    int diaCrecimiento = Integer.parseInt(semilla.getElementsByTagName("diasCrecimiento").item(0).getTextContent());
                    int precioCompraSemilla = Integer.parseInt(semilla.getElementsByTagName("precioCompraSemilla").item(0).getTextContent());
                    int precioVentaFruto = Integer.parseInt(semilla.getElementsByTagName("precioVentaFruto").item(0).getTextContent());
                    int maxFrutos = Integer.parseInt(semilla.getElementsByTagName("maxFrutos").item(0).getTextContent());

                    Semilla s = new Semilla(id, nombre, estaciones, diaCrecimiento, precioCompraSemilla, precioVentaFruto, maxFrutos);
                    lSemilla.add(s);

                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lSemilla;
    }

    /**
     * Busca una semilla en la lista de semillas por su identificador.
     *
     * @param id El identificador de la semilla a buscar.
     * @return La semilla correspondiente al identificador, o null si no se encuentra.
     */
    public Semilla buscarSemillaPorId(int id) {
        if (lSemilla == null || lSemilla.isEmpty()) {
            leerSemillas();
        }
        for (Semilla semilla : lSemilla) {
            if (id == semilla.getId()) {
                return semilla;

            }
        }
        return null;
    }

    /**
     * Compara este objeto con otro para determinar si son iguales.
     * <p>
     * Dos objetos son considerados iguales si tienen el mismo id.
     *
     * @param o El objeto a comparar.
     * @return true si los objetos son iguales, false de lo contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Semilla semilla = (Semilla) o;
        return id == semilla.id;  // Comparar basándose en el id
    }

}
