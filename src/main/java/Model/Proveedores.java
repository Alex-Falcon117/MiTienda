package Model;

import connection.ConProveedores;

public class Proveedores {

    ConProveedores conProveedores = new ConProveedores();

    private String id;
    private String nombre;
    private String cuild;
    private String direccion;
    private String celular;
    private String otraInfo;

    public Proveedores() {

    }

    public Proveedores(String id, String nombre, String cuild, String direccion, String celular, String otraInfo) {
        this.id = id;
        this.nombre = nombre;
        this.cuild = cuild;
        this.direccion = direccion;
        this.celular = celular;
        this.otraInfo = otraInfo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCuild() {
        return cuild;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCelular() {
        return celular;
    }

    public String getOtraInfo() {
        return otraInfo;
    }

}
