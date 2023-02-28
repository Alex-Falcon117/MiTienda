package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Sin_conexionController implements Initializable {

    @FXML
    private ImageView iv_conexion;

    @FXML
    private Button btnCerrar;

    @FXML
    private void onReintentar() {
        Stage stage = new Stage();

        stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iv_conexion.setImage(new Image("/icons/bd_sin_coneccion.png"));
    }

}
