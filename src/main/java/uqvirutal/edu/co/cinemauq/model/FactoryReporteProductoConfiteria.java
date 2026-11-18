package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class FactoryReporteProductoConfiteria extends FactoryReporte{
    private ArrayList<ProductoConfiteria> productos;

    public FactoryReporteProductoConfiteria(ArrayList<ProductoConfiteria> productos) {
        this.productos = productos;
    }

    @Override
    public Reporte crearReporte(LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador) {
        return new ReporteProductoConfiteria("Reporte de Productos de Confiteria", fechaInicio, fechaFin, exportador, productos);
    }
}
