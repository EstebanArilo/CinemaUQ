package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReporteVenta extends Reporte{
    private ArrayList<Compra> repoCompras;

    public ReporteVenta(String titulo, LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador, ArrayList<Compra> repoCompras) {
        super(titulo, fechaInicio, fechaFin, exportador);
        this.repoCompras = repoCompras;
    }

    @Override
    public String generarContenido() {
        String contenido = "Detalle de Ventas Registradas:\n";
        double totalVentas = 0.0;

        if (repoCompras != null && !repoCompras.isEmpty()) {
            for (int i = 0; i < repoCompras.size(); i++) {
                Compra compra = repoCompras.get(i);
                contenido = contenido + "- Compra ID: " + compra.getIdCompra()
                        + " | Total: $" + compra.getTotal()
                        + " | Estado: " + compra.getEstadoCompra() + "\n";
                totalVentas = totalVentas + compra.getTotal();
            }
        } else {
            contenido = contenido + "No se encontraron compras en el periodo.\n";
        }

        contenido = contenido + "\n----------------------------------------\n";
        contenido = contenido + "TOTAL GENERAL DE VENTAS: $" + totalVentas;
        return contenido;
    }
}
