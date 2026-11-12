package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;

public class FachadaCompra {
    private int contadorCompras = 1;

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

        Compra compra = compraBuilder.build();
        double totalCompra = compra.getTotal();

        TarjetaVirtual tarjeta = cliente.getTarjetaVirtual();
        if (!validarYDescontarSaldo(tarjeta, totalCompra)) {
            for (Asiento asiento : asientosSeleccionados) {
                asiento.liberar();
            }
            System.out.println("Transacción rechazada: Tarjeta inactiva o saldo insuficiente.");
            return false;
        }

        compra.setEstadoCompra("COMPLETADA");
        cliente.getHistorialCompras().add(compra);

        double puntosGanados = totalCompra * 0.1;
        cliente.acumularPuntos(puntosGanados);

        System.out.println("¡Compra exitosa! ID: " + idCompraStr + " | Total pagado: $" + totalCompra + " | Puntos obtenidos: " + puntosGanados);
        return true;
    }

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
    public boolean cancelarCompra(String idCompra) {
        System.out.println("Gestionando cancelación para la compra: " + idCompra);
        return true;
    }
}
