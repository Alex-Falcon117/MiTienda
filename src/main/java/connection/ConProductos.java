package connection;

import Model.Productos;
import controller.Con_ComprobarConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConProductos extends Con_ComprobarConexion{

    private static final DecimalFormat df = new DecimalFormat("###,#00.00");

    public static ObservableList listaID = FXCollections.observableArrayList();
    
//    private static final String URL = "classes/data_base/mi_tienda_bd.db";

    public static void insertar(String proveedor, String marca, String detalles, int cantidad, String pCompra, String pVenta) {
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("insert into productos values(?,?,?,?,?,?,?)");

//            PS.setString(1, "0");
            PS.setString(2, proveedor);
            PS.setString(3, marca);
            PS.setString(4, detalles);
            PS.setInt(5, cantidad);
            PS.setString(6, pCompra);
            PS.setString(7, pVenta);

            PS.executeUpdate();

            CN.close();
            PS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ObservableList<Productos> mostrar() {
        ObservableList listaProductos = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select * from productos");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                String id = RS.getString("ID");
                String proveedor = RS.getString("proveedor");
                String marca = RS.getString("marca");
                String saborAromaColor = RS.getString("detalles");
                String cantidad = RS.getString("cantidad");
                double pCompra = Double.parseDouble(RS.getString("precioCompra"));
                double pVenta = Double.parseDouble(RS.getString("precioVenta"));

                listaProductos.addAll(new Productos(id, proveedor, marca, saborAromaColor, cantidad, df.format(pCompra), df.format(pVenta)));

            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaProductos;
    }

    public static double[] precioCompraVenta(String id) {
        double[] datos = new double[2];

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select 	precioCompra, precioVenta from productos where ID=" + id);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                datos[0] = Double.parseDouble(RS.getString("precioCompra"));
                datos[1] = Double.parseDouble(RS.getString("precioVenta"));

            }
            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return datos;
    }

    public static boolean eliminar(String id) {
        int ID = Integer.parseInt(id);
        try {

            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("delete from productos where ID = ?");

            PS.setInt(1, ID);
            PS.executeUpdate();

            CN.close();
            PS.close();

            return true;

        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    public static void actualizar(String id, String proveedor, String marca, String detalles, int cantidad, String pCompra, String pVenta) {
        int ID = Integer.parseInt(id);

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("update productos set proveedor=?, marca=?, detalles=?, cantidad=?, precioCompra=?, precioVenta=? where ID=" + ID);

            PS.setString(1, proveedor);
            PS.setString(2, marca);
            PS.setString(3, detalles);
            PS.setInt(4, cantidad);
            PS.setString(5, pCompra);
            PS.setString(6, pVenta);

            PS.executeUpdate();

            CN.close();
            PS.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public String[] mostrarBusqueda(String busqueda) {
        String[] productos = new String[5];

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select ID, marca, cantidad, detalles, precioVenta from productos where marca like '%" + busqueda + "%'");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                productos[0] = RS.getString("ID");
                productos[1] = RS.getString("marca");
                productos[2] = RS.getString("cantidad");
                productos[3] = RS.getString("detalles");
                productos[4] = RS.getString("precioVenta");
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return productos;
    }

    public ObservableList buscar(String busqueda) {
        ObservableList lista = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select ID, marca, detalles from productos where marca like '%" + busqueda + "%'");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                listaID.add(RS.getString("ID"));
                lista.addAll(RS.getString("marca") + " | " + RS.getString("detalles"));
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return lista;
    }

    public String[] obtenerResultado(String resultado) {
        int Resultado = Integer.parseInt(resultado);

        String[] productos = new String[5];

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select ID, marca, cantidad, detalles, precioVenta from productos where ID =" + Resultado);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                productos[0] = RS.getString("ID");
                productos[1] = RS.getString("marca");
                productos[2] = RS.getString("cantidad");
                productos[3] = RS.getString("detalles");
                productos[4] = RS.getString("precioVenta");
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return productos;
    }

    public static void actualizarCantidad(String id, int cantidad) {

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("update productos set cantidad=? where ID =" + id);

            PS.setInt(1, cantidad);

            PS.executeUpdate();

            CN.close();
            PS.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public static int actualizarCantidad(String id) {

        int cantidad = 0;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select cantidad from productos where ID =" + id);

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                cantidad = RS.getInt("cantidad");

            }

            CN.close();
            RS.close();
            PS.close();

        } catch (SQLException e) {

            System.out.println(e + " Error aqui");
        }

        return cantidad;
    }

    public static ObservableList sinStock() {
        ObservableList lista = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select marca, detalles from productos where cantidad = 0");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                lista.addAll(RS.getString("marca") + " | " + RS.getString("detalles"));
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return lista;
    }

    public static double obtenerPrecio(String id) {
        double precioVenta = 0;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select precioVenta from productos where ID =" + id);

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                precioVenta = Double.parseDouble(RS.getString("precioVenta"));
                
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return precioVenta;
    }

}
