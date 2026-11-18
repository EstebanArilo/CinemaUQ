package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;

public abstract class FactoryReporte {
    public abstract Reporte crearReporte(LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador);
}
