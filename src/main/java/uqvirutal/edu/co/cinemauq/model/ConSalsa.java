package uqvirutal.edu.co.cinemauq.model;

public class ConSalsa extends DecoradorConfiteria{

    public ConSalsa(ProductoConfiteria producto) {
        super(producto);
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio() + 6000;
    }
    @Override
    public String getNombre() {
        return producto.getNombre()+ "Con Salsa Extra";
    }
}
