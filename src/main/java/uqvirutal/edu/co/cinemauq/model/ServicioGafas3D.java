package uqvirutal.edu.co.cinemauq.model;

public class ServicioGafas3D extends DecoradorEntrada{
    private double precioGafas;

    public ServicioGafas3D(Entrada entradaDecorada, double precioGafas) {
        super(entradaDecorada);
        this.precioGafas = precioGafas;
    }

    /**
     * Calcula el precio total de la entrada decorada añadiendo el valor de
     * las gafas 3D.
     * @return
     */
    @Override
    public double getPrecio() {
        return entradaDecorada.getPrecio() + this.precioGafas;
    }

    /**
     * Obtiene la descriçion detallada de la entrada añadiendo el texto
     * del servicio adicional de gafas 3D.
     * @return
     */
    @Override
    public String getDescripcion() {
        return entradaDecorada.getDescripcion() + " + Servicio Gafas 3D";
    }

    public double getPrecioGafas() { return precioGafas; }
    public void setPrecioGafas(double precioGafas) { this.precioGafas = precioGafas; }
}
