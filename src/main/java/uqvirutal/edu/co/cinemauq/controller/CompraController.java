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
        List<Funcion> activas = new ArrayList<>();
        if (cine.getListaFunciones() != null) {
            for (Funcion f : cine.getListaFunciones()) {
                if (!ReembolsoController.estaCancelada(f)) {
                    activas.add(f);
                }
            }
        }
        return activas;
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


    public boolean realizarCompra(Cliente cliente, Funcion funcion, ArrayList<Asiento> asientosSeleccionados, ArrayList<ProductoConfiteria> productos) {
        return fachadaCompra.procesarCompra(cliente, funcion, asientosSeleccionados, productos);
    }


    public Reembolso cancelarCompra(Compra compra) throws CancelacionNoPermitidaException {
        return fachadaCompra.cancelarCompra(compra);
    }
}
