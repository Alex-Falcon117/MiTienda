package Model;

public class Empleados {

    private final String id;
    private final String nombre;
    private final String celular;
    private final String direccion;
    private final String contrasena;

    public Empleados(String id, String nombre, String celular, String direccion, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.celular = celular;
        this.direccion = direccion;
        this.contrasena = contrasena;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCelular() {
        return celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getContrasena() {
        return contrasena;
    }

}
