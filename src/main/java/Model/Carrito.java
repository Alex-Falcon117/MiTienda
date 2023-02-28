
package Model;

public class Carrito {
    
    private final String id;
    private final String producto;
    private final String detalles;
    private final int cantidad;
    private final String precio;

    public Carrito(String id, String producto, String detalles,int cantidad, String precio) {
        this.id = id;
        this.producto = producto;
        this.detalles = detalles;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public String getProducto() {
        return producto;
    }
    
     public String getDetalles() {
        return detalles;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getPrecio() {
        return precio;
    }
    
    
    
}
