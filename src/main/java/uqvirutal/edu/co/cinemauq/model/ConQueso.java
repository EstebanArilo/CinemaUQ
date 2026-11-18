package uqvirutal.edu.co.cinemauq.model;

public class ConQueso extends DecoradorConfiteria{
    public ConQueso (ProductoConfiteria producto) {
        super(producto);
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio() + 10000;
    }
    @Override
    public String getNombre() {
        return producto.getNombre()+ "Con Queso Extra";
    }
}
