package uqvirutal.edu.co.cinemauq.controller;

import uqvirutal.edu.co.cinemauq.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TarjetaVirtualController {

    private final Cine cine;

    public TarjetaVirtualController() {
        this.cine = Cine.getInstancia();
    }

    public List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        if (cine.getListaUsuarios() != null) {
            for (Usuario u : cine.getListaUsuarios()) {
                if (u instanceof Cliente) {
                    clientes.add((Cliente) u);
                }
            }
        }
        return clientes;
    }

    public boolean recargarTarjeta(Cliente cliente, double monto) {
        if (cliente == null || cliente.getTarjetaVirtual() == null || monto <= 0) {
            return false;
        }
        TarjetaVirtual tarjeta = cliente.getTarjetaVirtual();
        if (!tarjeta.isActiva()) {
            return false;
        }

        Administrador admin = obtenerAdministrador();
        if (admin != null) {
            admin.recargarTarjeta(tarjeta, monto);
        } else {
            tarjeta.recargar(monto);
        }

        return true;
    }

    public void cambiarEstadoTarjeta(Cliente cliente, boolean activa) {
        if (cliente != null && cliente.getTarjetaVirtual() != null) {
            cliente.getTarjetaVirtual().setActiva(activa);
        }
    }

    public double calcularPuntosVigentes(Cliente cliente) {
        double totalPuntos = 0.0;
        if (cliente != null && cliente.getListPuntos() != null) {
            LocalDate hoy = LocalDate.now();
            for (Punto p : cliente.getListPuntos()) {
                if (p.getFechaVencimiento() != null && !p.getFechaVencimiento().isBefore(hoy)) {
                    totalPuntos += p.getCantidad();
                }
            }
        }
        return totalPuntos;
    }

    public boolean redimirPuntosPorSaldo(Cliente cliente, double puntosARedimir) {
        if (cliente == null || cliente.getTarjetaVirtual() == null || puntosARedimir <= 0) {
            return false;
        }
        if (!cliente.getTarjetaVirtual().isActiva()) {
            return false;
        }

        double disponibles = calcularPuntosVigentes(cliente);
        if (puntosARedimir > disponibles) {
            return false;
        }

        double restantePorDescontar = puntosARedimir;
        LocalDate hoy = LocalDate.now();

        for (Punto p : cliente.getListPuntos()) {
            if (restantePorDescontar <= 0) {
                break;
            }
            if (p.getFechaVencimiento() != null && !p.getFechaVencimiento().isBefore(hoy) && p.getCantidad() > 0) {
                if (p.getCantidad() <= restantePorDescontar) {
                    restantePorDescontar -= p.getCantidad();
                    p.setCantidad(0);
                } else {
                    p.setCantidad(p.getCantidad() - restantePorDescontar);
                    restantePorDescontar = 0;
                }
            }
        }

        double saldoEquivalente = puntosARedimir * 100.0;
        cliente.getTarjetaVirtual().recargar(saldoEquivalente);
        return true;
    }

    public List<Movimiento> obtenerMovimientos(Cliente cliente) {
        if (cliente != null && cliente.getTarjetaVirtual() != null) {
            return cliente.getTarjetaVirtual().getListMovimientos();
        }
        return new ArrayList<>();
    }

    private Administrador obtenerAdministrador() {
        if (cine.getListaUsuarios() != null) {
            for (Usuario u : cine.getListaUsuarios()) {
                if (u instanceof Administrador) {
                    return (Administrador) u;
                }
            }
        }
        return null;
    }
}