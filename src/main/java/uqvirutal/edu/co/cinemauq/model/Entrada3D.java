package uqvirutal.edu.co.cinemauq.model;

public class Entrada3D extends Entrada{
    private double costoAdicional3D;

    public Entrada3D(String idEntrada, String funcion, String asiento, double precioBase, double costoAdicional3D) {
        super(idEntrada, funcion, asiento, precioBase);
        this.costoAdicional3D = costoAdicional3D;
    }

    @Override
    public double getPrecio() {
        return this.precioBase + this.costoAdicional3D;
    }


    @Override
    public double getCosto() {
        return 0;
    }

    @Override
    public String getDescripcion() {
        return "Entrada Formato 3D [Asiento: " + asiento + "]";
    }

    public double getCostoAdicional3D() { return costoAdicional3D; }
    public void setCostoAdicional3D(double costoAdicional3D) { this.costoAdicional3D = costoAdicional3D; }
}
