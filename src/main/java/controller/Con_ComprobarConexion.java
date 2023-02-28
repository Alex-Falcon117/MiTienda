package controller;

import datos_salvados.Style;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Con_ComprobarConexion {
    
    Style style = new Style();

//    private static final String URL = "jdbc:mysql://localhost/mi_tienda_bd";//MySQL
    
//   public static final String URL = "jdbc:sqlite:classes/data_base/mi_tienda_db.db";//Para compilar
    
    public static final String URL = "jdbc:sqlite:src/main/resources/data_base/mi_tienda_db.db";//Para pruebas
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
     public boolean conectado() throws IOException {
        try {
            Connection CN = DriverManager.getConnection(URL);
            return true;
       
        } catch (SQLException e) {
            System.out.println("No se pudo conectar a la Base de Datos"+e);
            iniciarSinConexion();
            return false;
        }
    }
     
      private void iniciarSinConexion() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        
        AnchorPane root = (AnchorPane) loader.load(getClass().getResource("/mitienda/sin_conexion.fxml").openStream());
        
        Sin_conexionController sin_conexionController = (Sin_conexionController) loader.getController();
//        menuController.recibirUsuario(usuario);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(style.estiloSeleccionado()[0]);
        
        stage.setScene(scene);
        stage.setTitle("Sin conexion");
        stage.getIcons().add(new javafx.scene.image.Image("/icons/icon.png"));
        stage.setResizable(false);
        //stage.initOwner(stage);
        stage.initModality(Modality.APPLICATION_MODAL);
       
        stage.show();
        
//        stage = (Stage) btnIngresar.getScene().getWindow();
//        stage.close();
    }
}
