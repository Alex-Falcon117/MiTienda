package controller;

import Model.Productos;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeInDown;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOutDown;
import animatefx.animation.FadeOutUp;
import animatefx.animation.Pulse;
import connection.ConPerdidas;
import connection.ConProductos;
import connection.ConProveedores;
import datos_salvados.Datos;
import datos_salvados.Style;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class ProductoController implements Initializable {

    Con_ComprobarConexion con_ComprobarConexion = new Con_ComprobarConexion();

    Datos datos = new Datos();
    Style style = new Style();

    @FXML
    private Pane paneProveedores, paneDatos, paneMensaje, paneMensajeEliminar, paneMensajeAgregar;

    @FXML
    private ImageView ivDatos, ivProductos;

    @FXML
    private ComboBox cbProbeedor;

    @FXML
    private TextField tfMarca, tfPropiedades, tfCantidad, tfPCompra, tfPVenta;

    @FXML
    private Button btnEditar, btnQuitar;

    @FXML
    private Label lbMensaje, lbMensaje1, lbMensaje2, lbMensaje3, lbMensaje5, lbMensaje6, lbMensaje7, lbMensaje8, lbNombreProveedor, lbProveedor;

    @FXML
    private TableView<Productos> tvProductos;

    @FXML
    private TableColumn<Productos, String> tcID, tcProveedor, tcNombre, tcPropiedades, tcCantidad, tcPCompra, tcPVenta;

    private String[] datosRecuperados = new String[8];

    private final Color ROJO = new Color(0.5, 0, 0.1, 1);
    private final Color NEGRO = new Color(0, 0, 0, 1);

    private String ID = " ";
    private String Proveedor, NombreMarca, Propiedades;
    private String Dia, Mes, Annio;
    private int Cantidad, ItemTabla;
    private double PrecioCompra, PrecioVenta;
    private boolean estadoBoton = true;

    private boolean ActivarAnimacion;

    //------------Funciones------------------------------------------------------
    @FXML
    private void onGuardar() throws IOException {
        if (con_ComprobarConexion.conectado()) {

//            obtenerDatos();
            if (estadoBoton == true && obtenerDatos() == true) {//Cuando se guarda

                ConProductos.insertar(Proveedor, NombreMarca, Propiedades, Cantidad, PrecioCompra + "", PrecioVenta + "");
                perdidas();
                limpiarCampos();
                mostrarMensaje("Guardado");
                labelsColor(NEGRO);
                actualizarTabla();
                desactivarBotones(true);
                datos.guardarDatosProductos(" ", " ", " ", " ", " ", " ", " ");
                ID = " ";

            } else if (estadoBoton == false && obtenerDatos() == true) {//Cuando se edita
                ConProductos.actualizar(ID, Proveedor, NombreMarca, Propiedades, Cantidad, PrecioCompra + "", PrecioVenta + "");
                perdidas();
                mostrarMensaje("Datos actualizados");
                actualizarTabla();
                //cambiarColorCelda();
                limpiarCampos();
                labelsColor(NEGRO);
                desactivarBotones(true);

                datos.guardarDatosProductos(" ", " ", " ", " ", " ", " ", " ");
                estadoBoton = true;
                ID = " ";
            }
        }
    }

    @FXML
    private void onCancelar() throws IOException {
        limpiarCampos();
        labelsColor(NEGRO);
        desactivarBotones(true);
        datos.guardarDatosProductos(" ", " ", " ", " ", " ", " ", " ");
        ID = " ";
    }

    @FXML
    private void onEditar() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            labelsColor(NEGRO);
            estadoBoton = false;
            itemTabla();

            ID = tcID.getCellData(getItemTabla());
            Proveedor = tcProveedor.getCellData(getItemTabla());
            NombreMarca = tcNombre.getCellData(getItemTabla());
            Propiedades = tcPropiedades.getCellData(getItemTabla());
            Cantidad = Integer.parseInt(tcCantidad.getCellData(getItemTabla()));
            PrecioCompra = ConProductos.precioCompraVenta(ID)[0];
            PrecioVenta = ConProductos.precioCompraVenta(ID)[1];

            colocarDatosCampos();
            datos.guardarDatosProductos(ID, Proveedor, NombreMarca, Propiedades, Cantidad + "", PrecioCompra + "", PrecioVenta + "");

            if (ActivarAnimacion) {
                new BounceIn(paneDatos).play();
            }
        }
    }

    @FXML
    private void onBorrar() throws IOException {
        if (con_ComprobarConexion.conectado()) {

            String id = tcID.getCellData(getItemTabla());
            if (!id.equals(ID)) {
                paneMensajeEliminar.setVisible(true);
                mostrarMensajeBorrar(true);
                desactivarPane(true);
            } else {
                mostrarMensaje("No se puede borrar");
            }
        }
    }

    @FXML
    private void onSi() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            String id = tcID.getCellData(getItemTabla());
            if (ConProductos.eliminar(id) == true) {
                actualizarTabla();
                mostrarMensajeBorrar(false);
                mostrarMensaje("Borrado");
                desactivarBotones(true);
                desactivarPane(false);
            } else {
                mostrarMensaje("No se pudo borrar");
                mostrarMensajeBorrar(false);
                desactivarPane(false);
            }
        }
    }

    @FXML
    private void onNo() {
        desactivarPane(false);
        mostrarMensajeBorrar(false);
    }

    @FXML
    private void onClickedTable() {
        if (!tvProductos.getSelectionModel().isEmpty()) {
            desactivarBotones(false);

        } else {
            desactivarBotones(true);
        }

    }

    @FXML
    private void onComboBoxProveedores() throws IOException {
        if (!cbProbeedor.getSelectionModel().isEmpty()) {
            lbProveedor.setVisible(false);
            onEditandoDatos();
        } else {
            lbProveedor.setVisible(true);
        }

    }

    @FXML
    private void onEditandoDatos() throws IOException {
        String proveedor = (String) cbProbeedor.getValue();
        String nombreMarca = tfMarca.getText().concat(" ");
        String detalles = tfPropiedades.getText().concat(" ");
        String cantidad = tfCantidad.getText().concat(" ");
        String pCompra = tfPCompra.getText().concat(" ");
        String pVenta = tfPVenta.getText().concat(" ");

        if (!cantidad.isBlank()) {
            try {
                int cant = Integer.parseInt(cantidad.trim());

            } catch (NumberFormatException e) {
                mostrarMensaje("Ingrese solo números");
                tfCantidad.setText("");
                cantidad = " ";
            }
        }

        if (!pCompra.isBlank()) {
            try {
                double pC = Double.parseDouble(pCompra.trim());

            } catch (NumberFormatException e) {
                mostrarMensaje("Ingrese solo números");
                tfPCompra.setText("");
                pCompra = " ";
            }
        }

        if (!pVenta.isBlank()) {
            try {
                double pV = Double.parseDouble(pVenta.trim());

            } catch (NumberFormatException e) {
                mostrarMensaje("Ingrese solo números");
                tfPVenta.setText("");
                pVenta = " ";
            }
        }

        datos.guardarDatosProductos(ID, proveedor + " ", nombreMarca, detalles, cantidad, pCompra, pVenta);

    }

    //------------Otros metodos------------------------------------------------
    //Obtien los valores de los TextField y ComboBox
    private void datosTabla() throws IOException {
        if (con_ComprobarConexion.conectado()) {

            tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
            tcProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
            tcNombre.setCellValueFactory(new PropertyValueFactory<>("marca"));
            tcPropiedades.setCellValueFactory(new PropertyValueFactory<>("saborAromaColor"));
            tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            tcPCompra.setCellValueFactory(new PropertyValueFactory<>("precioCompra"));
            tcPVenta.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));

            tvProductos.setItems(ConProductos.mostrar());

        }
    }

    private void actualizarTabla() {
        tvProductos.setItems(ConProductos.mostrar());
    }

    private boolean obtenerDatos() {

        String proveedor = (String) cbProbeedor.getValue();
        String nombreMarca = tfMarca.getText();
        String cantidad = tfCantidad.getText();
        String pCompra = tfPCompra.getText();
        String pVenta = tfPVenta.getText();

        System.out.println(pVenta + " " + pCompra);

        Propiedades = tfPropiedades.getText();

        //Vevifica que los campos importantes no esten vacios
        if (proveedor != null && !nombreMarca.isBlank() && !Propiedades.isBlank() && !cantidad.isBlank() && !pCompra.isBlank() && !pVenta.isBlank()) {
            //Verifica que no haiga caracteres en los campos que solo aceptan numeros
            try {
                Proveedor = proveedor;
                NombreMarca = nombreMarca;
                Cantidad = Integer.parseInt(cantidad);
                PrecioCompra = Double.parseDouble(pCompra);
                PrecioVenta = Double.parseDouble(pVenta);
                if (Cantidad <= 0 || PrecioCompra <= 0 || PrecioVenta <= 0) {
                    mostrarMensaje("Ingrese un valor mayor");
                    labelsColor(ROJO);

                } else {
                    return true; //Si todo esta correcto
                }

            } catch (NumberFormatException e) {
                mostrarMensaje("Ingrese solo números");
                labelsColor(ROJO);
            }
        } else {
            mostrarMensaje("Falta información");
            labelsColor(ROJO);
        }
        return false;
    }

    private void limpiarCampos() {
        tfMarca.setText("");
        tfPropiedades.setText("");
        tfCantidad.setText("");
        tfPCompra.setText("");
        tfPVenta.setText("");

        cbProbeedor.setValue(null);
        lbProveedor.setVisible(true);
    }

    private void colocarDatosCampos() {
        tfMarca.setText(NombreMarca);
        tfPropiedades.setText(Propiedades);
        tfCantidad.setText(Cantidad + "");
        tfPCompra.setText(PrecioCompra + "");
        tfPVenta.setText(PrecioVenta + "");

        cbProbeedor.setValue(Proveedor);

        lbProveedor.setVisible(false);

    }

    //Mensaje de la parte superior
    private void mostrarMensaje(String mensaje) {
        lbMensaje5.setText(mensaje);
        paneMensaje.setVisible(true);
        new FadeInDown(paneMensaje).playOnFinished(new FadeOutUp(paneMensaje).setDelay(Duration.seconds(1))).play();

    }

    private void mostrarMensajeBorrar(boolean estado) {
        if (estado) {
            int item = tvProductos.getSelectionModel().getSelectedIndex();
            String nombre = tcNombre.getCellData(item);
            paneMensajeEliminar.setVisible(true);
            lbNombreProveedor.setText(nombre);
            if (ActivarAnimacion) {
                new FadeInUp(paneMensajeEliminar).play();
            }

        } else {
            if (ActivarAnimacion) {
                new FadeOutDown(paneMensajeEliminar).play();
            }else{
                paneMensajeEliminar.setVisible(estado);
            }
        }
    }

    private void labelsColor(Color color) {
        //Texto
        lbMensaje.setTextFill(color);
        lbMensaje1.setTextFill(color);
        lbMensaje2.setTextFill(color);
        lbMensaje3.setTextFill(color);

        //Numeros
        lbMensaje6.setTextFill(color);
        lbMensaje7.setTextFill(color);
        lbMensaje8.setTextFill(color);
    }

    private void desactivarBotones(boolean estado) {
        btnEditar.setDisable(estado);
        btnQuitar.setDisable(estado);
    }

    private void desactivarPane(boolean estado) {
        paneDatos.setDisable(estado);
        paneProveedores.setDisable(estado);

    }

    //Obtiene el intem de la tabla cuando se selecciona
    private int getItemTabla() {
        int item = tvProductos.getSelectionModel().getSelectedIndex();
        return item;
    }

    private void itemTabla() {
        ItemTabla = tvProductos.getSelectionModel().getSelectedIndex();
    }

    private void perdidas() {
        double total = 0;

        if (estadoBoton == true) {

            total = Cantidad * PrecioCompra;

            ConPerdidas.insertar(Proveedor, Annio, Mes, Dia, NombreMarca + " / " + Propiedades, Cantidad, total);

        } else {
            int cantidadDisponible = Integer.parseInt(tcCantidad.getCellData(ItemTabla)); //Obtiene la cantidad disponible en la tabla

            int cant = Cantidad - cantidadDisponible;

            total = cant * PrecioCompra;
            if (total > 0 && Cantidad > cantidadDisponible) {
                ConPerdidas.insertar(Proveedor, Annio, Mes, Dia, NombreMarca + " / " + Propiedades, cant, total);
            }
        }
    }

    private void obtenerFecha() {
        Date fechaActual = new Date(); //Obtiene la fecha actual

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Da un formato de dia-mes-año
        String fechaCompra = sdf.format(fechaActual);

        //Separa el dia, mes y año
        Dia = fechaCompra.substring(0, 2);
        Mes = fechaCompra.substring(3, 5);
        Annio = fechaCompra.substring(6, 10);

    }

    //Marca de color rojo las celdas en 0
//    private void cambiarColorCelda() {
//
//        tcCantidad.setCellFactory(new Callback<TableColumn<Productos, String>, TableCell<Productos, String>>() {
//            public TableCell<Productos, String> call(TableColumn<Productos, String> tableColumn) {
//
//                return new TableCell<Productos, String>() {
//                    @Override
//                    protected void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (item != null) {
//                            if (item.equals("0")) {
//                                setStyle("-fx-background-color: #F44336");
//                            }
//                            setText(item);
//                        }
//
//                    }
//                };
//            }
//        });
//    }
    private void recuperarDatos() {
        int valor2 = 1024827647;
        int valor3 = 470476102;
        int valor = Arrays.hashCode(datosRecuperados);

        if (!datosRecuperados[0].equals(" ")) {
            ID = datosRecuperados[0].trim();
            estadoBoton = false;
        }
        if (!datosRecuperados[1].equals("null ") && !datosRecuperados[1].isBlank()) {
            cbProbeedor.setValue(datosRecuperados[1].trim());
            lbProveedor.setVisible(false);
        }
        if (!datosRecuperados[2].isBlank()) {
            tfMarca.setText(datosRecuperados[2].trim());
        }
        if (!datosRecuperados[3].isBlank()) {
            tfPropiedades.setText(datosRecuperados[3].trim());
        }
        if (!datosRecuperados[4].isBlank()) {
            tfCantidad.setText(datosRecuperados[4].trim());
        }
        if (!datosRecuperados[5].isBlank()) {
            tfPCompra.setText(datosRecuperados[5].trim());
        }
        if (!datosRecuperados[6].isBlank()) {
            tfPVenta.setText(datosRecuperados[6].trim());
        }

        //Compreuba que exitan datos guardados
        if (valor != valor2 && valor != valor3) {
            mostrarMensaje("Hay datos sin guardar");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ActivarAnimacion = Boolean.parseBoolean(style.estiloSeleccionado()[1]);
        datosRecuperados = datos.recuperarDatosProductos();
        recuperarDatos();
        obtenerFecha();
        try {
            datosTabla();
        } catch (IOException ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        desactivarBotones(true);
        //cambiarColorCelda();

        ObservableList listProveedores = FXCollections.observableArrayList(ConProveedores.obtenerNombreProveedores().sorted());

        cbProbeedor.setItems(listProveedores);

        ivDatos.setImage(new Image("/icons/icon_informacion.png"));
        ivProductos.setImage(new Image("/icons/icon_productos.png"));

        if (ActivarAnimacion) {
            new Pulse(paneDatos).play();
            new Pulse(paneProveedores).play();
        }

    }

}
