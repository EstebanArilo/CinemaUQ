package uqvirutal.edu.co.cinemauq.model;

public class ServicioGafas3D extends DecoradorEntrada{
    private double precioGafas;

    public ServicioGafas3D(Entrada entradaDecorada, double precioGafas) {
        super(entradaDecorada);
        this.precioGafas = precioGafas;
    }

    @Override
    public double getPrecio() {
        return entradaDecorada.getPrecio() + this.precioGafas;
    }

    @Override
    public String getDescripcion() {
        return entradaDecorada.getDescripcion() + " + Servicio Gafas 3D";
    }

    public double getPrecioGafas() { return precioGafas; }
    public void setPrecioGafas(double precioGafas) { this.precioGafas = precioGafas; }
}
