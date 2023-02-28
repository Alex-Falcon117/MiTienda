package connection;

import Model.PERDIDAS;
import controller.Con_ComprobarConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConPerdidas extends Con_ComprobarConexion{
    
    private static final DecimalFormat df = new DecimalFormat("###,#00.00");
    
//    private static final String URL = "classes/data_base/mi_tienda_bd.db";

    public static void insertar(String proveedor, String annio, String mes, String dia, String producto, int cantidad, double total) {
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("insert into perdidas values(?,?,?,?,?,?,?,?)");

//            PS.setString(1, "0");
            PS.setString(2, proveedor);
            PS.setString(3, annio);
            PS.setString(4, mes);
            PS.setString(5, dia);
            PS.setString(6, producto);
            PS.setInt(7, cantidad);
            PS.setDouble(8, total);

            PS.executeUpdate();

            CN.close();
            PS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void actualizar(String id, String proveedor, int annio, int mes, int dia, String producto, int cantidad, int total) {

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("update perdidas set proveedor=?, annio=?, mes=?, dia=? producto=?, total=? where ID=" + id);

            PS.setString(1, proveedor);
            PS.setInt(2, annio);
            PS.setInt(3, mes);
            PS.setInt(4, dia);
            PS.setInt(5, total);

            PS.executeUpdate();

            CN.close();
            PS.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ObservableList mostrarMes() {
        ObservableList listaMes = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select mes from perdidas");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                listaMes.add(RS.getString("mes"));
            }

            Set annioSet = new HashSet<>(listaMes);//Quita las palabras duplicadas

            listaMes.clear();
            listaMes.addAll(annioSet);

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaMes;
    }

    public static int mostrarTotalPedidas(int annio, int mes) {
        int total = 0;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select total from perdidas where annio=" + annio + " and mes=" + mes);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                int valor = RS.getInt("total");

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

    public static double mostrarTotalPedidas(int annio) {
        double total = 0;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select total from perdidas where annio=" + annio);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                double valor = Double.parseDouble(RS.getString("total"));

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

    public static ObservableList<PERDIDAS> mostrarDetalle(int annio) {
        ObservableList listaDetalle = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select proveedor, annio, mes, dia, producto, cantidad, total from perdidas where annio=" + annio);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                String proveedor = RS.getString("proveedor");
                String producto = RS.getString("producto");
                String fecha = RS.getString("dia") + "-" + RS.getString("mes") + "-" + RS.getString("annio");
                int cantidad = RS.getInt("cantidad");
                double total = RS.getDouble("total");

                listaDetalle.addAll(new PERDIDAS(proveedor, producto, fecha, cantidad, df.format(total)));

            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaDetalle;
    }

}
