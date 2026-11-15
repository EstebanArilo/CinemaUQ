package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Compra {
    private String idCompra;
    private LocalDateTime fechaHora;
    private double total;
    private EstadoCompra estadoCompra;
    private ArrayList<Entrada> entradas;
    private ArrayList<ProductoConfiteria> productosConfiteria;
    private Reembolso reembolso;
    private TarjetaVirtual tarjetaVirtual;
    private Funcion funcion;

    private Compra(Builder builder) {
        this.idCompra = builder.idCompra;
        this.fechaHora = builder.fechaHora;
        this.total = builder.total;
        this.estadoCompra = builder.estadoCompra;
        this.entradas = builder.entradas;
        this.productosConfiteria = builder.productosConfiteria;
        this.tarjetaVirtual = builder.tarjetaVirtual;
        this.funcion = builder.funcion;
    }

    public String getIdCompra() { return idCompra; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public double getTotal() { return total; }
    public EstadoCompra getEstadoCompra() { return estadoCompra; }
    public void setEstadoCompra(EstadoCompra estadoCompra) { this.estadoCompra = estadoCompra; }
    public ArrayList<Entrada> getEntradas() { return entradas; }
    public ArrayList<ProductoConfiteria> getProductosConfiteria() { return productosConfiteria; }
    public Funcion getFuncion() { return funcion; }

    public static class Builder {
        private String idCompra;
        private LocalDateTime fechaHora = LocalDateTime.now();
        private double total = 0.0;
        private EstadoCompra estadoCompra = EstadoCompra.PENDIENTE;;
        private ArrayList<Entrada> entradas = new ArrayList<>();
        private ArrayList<ProductoConfiteria> productosConfiteria = new ArrayList<>();
        private TarjetaVirtual tarjetaVirtual;
        private Funcion funcion;

        public Builder idCompra(String idCompra) {
            this.idCompra = idCompra;
            return this;
        }

        public Builder fechaHora(LocalDateTime fechaHora) {
            this.fechaHora = fechaHora;
            return this;
        }
        public Builder tarjetaVirtual(TarjetaVirtual tarjetaVirtual) {
            this.tarjetaVirtual = tarjetaVirtual;
            return this;
        }
        public Builder agregarEntrada(Entrada entrada) {
            this.entradas.add(entrada);
            recalcularTotal();
            return this;
        }

        public Builder agregarProducto(ProductoConfiteria producto) {
            this.productosConfiteria.add(producto);
            recalcularTotal();
            return this;
        }
        public Builder funcion(Funcion funcion) {
            this.funcion = funcion;
            return this;
        }
        private void recalcularTotal() {
            double suma = 0.0;
            for (Entrada entrada : entradas) {
                suma += entrada.getCosto();
            }
            for (ProductoConfiteria producto : productosConfiteria) {
                suma += producto.getPrecio();
            }
            this.total = suma;
        }

        public Compra build() {
            return new Compra(this);
        }
    }
    public Reembolso getReembolso() {
        return reembolso;
    }

    public void setReembolso(Reembolso reembolso) {
        this.reembolso = reembolso;
    }
    public TarjetaVirtual getTarjetaVirtual() {
        return tarjetaVirtual;
    }
}
