package GestionBasesDatos;

import Clases.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BasesDatos {
    private final static  String RUTA_FICH_CONFB= "src/resources/base.properties";
    private static BasesDatos instancia;
    private Connection connection;

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

    public static BasesDatos getInstancia() {
        if (instancia==null){
            instancia= new BasesDatos();
        }

        return  instancia;
    }
    //Primero la carga de animales
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
                //Alimentos alimento = null;
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
                    producto = new Productos(id_producto,nombreProducto, precioProducto);
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
    public Alimentos cargarAlimentos(){
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return alimentos;
    }
    /*
    public Productos cargarProductos(){
        Productos productos  = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(" SELECT * FROM alimentos");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                //ALIMENTOS
                String nombreP = rs.getString("nombre");
                double preciop = rs.getDouble("precio");
                int cantidadP = rs.getInt("cantidad_disponible");
                productos= new Productos(id_ producto, nombreP, preciop);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productos;
    }
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
   //metodo para actualizar la tabla alimentos
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
    //metodo historial produccion

   //metodo historial consumo. Esto es un insertar
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
//Actualizar tabla productos
    public void actualizarTablaProductos(int id, int cantidad_disponible){

        try {
            String query = "UPDATE productos SET cantidad_disponible= ? WHERE id = ? ";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, cantidad_disponible );
            stmt.setInt(2, id);
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    //Insertar en la tabla Historial_produccion
    public void insertarTablaHistorialProduccion(int id_animal, int cantidad, Timestamp fecha_produccion){
        try {
            String query= " INSERT INTO historialproduccion ( id_animal, cantidad, fecha_produccion) VALUES (?, ?, ? )" ;
            PreparedStatement stmt = connection.prepareStatement( query );

            stmt.setInt(1, id_animal);
            stmt.setInt(2, cantidad);
            stmt.setTimestamp(3, fecha_produccion);
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //metodo transacciones
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


    //leer la cantidad de alimento
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
}

