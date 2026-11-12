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
}