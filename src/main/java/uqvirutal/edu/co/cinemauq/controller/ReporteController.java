package uqvirutal.edu.co.cinemauq.controller;

import uqvirutal.edu.co.cinemauq.model.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReporteController {

    private final Cine cine;

    public ReporteController() {
        this.cine = Cine.getInstancia();
    }

    /**
     * Genera y exporta el reporte usando Factory Method + Bridge + Adapter,
     * y retorna el texto completo del reporte para previsualizarlo en la interfaz.
     */
    public String generarYExportarReporte(String tipoReporte, String formatoExportacion,
                                          LocalDate fechaInicio, LocalDate fechaFin, String rutaDestino) {
        Exportador exportador = crearExportador(formatoExportacion);
        FactoryReporte factory = crearFactory(tipoReporte);

        if (factory == null || exportador == null) {
            return "Error: Tipo de reporte o formato de exportación no válido.";
        }

        Administrador admin = obtenerAdministrador();
        if (admin != null) {
            admin.generarReporte(factory, fechaInicio, fechaFin, exportador, rutaDestino);
        } else {
            Reporte reporteDirecto = factory.crearReporte(fechaInicio, fechaFin, exportador);
            reporteDirecto.exportar(rutaDestino);
        }

        Reporte reporteVista = factory.crearReporte(fechaInicio, fechaFin, exportador);
        return reporteVista.generarEncabezado() + "\n" + reporteVista.generarContenido();
    }

    private Exportador crearExportador(String formato) {
        if ("PDF".equalsIgnoreCase(formato)) {
            return new PdfAdapter();
        }
        return new ExportadorTxt();
    }

    private FactoryReporte crearFactory(String tipoReporte) {
        if (tipoReporte == null) {
            return null;
        }

        switch (tipoReporte) {
            case "Ventas":
                return new FactoryReporteVenta(recopilarTodasLasCompras());
            case "Cancelaciones":
                return new FactoryReporteCancelacion(recopilarTodasLasCompras());
            case "Entradas Vendidas":
                return new FactoryReporteEntrada(recopilarTodasLasEntradas());
            case "Ocupación de Salas":
                return new FactoryReporteOcupacionSala(obtenerSalas(), obtenerFunciones());
            case "Productos de Confitería":
                return new FactoryReporteProductoConfiteria(recopilarProductosConfiteriaVendidos());
            case "Puntos de Clientes":
                return new FactoryReportePunto(obtenerClientes());
            case "Recargas":
                return new FactoryReporteRecarga(recopilarTodosLosMovimientos());
            default:
                return null;
        }
    }

    private ArrayList<Cliente> obtenerClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        if (cine.getListaUsuarios() != null) {
            for (Usuario u : cine.getListaUsuarios()) {
                if (u instanceof Cliente) {
                    clientes.add((Cliente) u);
                }
            }
        }
        return clientes;
    }

    private ArrayList<Compra> recopilarTodasLasCompras() {
        ArrayList<Compra> compras = new ArrayList<>();
        for (Cliente c : obtenerClientes()) {
            if (c.getHistorialCompras() != null) {
                for (Compra compra : c.getHistorialCompras()) {
                    compras.add(compra);
                }
            }
        }
        return compras;
    }

    private ArrayList<Entrada> recopilarTodasLasEntradas() {
        ArrayList<Entrada> entradas = new ArrayList<>();
        for (Compra compra : recopilarTodasLasCompras()) {
            if (compra.getEntradas() != null) {
                for (Entrada e : compra.getEntradas()) {
                    entradas.add(e);
                }
            }
        }
        return entradas;
    }

    private ArrayList<ProductoConfiteria> recopilarProductosConfiteriaVendidos() {
        ArrayList<ProductoConfiteria> productos = new ArrayList<>();
        for (Compra compra : recopilarTodasLasCompras()) {
            if (compra.getProductosConfiteria() != null) {
                for (ProductoConfiteria p : compra.getProductosConfiteria()) {
                    productos.add(p);
                }
            }
        }
        return productos;
    }

    private ArrayList<Movimiento> recopilarTodosLosMovimientos() {
        ArrayList<Movimiento> movimientos = new ArrayList<>();
        for (Cliente c : obtenerClientes()) {
            if (c.getTarjetaVirtual() != null && c.getTarjetaVirtual().getListMovimientos() != null) {
                for (Movimiento m : c.getTarjetaVirtual().getListMovimientos()) {
                    movimientos.add(m);
                }
            }
        }
        return movimientos;
    }

    private ArrayList<Sala> obtenerSalas() {
        if (cine.getListaSalas() != null) {
            return cine.getListaSalas();
        }
        return new ArrayList<>();
    }

    private ArrayList<Funcion> obtenerFunciones() {
        if (cine.getListaFunciones() != null) {
            return cine.getListaFunciones();
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