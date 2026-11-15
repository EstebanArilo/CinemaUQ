package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

public class Reembolso {
    private String id;
    private LocalDateTime fechaHora;
    private double montoDevuelto;
    private double porcentajeAplicado;
    private MotivoReembolso motivo;
    private Compra compra;

    public Reembolso(String id, LocalDateTime fechaHora, double montoDevuelto,
                     double porcentajeAplicado, MotivoReembolso motivo, Compra compra) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.montoDevuelto = montoDevuelto;
        this.porcentajeAplicado = porcentajeAplicado;
        this.motivo = motivo;
        this.compra = compra;
    }

    public String getId() { return id; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public double getMontoDevuelto() { return montoDevuelto; }
    public double getPorcentajeAplicado() { return porcentajeAplicado; }
    public MotivoReembolso getMotivo() { return motivo; }
    public Compra getCompra() { return compra; }
}