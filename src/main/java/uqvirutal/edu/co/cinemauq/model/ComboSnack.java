package uqvirutal.edu.co.cinemauq.model;

public class ComboSnack extends DecoradorEntrada{
    private String descripcionCombo;
    private double precioCombo;

    public ComboSnack(Entrada entradaDecorada, String descripcionCombo, double precioCombo) {
        super(entradaDecorada);
        this.descripcionCombo = descripcionCombo;
        this.precioCombo = precioCombo;
    }

    @Override
    public double getPrecio() {
        return entradaDecorada.getPrecio() + this.precioCombo;
    }

    @Override
    public String getDescripcion() {
        return entradaDecorada.getDescripcion() + " + Combo Snack (" + descripcionCombo + ")";
    }

    public String getDescripcionCombo() { return descripcionCombo; }
    public double getPrecioCombo() { return precioCombo; }
}
