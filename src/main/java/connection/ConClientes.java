package connection;

import Model.Clientes;
import controller.Con_ComprobarConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConClientes extends Con_ComprobarConexion {

    public static ObservableList listaID = FXCollections.observableArrayList();
//    private static final String URL = "classes/data_base/mi_tienda_bd.db";
   
    public void insertar(String nombre, String dni, String domicilio, String celular) {
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("insert into clientes values(?,?,?,?,?)");

//            PS.setString(1, "0");
            PS.setString(2, nombre);
            PS.setString(3, dni);
            PS.setString(4, domicilio);
            PS.setString(5, celular);

            PS.executeUpdate();

            CN.close();
            PS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public ObservableList<Clientes> mostrar() {
        ObservableList listaClientes = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select * from clientes");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                String id = RS.getString("ID");
                String nombre = RS.getString("nombre");
                String dni = RS.getString("dni");
                String domicilio = RS.getString("domicilio");
                String celular = RS.getString("celular");

                listaClientes.addAll(new Clientes(id, nombre, dni, domicilio, celular));
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaClientes;
    }

    public boolean eliminar(String id) {
        int ID = Integer.parseInt(id);
        try {

            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("delete from clientes where ID = ?");

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

    public void actualizar(String id, String nombre, String dni, String domicilio, String celular) {
        int ID = Integer.parseInt(id);

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("update clientes set nombre=?, dni=?, domicilio=?, celular=? where ID=" + ID);

            PS.setString(1, nombre);
            PS.setString(2, dni);
            PS.setString(3, domicilio);
            PS.setString(4, celular);

            PS.executeUpdate();

            CN.close();
            PS.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ObservableList buscar(String busqueda) {
        ObservableList lista = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select ID, nombre, dni from clientes where nombre like '%" + busqueda + "%'");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                listaID.add(RS.getString("ID"));
                lista.addAll(RS.getString("nombre") + " | DNI: " + RS.getString("dni"));
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return lista;
    }

    public static String[] obtenerResultado(String resultado) {
        int Resultado = Integer.parseInt(resultado);

        String[] clientes = new String[4];

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select ID, nombre, dni, domicilio from clientes where ID =" + Resultado);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                clientes[0] = RS.getString("ID");
                clientes[1] = RS.getString("nombre");
                clientes[2] = RS.getString("dni");
                clientes[3] = RS.getString("domicilio");
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return clientes;
    }

}
