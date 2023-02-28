package controller;

import Model.Empleados;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeInDown;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOutDown;
import animatefx.animation.FadeOutUp;
import animatefx.animation.Pulse;
import connection.ConEmpleados;
import datos_salvados.Style;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Mi_negocioController implements Initializable {

    Con_ComprobarConexion con_ComprobarConexion = new Con_ComprobarConexion();

    Style style = new Style();

    @FXML
    private MenuButton mbColor;

    @FXML
    private CheckMenuItem cmAgregarProveedores, cmAgregarProductos, cmAgregarClientes, cmLibreta, cmEstadisticas, cmNinguno;

    @FXML
    private RadioMenuItem rmiCeleste, rmiPurpura, rmiMorado, rmiGris, rmiVerde;

    @FXML
    private Pane paneProveedores, paneDatos, paneMes, paneMensaje, paneMensajeEliminar, paneMensajeCerrar;

    @FXML
    private ImageView ivIS, ivAEmpleado, ivEmpleados;

    @FXML
    private TableView<Empleados> tvEmpleados;

    @FXML
    private TableColumn<Empleados, String> tcID, tcNombre, tcCelular, tcDireccion, tcContrasena;

    @FXML
    private TextField tfNombre, tfCelular, tfDireccion, tfContrasena;

    @FXML
    private Label lbNombreEmpleado, lbMensaje5, lbMensaje, lbMensaje1, lbMensaje2, lbMensaje3, lbMensaje8, lbMensaje4;

    @FXML
    private Button btnEditar, btnQuitar, btnSiCerrar, btnGuardarTema;

    @FXML
    private CheckBox cbAnimaciones;

    private boolean estadoBoton = true;

    private String ID = "";
    private String[] datosEmpreados = new String[5];

    private final String CELESTE_STYLE = "style/style_celeste.css";
    private final String VERDE_STYLE = "style/style_verde.css";
    private final String MORADO_STYLE = "style/style_morado.css";
    private final String PURPURA_STYLE = "style/style_purpura.css";
    private final String GRIS_STYLE = "style/style_gris.css";

    private final Color NEGRO = new Color(0, 0, 0, 1);
    private final Color ROJO = new Color(0.5, 0, 0.1, 1);

    private String EstiloNuevo;

    private String AgregarProveedores = " ";
    private String AgregarClientes = " ";
    private String AgregarProductos = " ";
    private String Libreta = " ";
    private String Estadisticas = " ";

    private boolean ActivarAnimaciones;

    //Funciones-----------------------------------------------------------
   
    @FXML
    private void onGuardarTema() {
        paneMensajeCerrar.setVisible(true);
        if (ActivarAnimaciones) {
            new FadeInUp(paneMensajeCerrar).play();
        } else {
            paneMensajeCerrar.setVisible(true);
        }

        desactivarPanes(true);
    }

    //Guarda el style seleccionado y reinicia la aplicacion
    @FXML
    private void onSiCerrar() throws IOException {
        if (cbAnimaciones.isSelected()) {
            style.nuevoEstilo(EstiloNuevo, true);
        } else {
            style.nuevoEstilo(EstiloNuevo, false);
        }
        cerrarSecion();
    }

    //Deja el estilo como esta
    @FXML
    private void onNoCerrar() {
        if (ActivarAnimaciones) {
            new FadeOutDown(paneMensajeCerrar).play();
        } else {
            paneMensajeCerrar.setVisible(false);
        }

        validacion(style.estiloSeleccionado()[0]);
        desactivarPanes(false);
    }

    @FXML
    private void onGuardar() throws IOException {
        if (con_ComprobarConexion.conectado()) {

            if (estadoBoton == true && obtenerDatosTextField() == true) {
                ConEmpleados.insertar(datosEmpreados[0], datosEmpreados[1], datosEmpreados[2], datosEmpreados[3], datosEmpreados[4]);
                actualizarTabla();
                limpiarCampos();
                mostrarMensaje("Guardado");
                labelAsteriscos(NEGRO);
                onValidacionNinguno();

            } else if (estadoBoton == false && obtenerDatosTextField() == true) {

                String id = tcID.getCellData(indiceTabla());

                ConEmpleados.actualizar(ID, datosEmpreados[0], datosEmpreados[1], datosEmpreados[2], datosEmpreados[3], datosEmpreados[4]);
                actualizarTabla();
                limpiarCampos();
                mostrarMensaje("Datos actualizados");
                labelAsteriscos(NEGRO);
                ID = "";
                desactivarBotones(true);
                onValidacionNinguno();

            } else {
                labelAsteriscos(ROJO);
                mostrarMensaje("Falta información");
            }
        }
    }

    @FXML
    private void onCancelar() {
        limpiarCampos();
        labelAsteriscos(NEGRO);
        desactivarBotones(true);
        onValidacionNinguno();
        ID = "";
    }

    @FXML
    private void onEditarDatos() {
        estadoBoton = false;
        indiceTabla();
        obtenerDatosTabla();

        if (ActivarAnimaciones) {
            new BounceIn(paneMes).play();
        }
    }

    @FXML
    private void onBorrar() {
        if (ID.equals(tcID.getCellData(indiceTabla()))) {
            mostrarMensaje("No se puede borrar");
        } else {
            mostrarMensajeBorrar(true);
        }
    }

    @FXML
    private void onSi() throws IOException {
        ConEmpleados.eliminar(tcID.getCellData(indiceTabla()));
        actualizarTabla();
        mostrarMensajeBorrar(false);
        mostrarMensaje("Borrado");
        desactivarBotones(true);
    }

    @FXML
    private void onNo() {
        mostrarMensajeBorrar(false);
    }

    @FXML
    private void onCLickTabla() {
        if (!tvEmpleados.getSelectionModel().isEmpty()) {
            desactivarBotones(false);
        }
    }

    //Menu items
    @FXML
    private void onMenuButon() {
        quitarSeleccionNinguno();
    }

    //Permisos----------------------------------------------------------------
    @FXML
    private void onValidacionAgregarProveedores() {
        //1
        if (cmAgregarProveedores.isSelected()) {
            AgregarProveedores = "Agregar proveedores";
        } else {
            AgregarProveedores = " ";
        }
    }

    @FXML
    private void onValidacionAgregarProductos() {
        if (cmAgregarProductos.isSelected()) {
            AgregarProductos = "Agregar productos";
        } else {
            AgregarProductos = " ";
        }
    }

    @FXML
    private void onValidacionAgregarClientes() {
        if (cmAgregarClientes.isSelected()) {
            AgregarClientes = "Agregar clientes";
        } else {
            AgregarClientes = " ";
        }
    }

    @FXML
    private void onValidacionLibreta() {
        if (cmLibreta.isSelected()) {
            Libreta = "Libreta";
        } else {
            Libreta = " ";
        }
    }

    @FXML
    private void onValidacionEstadisticas() {
        if (cmEstadisticas.isSelected()) {
            Estadisticas = "Estadisticas";
        } else {
            Estadisticas = " ";
        }
    }

    @FXML
    private void onValidacionNinguno() {
        cmAgregarProveedores.setSelected(false);
        cmAgregarClientes.setSelected(false);
        cmAgregarProductos.setSelected(false);
        cmLibreta.setSelected(false);
        cmEstadisticas.setSelected(false);

        AgregarProveedores = " ";
        AgregarProductos = " ";
        AgregarClientes = " ";
        Libreta = " ";
        Estadisticas = " ";

    }

    //Radio menu item de los Colores de la aplicacion--------------------------
    @FXML
    private void onValidacionCeleste() throws IOException {
        EstiloNuevo = CELESTE_STYLE;
        btnGuardarTema.setDisable(false);
    }

    @FXML
    private void onValidacionMorado() throws IOException {
        EstiloNuevo = MORADO_STYLE;
        btnGuardarTema.setDisable(false);
    }

    @FXML
    private void onValidacionPurpura() throws IOException {
        EstiloNuevo = PURPURA_STYLE;
        btnGuardarTema.setDisable(false);
    }

    @FXML
    private void onValidacionGris() throws IOException {
        EstiloNuevo = GRIS_STYLE;
        btnGuardarTema.setDisable(false);
    }

    @FXML
    private void onValidacionVerde() throws IOException {
        EstiloNuevo = VERDE_STYLE;
        btnGuardarTema.setDisable(false);
    }

    @FXML
    private void onActivarDesactivarAnimacion() throws IOException {

        if (cbAnimaciones.isSelected()) {
            style.nuevoEstilo(EstiloNuevo, true);
            mostrarMensaje("Activado");
            ActivarAnimaciones = true;
        } else {
            style.nuevoEstilo(EstiloNuevo, false);
            mostrarMensaje("Desactivado");
            ActivarAnimaciones = false;
        }

    }

    //Otras funciones--------------------------------------------------------------
    private void quitarSeleccionNinguno() {
        if (!cmAgregarProveedores.isSelected() && !cmAgregarClientes.isSelected() && !cmAgregarProductos.isSelected() && !cmLibreta.isSelected() && !cmEstadisticas.isSelected()) {
            cmNinguno.setSelected(true);
        } else {
            cmNinguno.setSelected(false);
        }
    }

    private void actualizarTabla() throws IOException {
        if (con_ComprobarConexion.conectado()) {

            tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
            tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tcCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
            tcDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
            tcContrasena.setCellValueFactory(new PropertyValueFactory<>("contrasena"));

            tvEmpleados.setItems(ConEmpleados.mostrar());
        }
    }

    private void limpiarCampos() {

        tfNombre.setText("");
        tfCelular.setText("");
        tfDireccion.setText("");
        tfContrasena.setText("");
    }

    private boolean obtenerDatosTextField() {
        datosEmpreados[0] = tfNombre.getText().trim();
        datosEmpreados[1] = tfCelular.getText().trim();
        datosEmpreados[2] = tfDireccion.getText().trim();
        datosEmpreados[3] = tfContrasena.getText().trim();
        datosEmpreados[4] = AgregarProveedores + "," + AgregarProductos + "," + AgregarClientes + "," + Libreta + "," + Estadisticas;

        return !datosEmpreados[0].equals("") && !datosEmpreados[1].equals("")
                && !datosEmpreados[2].equals("") && !datosEmpreados[3].equals("") && !datosEmpreados[4].isBlank();
    }

    private void obtenerDatosTabla() {
        ID = tcID.getCellData(indiceTabla());
        tfNombre.setText(tcNombre.getCellData(indiceTabla()));
        tfCelular.setText(tcCelular.getCellData(indiceTabla()));
        tfDireccion.setText(tcDireccion.getCellData(indiceTabla()));
        tfContrasena.setText(tcContrasena.getCellData(indiceTabla()));

        obtenerPermisos();
    }

    private void obtenerPermisos() {

        String[] Permisos = new String[5];

        Permisos = ConEmpleados.mostrarPermisos(ID);

        if (Permisos[0].equals("Agregar proveedores")) {
            cmAgregarProveedores.setSelected(true);
            AgregarProveedores = "Agregar proveedores";
        } else {
            cmAgregarProveedores.setSelected(false);
        }

        if (Permisos[1].equals("Agregar productos")) {
            cmAgregarProductos.setSelected(true);
            AgregarProductos = "Agregar productos";
        } else {
            cmAgregarProductos.setSelected(false);
        }

        if (Permisos[2].equals("Agregar clientes")) {
            cmAgregarClientes.setSelected(true);
            AgregarClientes = "Agregar clientes";
        } else {
            cmAgregarClientes.setSelected(false);
        }

        if (Permisos[3].equals("Libreta")) {
            cmLibreta.setSelected(true);
            Libreta = "Libreta";
        } else {
            cmLibreta.setSelected(false);
        }

        if (Permisos[4].equals("Estadisticas")) {
            cmEstadisticas.setSelected(true);
            Estadisticas = "Estadisticas";
        } else {
            cmEstadisticas.setSelected(false);
        }
    }

    private int indiceTabla() {
        return tvEmpleados.getSelectionModel().getSelectedIndex();
    }

    private void mostrarMensaje(String mensaje) {
        lbMensaje5.setText(mensaje);
        paneMensaje.setVisible(true);
        new FadeInDown(paneMensaje).playOnFinished(new FadeOutUp(paneMensaje).setDelay(Duration.seconds(1))).play();

    }

    private void desactivarBotones(boolean estado) {
        btnEditar.setDisable(estado);
        btnQuitar.setDisable(estado);

    }

    private void labelAsteriscos(Color color) {

        lbMensaje.setTextFill(color);
        lbMensaje1.setTextFill(color);
        lbMensaje2.setTextFill(color);
        lbMensaje3.setTextFill(color);
        lbMensaje4.setTextFill(color);
        lbMensaje8.setTextFill(color);
    }

    private void mostrarMensajeBorrar(boolean estado) {

        if (estado == true) {
            String nombre = tcNombre.getCellData(indiceTabla());

            paneMensajeEliminar.setVisible(estado);

            new FadeInUp(paneMensajeEliminar).play();
            lbNombreEmpleado.setText(nombre);

        } else {
            new FadeOutDown(paneMensajeEliminar).play();
        }
        paneDatos.setDisable(estado);
        paneMes.setDisable(estado);
        paneProveedores.setDisable(estado);
    }

    private void desactivarPanes(boolean estado) {
        paneDatos.setDisable(estado);
        paneMes.setDisable(estado);
        paneProveedores.setDisable(estado);
    }

    private void validacion(String estiloSeleccionado) {

        switch (estiloSeleccionado) {
            case CELESTE_STYLE:
                rmiCeleste.setSelected(true);
                break;
            case MORADO_STYLE:
                rmiMorado.setSelected(true);
                break;
            case PURPURA_STYLE:
                rmiPurpura.setSelected(true);
                break;
            case VERDE_STYLE:
                rmiVerde.setSelected(true);
                break;
            case GRIS_STYLE:
                rmiGris.setSelected(true);
                break;
        }
    }

    private void cerrarSecion() throws IOException {
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

        stage = (Stage) btnSiCerrar.getScene().getWindow();
        stage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validacion(style.estiloSeleccionado()[0]);
        ActivarAnimaciones = Boolean.parseBoolean(style.estiloSeleccionado()[1]);

        if (ActivarAnimaciones) {
            cbAnimaciones.setSelected(true);
            new Pulse(paneDatos).play();
            new Pulse(paneProveedores).play();
            new Pulse(paneMes).play();
        }

        EstiloNuevo = style.estiloSeleccionado()[0];

        try {
            actualizarTabla();
        } catch (IOException ex) {
            Logger.getLogger(Mi_negocioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        desactivarBotones(true);

        ivIS.setImage(new Image("/icons/icon_informacion.png"));
        ivAEmpleado.setImage(new Image("/icons/agregar_cliente.png"));
        ivEmpleados.setImage(new Image("/icons/icon_clientes.png"));

    }

}
