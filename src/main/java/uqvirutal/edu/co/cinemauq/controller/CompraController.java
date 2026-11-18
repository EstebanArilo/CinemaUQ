package uqvirutal.edu.co.cinemauq.controller;

import uqvirutal.edu.co.cinemauq.model.*;
import java.util.ArrayList;
import java.util.List;

public class CompraController {

    private final Cine cine;
    private final FachadaCompra fachadaCompra;

    public CompraController() {
        this.cine = Cine.getInstancia();
        this.fachadaCompra = new FachadaCompra();
    }

    public List<Funcion> obtenerFuncionesDisponibles() {
        return cine.getListaFunciones();
    }

    public List<Asiento> obtenerAsientosFuncion(Funcion funcion) {
        if (funcion != null) {
            return funcion.getAsientosFuncion();
        }
        return new ArrayList<>();
    }

    public List<Usuario> obtenerUsuarios() {
        return cine.getListaUsuarios();
    }

    /**
     * Crea una entrada aplicando el patrón Decorator según el tipo seleccionado.
     */
    public Entrada crearEntradaDecorada(String idEntrada, Funcion funcion, Asiento asiento, String tipoEntrada, boolean incluirGafas3D) {
        Entrada entradaBase;

        if ("VIP".equalsIgnoreCase(tipoEntrada)) {
            entradaBase = new EntradaVIP(idEntrada, funcion.getIdFuncion(), asiento.getNumero(), funcion.getPrecioEntrada(), 5000.0);
        } else if ("3D".equalsIgnoreCase(tipoEntrada)) {
            entradaBase = new Entrada3D(idEntrada, funcion.getIdFuncion(), asiento.getNumero(), funcion.getPrecioEntrada(), 4000.0);
        } else {
            entradaBase = new EntradaGeneral(idEntrada, funcion.getIdFuncion(), asiento.getNumero(), funcion.getPrecioEntrada());
        }

        if (incluirGafas3D) {
            entradaBase = new ServicioGafas3D(entradaBase, 3000.0);
        }

        return entradaBase;
    }

    /**
     * Procesa la compra completa a través de la FachadaCompra.
     */
    public boolean realizarCompra(Cliente cliente, Funcion funcion, ArrayList<Asiento> asientosSeleccionados, ArrayList<ProductoConfiteria> productos) {
        return fachadaCompra.procesarCompra(cliente, funcion, asientosSeleccionados, productos);
    }

    /**
     * Permite cancelar una compra existente delegando al GestorReembolso mediante la fachada.
     */
    public Reembolso cancelarCompra(Compra compra) throws CancelacionNoPermitidaException {
        return fachadaCompra.cancelarCompra(compra);
    }
}