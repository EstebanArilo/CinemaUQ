package uqvirutal.edu.co.cinemauq.model;

public class EntradaVIP extends Entrada{
    private double recargoVip;

    public EntradaVIP(String idEntrada, String funcion, String asiento, double precioBase, double recargoVip) {
        super(idEntrada, funcion, asiento, precioBase);
        this.recargoVip = recargoVip;
    }

    @Override
    public double getPrecio() {
        return 0;
    }

    @Override
    public double getCosto() {
        return this.precioBase + this.recargoVip;
    }

    @Override
    public String getDescripcion() {
        return "Entrada VIP (Incluye acceso a zona preferencial y confitería ligera) [Asiento: " + asiento + "]";
    }

    public double getRecargoVip() { return recargoVip; }
    public void setRecargoVip(double recargoVip) { this.recargoVip = recargoVip; }
}
