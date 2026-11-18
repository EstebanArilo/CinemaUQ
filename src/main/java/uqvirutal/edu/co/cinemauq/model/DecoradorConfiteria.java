package uqvirutal.edu.co.cinemauq.model;

public class DecoradorConfiteria extends ProductoConfiteria{
    protected final ProductoConfiteria producto;

    public DecoradorConfiteria(ProductoConfiteria producto) {
        this.producto = producto;
    }

    @Override
    public String getNombre() {
        return producto.getNombre();
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio();
    }
}