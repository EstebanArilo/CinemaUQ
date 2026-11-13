package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReporteCancelacion extends Reporte{
    private ArrayList<Compra> repoCompra;

    public ReporteCancelacion(String titulo, LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador, ArrayList<Compra> repoCompra) {
        super(titulo, fechaInicio, fechaFin, exportador);
        this.repoCompra = repoCompra;
    }

    @Override
    public String generarContenido() {
        String contenido = "Detalle de Compras Canceladas:\n";

        if (repoCompra != null && !repoCompra.isEmpty()) {
            for (int i = 0; i < repoCompra.size(); i++) {
                Compra compra = repoCompra.get(i);
                if ("CANCELADA".equalsIgnoreCase(compra.getEstadoCompra())) {
                    contenido = contenido + "- Compra ID: " + compra.getIdCompra()
                            .concat(" | Total: $").concat(String.valueOf(compra.getTotal())) + "\n";
                }
            }
        } else {
            contenido = contenido + "No hay compras canceladas registradas.\n";
        }

        return contenido;
    }
}
