package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;


public class Administrador extends Usuario {
    private ArrayList<ProductoConfiteria> listProductos;
    public Administrador(String id, String nombre, String correo, String contraseña) {
        super(id, nombre, correo, contraseña);
        this.listProductos = new ArrayList<>();
    }
    /**
     * Permite al administrador crear un combo utilizando el Builder.
     */
    public ComboConfiteria crearCombo(String id, String nombre, double descuento, ArrayList<ProductoConfiteria> productosBase) {
        ComboPersonalizado.Builder builder = new ComboPersonalizado.Builder()
                .setIdProducto(id)
                .setNombre(nombre)
                .setDescuento(descuento);

        for (ProductoConfiteria p : productosBase) {
            builder.agregarProducto(p);
        }

        ComboConfiteria nuevoCombo = builder.build();
        this.listProductos.add(nuevoCombo);
        return nuevoCombo;
    }
    /**
     * Permite al administrador eliminar un combo
     */
    public boolean eliminarCombo(ComboConfiteria combo) {
        return this.listProductos.remove(combo);
    }
    /**
     * Crea un producto simple de confitería y lo añade a la lista
     */
    public ProductoSimple crearProducto(String id, String nombre, double precio, CategoriaProducto categoria) {
        ProductoSimple nuevoProducto = new ProductoSimple(id,precio, nombre, categoria);
        this.listProductos.add(nuevoProducto);
        return nuevoProducto;
    }
    /**
     * Permite al administrador eliminar un producto
     */
    public boolean eliminarProducto(ProductoConfiteria producto) {
        return this.listProductos.remove(producto);
    }
}