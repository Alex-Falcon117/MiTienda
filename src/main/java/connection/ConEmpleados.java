package connection;

import Model.Empleados;
import controller.Con_ComprobarConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConEmpleados extends Con_ComprobarConexion{
    
//    private static final String URL = "classes/data_base/mi_tienda_bd.db";

    public static void insertar(String nombre, String celular, String direccion, String contracena, String permisos) {
        try {
            Connection CN = DriverManager.getConnection(URL);

            PreparedStatement PS = CN.prepareStatement("insert into empleados values(?,?,?,?,?,?)");

//            PS.setString(1, "0");
            PS.setString(2, nombre);
            PS.setString(3, celular);
            PS.setString(4, direccion);
            PS.setString(5, contracena);
            PS.setString(6, permisos);

            PS.executeUpdate();

            CN.close();
            PS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ObservableList<Empleados> mostrar() {
        ObservableList listaEmpleados = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select * from empleados");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                String id = RS.getString("ID");
                String nombre = RS.getString("nombre");
                String celular = RS.getString("celular");
                String direccion = RS.getString("direccion");
                String contrasena = RS.getString("contrasena");

                listaEmpleados.addAll(new Empleados(id, nombre, celular, direccion, contrasena));

            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaEmpleados;
    }

    public static boolean eliminar(String id) {
        try {

            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("delete from empleados where ID =" + id);

            PS.executeUpdate();

            CN.close();
            PS.close();

            return true;

        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    public static void actualizar(String id, String nombre, String celular, String direccion, String contracena, String permisos) {

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("update empleados set nombre=?, celular=?, direccion=?, contrasena=?, permisos=? where ID=" + id);

            PS.setString(1, nombre);
            PS.setString(2, celular);
            PS.setString(3, direccion);
            PS.setString(4, contracena);
            PS.setString(5, permisos);

            PS.executeUpdate();

            CN.close();
            PS.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ObservableList<String> mostrarRol() {
        ObservableList listaRol = FXCollections.observableArrayList("Administrador");

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select rol from empleados");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                listaRol.add(RS.getString("rol"));
            }

            Set duplicados = new HashSet<>(listaRol);//Quita las palabras duplicadas

            listaRol.clear();
            listaRol.addAll(duplicados);

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);

        }

        return listaRol;
    }

    public static String[] mostrarPermisos(String id) {

        String[] Permisos = new String[5];
        String permisos;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select permisos from empleados where ID = " + id);

            ResultSet RS = PS.executeQuery();
            if (RS.next()) {

                permisos = RS.getString("permisos");
                Permisos = permisos.split(",");
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            Permisos[0] = " ";
            Permisos[1] = " ";
            Permisos[2] = " ";
            Permisos[3] = " ";
            Permisos[4] = " ";

            System.out.println("Error en funcion mostrarPermisos " + e);

        }

        return Permisos;
    }

    public static ObservableList<String> mostrarEmpleados() {
        ObservableList listaEmpleados = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select nombre from empleados");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                listaEmpleados.addAll(RS.getString("nombre"));
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println("No se puede obtener empleados "+e);
        }

        return listaEmpleados;
    }

    public static String[] login(String usuario) {
        String[] login = new String[3];

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select nombre, contrasena, permisos from empleados where nombre=" + "'" + usuario + "'");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                login[0] = RS.getString("nombre");
                login[1] = RS.getString("contrasena");
                login[2] = RS.getString("permisos");
            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return login;
    }

}
