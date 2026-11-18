package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class FactoryReporteEntrada extends FactoryReporte{
    private ArrayList<Entrada> entradas;

    public FactoryReporteEntrada(ArrayList<Entrada> entradas) {
        this.entradas = entradas;
    }

    @Override
    public Reporte crearReporte(LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador) {
        return new ReporteEntrada("Reporte de Entradas Vendidas", fechaInicio, fechaFin, exportador, entradas);
    }
}
