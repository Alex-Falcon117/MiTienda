package controller;

import Model.Clientes;
import Model.Libreta;
import animatefx.animation.FadeInDown;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOutDown;
import animatefx.animation.FadeOutUp;
import animatefx.animation.Pulse;
import connection.ConClientes;
import connection.ConEstadistica;
import connection.ConLibreta;
import datos_salvados.Style;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class LibretaController implements Initializable {
    
    ConClientes conClientes = new ConClientes();
    
    Con_ComprobarConexion con_ComprobarConexion = new Con_ComprobarConexion();
    
    DecimalFormat df = new DecimalFormat("###,#00.00");
    
    Style style = new Style();
    
    @FXML
    private Pane paneProveedores, paneDatos, panePagarTodo, paneMensaje;
    
    @FXML
    private ImageView ivClientes, ivProductos;
    
    @FXML
    private TableView<Clientes> tvClientes;
    
    @FXML
    private TableColumn<Clientes, String> tcIDCliente, tcNombre, tcDNI, tcCelular, tcDomicilio;
    
    @FXML
    private TableView<Libreta> tvLibreta;
    
    @FXML
    private TableColumn<Libreta, String> tcIDProducto, tcIDCLiente, tcProducto, tcDetalle, tcCantidad, tcFecha, tcPrecio;
    
    @FXML
    private Label lbTotal, lbPagarTotal, lbMensaje5;
    
    @FXML
    private CheckBox cbPagarTodo;
    
    @FXML
    private Button btnPagar;
    
    private ObservableList listProductos = FXCollections.observableArrayList();
    
    private String ID;
    private int Annio, Mes, Dia;
    
    private boolean ActivarAnimacion;

    //Funciones---------------------------------------------------
    @FXML
    private void onTableCliked() throws IOException {
        if (!tvClientes.getSelectionModel().isEmpty()) {
            
            int item = tvClientes.getSelectionModel().getSelectedIndex();
            
            ID = tcIDCliente.getCellData(item);
            listProductos = ConLibreta.mostrar(ID);
            cbPagarTodo.setSelected(false);
            desactivarBotones(true);
            
            paneProveedores.setDisable(false);
            
            actualizarTablaProducto(ID);
        }
    }
    
    @FXML
    private void onTablaLibreta() {
        if (!tvLibreta.getSelectionModel().isEmpty()) {
            desactivarBotones(false);
        } else {
            desactivarBotones(true);
        }
        
    }
    
    @FXML
    private void onPagar() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            
            if (cbPagarTodo.isSelected()) {//Pagar todo
                mostrarMensajePagarTodo();
                desactivarPanes(true);
                
            } else {
                
                int item = tvLibreta.getSelectionModel().getSelectedIndex();
                
                pagar(item);
                
                String idProducto = (String) tcIDProducto.getCellData(item);
                ConLibreta.eliminar(idProducto);
                lbTotal.setText("Total: $" + df.format(ConLibreta.mostrarTotal(ID)));
                
                listProductos.remove(item);
                //actualizarTablaProducto(ID);
                mostrarMensaje("Pagado");
                
            }
            desactivarBotones(true);
        }
    }
    
    @FXML
    private void onSi() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            int listTamano = listProductos.size();
            int item = 0;
            
            while (item < listTamano) {
                
                pagar(item);
                item++;
            }
            
            ConLibreta.eliminarTodo(ID);
            listProductos.clear();
            lbTotal.setText("Total: $" + df.format(ConLibreta.mostrarTotal(ID)));
            // actualizarTablaProducto(ID);
            mostrarMensaje("Todo pagado");
            ocultarMensajePagarTodo();
            desactivarPanes(false);
            cbPagarTodo.setSelected(false);
        }
    }
    
    @FXML
    private void onNo() {
        ocultarMensajePagarTodo();
        desactivarPanes(false);
        cbPagarTodo.setSelected(false);
        
    }
    //Otras funciones--------------------------------------------------------

    //Guarda en las estadistica los productos pagados
    private void pagar(int item) {
        String idProducto = "";
        String producto = "";
        String detalles = "";
        int cantidad = 0;
        double precio = 0;
        
        try {
            idProducto = tcIDProducto.getCellData(item);
            producto = tcProducto.getCellData(item);
            detalles = tcDetalle.getCellData(item);
            precio = ConLibreta.mostrarPrecio(ID, idProducto);
            cantidad = Integer.parseInt(tcCantidad.getCellData(item));
            
            ConEstadistica.insertar(producto + " / " + detalles, Annio, Mes, Dia, cantidad, precio + "");
        } catch (NumberFormatException e) {
            ConEstadistica.insertar(producto + " / " + detalles, Annio, Mes, Dia, 0, precio + "");
        }
    }
    
    private void actualizarTablaProducto(String idCliente) throws IOException {
        if (con_ComprobarConexion.conectado()) {
            
            tcIDProducto.setCellValueFactory(new PropertyValueFactory<>("id"));
            tcIDCLiente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
            tcProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
            tcDetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));
            tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            tcFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCompra"));
            tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            
            tvLibreta.setItems(listProductos);
            lbTotal.setText("Total: $" + df.format(ConLibreta.mostrarTotal(idCliente)));
        }
    }
    
    private void actualizarTabla() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            tcIDCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
            tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tcDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
            tcDomicilio.setCellValueFactory(new PropertyValueFactory<>("direccion"));
            tcCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
            
            tvClientes.setItems(conClientes.mostrar());
        }
    }
    
    private void obtenerFechaActual() {
        Date fechaActual = new Date(); //Obtiene la fecha actual

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Da un formato de dia-mes-año
        String fechaCompra = sdf.format(fechaActual);

        //Separa el dia, mes y año
        Dia = Integer.parseInt(fechaCompra.substring(0, 2));
        Mes = Integer.parseInt(fechaCompra.substring(3, 5));
        Annio = Integer.parseInt(fechaCompra.substring(6, 10));
    }
    
    private void desactivarPanes(boolean estado) {
        paneDatos.setDisable(estado);
        paneProveedores.setDisable(estado);
        
    }
    
    private void desactivarBotones(boolean estado) {
        btnPagar.setDisable(estado);
        cbPagarTodo.setDisable(estado);
        
    }
    
    private void mostrarMensaje(String mensaje) {
        lbMensaje5.setText(mensaje);
        new FadeInDown(paneMensaje).playOnFinished(new FadeOutUp(paneMensaje).setDelay(Duration.seconds(1))).play();
        paneMensaje.setVisible(true);
    }
    
    private void mostrarMensajePagarTodo() {
        panePagarTodo.setVisible(true);
        lbPagarTotal.setText("Total: $" + df.format(ConLibreta.mostrarTotal(ID)));
        
        if (ActivarAnimacion) {
            new FadeInUp(panePagarTodo).play();
        }
    }
    
    private void ocultarMensajePagarTodo() {
        if (ActivarAnimacion) {
            new FadeOutDown(panePagarTodo).play();
        } else {
            panePagarTodo.setVisible(false);
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ActivarAnimacion = Boolean.parseBoolean(style.estiloSeleccionado()[1]);
        
        try {
            actualizarTabla();
        } catch (IOException ex) {
            Logger.getLogger(LibretaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        obtenerFechaActual();
        
        ivClientes.setImage(new Image("/icons/icon_clientes.png"));
        ivProductos.setImage(new Image("/icons/icon_productos.png"));
        
        if (ActivarAnimacion) {
            new Pulse(paneDatos).play();
            new Pulse(paneProveedores).play();
        }
    }
}
