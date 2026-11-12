package uqvirutal.edu.co.cinemauq.model;


import java.time.LocalDate;

public class Movimiento {
    private String id;
    private TipoMovimiento tipoMovimiento;
    private double monto;
    private LocalDate fecha;

    public Movimiento(String id, TipoMovimiento tipoMovimiento, double monto, LocalDate fecha) {
        this.id = id;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}