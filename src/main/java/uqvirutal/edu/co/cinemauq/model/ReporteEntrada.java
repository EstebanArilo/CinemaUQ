package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReporteEntrada extends Reporte{
    private ArrayList<Entrada> repoEntrada;

    public ReporteEntrada(String titulo, LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador, ArrayList<Entrada> repoEntrada) {
        super(titulo, fechaInicio, fechaFin, exportador);
        this.repoEntrada = repoEntrada;
    }

    @Override
    public String generarContenido() {
        String contenido = "Detalle de Entradas Vendidas:\n";

        if (repoEntrada != null && !repoEntrada.isEmpty()) {
            for (int i = 0; i < repoEntrada.size(); i++) {
                Entrada entrada = repoEntrada.get(i);
                contenido = contenido + "- Entrada ID: " + entrada.getIdEntrada()
                        + " | Asiento: " + entrada.getAsiento()
                        + " | Precio: $" + entrada.getPrecio() + "\n";
            }
        } else {
            contenido = contenido + "No se encontraron entradas registradas.\n";
        }

        return contenido;
    }
}
