package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class FactoryReporteCancelacion extends FactoryReporte{
    private ArrayList<Compra> compras;

    public FactoryReporteCancelacion(ArrayList<Compra> compras) {
        this.compras = compras;
    }

    @Override
    public Reporte crearReporte(LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador) {
        return new ReporteCancelacion("Reporte de Compras Canceladas", fechaInicio, fechaFin, exportador, compras);
    }
}
