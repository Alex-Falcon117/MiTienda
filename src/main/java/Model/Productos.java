package Model;

public class Productos {

    private String proveedor;
    private String id;
    private String marca;
    private String saborAromaColor;
    private String cantidad;
    private String precioCompra;
    private String precioVenta;
    
    public Productos(){
    
    }

    public Productos(String id, String proveedor, String marca, String saborAromaColor, String cantidad, String precioCompra, String precioVenta) {
        this.id = id;
        this.marca = marca;
        this.saborAromaColor = saborAromaColor;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.proveedor = proveedor;
    }

    public String getProveedor() {
        return proveedor;
    }

    public String getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getSaborAromaColor() {
        return saborAromaColor;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getPrecioCompra() {
        return precioCompra;
    }

    public String getPrecioVenta() {
        return precioVenta;
    } 
}
