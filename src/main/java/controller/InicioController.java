package controller;

import Model.Carrito;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeInDown;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOutDown;
import animatefx.animation.FadeOutUp;
import animatefx.animation.Pulse;
import connection.ConClientes;
import connection.ConEstadistica;
import connection.ConLibreta;
import connection.ConProductos;
import datos_salvados.DatosCompra;
import datos_salvados.Style;
import java.io.FileNotFoundException;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class InicioController implements Initializable {

    Con_ComprobarConexion con_ComprobarConexion = new Con_ComprobarConexion();
    ConProductos conProductos = new ConProductos();

    DatosCompra datosCompra = new DatosCompra();
    Style style = new Style();

    DecimalFormat df = new DecimalFormat("###,#00.00");

    @FXML
    private ListView lvProducto, lvClientes, lvSinStock;

    @FXML
    private Pane paneCliente, paneGanancias, paneStock, paneCarrito, paneVentas, paneMensaje, paneCalcularVuelto, paneVentaLibre;

    @FXML
    private ImageView ivGanancias, ivPerdidas, ivCarrito, ivVender, ivCliente;

    @FXML
    private CheckBox cbFiados, cbVentaLibre;

    @FXML
    private TableView tvCarrito;

    @FXML
    private TableColumn tcID, tcProducto, tcDetalle, tcCantidad, tcPrecio;

    @FXML
    private TextField tfBuscar, tfNombreCliente, tfCantidad, tfDineroEntregado, tfPrecio;

    @FXML
    private Label lbCantidad, lbPrecio, lbDNI, lbDireccion, lbDeuda, lbGanancia,
            lbTotal, lbTotalProducto, lbMensaje5, lbDetalles, lbPrecioTotal, lbVuelto;

    @FXML
    private Button btnVender, btnAgregarCarrito, btnAgregarCarrito2, btnQuitar, btnAgragar, btnCalcularVuelto, btnFinalizarVenta;

    private ObservableList listaCompra = FXCollections.observableArrayList();
    private ObservableList listaRecuperada = FXCollections.observableArrayList();

    private String[] productosResultados = new String[5];
    private String[] clientesResultado = new String[4];

    private String idCliente = "";
    private int Annio, Mes, Dia;

    private int cantidadIngresada = 0; //La cantidad a comprar
    private double precioProducto = 0; //Precio del producto
    private int cantidadDisponible = 0; //Cantidad disponible
    private double totalCarrito = 0; //La suma total de todo el carrito
    private double totalProducto = 0; // La suma de la cantidad a comprar de un producto
    private int nuevaCantidaDisponible = 0; //La cantidad que se resta a la cantidad disponible

    private boolean ActivarAnimacion;

    //Funciones---------------------------------------------
    @FXML
    private void onCheck() {//Cuando se marca el CheckBox "Agregar a libreta"

        if (cbFiados.isSelected()) {
            if (ActivarAnimacion) {
                new BounceIn(paneCliente).play();
            }

            btnVender.setDisable(true);
            btnCalcularVuelto.setDisable(true);
            paneCliente.setDisable(false);
        } else {
            btnVender.setDisable(false);
            btnCalcularVuelto.setDisable(false);
            paneCliente.setDisable(true);
            limpiarCamposCliente();
        }
    }

    @FXML
    private void onVentaLibre() {//Cuando se marca el CheckBox "Venta libre"

        if (cbVentaLibre.isSelected()) {
            paneVentaLibre.setVisible(true);
            if (ActivarAnimacion) {
                new FadeInUp(paneVentaLibre).play();
            }

            paneCarrito.setDisable(true);
            paneStock.setDisable(true);
            paneGanancias.setDisable(true);
            paneVentas.setDisable(true);
            cantidadIngresada = 0;
        }
    }

    @FXML
    private void onCerrarVentaLibre() {
        ventaLibre();
    }

    @FXML
    private void onAgregarCarrito() throws IOException {//Cuando se preciona el boton "Agregar al carrito"
        if (con_ComprobarConexion.conectado()) {
            if (ActivarAnimacion) {
                new BounceIn(paneCarrito).play();
            }

            tfCantidad.setDisable(true);

            actualizarTabla(productosResultados[0], productosResultados[1], productosResultados[3], cantidadIngresada, totalProducto);
            sumarCarrito(totalProducto);
            limpiarCampos();
            listaCompra("");

            if (cbVentaLibre.isSelected()) {
                ConProductos.actualizarCantidad(productosResultados[0], Integer.parseInt(productosResultados[2]));
                ventaLibre();
            } else {
                ConProductos.actualizarCantidad(productosResultados[0], nuevaCantidaDisponible);
                sinStock();
            }
        }
    }

//    @FXML
//    private void onAgregarCarrito2() throws FileNotFoundException, IOException {
//        new BounceIn(paneCarrito).play();
//        actualizarTabla(productosResultados[0], productosResultados[1], productosResultados[3], cantidadIngresada, totalProducto);
//        sumarCarrito(totalProducto);
//        limpiarCampos();
//        listaCompra("");
//
//    }
    @FXML
    private void onIngresarPrecioLibre() {
        String precio = tfPrecio.getText().trim();

        if (!precio.equals("")) {
            try {
                totalProducto = Double.parseDouble(precio);
                btnAgregarCarrito2.setDisable(false);

            } catch (NumberFormatException e) {
                mostrarMensaje("Ingrese solo números");
                tfPrecio.setText("");
            }

        } else {
            btnAgregarCarrito2.setDisable(true);
        }
    }

    @FXML
    private void onCancelar() {
        limpiarCampos();

    }

    @FXML
    private void onCancelarClientes() throws IOException {
        limpiarCamposCliente();
//        datosCompra.guardarDatosLibreta(" ", " ", " ", " ", " ");
    }

    @FXML
    private void onBuscarProductos() throws IOException {//Cuando se busca un producto muestra un resultado automaticamente

        if (con_ComprobarConexion.conectado()) {
            String busqueda = tfBuscar.getText().trim();

            // resultados = conProductos.mostrarBusqueda(busqueda);
            if (!busqueda.equals("")) {

                //Realiza la busqueda en la BD
                lvProducto.setVisible(true);
                lvProducto.setItems(conProductos.buscar(busqueda));

                //Si no obtubo resultados
                if (lvProducto.getItems().isEmpty()) {
                    mostrarMensaje("Sin resultados");
                }

            } else {

                lvProducto.setVisible(false);
                ConProductos.listaID.clear();
                limpiarCampos();
            }
        }
    }

    @FXML
    private void onBuscarCliente() throws IOException {//Cuando se busca un cliente
        if (con_ComprobarConexion.conectado()) {
            String busqueda = tfNombreCliente.getText().trim();

            if (!busqueda.isEmpty()) {

                btnAgragar.setDisable(false);
                lvClientes.setVisible(true);
                lvClientes.setItems(ConClientes.buscar(busqueda));

                if (lvClientes.getItems().isEmpty()) {
                    mostrarMensaje("Sin resultados");
                }

            } else {
                lvClientes.setVisible(false);
                ConClientes.listaID.clear();
                btnAgragar.setDisable(true);
                limpiarCamposCliente();

            }
        }
    }

    @FXML
    private void onBorrarBusquedaProducto() {//Cuando se ba borrando la busqueda
        ConProductos.listaID.clear();

    }

    @FXML
    private void onBorrarBusquedaCliente() {
        ConClientes.listaID.clear();
    }

    @FXML
    private void onListaProductos() {
        //Se ontiene el valor del elemento seleccionado de la lista, luego se obtiene el id
        //de ese producto en la tabla
        int index = lvProducto.getSelectionModel().getSelectedIndex();

        if (index >= 0) {
            String idResultado = (String) ConProductos.listaID.get(index);

            productosResultados = conProductos.obtenerResultado(idResultado);

            lvProducto.setVisible(false);
            ConProductos.listaID.clear();

            asignarValores();
            tfCantidad.setDisable(false);
            cbVentaLibre.setDisable(false);
        }
    }

    @FXML
    private void onListaClientes() throws IOException {
        int index = lvClientes.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            idCliente = (String) ConClientes.listaID.get(index);
            clientesResultado = ConClientes.obtenerResultado(idCliente);

            lvClientes.setVisible(false);
            ConClientes.listaID.clear();
            asignarValoresCliente(idCliente);
        }
    }

    //Cuando se ingresa una cantidad a comprar
    @FXML
    private void onIngresarCantidad() {
        ingresarCantidad();
    }

    @FXML
    private void onCalcularVuelto() {
        mostrarPaneCalcularVuelto(true);

    }

    @FXML
    private void onQuitarLista() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            int index = tvCarrito.getSelectionModel().getSelectedIndex();
            String id = (String) tcID.getCellData(index);
            double restar = Double.parseDouble(tcPrecio.getCellData(index).toString());
            int cantidad = (int) tcCantidad.getCellData(index);

            int nuevaCantidad = ConProductos.actualizarCantidad(id) + cantidad;//Se obtiene la cantidad de la BD y se le suma la cantidad que se quito del carrito

            ConProductos.actualizarCantidad(id, nuevaCantidad);

            restarCarrito(restar);

            listaCompra.remove(index);
            guardarCarrito();

            listaCompra("Carrito vacio");
            btnQuitar.setDisable(true);
            if (id.equals(productosResultados[0])) {
                limpiarCampos();
            }
            sinStock();
        }
    }

    @FXML
    private void onClickTabla() {
        if (!tvCarrito.getSelectionModel().isEmpty()) {
            btnQuitar.setDisable(false);
        } else {
            btnQuitar.setDisable(true);
        }
    }

    @FXML
    private void onAgragarLibreta() throws IOException {//Agrega todo el contenido de la tabla a la libreta asiganada a un cliente
        if (con_ComprobarConexion.conectado()) {
            agregarLibreta(idCliente);
            ConClientes.listaID.clear();
            idCliente = "";
            limpiarCamposCliente();
            paneCliente.setDisable(true);
            cbFiados.setSelected(false);
            listaCompra.clear();
            listaCompra("Agregado a libreta");

//            datosCompra.guardarDatosLibreta(" ", " ", " ", " ", " ");
            datosCompra.borrarCompraProducto();
        }
    }

    @FXML
    private void onVender() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            int carrito = listaCompra.size();
            int valor = 0;

            while (valor < carrito) {

                String producto = (String) tcProducto.getCellData(valor);
                String detalle = (String) tcDetalle.getCellData(valor);
                int cantidad = (int) tcCantidad.getCellData(valor);
                double precio = Double.parseDouble(tcPrecio.getCellData(valor).toString());

                System.out.println(precio);

                ConEstadistica.insertar(producto + " / " + detalle, Annio, Mes, Dia, cantidad, precio + "");
                valor++;
            }

            gananciaDia();
            listaCompra.clear();
            listaCompra("Productos vendidos");

            sinStock();
            datosCompra.borrarCompraProducto();
        }
    }

    @FXML
    private void onFinalizarVenta() throws IOException {
        onVender();
        paneCarrito.setDisable(true);
        mostrarPaneCalcularVuelto(false);
        finalizarVenta();
        mostrarPaneCalcularVuelto(false);
        listaCompra("Productos vendidos");
        datosCompra.borrarCompraProducto();
    }

    @FXML
    private void onCerrar() {
        mostrarPaneCalcularVuelto(false);
    }

    @FXML
    private void onCalculandoVuelto() {
        vuelto();
    }

    @FXML
    private void onListaSinStock() {
        if (!lvSinStock.getSelectionModel().isEmpty()) {
            mostrarMensaje("Producto sin stock");
        }
    }

    //Otras funciones-------------------------------------------------
    //Calcula el total de todos los productos en el carrito
    private void sumarCarrito(double precio) {

        totalCarrito = precio + totalCarrito;
        lbTotal.setText("$" + df.format(totalCarrito));

    }

    //Cuando se quita un producto del carrito este se resta del total
    private void restarCarrito(double precio) {

        totalCarrito = totalCarrito - precio;
        lbTotal.setText("$" + df.format(totalCarrito));

    }

    private void ingresarCantidad() {
        String cantida = tfCantidad.getText().trim();

        try { //El Tray Catch valida que solo se ingresen numero

            if (!cantida.isEmpty()) { //Si el campo no esta vacio
                cbVentaLibre.setDisable(true);
                cantidadIngresada = Integer.parseInt(cantida); //Se obtiene la cantidad ingresada

                if (cantidadIngresada > 0) {

                    if (cantidadIngresada <= cantidadDisponible) { //Verifica que exista la cantida suficiente a comprar

                        nuevaCantidaDisponible = cantidadDisponible - cantidadIngresada;

                        totalProducto = cantidadIngresada * precioProducto;

                        lbTotalProducto.setText("$" + df.format(totalProducto));
                        btnAgregarCarrito.setDisable(false);

                    } else {
                        //En caso de que se ingrese una cantida superior a la disponbile
                        btnAgregarCarrito.setDisable(true);
                        mostrarMensaje("Cantidad no disponible");
                    }
                } else {
                    mostrarMensaje("Cantidad insuficiente");
                    tfCantidad.setText("");
                }
            } else {
                cbVentaLibre.setDisable(false);
                lbTotalProducto.setText("");
                btnAgregarCarrito.setDisable(true); //Se debe desactivar el boton agregar al carrito aqui
            }
        } catch (NumberFormatException e) {
            //En caso de ingresar letras u otros caracteres que no sean numeros
            mostrarMensaje("Ingrese solo números");
            tfCantidad.setText("");
        }
    }

    //Limpia todos los TextField y Labels
    private void limpiarCampos() {
        lbCantidad.setText("");
        lbTotalProducto.setText("");
        lbPrecio.setText("");
        lbDetalles.setText("");

        btnAgregarCarrito.setDisable(true);

        tfCantidad.setDisable(true);
        tfCantidad.setText("");
        tfBuscar.setText("");

        lvProducto.setVisible(false);

        cbVentaLibre.setDisable(true);

        cantidadDisponible = 0;
        precioProducto = 0;
        cantidadIngresada = 0;

    }

    private void limpiarCamposCliente() {
        lbDNI.setText("");
        lbDireccion.setText("");
        lbDeuda.setText("");
        tfNombreCliente.setText("");
        lvClientes.setVisible(false);
    }

    //Muesta el mensaje de la parte superior de la ventana
    private void mostrarMensaje(String mensaje) {
        lbMensaje5.setText(mensaje);
        new FadeInDown(paneMensaje).playOnFinished(new FadeOutUp(paneMensaje).setDelay(Duration.seconds(1))).play();
        paneMensaje.setVisible(true);
    }

    //Asigna los valores del Array
    private void asignarValores() {
        tfBuscar.setText(productosResultados[1]);

        lbCantidad.setText(productosResultados[2]);
        lbDetalles.setText(productosResultados[3]);

        lbPrecio.setText("$" + formatearDecimal(productosResultados[4]));

        cantidadDisponible = Integer.parseInt(productosResultados[2]);
        precioProducto = Double.parseDouble(productosResultados[4]);

    }

    private String formatearDecimal(String valor) {
        double valor2 = Double.parseDouble(valor);
        String valor3 = df.format(valor2);

        return valor3;
    }

    private void asignarValoresCliente(String idCliente) throws IOException {
        lbDNI.setText(clientesResultado[2]);
        lbDireccion.setText(clientesResultado[3]);
        tfNombreCliente.setText(clientesResultado[1]);
        lbDeuda.setText("$" + df.format(ConLibreta.mostrarTotal(idCliente)));

    }

    private void actualizarTabla(String id, String producto, String detalle, int cantidad, double precio) throws FileNotFoundException, IOException {

        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        tcDetalle.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        listaCompra.add(new Carrito(id, producto, detalle, cantidad, precio + ""));
        tvCarrito.setItems(listaCompra);//Agrega los datos a la tabla
        guardarCarrito();
    }

    //Guarda todos los productos del carrito
    private void guardarCarrito() throws FileNotFoundException, IOException {
        //Primero limpia la lista
        datosCompra.borrarCompraProducto();
        int index = 0;
        int tamanoLista = listaCompra.size();

        //Luego vuelve a cargar todo los productos de la tabla
        while (index < tamanoLista) {

            String id = tcID.getCellData(index).toString();
            String producto = tcProducto.getCellData(index).toString();
            String detalle = tcDetalle.getCellData(index).toString();
            String cantidad = tcCantidad.getCellData(index).toString();
            String precio = tcPrecio.getCellData(index).toString();

            datosCompra.guardarCompraProducto(id, producto, detalle, cantidad, precio);
            index++;
        }
    }

    //Recupera los datos del carrito en caso de que se sierre la aplicacion sin compretar la venta
    private void recuperarCarrito() {
        try {
            listaRecuperada = datosCompra.recuperarCompraProducto();
            if (!listaRecuperada.isEmpty()) {
                mostrarMensaje("Venta sin completar");
                int i = 1;
                String id = "";
                String producto = "";
                String detalle = "";
                String cantidad = "";
                String precio = "";

                for (Object l : listaRecuperada) {

                    String valor = l.toString();

                    switch (i) {
                        case 1:
                            id = valor;
                            break;
                        case 2:
                            producto = valor;
                            break;
                        case 3:
                            detalle = valor;
                            break;
                        case 4:
                            cantidad = valor;
                            break;
                        case 5:
                            precio = valor;

                            //Aqui se deve guardar en la tabla
                            actualizarTabla(id, producto, detalle, Integer.parseInt(cantidad), Double.parseDouble(precio));
                            totalCarrito = totalCarrito + Double.parseDouble(precio);
                            i = 0;
                            break;
                    }
                    i++;
                }
                sumarCarrito(0);
                listaCompra("");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Agrega todos los productos de la lista a la libreta de un cliente
    private void agregarLibreta(String idCliente) throws IOException {
        int i = listaCompra.size();
        int index = 0;

        obtenerFechaActual();

        while (index < i) {

            String producto = (String) tcProducto.getCellData(index);
            int cantidad = (int) tcCantidad.getCellData(index);
            String detalle = (String) tcDetalle.getCellData(index);
            double precio = Double.parseDouble(tcPrecio.getCellData(index).toString());

            ConLibreta.insertar(idCliente, producto, cantidad, detalle, Dia, Mes, Annio, precio + "");

            index++;
        }

        sinStock();
    }

    private void listaCompra(String mensaje) {
        if (!listaCompra.isEmpty()) {
            paneCarrito.setDisable(false);

        } else {
            mostrarMensaje(mensaje);
            paneCarrito.setDisable(true);
            totalCarrito = 0;
            lbTotal.setText("");
            cbFiados.setSelected(false);
            onCheck();

        }
    }

    private void mostrarPaneCalcularVuelto(boolean estado) {

        paneCarrito.setDisable(estado);
        paneStock.setDisable(estado);
        paneGanancias.setDisable(estado);
        paneVentas.setDisable(estado);

        lbPrecioTotal.setText(" $" + df.format(totalCarrito));

        if (estado) {
            if (ActivarAnimacion) {
                new FadeInUp(paneCalcularVuelto).play();
            }
            paneCalcularVuelto.setVisible(estado);
        } else {
            if (ActivarAnimacion) {
                new FadeOutDown(paneCalcularVuelto).play();
            } else {
                paneCalcularVuelto.setVisible(estado);
                lbPrecioTotal.setText("");
            }
        }
    }

    private void finalizarVenta() {
//        paneStock.setDisable(false);
//        paneGanancias.setDisable(false);
//        paneVentas.setDisable(false);
//
//        new FadeOutDown(paneCalcularVuelto).play();
        lbPrecioTotal.setText("");
        lbVuelto.setText("");
        tfDineroEntregado.setText("");

    }

    //Calcula el vielto a entregar
    private void vuelto() {
        String dineroEntregado = tfDineroEntregado.getText().trim();

        if (!dineroEntregado.isBlank()) {
            try {
                double dinero = Double.parseDouble(dineroEntregado);

                if (dinero > totalCarrito) {

                    double vuelto = dinero - totalCarrito;

                    lbVuelto.setText("$" + df.format(vuelto));

                    btnFinalizarVenta.setDisable(false);
                } else {
                    lbVuelto.setText("$...");
                    btnFinalizarVenta.setDisable(true);
                }
            } catch (NumberFormatException e) {
                tfDineroEntregado.setText("");
                btnFinalizarVenta.setDisable(true);
                mostrarMensaje("Ingrese solo números");
            }
        } else {
            lbVuelto.setText("$...");
            btnFinalizarVenta.setDisable(true);

        }
    }

    //Comprueva si existe cantidad suficiente de un roducto
    private void sinStock() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            lvSinStock.setItems(ConProductos.sinStock());
        }
    }

    public void obtenerFechaActual() {
        Date fechaActual = new Date(); //Obtiene la fecha actual

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Da un formato de dia-mes-año
        String fechaCompra = sdf.format(fechaActual);

        //Separa el dia, mes y año
        Dia = Integer.parseInt(fechaCompra.substring(0, 2));
        Mes = Integer.parseInt(fechaCompra.substring(3, 5));
        Annio = Integer.parseInt(fechaCompra.substring(6, 10));

    }

    public void gananciaDia() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            lbGanancia.setText("$" + df.format(ConEstadistica.mostrarGanaciaActual(Annio, Mes, Dia)));
        }
    }

    private void ventaLibre() {
        if (ActivarAnimacion) {
            new FadeOutDown(paneVentaLibre).play();
        } else {
            paneVentaLibre.setVisible(false);
        }

        paneCarrito.setDisable(false);
        paneStock.setDisable(false);
        paneGanancias.setDisable(false);
        paneVentas.setDisable(false);
        cbVentaLibre.setSelected(false);
        tfPrecio.setText("");
        btnAgregarCarrito2.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ActivarAnimacion = Boolean.parseBoolean(style.estiloSeleccionado()[1]);

        recuperarCarrito();

        obtenerFechaActual();
        try {
            sinStock();
            gananciaDia();
        } catch (IOException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        ivGanancias.setImage(new Image("/icons/icon_ganancias.png"));
        ivPerdidas.setImage(new Image("/icons/icon_perdidas.png"));
        ivCarrito.setImage(new Image("/icons/icon_carrito.png"));
        ivVender.setImage(new Image("icons/icon_ventas.png"));
        ivCliente.setImage(new Image("/icons/icon_clientes.png"));

        if (ActivarAnimacion) {
            new Pulse(paneCarrito).play();
            new Pulse(paneCliente).play();
            new Pulse(paneGanancias).play();
            new Pulse(paneStock).play();
            new Pulse(paneVentas).play();
        }

        //Seguir cambiando los int a double en las otras partes, continuar con las pruevas
    }

}
