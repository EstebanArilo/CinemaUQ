package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;

public class ComboPersonalizado extends ComboConfiteria {
    private String idProducto;
    private String nombre;
    private double descuento = 0.0;
    private ArrayList<ProductoConfiteria> productos = new ArrayList<>();

    public ComboPersonalizado(Builder builder) {
        super(builder.idProducto, builder.nombre, builder.descuento, builder.productos);
        this.idProducto = builder.idProducto;
        this.nombre = builder.nombre;
        this.descuento = builder.descuento;
        this.productos = new ArrayList<>(builder.productos);
    }
    public String getIdProducto() {
        return idProducto;
    }

    @Override
    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public ArrayList<ProductoConfiteria> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<ProductoConfiteria> productos) {
        this.productos = productos;
    }
    public static class Builder {
        private String idProducto;
        private String nombre;
        private double descuento = 0.0;
        private ArrayList<ProductoConfiteria> productos = new ArrayList<>();

        public Builder setIdProducto(String idProducto) {
            this.idProducto = idProducto;
            return this;
        }

        public Builder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder setDescuento(double descuento) {
            this.descuento = descuento;
            return this;
        }

        public Builder agregarProducto(ProductoConfiteria producto) {
            if (producto != null) {
                this.productos.add(producto);
            }
            return this;
        }

        public ComboPersonalizado build() {
            return new ComboPersonalizado(this);
        }
    }
}