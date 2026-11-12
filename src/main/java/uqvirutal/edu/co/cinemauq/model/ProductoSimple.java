package uqvirutal.edu.co.cinemauq.model;

public class ProductoSimple extends ProductoConfiteria{
    private CategoriaProducto categoriaProducto;

    public ProductoSimple(String idProducto, double precio, String nombre, CategoriaProducto categoriaProducto) {
        super(idProducto, precio, nombre);
        this.categoriaProducto = categoriaProducto;
    }

    public CategoriaProducto getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(CategoriaProducto categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }
}