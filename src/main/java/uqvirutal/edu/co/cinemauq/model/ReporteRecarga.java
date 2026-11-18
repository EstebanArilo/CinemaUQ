package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReporteRecarga extends Reporte{
    private ArrayList<Movimiento> repoMovimientos;

    public ReporteRecarga(String titulo, LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador, ArrayList<Movimiento> repoMovimientos) {
        super(titulo, fechaInicio, fechaFin, exportador);
        this.repoMovimientos = repoMovimientos;
    }

    /**
     * Genera el cuerpo textual del reporte sobre las recargas a la tarjeta virtual.
     * @return
     */
    @Override
    public String generarContenido() {
        String contenido = "Detalle de Recargas de Tarjetas Virtuales:\n";
        double totalRecargas = 0.0;

        if (repoMovimientos != null && !repoMovimientos.isEmpty()) {
            for (int i = 0; i < repoMovimientos.size(); i++) {
                Movimiento mov = repoMovimientos.get(i);
                if (mov.getTipoMovimiento() == TipoMovimiento.RECARGA) {
                    contenido = contenido + "- ID Mov: " + mov.getId()
                            + " | Monto: $" + mov.getMonto()
                            + " | Fecha: " + mov.getFecha() + "\n";
                    totalRecargas = totalRecargas + mov.getMonto();
                }
            }
        } else {
            contenido = contenido + "No hay movimientos de recarga registrados.\n";
        }

        contenido = contenido + "\n----------------------------------------\n";
        contenido = contenido + "TOTAL RECARGADO: $" + totalRecargas;
        return contenido;
    }
}
