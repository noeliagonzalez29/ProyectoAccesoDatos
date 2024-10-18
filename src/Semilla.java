import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Semilla {
    private final static String RUTA_FICH_XML= "resources/semillas.xml";

    private String id;
    private String nombre;
    private List<Estacion> estaciones; //tiene puesto lista pero no sé por qué
    private int diasCrecimiento;
    private int precioCompraSemilla;
    private int precioVentaFruto;
    private int maxFrutos;


    public Semilla(String id, String nombre, List<Estacion>estaciones, int diasCrecimiento, int precioCompraSemilla, int precioVentaFruto, int maxFrutos) {
        this.id = id;
        this.nombre = nombre;
        this.estaciones = estaciones;
        this.diasCrecimiento = diasCrecimiento;
        this.precioCompraSemilla = precioCompraSemilla;
        this.precioVentaFruto = precioVentaFruto;
        this.maxFrutos = maxFrutos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Estacion> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(List<Estacion>estaciones) {
        this.estaciones = estaciones;
    }

    public int getDiasCrecimiento() {
        return diasCrecimiento;
    }

    public void setDiasCrecimiento(int diasCrecimiento) {
        this.diasCrecimiento = diasCrecimiento;
    }

    public int getPrecioCompraSemilla() {
        return precioCompraSemilla;
    }

    public void setPrecioCompraSemilla(int precioCompraSemilla) {
        this.precioCompraSemilla = precioCompraSemilla;
    }

    public int getPrecioVentaFruto() {
        return precioVentaFruto;
    }

    public void setPrecioVentaFruto(int precioVentaFruto) {
        this.precioVentaFruto = precioVentaFruto;
    }

    public int getMaxFrutos() {
        return maxFrutos;
    }

    public void setMaxFrutos(int maxFrutos) {
        this.maxFrutos = maxFrutos;
    }

    public ArrayList<Semilla> leerSemillas(){
        ArrayList<Semilla>lSemilla=new ArrayList<>();
        List<Estacion>estaciones= new ArrayList<>();
        try{
            DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(RUTA_FICH_XML));

            doc.getDocumentElement().normalize();
            NodeList listaSemillas = doc.getElementsByTagName("semilla");

            for (int i=0; i < listaSemillas.getLength(); i++){
                Node nodoSemilla= listaSemillas.item(i);
                nodoSemilla.getAttributes();
                if (nodoSemilla.getNodeType() == Node.ELEMENT_NODE){
                    Element semilla = (Element) nodoSemilla;

                    String id = semilla.getAttribute("id");

                    String nombre = semilla.getElementsByTagName("nombre").item(0).getTextContent();
                    NodeList estacionNodes = semilla.getElementsByTagName("estacion");

                    for (int j = 0; j < estacionNodes.getLength(); j++) {
                        // Obtener el contenido de cada estación
                        String est = estacionNodes.item(j).getTextContent();
                        // Agregar la estación a la lista
                        estaciones.add(Estacion.valueOf(est));
                    }
                    int diaCrecimiento= Integer.parseInt(semilla.getElementsByTagName("diasCrecimiento").item(0).getTextContent());
                    int precioCompraSemilla= Integer.parseInt(semilla.getElementsByTagName("precioCompraSemilla").item(0).getTextContent());
                    int precioVentaFruto= Integer.parseInt(semilla.getElementsByTagName("precioVentaFruto").item(0).getTextContent());
                    int maxFrutos= Integer.parseInt(semilla.getElementsByTagName("maxFrutos").item(0).getTextContent());

                    Semilla s= new Semilla(id, nombre,estaciones,diaCrecimiento,precioCompraSemilla,precioVentaFruto,maxFrutos);
                    lSemilla.add(s);

                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lSemilla;
    }
}
