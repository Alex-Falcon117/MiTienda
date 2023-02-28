package controller;

import Model.Proveedores;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeInDown;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOutDown;
import animatefx.animation.FadeOutUp;
import animatefx.animation.Pulse;
import connection.ConProveedores;
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

public class ProveedoresController implements Initializable {

    Con_ComprobarConexion con_ComprobarConexion = new Con_ComprobarConexion();

    Datos datos = new Datos();
    Style style = new Style();

    @FXML
    private Pane paneDatos, paneProveedores, paneMensaje, paneMensajeEliminar;

    @FXML
    private ImageView ivDatos, ivProveedores;

    @FXML
    private TableView<Proveedores> tvProveedores;

    @FXML
    private TableColumn<Proveedores, String> tcID, tcNombre, tcCuild, tcDireccion, tcCelular, tcOtraInfo;

    @FXML
    private Button btnEditar, btnQuitar, btnGuardar;

    @FXML
    private TextField tfNombre, tfCuild, tfDireccion, tfCelular, tfOtraInfo;

    @FXML
    private Label lbMensaje, lbMensaje1, lbMensaje2, lbMensaje3, lbMensaje4, lbMensaje5, lbNombreProveedor;

    private final Color ROJO = new Color(0.5, 0, 0.1, 1);
    private final Color NEGRO = new Color(0, 0, 0, 1);

    private boolean estadoBoton = true;
    private String ID = " ";
    private String Nombre, Cuild, Direccion, Celular, OtraInfo;
    private String[] datosObtenidos = new String[6];
    private boolean ActivarAnimaciones;

    //-------------Funciones---------------------------------------------------
    //El boton guardar tiene dos funciones Guardar o Actualizar los datos en la BD
    @FXML
    private void onGuardar() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            if (obtenerDatosTextField()) {
                if (estadoBoton) {
                    ConProveedores.insertar(Nombre, Cuild, Direccion, Celular, OtraInfo);
                    datos.guardarDatosProveedores(" ", " ", " ", " ", " ", " ");
                    mostrarMensaje("Guardado");
                    actualizarTabla();
                    limpiarCampos();
                    labelColor(NEGRO);

                } else {

                    ConProveedores.actualizar(ID, Nombre, Cuild, Direccion, Celular, OtraInfo);
                    datos.guardarDatosProveedores(" ", " ", " ", " ", " ", " ");

                    mostrarMensaje("Datos actualizados");
                    actualizarTabla();
                    limpiarCampos();
                    labelColor(NEGRO);

                    estadoBoton = true;
                    desactivarBotones(true);
                    ID = " ";
                }
            } else {
                labelColor(ROJO);
                mostrarMensaje("Falta informacion");
            }
        }
    }

    //Borra los datos en la BD si estos no estan siendo usados o actulizados
    @FXML
    private void onBorrar() {
        // int item = tvProveedores.getSelectionModel().getSelectedIndex();
        String id = tcID.getCellData(obtenerItemTabla());

        if (!id.equals(ID)) {
            mostrarMensajeBorrar();
            desactivarPane(true);

        } else {
            mostrarMensaje("No se puede borrar");
        }
    }

    //Cuando se confirma el borrado de datos
    @FXML
    private void onSi() throws IOException {

        //Obtine el idice de la tabla y elimina los datos de la BD
        //int item = tvProveedores.getSelectionModel().getSelectedIndex();
        String id = tcID.getCellData(obtenerItemTabla());

        ConProveedores.eliminar(id);

        actualizarTabla();
        ocultarMensajeBorrar();
        mostrarMensaje("Borrado");
        desactivarBotones(true);

        desactivarPane(false);

    }

    //Cuando se cancela el borrado de datos
    @FXML
    private void onNo() {
        ocultarMensajeBorrar();
        desactivarPane(false);

    }

    @FXML
    private void onEditar() throws IOException {
        labelColor(NEGRO);

        estadoBoton = false;

        //Obtiene los datos de la tabla y los asigna al los TF
        // int item = tvProveedores.getSelectionModel().getSelectedIndex();
        ID = tcID.getCellData(obtenerItemTabla());
        Nombre = tcNombre.getCellData(obtenerItemTabla());
        Cuild = tcCuild.getCellData(obtenerItemTabla());
        Direccion = tcDireccion.getCellData(obtenerItemTabla());
        Celular = tcCelular.getCellData(obtenerItemTabla());
        OtraInfo = tcOtraInfo.getCellData(obtenerItemTabla());

        tfNombre.setText(Nombre);
        tfCuild.setText(Cuild);
        tfDireccion.setText(Direccion);
        tfCelular.setText(Celular);
        tfOtraInfo.setText(OtraInfo);

        //Reproduce una animacion
        if (ActivarAnimaciones) {
            new BounceIn(paneDatos).play();
        }

        comprobarDatos(ID, Nombre, Cuild, Direccion, Celular, OtraInfo);
    }

    @FXML
    private void onCancelar() throws IOException {
        datos.guardarDatosProveedores(" ", " ", " ", " ", " ", " ");
        ID = " ";

        //Limpia los TF para cancelar la operacion
        limpiarCampos();
        desactivarBotones(true);
        labelColor(NEGRO);

        estadoBoton = true;
    }

    //Cuando se selecciona una fila de la tabla
    @FXML
    private void onClickTable() {

        //Activa o desactiva los botones de Editar y Borrar
        if (!tvProveedores.getSelectionModel().isEmpty()) {
            desactivarBotones(false);
        } else {
            desactivarBotones(true);
        }

    }

    //Cuando se esta ingresando informacion a cualquiera de los TextField
    @FXML
    private void onEditando() throws IOException {

        /*Se obtiene la informacion que se este ingresando mas un espacio para 
        evitar un error en caso de que no se ingrese nada*/
        String nombre = tfNombre.getText().concat(" ");
        String cuild = tfCuild.getText().concat(" ");
        String direccion = tfDireccion.getText().concat(" ");
        String celular = tfCelular.getText().concat(" ");
        String otraInfo = tfOtraInfo.getText().concat(" ");

        datos.guardarDatosProveedores(ID, nombre, cuild, direccion, celular, otraInfo);
//        
//        if (!ID.equals(" ")) {
//            datos.guardarDatosProveedores(ID, nombre, cuild, direccion, celular, otraInfo);
//
//        } else {
//            datos.guardarDatosSinGuardar(nombre, cuild, direccion, celular, otraInfo);
//        }
    }

    //-------------------Otras funciones---------------------------------------
    //Vacia los TF
    private void limpiarCampos() {
        tfNombre.setText("");
        tfCuild.setText("");
        tfDireccion.setText("");
        tfCelular.setText("");
        tfOtraInfo.setText("");
    }

    //Carga los datos en la tabla
    private void actualizarTabla() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
            tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tcCuild.setCellValueFactory(new PropertyValueFactory<>("cuild"));
            tcDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
            tcCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
            tcOtraInfo.setCellValueFactory(new PropertyValueFactory<>("otraInfo"));

            tvProveedores.setItems(ConProveedores.mostrar());//Agrega los datos a la tabla
        }
    }

    //Establece un color a los label
    private void labelColor(Color color) {
        lbMensaje.setTextFill(color);
        lbMensaje1.setTextFill(color);
        lbMensaje2.setTextFill(color);
        lbMensaje3.setTextFill(color);
        lbMensaje4.setTextFill(color);
    }

    //Obtiene los datos de los textField y comprueba que los datos importantes no esten vacios
    private boolean obtenerDatosTextField() {
        Nombre = tfNombre.getText().trim();
        Cuild = tfCuild.getText().trim();
        Direccion = tfDireccion.getText().trim();
        Celular = tfCelular.getText().trim();
        OtraInfo = tfOtraInfo.getText().trim();

        return !Nombre.isEmpty() && !Cuild.isEmpty() && !Direccion.isEmpty() && !Celular.isEmpty();
    }

    //Muesta el mensaje de la parte superior de la ventana
    private void mostrarMensaje(String mensaje) {
        paneMensaje.setVisible(true);
        lbMensaje5.setText(mensaje);
        new FadeInDown(paneMensaje).playOnFinished(new FadeOutUp(paneMensaje).setDelay(Duration.seconds(1))).play();

    }

    //Muestra un mensaje en la parte inferior de la ventana
    private void mostrarMensajeBorrar() {
        String nombre = tcNombre.getCellData(obtenerItemTabla());
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

    //Estas finciones activan o desactivan los botones de Editar y Borrar
    private void desactivarBotones(boolean estado) {
        btnQuitar.setDisable(estado);
        btnEditar.setDisable(estado);
    }

    private int obtenerItemTabla() {
        int item = tvProveedores.getSelectionModel().getSelectedIndex();
        return item;
    }

    private void desactivarPane(boolean estado) {
        paneDatos.setDisable(estado);
        paneProveedores.setDisable(estado);
    }

    //Asigna los datos recuperados a los textField correspondientes
    private void datosRecuperados() {
        //Para verificar si esxisten datos que se estan editando y no se guardaros
        int valor2 = 1834174273;
        int valor = Arrays.hashCode(datosObtenidos);

        //Vefifica si anteriormente los datos se estaban editando o ingresando nuevos
        if (!datosObtenidos[0].equals(" ")) {
            estadoBoton = false;
            ID = datosObtenidos[0].trim();

        }

        if (!datosObtenidos[1].isBlank()) {
            tfNombre.setText(datosObtenidos[1].trim());
        }
        if (!datosObtenidos[2].isBlank()) {
            tfCuild.setText(datosObtenidos[2].trim());
        }
        if (!datosObtenidos[3].isBlank()) {
            tfDireccion.setText(datosObtenidos[3].trim());
        }
        if (!datosObtenidos[4].isBlank()) {
            tfCelular.setText(datosObtenidos[4].trim());
        }
        if (!datosObtenidos[5].isBlank()) {
            tfOtraInfo.setText(datosObtenidos[5].trim());
        }

        //En caso de que no se ayan guardado mostrara un mensaje
        if (valor != valor2) {
            mostrarMensaje("Hay datos sin guardar");
        }
    }

    //Guarda los datos que se esten ingresando a los textFied en un archivo de texto
    private void comprobarDatos(String id, String nombre, String cuild, String direccion, String celular, String otraInfo) throws IOException {

        datos.guardarDatosProveedores(id, nombre.concat(" "), cuild.concat(" "), direccion.concat(" "), celular.concat(" "), otraInfo.concat(" "));
    }

    //Marcara con un color la fila de la tabla que se esta editando
//    private void editandoDatos() {
//        if (!datosObtenidos[0].isBlank()) {
//            
//           // Arreglar aqui
////            int indice = tvProveedores.
////            tvProveedores.getSelectionModel().select(indice);
////            System.out.println(indice);
//        }
//    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ActivarAnimaciones = Boolean.parseBoolean(style.estiloSeleccionado()[1]);

        paneMensaje.setVisible(false);
        paneMensajeEliminar.setVisible(false);
        //Recupera los datos que aun no se guardaron en la BD 
        //porque se estan editando o ingresando nuevos
        datosObtenidos = datos.recuperarDatosProveedores();
        datosRecuperados();

        try {
            //Muestra la informacion en la tabla
            actualizarTabla();
        } catch (IOException ex) {
            Logger.getLogger(ProveedoresController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Coloca los iconos
        ivDatos.setImage(new Image("/icons/icon_informacion.png"));
        ivProveedores.setImage(new Image("/icons/icon_proveedor.png"));

        //Reproduce la animacion
        if (ActivarAnimaciones) {
            new Pulse(paneDatos).play();
            new Pulse(paneProveedores).play();

        }
    }
}
