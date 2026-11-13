package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReportePunto extends Reporte{
    private ArrayList<Cliente> repoClientes;

    public ReportePunto(String titulo, LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador, ArrayList<Cliente> repoClientes) {
        super(titulo, fechaInicio, fechaFin, exportador);
        this.repoClientes = repoClientes;
    }

    @Override
    public String generarContenido() {
        String contenido = "Detalle de Puntos de Clientes:\n";

        if (repoClientes != null && !repoClientes.isEmpty()) {
            for (int i = 0; i < repoClientes.size(); i++) {
                Cliente cliente = repoClientes.get(i);
                double totalPuntosCliente = 0.0;

                if (cliente.getListPuntos() != null) {
                    for (int j = 0; j < cliente.getListPuntos().size(); j++) {
                        totalPuntosCliente = totalPuntosCliente + cliente.getListPuntos().get(j).getCantidad();
                    }
                }

                contenido = contenido + "- Cliente: " + cliente.getNombre()
                        + " | Correo: " + cliente.getCorreo()
                        + " | Puntos Acumulados: " + totalPuntosCliente + "\n";
            }
        } else {
            contenido = contenido + "No hay clientes registrados en el sistema.\n";
        }

        return contenido;
    }
}
