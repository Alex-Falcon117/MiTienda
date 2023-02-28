package datos_salvados;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Datos {
    
//    String url = "classes/saved_data/"; //Para compilar
     String url = "src/main/resources/saved_data/"; //Para pruebas

    FileWriter fichero;
    PrintWriter pw;

    File archivo;
    FileReader fr;
    BufferedReader br;

    //Proveedor----------------------------------------------------------------
    public void guardarDatosProveedores(String id, String nombre, String cuild, String direccion, String celular, String otraIinfo) throws IOException {

        String datos = id + "," + nombre + "," + cuild + "," + direccion + "," + celular + "," + otraIinfo;

        fichero = new FileWriter(url+"file_000.txt", false);
        pw = new PrintWriter(fichero);

        pw.print(datos);

        fichero.close();
        pw.close();

    }

    public String[] recuperarDatosProveedores() {
        String datos;
        String[] datosObtenidos = new String[6];
        try {

            archivo = new File(url+"file_000.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            datos = br.readLine();
            datosObtenidos = datos.split(",");

            fr.close();
            br.close();

            return datosObtenidos;

        } catch (IOException e) {
            datosObtenidos[0] = " ";
            datosObtenidos[1] = " ";
            datosObtenidos[2] = " ";
            datosObtenidos[3] = " ";
            datosObtenidos[4] = " ";
            datosObtenidos[5] = " ";

            return datosObtenidos;
        }
    }

//Productos-----------------------------------------------------------------------------------------
    public void guardarDatosProductos(String id, String proveedor, String nombre, String detalle, String cantidad, String pCompra, String pVenta) throws IOException {

        String datos = id + "," + proveedor + "," + nombre + "," + detalle + "," + cantidad + "," + pCompra + "," + pVenta;

        fichero = new FileWriter(url+"file_001.txt", false);
        pw = new PrintWriter(fichero);

        pw.print(datos);

        fichero.close();
        pw.close();

    }

    public String[] recuperarDatosProductos() {
        String datos;
        String[] datosObtenidos = new String[7];
        try {

            archivo = new File(url+"file_001.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            datos = br.readLine();
            datosObtenidos = datos.split(",");

            fr.close();
            br.close();

            return datosObtenidos;

        } catch (IOException e) {
            datosObtenidos[0] = " ";
            datosObtenidos[1] = " ";
            datosObtenidos[2] = " ";
            datosObtenidos[3] = " ";
            datosObtenidos[4] = " ";
            datosObtenidos[5] = " ";
            datosObtenidos[6] = " ";

            return datosObtenidos;
        }
    }
    
    //Clientes-----------------------------------------------------------------------------------------
    public void guardarDatosClientes(String id, String nombre, String dni, String domicilio, String celular) throws IOException {

        String datos = id+","+nombre+","+dni+","+domicilio+","+celular;

        fichero = new FileWriter(url+"file_002.txt", false);
        pw = new PrintWriter(fichero);

        pw.print(datos);

        fichero.close();
        pw.close();

    }

    public String[] recuperarDatosClientes() {
        String datos;
        String[] datosObtenidos = new String[5];
        try {

            archivo = new File(url+"file_002.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            datos = br.readLine();
            datosObtenidos = datos.split(",");

            fr.close();
            br.close();

            return datosObtenidos;

        } catch (IOException e) {
            datosObtenidos[0] = " ";
            datosObtenidos[1] = " ";
            datosObtenidos[2] = " ";
            datosObtenidos[3] = " ";
            datosObtenidos[4] = " ";

            return datosObtenidos;
        }
    }
    
}
