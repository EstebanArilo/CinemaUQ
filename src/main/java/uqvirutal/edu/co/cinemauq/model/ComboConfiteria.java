package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;

public abstract class ComboConfiteria extends ProductoConfiteria{
    private double descuento;
    private ArrayList<ProductoConfiteria> listProductos;

    public ComboConfiteria(String idProducto, String nombre, double descuento, ArrayList<ProductoConfiteria> productos) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descuento = descuento;
        this.listProductos = new ArrayList<>(productos);
    }

    public double getDescuento() {
        return descuento;
    }

    public ArrayList<ProductoConfiteria> getListProductos() {
        return listProductos;
    }

    @Override
    public double getPrecio() {
        double subtotal = 0.0;
        for (ProductoConfiteria p : listProductos) {
            subtotal += p.getPrecio();
        }
        return subtotal * (1.0 - descuento);
    }
    @Override
    public String getNombre() {
        return this.nombre + " (Combo)";
    }

}