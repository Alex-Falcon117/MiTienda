package Model;

import java.util.Set;

public class Estadistica {

    private  String producto;
    private  String fecha;
    private  String cantidad;
    private  Set annio;
    private  String total;

    public Estadistica(Set annio, String total) {
        this.annio = annio;
        this.total = total;
    }

    public Estadistica(String producto, String fecha, String cantidad, String total) {
        this.producto = producto;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.total = total;
    }
    
    

    public Set getAnnio() {
        return annio;
    }

    public String getTotal() {
        return total;
    }

    public String getProducto() {
        return producto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCantidad() {
        return cantidad;
    }
    
    

}
