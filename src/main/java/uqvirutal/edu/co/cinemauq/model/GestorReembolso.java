package uqvirutal.edu.co.cinemauq.model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestorReembolso {
    private PoliticaReembolso politicaReembolso;
    private ArrayList<Reembolso> historialReembolsos;
    private int contadorReembolsos;

    public GestorReembolso() {
        this.historialReembolsos = new ArrayList<>();
        this.contadorReembolsos = 1;
    }

    public Reembolso procesarReembolso(Compra compra) throws CancelacionNoPermitidaException {
        if (compra == null) {
            throw new CancelacionNoPermitidaException("La compra no existe.");
        }
        if (compra.getReembolso() != null || compra.getEstadoCompra() == EstadoCompra.REEMBOLSADA) {
            throw new CancelacionNoPermitidaException("Esta compra ya fue reembolsada anteriormente.");
        }
        if (compra.getEstadoCompra() != EstadoCompra.PAGADA) {
            throw new CancelacionNoPermitidaException("Solo se pueden reembolsar compras que hayan sido pagadas.");
        }

        LocalDateTime fechaFuncion = compra.getFechaHora();
        if (compra.getFuncion() != null) {
            fechaFuncion = compra.getFuncion().getFechaHora();
        }

        LocalDateTime ahora = LocalDateTime.now();
        double horasRestantes = Duration.between(ahora, fechaFuncion).toMinutes() / 60.0;
        this.politicaReembolso = seleccionarPolitica(horasRestantes, MotivoReembolso.CANCELACION_CLIENTE);

        double montoDevuelto = politicaReembolso.calcularReembolso(compra.getTotal(), horasRestantes);

        if (compra.getTarjetaVirtual() != null) {
            compra.getTarjetaVirtual().devolverSaldo(montoDevuelto);
        }

        String idReembolso = "REB-" + contadorReembolsos;
        contadorReembolsos++;

        Reembolso nuevoReembolso = new Reembolso(
                idReembolso,
                LocalDateTime.now(),
                montoDevuelto,
                politicaReembolso.obtenerPorcentaje(),
                MotivoReembolso.CANCELACION_CLIENTE,
                compra
        );

        compra.setReembolso(nuevoReembolso);
        compra.setEstadoCompra(EstadoCompra.REEMBOLSADA);
        historialReembolsos.add(nuevoReembolso);

        return nuevoReembolso;
    }

    public List<Reembolso> procesarReembolsoPorFuncion(List<Compra> comprasFuncion) {
        List<Reembolso> reembolsosGenerados = new ArrayList<>();
        this.politicaReembolso = new ReembolsoTotal();

        for (Compra compra : comprasFuncion) {
            if (compra.getReembolso() == null && compra.getEstadoCompra() == EstadoCompra.PAGADA) {
                double montoDevuelto = compra.getTotal();

                if (compra.getTarjetaVirtual() != null) {
                    compra.getTarjetaVirtual().devolverSaldo(montoDevuelto);
                }

                String idReembolso = "REB-" + contadorReembolsos;
                contadorReembolsos++;

                Reembolso reembolso = new Reembolso(
                        idReembolso,
                        LocalDateTime.now(),
                        montoDevuelto,
                        1.0,
                        MotivoReembolso.CANCELACION_FUNCION,
                        compra
                );

                compra.setReembolso(reembolso);
                compra.setEstadoCompra(EstadoCompra.REEMBOLSADA);
                historialReembolsos.add(reembolso);
                reembolsosGenerados.add(reembolso);
            }
        }
        return reembolsosGenerados;
    }

    private PoliticaReembolso seleccionarPolitica(double horasRestantes, MotivoReembolso motivo) {
        if (motivo == MotivoReembolso.CANCELACION_FUNCION || horasRestantes > 24.0) {
            return new ReembolsoTotal();
        } else if (horasRestantes >= 2.0) {
            return new ReembolsoParcial();
        } else {
            return new SinReembolso();
        }
    }

    public ArrayList<Reembolso> getHistorialReembolsos() {
        return historialReembolsos;
    }
}