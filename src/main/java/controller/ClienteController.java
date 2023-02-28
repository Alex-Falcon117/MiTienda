package controller;

import Model.Clientes;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeInDown;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOutDown;
import animatefx.animation.FadeOutUp;
import animatefx.animation.Pulse;
import connection.ConClientes;
import datos_salvados.Datos;
import datos_salvados.Style;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ClienteController implements Initializable {

    Con_ComprobarConexion con_ComprobarConexion = new Con_ComprobarConexion();
    ConClientes conClientes = new ConClientes();
    Datos datos = new Datos();
    Style style = new Style();

    @FXML
    private Pane paneDatos, paneProveedores, paneMensajeEliminar, paneMensaje;

    @FXML
    private ImageView ivDatos, ivClientes;

    @FXML
    private TextField tfNombre, tfDNI, tfDomicilio, tfCelular;

    @FXML
    private TableView<Clientes> tvClientes;

    @FXML
    private TableColumn<Clientes, String> tcID, tcNombre, tcDNI, tcDomicilio, tcCelular;

    @FXML
    private Button btnQuitar, btnEditar;

    @FXML
    private Label lbMensaje, lbMensaje1, lbMensaje2, lbMensaje3, lbMensaje5, lbNombreProveedor;

    private String[] datosRecuperados = new String[5];

    private final Color ROJO = new Color(0.5, 0, 0.1, 1);
    private final Color NEGRO = new Color(0, 0, 0, 1);

    private String ID = " ";
    private String Nombre, DNI, Domicilio, Celular;
    private boolean estadoBoton = true;
    private boolean ActivarAnimaciones;

    //------------Funciones------------------------------------------------------
    @FXML
    private void onGuardar() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            obtenerDatos();
            if (estadoBoton == true && obtenerDatos() == true) {

                conClientes.insertar(Nombre, DNI, Domicilio, Celular);
                limpiarCampos();
                mostrarMensaje("Guardado");
                labelsColor(NEGRO);
                actualizarTabla();
                desactivarBotones(true);
                datos.guardarDatosClientes(" ", " ", " ", " ", " ");

            } else if (estadoBoton == false && obtenerDatos() == true) {
                conClientes.actualizar(ID, Nombre, DNI, Domicilio, Celular);
                mostrarMensaje("Datos actualizados");
                actualizarTabla();
                limpiarCampos();
                labelsColor(NEGRO);
                desactivarBotones(true);
                ID = " ";
                estadoBoton = true;
                datos.guardarDatosClientes(" ", " ", " ", " ", " ");
            }
        }
    }

    @FXML
    private void onCancelar() throws IOException {
        limpiarCampos();
        labelsColor(NEGRO);
        desactivarBotones(true);
        datos.guardarDatosClientes(" ", " ", " ", " ", " ");
        ID = " ";
    }

    @FXML
    private void onEditar() throws IOException {
        labelsColor(NEGRO);
        estadoBoton = false;

        // int item = tvProductos.getSelectionModel().getSelectedIndex();
        ID = tcID.getCellData(getItemTabla());
        Nombre = tcNombre.getCellData(getItemTabla());
        DNI = tcDNI.getCellData(getItemTabla());
        Domicilio = tcDomicilio.getCellData(getItemTabla());
        Celular = tcCelular.getCellData(getItemTabla());

        colocarDatosCampos();
        datos.guardarDatosClientes(ID, Nombre, DNI, Domicilio, Celular);
        if (ActivarAnimaciones) {
            new BounceIn(paneDatos).play();
            
        }
    }

    @FXML
    private void onBorrar() {
        String id = tcID.getCellData(getItemTabla());
        if (!id.equals(ID)) {
            paneMensajeEliminar.setVisible(true);
            mostrarMensajeBorrar();
            desactivarPane(true);
        } else {
            mostrarMensaje("No se puede borrar");
        }
    }

    @FXML
    private void onSi() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            String id = tcID.getCellData(getItemTabla());
            if (conClientes.eliminar(id) == true) {
                actualizarTabla();
                ocultarMensajeBorrar();
                mostrarMensaje("Borrado");
                desactivarBotones(true);
                desactivarPane(false);
            } else {
                mostrarMensaje("No se pudo borrar");
                ocultarMensajeBorrar();
                desactivarPane(false);
            }
        }
    }

    @FXML
    private void onNo() {
        desactivarPane(false);
        ocultarMensajeBorrar();
    }

    @FXML
    private void onClickedTable() {
        if (!tvClientes.getSelectionModel().isEmpty()) {
            desactivarBotones(false);

        } else {
            desactivarBotones(true);
        }

    }

    @FXML
    private void onEditando() throws IOException {
        String nombre = tfNombre.getText().concat(" ");
        String dni = tfDNI.getText().concat(" ");
        String domicilio = tfDomicilio.getText().concat(" ");
        String celular = tfCelular.getText().concat(" ");

        datos.guardarDatosClientes(ID, nombre, dni, domicilio, celular);

    }

    //------------Otros metodos------------------------------------------------
    //Hace la relacion del Modelos con las Columnas de la tabla y coloca los datos
    private void actualizarTabla() throws IOException {
        if (con_ComprobarConexion.conectado()) {

            tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
            tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tcDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
            tcDomicilio.setCellValueFactory(new PropertyValueFactory<>("direccion"));
            tcCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));

            tvClientes.setItems(conClientes.mostrar());

        }
    }

    private boolean obtenerDatos() {

        String nombre = tfNombre.getText().trim();
        String dni = tfDNI.getText().trim();
        String domicilio = tfDomicilio.getText().trim();
        String celular = tfCelular.getText().trim();

        //Vevifica que los campos importantes no esten vacios
        if (!nombre.isEmpty() && !dni.isEmpty() && !domicilio.isEmpty()) {
            //Verifica que no haiga caracteres en los campos que solo aceptan numeros
            Nombre = nombre;
            DNI = dni;
            Domicilio = domicilio;
            Celular = celular;
            return true;

        } else {
            mostrarMensaje("Falta información");
            labelsColor(ROJO);
        }
        return false;
    }

    private void limpiarCampos() {
        tfNombre.setText("");
        tfDNI.setText("");
        tfDomicilio.setText("");
        tfCelular.setText("");
    }

    private void colocarDatosCampos() {
        tfNombre.setText(Nombre);
        tfDNI.setText(DNI);
        tfDomicilio.setText(Domicilio);
        tfCelular.setText(Celular);

    }

    private void mostrarMensaje(String mensaje) {
        lbMensaje5.setText(mensaje);
        new FadeInDown(paneMensaje).playOnFinished(new FadeOutUp(paneMensaje).setDelay(Duration.seconds(1))).play();
        paneMensaje.setVisible(true);
    }

    private void mostrarMensajeBorrar() {
        int item = tvClientes.getSelectionModel().getSelectedIndex();
        String nombre = tcNombre.getCellData(item);
        paneMensajeEliminar.setVisible(true);
        lbNombreProveedor.setText(nombre);

        if (ActivarAnimaciones) {
            new FadeInUp(paneMensajeEliminar).play();
        }

    }

    private void ocultarMensajeBorrar() {
        if (ActivarAnimaciones) {
            new FadeOutDown(paneMensajeEliminar).play();
        } else {
            paneMensajeEliminar.setVisible(false);
        }

    }

    //Cambia de color los Labels
    private void labelsColor(Color color) {
        //Texto
        lbMensaje.setTextFill(color);
        lbMensaje1.setTextFill(color);
        lbMensaje2.setTextFill(color);
        lbMensaje3.setTextFill(color);

    }

    private void desactivarBotones(boolean estado) {
        btnEditar.setDisable(estado);
        btnQuitar.setDisable(estado);
    }

    private void desactivarPane(boolean estado) {
        paneDatos.setDisable(estado);
        paneProveedores.setDisable(estado);

    }

    private int getItemTabla() {
        int item = tvClientes.getSelectionModel().getSelectedIndex();
        return item;
    }

    private void recuperarDatos() {
        int valor = 59166911;
        int valor2 = Arrays.hashCode(datosRecuperados);

        if (!datosRecuperados[0].equals(" ")) {
            ID = datosRecuperados[0].trim();
            estadoBoton = false;
        }
        if (!datosRecuperados[1].isBlank()) {
            tfNombre.setText(datosRecuperados[1].trim());
        }
        if (!datosRecuperados[2].isBlank()) {
            tfDNI.setText(datosRecuperados[2].trim());
        }
        if (!datosRecuperados[3].isBlank()) {
            tfDomicilio.setText(datosRecuperados[3].trim());
        }
        if (!datosRecuperados[4].isBlank()) {
            tfCelular.setText(datosRecuperados[4].trim());
        }
        if (valor != valor2) {
            mostrarMensaje("Hay datos sin guardar");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datosRecuperados = datos.recuperarDatosClientes();
        ActivarAnimaciones = Boolean.parseBoolean(style.estiloSeleccionado()[1]);
        recuperarDatos();
        try {
            actualizarTabla();
        } catch (IOException ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        desactivarBotones(true);

        ivDatos.setImage(new Image("/icons/icon_informacion.png"));
        ivClientes.setImage(new Image("/icons/icon_clientes.png"));

        if (ActivarAnimaciones) {
            new Pulse(paneDatos).play();
            new Pulse(paneProveedores).play();
        }
    }

}
