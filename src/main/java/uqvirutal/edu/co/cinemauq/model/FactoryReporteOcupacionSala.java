package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class FactoryReporteOcupacionSala extends FactoryReporte{
    private ArrayList<Sala> salas;
    private ArrayList<Funcion> funciones;

    public FactoryReporteOcupacionSala(ArrayList<Sala> salas, ArrayList<Funcion> funciones) {
        this.salas = salas;
        this.funciones = funciones;
    }

    @Override
    public Reporte crearReporte(LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador) {
        return new ReporteOcupacionSala("Reporte de Ocupacion de Salas", fechaInicio, fechaFin, exportador, salas, funciones);
    }
}
