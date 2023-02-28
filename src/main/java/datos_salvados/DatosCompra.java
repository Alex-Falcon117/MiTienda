package datos_salvados;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatosCompra extends Datos{
    
//    String url = "classes/saved_data/";
//
//    FileWriter fichero;
//    PrintWriter pw;
//
//    File archivo;
//    FileReader fr;
//    BufferedReader br;

    //Guarda todo los datos en la lista
    public void guardarCompraProducto(String id, String marca, String detalle, String cantidad, String precio) throws IOException {

        String datos = id + "," + marca + "," + detalle + "," + cantidad + "," + precio;
        fichero = new FileWriter(url+"file_003.txt", true);
        pw = new PrintWriter(fichero);

        pw.println(datos);

        fichero.close();
        pw.close();

    }

    //Recupera todo los datos de la lista
    public ObservableList<String> recuperarCompraProducto() throws FileNotFoundException {
        String datos = "";
        String[] datosRecuperados = new String[5];
        ObservableList<String> lista = FXCollections.observableArrayList();

        try {

            archivo = new File(url+"file_003.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            datos = br.readLine();

            while (datos != null) {
                datosRecuperados = datos.split(",");
                lista.addAll(datosRecuperados);
//                lista.addAll(datosRecuperados[0], datosRecuperados[1], datosRecuperados[2], datosRecuperados[3], datosRecuperados[4]);

                datos = br.readLine();

            }
            fr.close();
            br.close();

            return lista;

        } catch (IOException e) {

            return lista;

        }
    }

    //Limpia la lista
    public void borrarCompraProducto() throws IOException {

        fichero = new FileWriter(url+"file_003.txt", false);
        pw = new PrintWriter(fichero);

        fichero.close();
        pw.close();

    }

//    //Datos del Cliente para guardar en Libreta----------------------------------------------------
//    public void guardarDatosLibreta(String id, String nombre, String dni, String domicilio, String deudaTotal) throws IOException {
//
//        String datos = id + "," + nombre + "," + dni + "," + domicilio + "," + deudaTotal;
//        fichero = new FileWriter("datosLibreta.txt", true);
//        pw = new PrintWriter(fichero);
//
//        pw.print(datos);
//
//        fichero.close();
//        pw.close();
//
//    }
//
//    //Recuperar datos
//    public String[] recuperarDatosLibreta() throws FileNotFoundException {
//        String datos;
//        String[] datosRecuperados = new String[5];
//
//        try {
//
//            archivo = new File("datosLibreta.txt");
//            fr = new FileReader(archivo);
//            br = new BufferedReader(fr);
//            datos = br.readLine();
//
//            if (datos != null) {
//
//                datosRecuperados = datos.split(",");
//
//            }
//            fr.close();
//            br.close();
//
//            return datosRecuperados;
//
//        } catch (IOException e) {
//
//            datosRecuperados[0] = " ";
//            datosRecuperados[1] = " ";
//            datosRecuperados[2] = " ";
//            datosRecuperados[3] = " ";
//            datosRecuperados[4] = " ";
//
//            return datosRecuperados;
//
//        }
//    }
//    
//     //Limpia la Libreta
//    public void borrarDatosLibreta() throws IOException {
//
//        fichero = new FileWriter("datosLibreta.txt", false);
//        pw = new PrintWriter(fichero);
//
//        fichero.close();
//        pw.close();
//
//    }

}
