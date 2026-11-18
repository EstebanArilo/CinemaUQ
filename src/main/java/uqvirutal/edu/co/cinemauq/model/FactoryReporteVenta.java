package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class FactoryReporteVenta extends FactoryReporte{
    private ArrayList<Compra> compras;

    public FactoryReporteVenta(ArrayList<Compra> compras) {
        this.compras = compras;
    }

    @Override
    public Reporte crearReporte(LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador) {
        return new ReporteVenta("Reporte de Ventas", fechaInicio, fechaFin, exportador, compras);
    }
}
