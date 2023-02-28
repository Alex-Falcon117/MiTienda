package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConCarrito {

    private final String URL = "jdbc:mysql://localhost/mi_tienda_bd";
    private final String USER = "root";
    private final String PASSWORD = "";

    public void insertar(String nombre, String cuild, String direccion, String celular, String otraInfo) {
        try {
            Connection CN = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement PS = CN.prepareStatement("insert into proveedores values(?,?,?,?,?,?)");

            PS.setString(1, "0");
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

}
