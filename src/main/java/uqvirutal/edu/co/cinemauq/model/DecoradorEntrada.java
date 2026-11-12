package uqvirutal.edu.co.cinemauq.model;

public abstract class DecoradorEntrada extends Entrada{
    protected Entrada entradaDecorada;

    public DecoradorEntrada(Entrada entradaDecorada) {
        super(entradaDecorada.getIdEntrada(), entradaDecorada.getFuncion(), entradaDecorada.getAsiento(), entradaDecorada.getPrecioBase());
        this.entradaDecorada = entradaDecorada;
    }

    @Override
    public double getCosto() {
        return entradaDecorada.getCosto();
    }

    @Override
    public String getDescripcion() {
        return entradaDecorada.getDescripcion();
    }
}
