package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReporteOcupacionSala extends Reporte{
    private ArrayList<Sala> repoSalas;
    private ArrayList<Funcion> repoFunciones;

    public ReporteOcupacionSala(String titulo, LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador, ArrayList<Sala> repoSalas, ArrayList<Funcion> repoFunciones) {
        super(titulo, fechaInicio, fechaFin, exportador);
        this.repoSalas = repoSalas;
        this.repoFunciones = repoFunciones;
    }

    @Override
    public String generarContenido() {
        String contenido = "Reporte de Ocupacion de Salas y Funciones:\n";

        if (repoFunciones != null && !repoFunciones.isEmpty()) {
            for (int i = 0; i < repoFunciones.size(); i++) {
                Funcion funcion = repoFunciones.get(i);
                contenido = contenido + "- Funcion ID: " + funcion.getIdFuncion()
                        + " | Pelicula: " + funcion.getPelicula().getTitulo()
                        + " | Sala: " + funcion.getSala().getNombre() + "\n";
            }
        } else {
            contenido = contenido + "No hay funciones programadas en el periodo.\n";
        }

        return contenido;
    }
}
