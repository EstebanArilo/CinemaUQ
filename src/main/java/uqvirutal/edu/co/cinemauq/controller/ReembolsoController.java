package uqvirutal.edu.co.cinemauq.controller;

import uqvirutal.edu.co.cinemauq.model.*;
import java.util.ArrayList;
import java.util.List;

public class ReembolsoController {

    private final Cine cine;
    private final GestorReembolso gestorReembolso;

    public ReembolsoController() {
        this.cine = Cine.getInstancia();
        this.gestorReembolso = new GestorReembolso();
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

    public List<Compra> obtenerComprasPagadasCliente(Cliente cliente) {
        List<Compra> pagadas = new ArrayList<>();
        if (cliente != null && cliente.getHistorialCompras() != null) {
            for (Compra c : cliente.getHistorialCompras()) {
                if (c.getEstadoCompra() == EstadoCompra.PAGADA) {
                    pagadas.add(c);
                }
            }
        }
        return pagadas;
    }

    public List<Funcion> obtenerFunciones() {
        if (cine.getListaFunciones() != null) {
            return cine.getListaFunciones();
        }
        return new ArrayList<>();
    }

    /**
     * Procesa el reembolso individual de una compra de cliente y libera sus asientos.
     */
    public Reembolso solicitarReembolsoCliente(Compra compra) throws CancelacionNoPermitidaException {
        Reembolso reembolso = gestorReembolso.procesarReembolso(compra);
        liberarAsientosDeCompra(compra);
        return reembolso;
    }

    /**
     * Cancela una función (notificando via Administrador si existe) y reembolsa al 100%
     * todas las compras pagadas asociadas a dicha función.
     */
    public List<Reembolso> cancelarFuncionYReembolsar(Funcion funcion) {
        if (funcion == null) {
            return new ArrayList<>();
        }

        Administrador admin = obtenerAdministrador();
        if (admin != null) {
            admin.cancelarFuncion(funcion);
        } else {
            funcion.cancelarFuncion();
        }

        List<Compra> comprasAfectadas = new ArrayList<>();
        for (Cliente cliente : obtenerClientes()) {
            if (cliente.getHistorialCompras() != null) {
                for (Compra compra : cliente.getHistorialCompras()) {
                    if (compra.getFuncion() == funcion && compra.getEstadoCompra() == EstadoCompra.PAGADA) {
                        comprasAfectadas.add(compra);
                    }
                }
            }
        }

        List<Reembolso> generados = gestorReembolso.procesarReembolsoPorFuncion(comprasAfectadas);
        for (Compra compra : comprasAfectadas) {
            liberarAsientosDeCompra(compra);
        }

        return generados;
    }

    public List<Reembolso> obtenerHistorialReembolsos() {
        return gestorReembolso.getHistorialReembolsos();
    }

    private void liberarAsientosDeCompra(Compra compra) {
        if (compra != null && compra.getFuncion() != null && compra.getEntradas() != null) {
            List<Asiento> asientosFuncion = compra.getFuncion().getAsientosFuncion();
            if (asientosFuncion != null) {
                for (Entrada entrada : compra.getEntradas()) {
                    for (Asiento asiento : asientosFuncion) {
                        if (asiento.getNumero() != null && asiento.getNumero().equals(entrada.getAsiento())) {
                            asiento.liberar();
                        }
                    }
                }
            }
        }
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