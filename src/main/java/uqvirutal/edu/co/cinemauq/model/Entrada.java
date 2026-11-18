package uqvirutal.edu.co.cinemauq.model;

public abstract class Entrada {
    protected String idEntrada;
    protected String funcion;
    protected String asiento;
    protected double precioBase;

    public Entrada(String idEntrada, String funcion, String asiento, double precioBase) {
        this.idEntrada = idEntrada;
        this.funcion = funcion;
        this.asiento = asiento;
        this.precioBase = precioBase;
    }

    public abstract double getPrecio();

    public abstract double getCosto();
    public abstract String getDescripcion();

    public String getIdEntrada() { return idEntrada; }
    public void setIdEntrada(String idEntrada) { this.idEntrada = idEntrada; }

    public String getFuncion() { return funcion; }
    public void setFuncion(String funcion) { this.funcion = funcion; }

    public String getAsiento() { return asiento; }
    public void setAsiento(String asiento) { this.asiento = asiento; }

    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
}
