package connection;

import Model.Estadistica;
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

public class ConEstadistica extends Con_ComprobarConexion{

    private static final DecimalFormat df = new DecimalFormat("###,#00.00");
    
//    private static final String URL = "classes/data_base/mi_tienda_bd.db";

    public static void insertar(String producto, int annio, int mes, int dia, int cantidad, String total) {
        System.out.println(total);
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("insert into estadisticas values(?,?,?,?,?,?,?)");

//            PS.setString(1, "");
            PS.setString(2, producto);
            PS.setInt(3, annio);
            PS.setInt(4, mes);
            PS.setInt(5, dia);
            PS.setInt(6, cantidad);
            PS.setString(7, total);

            PS.executeUpdate();

            CN.close();
            PS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

//    public static ObservableList<Estadistica> mostrar() {
//        ObservableList listaEstadistica = FXCollections.observableArrayList();
//
//        try {
//            Connection CN = DriverManager.getConnection(URL, USER, PASSWORD);
//            PreparedStatement PS = CN.prepareStatement("select * from estadisticas");
//
//            ResultSet RS = PS.executeQuery();
//
//            while (RS.next()) {
//                String id = RS.getString("ID");
//                String annio = RS.getString("annio");
//                String mes = RS.getString("mes");
//                String dia = RS.getString("dia");
//                int total = RS.getInt("total");
//
//                listaEstadistica.addAll(new Estadistica(id, annio, mes, dia, total));
//
//            }
//
//            CN.close();
//            PS.close();
//            RS.close();
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//
//        return listaEstadistica;
//    }
    public static void actualizar(String id, int annio, int mes, int dia, String total) {

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("update estadisticas set annio=?, mes=?, dia=? where ID=" + id);

            PS.setInt(1, annio);
            PS.setInt(2, mes);
            PS.setInt(3, dia);
            PS.setString(4, total);

            PS.executeUpdate();

            CN.close();
            PS.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static double mostrarGanaciaActual(int annio, int mes, int dia) {
        double total = 0;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select total from estadisticas where annio=" + annio + " and mes=" + mes + " and dia=" + dia);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                double valor = RS.getDouble("total");

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

    public static ObservableList<Estadistica> mostrarTotalAnnio() {
        ObservableList listaAnnio = FXCollections.observableArrayList();

        int totalAnnio = 0;
        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select annio, total from estadisticas");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                listaAnnio.add(RS.getString("annio"));

                int valor = RS.getInt("total");
                totalAnnio = totalAnnio + valor;
            }

            Set annioSet = new HashSet<>(listaAnnio);//Quita las palabras duplicadas

            listaAnnio.clear();
            listaAnnio.addAll(new Estadistica(annioSet, totalAnnio + ""));

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaAnnio;
    }

    public static ObservableList mostrarMes(String annio) {
        ObservableList listaMes = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select mes from estadisticas where annio=" + annio);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                listaMes.add(RS.getString("mes"));
            }

            Set mesSet = new HashSet<>(listaMes);//Quita las palabras duplicadas

            listaMes.clear();
            listaMes.addAll(mesSet);

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaMes;
    }

    public static int mostrarTotal(int annio, int mes) {
        int total = 0;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select total from estadisticas where annio=" + annio + " and mes=" + mes);
            

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

    public static double mostrarTotal(int annio) {
        double total = 0;

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select total from estadisticas where annio=" + annio);

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

    public static ObservableList mostrarAnnio() {
        ObservableList listaAnnio = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select annio from estadisticas");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                listaAnnio.add(RS.getString("annio"));

            }

            Set annioSet = new HashSet<>(listaAnnio);//Quita las palabras duplicadas

            listaAnnio.clear();
            listaAnnio.addAll(annioSet);

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaAnnio;
    }

    public static ObservableList<Estadistica> mostrarDetalle(int annio) {
        ObservableList listaDetalle = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL);
            PreparedStatement PS = CN.prepareStatement("select producto, annio, mes, dia, cantidad, total from estadisticas where annio=" + annio);

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {

                String producto = RS.getString("producto");
                String fecha = RS.getString("dia") + "-" + RS.getString("mes") + "-" + RS.getString("annio");
                String cantidad = RS.getString("cantidad");
                double total = RS.getDouble("total");

                if (cantidad.equals("0")) {
                    listaDetalle.addAll(new Estadistica(producto, fecha, "Venta Libre", df.format(total)));
                } else {
                    listaDetalle.addAll(new Estadistica(producto, fecha, cantidad, df.format(total)));

                }
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
