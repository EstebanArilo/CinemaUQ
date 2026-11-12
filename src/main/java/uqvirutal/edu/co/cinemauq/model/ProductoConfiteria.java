package uqvirutal.edu.co.cinemauq.model;

public class ProductoConfiteria {
    protected String idProducto;
    protected String nombre;
    protected double precio;


    public ProductoConfiteria() {
    }
    public ProductoConfiteria(String idProducto, double precio, String nombre) {
        this.idProducto = idProducto;
        this.precio = precio;
        this.nombre = nombre;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
