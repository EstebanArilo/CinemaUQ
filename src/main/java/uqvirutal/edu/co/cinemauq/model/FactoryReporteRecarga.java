package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class FactoryReporteRecarga extends FactoryReporte{
    private ArrayList<Movimiento> movimientos;

    public FactoryReporteRecarga(ArrayList<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    @Override
    public Reporte crearReporte(LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador) {
        return new ReporteRecarga("Reporte de Recargas", fechaInicio, fechaFin, exportador, movimientos);
    }
}
