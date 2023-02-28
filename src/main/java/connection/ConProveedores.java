package connection;

import Model.Proveedores;
import controller.Con_ComprobarConexion;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConProveedores extends Con_ComprobarConexion{

//    private static final String URL = "classes/data_base/mi_tienda_bd.db";

    public static void insertar(String nombre, String cuild, String direccion, String celular, String otraInfo) {
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("insert into proveedores values(?,?,?,?,?,?)");

//            PS.setString(1, "0");
            PS.setString(2, nombre);
            PS.setString(3, cuild);
            PS.setString(4, direccion);
            PS.setString(5, celular);
            PS.setString(6, otraInfo);

            PS.executeUpdate();

            CN.close();
            PS.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ObservableList<Proveedores> mostrar() {
        ObservableList listProveedores = FXCollections.observableArrayList();
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select * from proveedores");

            ResultSet rs = PS.executeQuery();

            while (rs.next()) {
                String id = rs.getString("ID");
                String nombre = rs.getString("nombre");
                String cuild = rs.getString("cuild");
                String direccion = rs.getString("direccion");
                String celular = rs.getString("celular");
                String otraInfo = rs.getString("otraInfo");

                listProveedores.addAll(new Proveedores(id, nombre, cuild, direccion, celular, otraInfo));
            }

            CN.close();
            PS.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listProveedores;

    }

    public static void eliminar(String id) {
        int ID = Integer.parseInt(id);
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("delete from proveedores where ID = ?");

            PS.setInt(1, ID);
            PS.executeUpdate();

            CN.close();
            PS.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public static void actualizar(String id, String nombre, String cuild, String direccion, String celular, String otraInfo) {
        int ID = Integer.parseInt(id);
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("update proveedores set nombre=?, cuild=?, direccion=?, celular=?, otraInfo=? where ID=" + ID);

            PS.setString(1, nombre);
            PS.setString(2, cuild);
            PS.setString(3, direccion);
            PS.setString(4, celular);
            PS.setString(5, otraInfo);

            PS.executeUpdate();

            CN.close();
            PS.close();

        } catch (SQLException e) {
            System.out.println(e);

        }
    }

    public static ObservableList<String> obtenerNombreProveedores() {
        ObservableList listProveedores = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select nombre from proveedores");

            ResultSet rs = PS.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");

                listProveedores.addAll(nombre);
            }

            CN.close();
            PS.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return listProveedores;
    }

}
