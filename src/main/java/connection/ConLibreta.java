package connection;

import Model.Libreta;
import controller.Con_ComprobarConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConLibreta extends Con_ComprobarConexion{
    
//    private static final String URL = "classes/data_base/mi_tienda_bd.db";

    public static void insertar(String idCliente, String producto, int cantidad, String detalle, int dia, int mes, int annio, String precioProducto) {
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("insert into libreta values(?,?,?,?,?,?,?,?,?)");

//            PS.setString(1, "0");
            PS.setString(2, idCliente);
            PS.setString(3, producto);
            PS.setInt(4, cantidad);
            PS.setString(5, detalle);
            PS.setInt(6, dia);
            PS.setInt(7, mes);
            PS.setInt(8, annio);
            PS.setString(9, precioProducto);

            PS.executeUpdate();

            CN.close();
            PS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ObservableList<Libreta> mostrar(String idCliente) {
        DecimalFormat df = new DecimalFormat("###,###,###.00");

        ObservableList listaLibreta = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select * from libreta where idCliente = " + idCliente);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                String id = RS.getString("ID");
                String idClientes = RS.getString("idCliente");
                String producto = RS.getString("producto");
                String cantidad = RS.getString("cantidad");
                String detalle = RS.getString("detalle");
                String dia = RS.getString("dia");
                String mes = RS.getString("mes");
                String annio = RS.getString("annio");
                double pProducto = RS.getDouble("precioProducto");

                String fechaCompra = dia + "-" + mes + "-" + annio; //Une el dia, mes y año
                if (cantidad.equals("0")) {
                    listaLibreta.addAll(new Libreta(id, idClientes, producto, "Venta libre", detalle, fechaCompra, df.format(pProducto)));
                } else {

                    listaLibreta.addAll(new Libreta(id, idClientes, producto, cantidad, detalle, fechaCompra, df.format(pProducto)));
                }
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaLibreta;
    }

    public static boolean eliminar(String id) {

        try {

            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("delete from libreta where ID = " + id);

            PS.executeUpdate();

            CN.close();
            PS.close();

            return true;

        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    public static boolean eliminarTodo(String id) {
        try {

            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("delete from libreta where idCliente =" + id);

            PS.executeUpdate();

            CN.close();
            PS.close();

            return true;

        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    public static double mostrarTotal(String idCliente) {
        double total = 0;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select precioProducto from libreta where idCliente = " + idCliente);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                double pProducto;

                double valor = Double.parseDouble(RS.getString("precioProducto"));

                total = total + valor;

            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return total;
    }

    public static double mostrarPrecio(String idCliente, String idProducto) {
        double pProducto = 0;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select precioProducto from libreta where idCliente = " + idCliente+" and ID="+idProducto);

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                pProducto = RS.getDouble("precioProducto");

            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return pProducto;
    }

}
