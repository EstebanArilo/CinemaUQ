package uqvirutal.edu.co.cinemauq.model;

public class EntradaGeneral extends Entrada{
    public EntradaGeneral(String idEntrada, String funcion, String asiento, double precioBase) {
        super(idEntrada, funcion, asiento, precioBase);
    }

    @Override
    public double getPrecio() {
        return 0;
    }

    @Override
    public double getCosto() {
        return this.precioBase;
    }

    @Override
    public String getDescripcion() {
        return "Entrada General [Asiento: " + asiento + "]";
    }
}
