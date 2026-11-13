package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class FactoryReportePunto extends FactoryReporte{
    private ArrayList<Cliente> clientes;

    public FactoryReportePunto(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public Reporte crearReporte(LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador) {
        return new ReportePunto("Reporte de Puntos de Clientes", fechaInicio, fechaFin, exportador, clientes);
    }
}
