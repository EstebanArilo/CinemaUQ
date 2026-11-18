package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;

public class FachadaCompra {
    private int contadorCompras = 1;
    private GestorReembolso gestorReembolso = new GestorReembolso();

    /**
     *Procesa la compra de entradas y productos de confiteria
     * Verifica la disponibilidad de los asientos seleccionados y construye la compra mediante el patron builder
     * Valida y descuenta el saldo de la tarjeta virtual
     * Registra la compra como pagada en el historial de cliente y acumula los puntos
     * @param cliente
     * @param funcion
     * @param asientosSeleccionados
     * @param productos
     * @return
     */
    public boolean procesarCompra(Cliente cliente, Funcion funcion, ArrayList<Asiento> asientosSeleccionados, ArrayList<ProductoConfiteria> productos) {

        String idCompraStr = "CMP-" + contadorCompras;
        contadorCompras++;

        Compra.Builder compraBuilder = new Compra.Builder().idCompra(idCompraStr);

        for (Asiento asiento : asientosSeleccionados) {
            if (!asiento.isDisponible()) {
                System.out.println("Error: El asiento " + asiento.getNumero() + " ya se encuentra ocupado.");
                return false;
            }
            asiento.reservar();

            Entrada entrada = new EntradaGeneral(
                    "ENT-" + asiento.getNumero(),
                    funcion.getIdFuncion(),
                    asiento.getNumero(),
                    funcion.getPrecioEntrada()
            );
            compraBuilder.agregarEntrada(entrada);
        }

        if (productos != null) {
            for (ProductoConfiteria producto : productos) {
                compraBuilder.agregarProducto(producto);
            }
        }

        TarjetaVirtual tarjeta = cliente.getTarjetaVirtual();
        Compra compra = compraBuilder
                .tarjetaVirtual(tarjeta)
                .funcion(funcion)
                .build();
        double totalCompra = compra.getTotal();

        if (!validarYDescontarSaldo(tarjeta, totalCompra)) {
            for (Asiento asiento : asientosSeleccionados) {
                asiento.liberar();
            }
            System.out.println("Transacción rechazada: Tarjeta inactiva o saldo insuficiente.");
            return false;
        }

        compra.setEstadoCompra(EstadoCompra.PAGADA);
        cliente.getHistorialCompras().add(compra);

        double puntosGanados = totalCompra * 0.1;
        cliente.acumularPuntos(puntosGanados);

        System.out.println("¡Compra exitosa! ID: " + idCompraStr + " | Total pagado: $" + totalCompra + " | Puntos obtenidos: " + puntosGanados);
        return true;
    }

    /**
     * Valida y descueta el saldo de la tarjeta virtual despues de la compra
     * @param tarjeta
     * @param total
     * @return
     */
    public boolean validarYDescontarSaldo(TarjetaVirtual tarjeta, double total) {
        if (tarjeta == null || !tarjeta.isActiva()) {
            return false;
        }
        if (tarjeta.getSaldo() < total) {
            return false;
        }
        tarjeta.descontarSaldo(total);
        return true;
    }

    /**
     * Cancela la compra aplicando el reembolso
     * @param compra
     * @return
     * @throws CancelacionNoPermitidaException
     */
    public Reembolso cancelarCompra(Compra compra) throws CancelacionNoPermitidaException {
        return gestorReembolso.procesarReembolso(compra);
    }
}
