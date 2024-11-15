package GestionBasesDatos;

import Clases.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/**
 * Clase que gestiona la conexión y operaciones con la base de datos de la aplicación.
 * Proporciona métodos para cargar, actualizar y eliminar información relacionada
 * con los animales, alimentos y productos en la granja.
 */
public class BasesDatos implements Serializable {
    private final static  String RUTA_FICH_CONFB= "src/resources/base.properties";
    private static BasesDatos instancia;
    private transient Connection connection;
    private static final long serialVersionUID = 1L;
    /**
     * Constructor de la clase BasesDatos. Establece la conexión a la base de datos
     * utilizando las propiedades de configuración especificadas.
     * @throws RuntimeException si ocurre un error al establecer la conexión.
     */
    public BasesDatos() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(RUTA_FICH_CONFB));
            String dbUrl = properties.getProperty("dbUrl");
            String dbDriver = properties.getProperty("dbDriver");
            String dbUser = properties.getProperty("dbUser");
            String dbPassword = properties.getProperty("dbPassword");

            //primero cargar el driver
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al iniciar la conexion a base de datos" , e);
        }
    }
    /**
     * Devuelve una instancia única de BasesDatos (patrón Singleton).
     * Si la instancia no ha sido creada, la inicializa.
     * @return instancia de BasesDatos
     */
    public static BasesDatos getInstancia() {
        if (instancia==null){
            instancia= new BasesDatos();
        }

        return  instancia;
    }
    /**
     * Carga la lista de animales desde la base de datos.
     * Incluye la información de los productos y alimentos asociados a cada animal.
     * @return Lista de objetos de tipo Animales
     * @throws RuntimeException si ocurre un error al realizar la consulta.
     */
    public List<Animales>cargarAnimales(){
        List<Animales> lAnimales= new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(  "SELECT a.*, " +
                            "al.id as id_alimento, al.nombre as nombre_alimento, al.precio as precio_alimento, al.cantidad_disponible, " +
                            "p.id as id_producto, p.nombre as nombre_producto, p.precio as precio_producto " +
                            "FROM animales a " +
                            "LEFT JOIN alimentos al ON a.id_alimento = al.id " +
                            "LEFT JOIN productos p ON a.id_producto = p.id" );
            ResultSet rs= stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                Anim tipoA = Anim.valueOf(rs.getString("tipo").toUpperCase());
                String nombre = rs.getString("nombre");
                int diaInsercion = rs.getInt("dia_insercion");
                int peso = rs.getInt("peso");
                int id_alimento = rs.getInt("id_alimento");
                int id_producto = rs.getInt("id_producto");
                int cantidadP = rs.getInt("cantidad_disponible");
                Alimentos alimento = null;
                String nombreAlimento = rs.getString("nombre_alimento");
                if(nombreAlimento !=null){
                    double precioAlimento = rs.getDouble("precio_alimento");
                    int cantidadDisponible = rs.getInt("cantidad_disponible");
                    alimento = new Alimentos(id_alimento, nombreAlimento, precioAlimento, cantidadDisponible);
                }

                Productos producto = null;
                String nombreProducto = rs.getString("nombre_producto");
                if(nombreProducto!=null){
                    double precioProducto = rs.getDouble("precio_producto");
                    producto = new Productos(id_producto,nombreProducto, precioProducto, cantidadP);
                }

                Animales a;
                switch (tipoA) {
                    case VACA:
                        peso = rs.getInt("peso");
                        a = new Vacas(id, tipoA, nombre, peso);
                        break;
                    case OVEJA:

                        a = new Ovejas(id, tipoA, nombre);
                        break;

                    case GALLINA:

                        a = new Gallinas(id, tipoA, nombre);
                        break;
                    case CERDO:

                        a = new Cerdos(id, tipoA, nombre);
                        break;
                    default:
                        throw new IllegalArgumentException("Tipo de animal desconocido: " + tipoA);
                }
                if (alimento!=null){
                    a.setA(alimento);
                }
                if (producto!=null){
                    a.setP(producto);
                }
                lAnimales.add(a);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lAnimales;
    }
    /**
     * Carga la lista de alimentos disponibles en la base de datos.
     * @return Lista de objetos de tipo Alimentos
     * @throws RuntimeException si ocurre un error al realizar la consulta.
     */
    public List<Alimentos> cargarAlimentos(){
        List <Alimentos> lAlimentos= new ArrayList<>();
        Alimentos alimentos = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(" SELECT * FROM alimentos");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                //ALIMENTOS
                int id = rs.getInt("id");
                String nombreA = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad_disponible");
                alimentos= new Alimentos(id,nombreA, precio, cantidad);

                lAlimentos.add(alimentos);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lAlimentos;
    }
    /**
     * Carga la lista de productos disponibles en la base de datos.
     * @return Lista de objetos de tipo Productos
     * @throws RuntimeException si ocurre un error al realizar la consulta.
     */
    public List<Productos> cargarProductos(){
       List<Productos>lProductos= new ArrayList<>(); //cambiar a lista
        try {
            PreparedStatement stmt = connection.prepareStatement(" SELECT * FROM productos");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                //ALIMENTOS
                int id= rs.getInt("id");
                String nombreP = rs.getString("nombre");
                double preciop = rs.getDouble("precio");
                int cantidadP = rs.getInt("cantidad_disponible");
             Productos  productos= new Productos(id, nombreP, preciop, cantidadP);
             lProductos.add(productos);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lProductos;
    }

    /**
     * Muestra en consola los nombres y cantidades disponibles de los alimentos.
     * @throws RuntimeException si ocurre un error al realizar la consulta.
     */
    public void alimentos(){
        try {
            PreparedStatement stmt = connection.prepareStatement( " SELECT * FROM alimentos" );
            ResultSet rs = stmt.executeQuery();
            System.out.println("----------------------------------------------------");
            while (rs.next()){
                //que aparezca nombre y cantidad
                String nombre = rs.getString("nombre");
                int cantidad_disponible = rs.getInt("cantidad_disponible");

                System.out.println("|Alimento     " +  nombre + "|");
                System.out.println("|Cantidad  disponible   " + cantidad_disponible + " |");

            }
            System.out.println("---------------------------------------------------");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Muestra en consola los nombres y cantidades disponibles de los productos.
     * @throws RuntimeException si ocurre un error al realizar la consulta.
     */
    public void productos(){
        try {
            PreparedStatement stmt = connection.prepareStatement( " SELECT * FROM productos" );
            ResultSet rs = stmt.executeQuery();
            System.out.println("-----------------------------------------------------");
            while (rs.next()){
                String nombre = rs.getString("nombre");
                int cantidad_disponible = rs.getInt("cantidad_disponible");
                System.out.println("|Producto     " +  nombre + "|");
                System.out.println("|Cantidad disponible    " + cantidad_disponible + " |");


            }
            System.out.println("-----------------------------------------------------");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Actualiza la cantidad disponible de un alimento específico en la base de datos.
     * @param id_alimento ID del alimento a actualizar
     * @param cantidad_disponible Nueva cantidad disponible
     * @throws RuntimeException si ocurre un error al realizar la actualización.
     */
    public void actualizarAlimentos(int id_alimento, int cantidad_disponible){
        try {
            PreparedStatement stmt = connection.prepareStatement( " UPDATE alimentos SET cantidad_disponible = ? WHERE id = ?" );

            stmt.setInt(1, cantidad_disponible);
            stmt.setInt(2, id_alimento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Inserta un registro de consumo de alimento para un animal específico en la base de datos.
     * @param id_animal ID del animal
     * @param cantidad_consumida Cantidad consumida por el animal
     * @param fecha_consumo Fecha del consumo
     * @throws RuntimeException si ocurre un error al realizar la inserción.
     */
    public void inssertarTablaConsumo( int id_animal, int cantidad_consumida, Timestamp fecha_consumo){
        try {
            String query= " INSERT INTO historialconsumo ( id_animal, cantidad_consumida, fecha_consumo) VALUES ( ?, ?, ?)" ;
            PreparedStatement stmt = connection.prepareStatement( query );

            stmt.setInt(1, id_animal);
            stmt.setInt(2, cantidad_consumida);
            stmt.setTimestamp(3, fecha_consumo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Actualiza la cantidad disponible de un producto específico en la base de datos.
     * @param id ID del producto a actualizar
     * @param cantidad_disponible Cantidad a agregar a la cantidad disponible actual
     * @throws RuntimeException si ocurre un error al realizar la actualización.
     */
    public void actualizarTablaProductos(int id, int cantidad_disponible){

        try {
            String query = "UPDATE productos SET  cantidad_disponible = cantidad_disponible + ? WHERE id = ? ";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, cantidad_disponible );
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Inserta un registro de producción de producto en la base de datos.
     * @param id_animal ID del animal que produjo el producto
     * @param cantidad Cantidad de producto producida
     * @param fecha_produccion Fecha de producción
     * @throws RuntimeException si ocurre un error al realizar la inserción.
     */
    public void insertarTablaHistorialProduccion(int id_animal, int cantidad, Timestamp fecha_produccion){
        try {
            String query= " INSERT INTO historialproduccion ( id_animal, cantidad, fecha_produccion) VALUES (?, ?, ? )" ;
            PreparedStatement stmt = connection.prepareStatement( query );

            stmt.setInt(1, id_animal);
            stmt.setInt(2, cantidad);
            stmt.setTimestamp(3, fecha_produccion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Registra una transacción en la base de datos.
     * @param tipoTransaccion Tipo de transacción realizada (compra o venta)
     * @param tipoElemento Tipo de elemento involucrado (producto o alimento)
     * @param precio Precio de la transacción
     * @param fecha Fecha de la transacción
     * @throws RuntimeException si ocurre un error al realizar la inserción.
     */
    public void registrarTablaTransacciones( Tipo_transaccion tipoTransaccion,
                                            Tipo_elemento tipoElemento, double precio, Timestamp fecha){

        try {
            String query = " INSERT INTO transacciones (tipo_transaccion, tipo_elemento, precio, fecha_transaccion) VALUES ( ? , ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement( query );

            // Usamos los enums
            stmt.setString(1, tipoTransaccion.name());  // Convierte el enum a string
            stmt.setString(2, tipoElemento.name());
            stmt.setDouble(3, precio);
            stmt.setTimestamp(4, fecha);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Consulta la cantidad disponible de un alimento específico en la base de datos.
     * @param id ID del alimento
     * @return Cantidad disponible del alimento
     * @throws RuntimeException si ocurre un error al realizar la consulta.
     */
    public int leerCantidadAlimento(int id){
        int cantidad_disponible=0;

        try {
            String query = "SELECT  cantidad_disponible FROM alimentos WHERE id = ? ";
            PreparedStatement stmt = connection.prepareStatement( query );
            stmt.setInt(1, id );
            ResultSet rs= stmt.executeQuery();

            while (rs.next()){
            cantidad_disponible=    rs.getInt( "cantidad_disponible");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cantidad_disponible;
    }


    /**
     * Elimina todos los registros en la tabla de historial de consumo.
     * @throws RuntimeException si ocurre un error al realizar la eliminación.
     */
    public  void eliminarHistoricoConsumo(){

        try {
            String query= "DELETE FROM historialconsumo ";
            PreparedStatement stmt = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Elimina todos los registros en la tabla de historial de producción.
     * @throws RuntimeException si ocurre un error al realizar la eliminación.
     */
    public void eliminarHistoricoProduccion(){
        try {
            String query= "DELETE FROM historialproduccion ";
            PreparedStatement stmt = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Elimina todos los registros en la tabla de transacciones.
     * @throws RuntimeException si ocurre un error al realizar la eliminación.
     */
    public void eliminarTransacciones(){
        try {
            String query= "DELETE FROM transacciones ";
            PreparedStatement stmt = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Establece la cantidad disponible de todos los productos en la base de datos a cero.
     * @throws RuntimeException si ocurre un error al realizar la actualización.
     */
    public void actualizacionEliminacionProductosComienzo(){
        try {
            String query = "UPDATE productos SET  cantidad_disponible = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
           stmt.setInt(1, 0 );

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Restablece la conexión a la base de datos si la conexión actual es nula.
     * Carga las propiedades de configuración y reinicia la conexión.
     * @throws RuntimeException si ocurre un error al restablecer la conexión.
     */
    //restablecer conexion al cargar la partida porque he puesto como transient la connection si no no me dejaba guardar
    public void restablecerConexion(){
        if (this.connection == null) {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(RUTA_FICH_CONFB));
                String dbUrl = properties.getProperty("dbUrl");
                String dbDriver = properties.getProperty("dbDriver");
                String dbUser = properties.getProperty("dbUser");
                String dbPassword = properties.getProperty("dbPassword");

                Class.forName(dbDriver);
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            } catch (IOException | ClassNotFoundException | SQLException e) {
                throw new RuntimeException("Error al reestablecer la conexión a la base de datos", e);
            }
        }
    }
}

