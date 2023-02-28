package connection;

import Model.Productos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConTipoProducto {

    private final String URL = "jdbc:mysql://localhost/mi_tienda_bd";
    private final String USER = "root";
    private final String PASSWORD = "";

    public void insertar(String tipoProducto) {
        try {
            Connection CN = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement PS = CN.prepareStatement("insert into tiposproductos values(?,?)");

            PS.setString(1, "0");
            PS.setString(2, tipoProducto);

            PS.executeUpdate();

            CN.close();
            PS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public ObservableList<String> mostrar() {
        ObservableList listaTipos = FXCollections.observableArrayList();

        try {
            Connection CN = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement PS = CN.prepareStatement("select tipoProducto from tiposproductos");

            ResultSet RS = PS.executeQuery();

            while (RS.next()) {
                String tiposProductos = RS.getString("tipoProducto");

                listaTipos.addAll(tiposProductos);

            }

            CN.close();
            PS.close();
            RS.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return listaTipos;
    }

}
