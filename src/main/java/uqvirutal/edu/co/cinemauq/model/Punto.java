package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;

public class Punto {
    private double cantidad;
    private LocalDate fechaObtencion;
    private LocalDate fechaVencimiento;

    public Punto(double cantidad, LocalDate fechaObtencion, LocalDate fechaVencimiento) {
        this.cantidad = cantidad;
        this.fechaObtencion = fechaObtencion;
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaObtencion() {
        return fechaObtencion;
    }

    public void setFechaObtencion(LocalDate fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}