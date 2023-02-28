package controller;

import animatefx.animation.SlideInDown;
import connection.ConEmpleados;
import datos_salvados.Style;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    Con_ComprobarConexion Con_ComprobarConexion = new Con_ComprobarConexion();

    Style style = new Style();

    @FXML
    private Pane pane, pane1;

    @FXML
    private Button btnIngresar;

    @FXML
    private ToggleButton tbVer;

    @FXML
    private Label lbContraseña, lbRol, lbUsuarios, lbContrasenaIncorrecta;

    @FXML
    private PasswordField pfContrasena;

    @FXML
    private ImageView ivTipo, ivUsuario, ivContrasena, ivVer, ivLogo;

    @FXML
    private ComboBox cbRol, cbUsuario;

    private String Rol, Usuario, Contrasena;

    private final String contrasenaAdministrador = "Admin";

    private String Permisos;

    private final ObservableList Roles = FXCollections.observableArrayList("Administrador", "Empleado");

    @FXML
    private void onIngresar() throws IOException {

        try {//Comprueba que exista una conexion a la Base de Datos
            if (Con_ComprobarConexion.conectado()) {
                Contrasena = pfContrasena.getText().trim();

                if (Rol.equals("Administrador")) {

                    if (Contrasena.equals(contrasenaAdministrador)) {

                        iniciarMiNegocio("Administrador");
                    } else {
                        lbContrasenaIncorrecta.setVisible(true);
                    }

                } else {
                    String[] login = new String[3];
                    login = ConEmpleados.login(Usuario);

                    if (Usuario.equals(login[0]) && Contrasena.equals(login[1])) {
                        Permisos = login[2];
                        iniciarMiNegocio(Usuario);

                    } else {

                        lbContrasenaIncorrecta.setVisible(true);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    @FXML
    private void onIngresarContrasena() {
        lbContrasenaIncorrecta.setVisible(false);
    }

    @FXML
    private void onVerOcultar() {
        if (tbVer.isSelected()) {
            ivVer.setImage(new Image("/icons/ocultar_contrasena.png"));
            String contrasena = pfContrasena.getText();
            pfContrasena.setVisible(false);
            lbContraseña.setText(contrasena);
            lbContraseña.setVisible(true);
        } else {
            ivVer.setImage(new Image("/icons/ver_contrasena.png"));
            pfContrasena.setVisible(true);
            lbContraseña.setVisible(false);
        }

    }

    @FXML
    private void onRol() throws IOException {

        if (!cbRol.getSelectionModel().isEmpty()) {

            Rol = (String) cbRol.getSelectionModel().getSelectedItem();

            if (Rol.equals("Administrador")) {

                cbUsuario.setDisable(true);

                lbRol.setVisible(false);
                pane1.setDisable(false);
                pfContrasena.setDisable(false);
                pfContrasena.setText("");
                btnIngresar.setDisable(false);
                tbVer.setDisable(false);

            } else {

                cbUsuario.setItems(ConEmpleados.mostrarEmpleados().sorted());
                cbUsuario.setDisable(false);

                cbUsuario.setValue("");
                lbRol.setVisible(false);
                pane1.setDisable(true);
                pfContrasena.setDisable(true);
                pfContrasena.setText("");

            }

        } else {

            lbRol.setVisible(true);
            cbUsuario.setDisable(true);
        }
    }

    @FXML
    private void onUsuario() {

        if (!cbUsuario.getSelectionModel().isEmpty()) {

            Usuario = (String) cbUsuario.getSelectionModel().getSelectedItem();

            activarDesactivar(false);
            pfContrasena.setText("");
            lbContrasenaIncorrecta.setVisible(false);
        } else {
            activarDesactivar(true);
        }

    }

    private void activarDesactivar(boolean estado) {

        pfContrasena.setDisable(estado);
        btnIngresar.setDisable(estado);
        tbVer.setDisable(estado);
        pane1.setDisable(estado);
        lbUsuarios.setVisible(estado);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ivLogo.setImage(new Image("/icons/logo.png"));
        ivTipo.setImage(new Image("/icons/tipo_usuario.png"));
        ivUsuario.setImage(new Image("/icons/usuario.png"));
        ivContrasena.setImage(new Image("/icons/contrasena.png"));
        ivVer.setImage(new Image("/icons/ver_contrasena.png"));
        
        if (style.estiloSeleccionado()[1].equals("true")) {
            new SlideInDown(pane).play();
        }

        cbRol.setItems(Roles);

    }

    private void iniciarMiNegocio(String usuario) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();

        BorderPane root = (BorderPane) loader.load(getClass().getResource("/mitienda/menu.fxml").openStream());

        MenuController menuController = (MenuController) loader.getController();
        menuController.recibirUsuario(usuario, Permisos);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(style.estiloSeleccionado()[0]);

        stage.setScene(scene);
        stage.setTitle("Mi negocio");
        stage.getIcons().add(new javafx.scene.image.Image("/icons/icon.png"));
        stage.setResizable(false);
        stage.show();

        stage = (Stage) btnIngresar.getScene().getWindow();
        stage.close();
    }

}
