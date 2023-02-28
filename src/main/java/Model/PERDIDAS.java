
package Model;

public class PERDIDAS {
    
   private final String proveedor;
   private final String producto;
   private final String fecha;
   private final int cantidad;
   private final String precio;

    public PERDIDAS(String proveedor, String producto, String fecha, int cantidad, String precio) {
        this.proveedor = proveedor;
        this.producto = producto;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getProveedor() {
        return proveedor;
    }

    public String getProducto() {
        return producto;
    }

    public String getFecha() {
        return fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getPrecio() {
        return precio;
    }

 
   
    
}
