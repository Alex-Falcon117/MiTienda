package Model;

public class Libreta {

    private final String id;
    private final String idCliente;
    private final String producto;
    private final String cantidad;
    private final String detalle;
    private final String fechaCompra;
    private final String precio;

    public Libreta(String id, String idCliente, String producto, String cantidad, String detalle, String fechaCompra, String precio) {
        this.id = id;
        this.idCliente = idCliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.detalle = detalle;
        this.fechaCompra = fechaCompra;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getProducto() {
        return producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public String getPrecio() {
        return precio;
    }

    
  
}
