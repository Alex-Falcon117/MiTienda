package Model;


public class Clientes {
    
    private final String id;
    private final String nombre;
    private final String dni;
    private final String direccion;
    private final String celular;

    public Clientes(String id, String nombre, String dni, String direccion, String celular) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.direccion = direccion;
        this.celular = celular;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCelular() {
        return celular;
    }
    
    
    
    
    
    
    
}
