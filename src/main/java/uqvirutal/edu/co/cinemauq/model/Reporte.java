package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Reporte {
    protected String titulo;
    protected LocalDateTime fechaGeneracion;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    protected Exportador exportador;

    public Reporte(String titulo, LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador) {
        this.titulo = titulo;
        this.fechaGeneracion = LocalDateTime.now();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.exportador = exportador;
    }

    public String generarEncabezado() {
        String inicioStr = (fechaInicio != null) ? fechaInicio.toString() : "N/A";
        String finStr = (fechaFin != null) ? fechaFin.toString() : "N/A";

        String encabezado = "========================================\n" +
                "CINEMA UQ - " + titulo + "\n" +
                "Fecha de Generacion: " + fechaGeneracion + "\n" +
                "Periodo: " + inicioStr + " al " + finStr + "\n" +
                "========================================\n";
        return encabezado;
    }

    public abstract String generarContenido();

    public void exportar(String rutaArchivo) {
        String contenidoCompleto = generarEncabezado() + "\n" + generarContenido();
        if (exportador != null) {
            exportador.exportar(contenidoCompleto, rutaArchivo);
        } else {
            System.out.println("Error: No se ha asignado un exportador.");
        }
    }

    public Exportador getExportador() { return exportador; }
    public void setExportador(Exportador exportador) { this.exportador = exportador; }
}
