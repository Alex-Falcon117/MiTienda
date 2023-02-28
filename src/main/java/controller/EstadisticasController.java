package controller;

import Model.Estadistica;
import Model.PERDIDAS;
import animatefx.animation.Pulse;
import connection.ConEstadistica;
import connection.ConPerdidas;
import datos_salvados.Style;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class EstadisticasController implements Initializable {

    DecimalFormat df = new DecimalFormat("###,###,#00.00");
    Con_ComprobarConexion con_ComprobarConexion = new Con_ComprobarConexion();
    Style style = new Style();

    @FXML
    private Pane paneProveedores, paneCarrito;

    @FXML
    private ImageView ivDia;

    @FXML
    private BarChart<?, ?> bcEstadistica;

    @FXML
    private Label lbGanaciaAnual, lbGanaciaAnual1, lbP, lbG;

    @FXML
    private ComboBox cbAnnio;

    @FXML
    private Button btnVerDetalles;

    @FXML
    private TableView<Estadistica> tvGanancias;

    @FXML
    private TableColumn<Estadistica, String> tcProductoG, tcFechaG, tcCantidadG, tcTotalG;

    @FXML
    private TableView<PERDIDAS> tvPerdidas;

    @FXML
    private TableColumn<PERDIDAS, String> tcProveedor, tcProducto, tcFecha, tcCantidad, tcTotal;

    private final XYChart.Series PERDIDAS = new XYChart.Series<>();
    private final XYChart.Series GANANCIAS = new XYChart.Series<>();

    private int Annio;
    private double totalGannacia;

    @FXML
    private void onSeleccionAnnio() throws IOException {
        if (con_ComprobarConexion.conectado()) {

            Annio = Integer.parseInt(cbAnnio.getSelectionModel().getSelectedItem().toString());

            estadistica();

            totalGannacia = ConEstadistica.mostrarTotal(Annio) - ConPerdidas.mostrarTotalPedidas(Annio);
            lbGanaciaAnual.setVisible(true);
            lbGanaciaAnual.setText("Ganancias del año: $" + df.format(totalGannacia));

            btnVerDetalles.setDisable(false);
        }
    }

    @FXML
    private void onVerDetalles() throws IOException {
        if (con_ComprobarConexion.conectado()) {
            paneProveedores.setVisible(false);
            paneCarrito.setVisible(true);
            actualizarTablaGanancias();
            actualizarTablaPerdidas();
            lbGanaciaAnual1.setText("Ganancias del año: $" + df.format(totalGannacia));
        }
    }

    @FXML
    private void onOcultarDetalles() {
        paneProveedores.setVisible(true);
        paneCarrito.setVisible(false);
    }

    private void estadistica() throws IOException {

        GANANCIAS.setName("Ganancias");
        GANANCIAS.getData().add(new XYChart.Data<>("Enero", ConEstadistica.mostrarTotal(Annio, 1)));
        GANANCIAS.getData().add(new XYChart.Data<>("Febrero", ConEstadistica.mostrarTotal(Annio, 2)));
        GANANCIAS.getData().add(new XYChart.Data<>("Marzo", ConEstadistica.mostrarTotal(Annio, 3)));
        GANANCIAS.getData().add(new XYChart.Data<>("Abril", ConEstadistica.mostrarTotal(Annio, 4)));
        GANANCIAS.getData().add(new XYChart.Data<>("Mayo", ConEstadistica.mostrarTotal(Annio, 5)));
        GANANCIAS.getData().add(new XYChart.Data<>("Junio", ConEstadistica.mostrarTotal(Annio, 6)));
        GANANCIAS.getData().add(new XYChart.Data<>("Julio", ConEstadistica.mostrarTotal(Annio, 7)));
        GANANCIAS.getData().add(new XYChart.Data<>("Agosto", ConEstadistica.mostrarTotal(Annio, 8)));
        GANANCIAS.getData().add(new XYChart.Data<>("Septiembre", ConEstadistica.mostrarTotal(Annio, 9)));
        GANANCIAS.getData().add(new XYChart.Data<>("Octubre", ConEstadistica.mostrarTotal(Annio, 10)));
        GANANCIAS.getData().add(new XYChart.Data<>("Nobiembre", ConEstadistica.mostrarTotal(Annio, 11)));
        GANANCIAS.getData().add(new XYChart.Data<>("Diciembre", ConEstadistica.mostrarTotal(Annio, 12)));

        PERDIDAS.setName("Perdidas");
        PERDIDAS.getData().add(new XYChart.Data<>("Enero", ConPerdidas.mostrarTotalPedidas(Annio, 1)));
        PERDIDAS.getData().add(new XYChart.Data<>("Febrero", ConPerdidas.mostrarTotalPedidas(Annio, 2)));
        PERDIDAS.getData().add(new XYChart.Data<>("Marzo", ConPerdidas.mostrarTotalPedidas(Annio, 3)));
        PERDIDAS.getData().add(new XYChart.Data<>("Abril", ConPerdidas.mostrarTotalPedidas(Annio, 4)));
        PERDIDAS.getData().add(new XYChart.Data<>("Mayo", ConPerdidas.mostrarTotalPedidas(Annio, 5)));
        PERDIDAS.getData().add(new XYChart.Data<>("Junio", ConPerdidas.mostrarTotalPedidas(Annio, 6)));
        PERDIDAS.getData().add(new XYChart.Data<>("Julio", ConPerdidas.mostrarTotalPedidas(Annio, 7)));
        PERDIDAS.getData().add(new XYChart.Data<>("Agosto", ConPerdidas.mostrarTotalPedidas(Annio, 8)));
        PERDIDAS.getData().add(new XYChart.Data<>("Septiembre", ConPerdidas.mostrarTotalPedidas(Annio, 9)));
        PERDIDAS.getData().add(new XYChart.Data<>("Octubre", ConPerdidas.mostrarTotalPedidas(Annio, 10)));
        PERDIDAS.getData().add(new XYChart.Data<>("Nobiembre", ConPerdidas.mostrarTotalPedidas(Annio, 11)));
        PERDIDAS.getData().add(new XYChart.Data<>("Diciembre", ConPerdidas.mostrarTotalPedidas(Annio, 12)));
    }

    private void actualizarTablaGanancias() {
        tcProductoG.setCellValueFactory(new PropertyValueFactory<>("producto"));
        tcFechaG.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        tcCantidadG.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tcTotalG.setCellValueFactory(new PropertyValueFactory<>("total"));

        tvGanancias.setItems(ConEstadistica.mostrarDetalle(Annio));
        lbG.setText("Total: $" + df.format(ConEstadistica.mostrarTotal(Annio)));
    }

    private void actualizarTablaPerdidas() {

        tcProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tcTotal.setCellValueFactory(new PropertyValueFactory<>("precio"));

        tvPerdidas.setItems(ConPerdidas.mostrarDetalle(Annio));
        lbP.setText("Total: $" + df.format(ConPerdidas.mostrarTotalPedidas(Annio)));
    }

    private void mostrarAnnios() throws IOException {
        cbAnnio.setItems(ConEstadistica.mostrarAnnio());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            if (con_ComprobarConexion.conectado()) {
                mostrarAnnios();
                estadistica();
            }
        } catch (IOException ex) {
            Logger.getLogger(EstadisticasController.class.getName()).log(Level.SEVERE, null, ex);
        }

        bcEstadistica.getData().addAll(GANANCIAS, PERDIDAS);

        ivDia.setImage(new Image("/icons/icon_ganancias.png"));

        if (style.estiloSeleccionado()[1].equals("true")) {
            new Pulse(paneProveedores).play();
            bcEstadistica.setAnimated(true);
        }

    }

}
