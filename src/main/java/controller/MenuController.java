
package controller;

import animatefx.animation.SlideInLeft;
import datos_salvados.Style;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MenuController implements Initializable {

    Style style = new Style();

    @FXML
    private BorderPane borderPane;

    @FXML
    private ToggleButton btnCerrarSecion, btnProveedor, btnProductos, btnCientes, btnLibreta, btnEstadistica, btnMiNegocio;

    @FXML
    private VBox vbox;

    @FXML
    private Label lbUsuario;

    @FXML
    private ImageView ivLogo, ivInicio, ivProveedor, ivProducto, ivCliente, ivLibreta, ivEstadistica, ivNegocio, ivCerrar;

    @FXML
    private void onInicio() throws IOException {
        cambioPantalla("inicio");
    }

    @FXML
    private void onProveedor() throws IOException {
        cambioPantalla("proveedores");

    }

    @FXML
    private void onProducto() throws IOException {
        cambioPantalla("producto");

    }

    @FXML
    private void onCliente() throws IOException {
        cambioPantalla("cliente");
    }

    @FXML
    private void onLibreta() throws IOException {
        cambioPantalla("libreta");

    }

    @FXML
    private void onEstadisticas() throws IOException {
        cambioPantalla("estadisticas");

    }

    @FXML
    private void onMiTienda() throws IOException {
        cambioPantalla("mi_negocio");
    }

    @FXML
    private void onCerrarSecion() throws IOException {
        Stage stage = new Stage();

        FXMLLoader fXMLLoader = new FXMLLoader();
        AnchorPane root = (AnchorPane) fXMLLoader.load(getClass().getResource("/mitienda/login.fxml").openStream());

        Scene scene = new Scene(root);
        scene.getStylesheets().add(style.estiloSeleccionado()[0]);

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.getIcons().add(new javafx.scene.image.Image("/icons/icon.png"));
        stage.setResizable(false);
        stage.show();

        stage = (Stage) btnCerrarSecion.getScene().getWindow();
        stage.close();

    }

    private void cambioPantalla(String pantalla) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/mitienda/" + pantalla + ".fxml"));
        root.getStylesheets().add(style.estiloSeleccionado()[0]);
        borderPane.setCenter(root);

    }

    public DropShadow sombra() {
        DropShadow dropShadow = new DropShadow();

        dropShadow.setRadius(5.0);

        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        dropShadow.setSpread(0.0);

        return dropShadow;
    }

    public void recibirUsuario(String usuario, String permisos) {

        lbUsuario.setText(usuario);

        if (!usuario.equals("Administrador")) {
            btnMiNegocio.setDisable(true);
            String[] Permisos = new String[5];

            Permisos = permisos.split(",");

            if (!Permisos[0].equals("Agregar proveedores")) {
                btnProveedor.setDisable(true);
            } else {
                btnProveedor.setDisable(false);
            }
            if (!Permisos[1].equals("Agregar productos")) {
                btnProductos.setDisable(true);
            } else {
                btnProductos.setDisable(false);
            }
            if (!Permisos[2].equals("Agregar clientes")) {
                btnCientes.setDisable(true);
            } else {
                btnCientes.setDisable(false);
            }
            if (!Permisos[3].equals("Libreta")) {
                btnLibreta.setDisable(true);
            } else {
                btnLibreta.setDisable(false);
            }
            if (!Permisos[4].equals("Estadisticas")) {
                btnEstadistica.setDisable(true);
            } else {
                btnEstadistica.setDisable(false);
            }

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ivLogo.setImage(new Image("/icons/mi_negocio_logo.png"));
        ivInicio.setImage(new Image("/icons/inicio.png"));
        ivProveedor.setImage(new Image("/icons/agregar_proveedor.png"));
        ivProducto.setImage(new Image("icons/agregar_producto.png"));
        ivCliente.setImage(new Image("/icons/agregar_cliente.png"));
        ivLibreta.setImage(new Image("/icons/libreta.png"));
        ivEstadistica.setImage(new Image("/icons/estadisticas.png"));
        ivNegocio.setImage(new Image("icons/mi_negocio.png"));
        ivCerrar.setImage(new Image("/icons/cerrar_sesion.png"));

        if (style.estiloSeleccionado()[1].equals("true")) {
            new SlideInLeft(vbox).play();

        }

        // vbox.setEffect(sombra());
        try {
            cambioPantalla("inicio");
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
