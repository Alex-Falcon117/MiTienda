package mitienda;

import controller.Con_ComprobarConexion;
import datos_salvados.Style;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.layout.AnchorPane;

public class App extends Application {

    Con_ComprobarConexion con_ComprobarConexion = new Con_ComprobarConexion();
    Style style = new Style();

    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws IOException {
        login(stage);
        //Si esta conectado a la BD la aplicacion iniciara normalmente de lo contrario pedira
        //Que se activen los servicios correspondientes
        if (con_ComprobarConexion.conectado()) {
            login(stage);
        } 
    }
    
    private void login(Stage stage) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        
        AnchorPane root = (AnchorPane) loader.load(getClass().getResource("/mitienda/login.fxml").openStream());
        
        scene = new Scene(root);
        scene.getStylesheets().add(style.estiloSeleccionado()[0]);
        
        stage.setScene(scene);
        stage.setTitle("login");
        stage.getIcons().add(new javafx.scene.image.Image("/icons/icon.png"));
        stage.setResizable(false);
       
        stage.show();
        
//        stage = (Stage) btnIngresar.getScene().getWindow();
//        stage.close();
    
//        scene = new Scene(loadFXML("Login"), 350, 480);
//        scene.getStylesheets().add(style.estiloSeleccionado());
////        scene.getStylesheets().add("style/style.css");
//        
//        stage.setScene(scene);
//        stage.setTitle("Login");
//        stage.getIcons().add(new javafx.scene.image.Image("/icons/icon.png"));
//        stage.setResizable(false);
//        stage.show();
        
    }
    
//    private void sinConexion(Stage stage) throws IOException {
//        scene = new Scene(loadFXML("sin_conexion"), 600, 400);
//        scene.getStylesheets().add(style.estiloSeleccionado());
//        
//        stage.setScene(scene);
//        stage.setTitle("Sin conexion");
//        stage.getIcons().add(new javafx.scene.image.Image("/icons/icon.png"));
//        stage.setResizable(false);
//        stage.show();
//        
//    }
    
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void main(String[] args) {
        launch();
    }
    
}
