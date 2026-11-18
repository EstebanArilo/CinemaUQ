package uqvirutal.edu.co.cinemauq.controller;

import uqvirutal.edu.co.cinemauq.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReporteController {

    private final Cine cine;

    public ReporteController() {
        this.cine = Cine.getInstancia();
    }

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


    public double calcularTotalVentasPagadas() {
        double total = 0.0;
        for (Compra c : recopilarTodasLasCompras()) {
            if (c.getEstadoCompra() == EstadoCompra.PAGADA) {
                total += c.getTotal();
            }
        }
        return total;
    }

    public int contarTotalEntradasVendidas() {
        int contador = 0;
        for (Compra c : recopilarTodasLasCompras()) {
            if (c.getEstadoCompra() == EstadoCompra.PAGADA && c.getEntradas() != null) {
                for (Entrada e : c.getEntradas()) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public int contarTotalReembolsos() {
        int contador = 0;
        for (Compra c : recopilarTodasLasCompras()) {
            if (c.getEstadoCompra() == EstadoCompra.REEMBOLSADA) {
                contador++;
            }
        }
        return contador;
    }

    public int[] obtenerConteoAsientosOcupadosYDisponibles() {
        int ocupados = 0;
        int disponibles = 0;
        for (Funcion f : obtenerFunciones()) {
            if (f.getAsientosFuncion() != null) {
                for (Asiento a : f.getAsientosFuncion()) {
                    if (a.isDisponible()) {
                        disponibles++;
                    } else {
                        ocupados++;
                    }
                }
            }
        }
        return new int[]{ocupados, disponibles};
    }

    public Map<String, Double> obtenerVentasPorPelicula() {
        Map<String, Double> mapa = new LinkedHashMap<>();
        if (cine.getListaPeliculas() != null) {
            for (Pelicula p : cine.getListaPeliculas()) {
                mapa.put(p.getTitulo(), 0.0);
            }
        }
        for (Compra c : recopilarTodasLasCompras()) {
            if (c.getEstadoCompra() == EstadoCompra.PAGADA && c.getFuncion() != null && c.getFuncion().getPelicula() != null) {
                String titulo = c.getFuncion().getPelicula().getTitulo();
                double actual = mapa.containsKey(titulo) ? mapa.get(titulo) : 0.0;
                mapa.put(titulo, actual + c.getTotal());
            }
        }
        return mapa;
    }

    public Map<String, Integer> obtenerProductosMasVendidos() {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        for (Compra c : recopilarTodasLasCompras()) {
            if (c.getEstadoCompra() == EstadoCompra.PAGADA && c.getProductosConfiteria() != null) {
                for (ProductoConfiteria p : c.getProductosConfiteria()) {
                    String nombre = (p.getNombre() != null) ? p.getNombre() : "Producto";
                    int actual = mapa.containsKey(nombre) ? mapa.get(nombre) : 0;
                    mapa.put(nombre, actual + 1);
                }
            }
        }
        return mapa;
    }

    public Map<String, Double> obtenerDistribucionVentasPorCategoria() {
        double totalEntradas = 0.0;
        double totalConfiteria = 0.0;

        for (Compra c : recopilarTodasLasCompras()) {
            if (c.getEstadoCompra() == EstadoCompra.PAGADA) {
                if (c.getEntradas() != null) {
                    for (Entrada e : c.getEntradas()) {
                        totalEntradas += e.getCosto();
                    }
                }
                if (c.getProductosConfiteria() != null) {
                    for (ProductoConfiteria p : c.getProductosConfiteria()) {
                        totalConfiteria += p.getPrecio();
                    }
                }
            }
        }

        Map<String, Double> categorias = new LinkedHashMap<>();
        categorias.put("Entradas", totalEntradas);
        categorias.put("Confitería", totalConfiteria);
        return categorias;
    }

    public Map<String, Double> obtenerEvolucionVentasPorFecha() {
        Map<String, Double> serie = new LinkedHashMap<>();
        for (Compra c : recopilarTodasLasCompras()) {
            if (c.getEstadoCompra() == EstadoCompra.PAGADA && c.getFechaHora() != null) {
                String fecha = c.getFechaHora().toLocalDate().toString();
                double actual = serie.containsKey(fecha) ? serie.get(fecha) : 0.0;
                serie.put(fecha, actual + c.getTotal());
            }
        }
        return serie;
    }

    public Map<String, Double> obtenerEvolucionRecargasPorFecha() {
        Map<String, Double> serie = new LinkedHashMap<>();
        for (Movimiento m : recopilarTodosLosMovimientos()) {
            if (m.getTipoMovimiento() == TipoMovimiento.RECARGA && m.getFecha() != null) {
                String fecha = m.getFecha().toString();
                double actual = serie.containsKey(fecha) ? serie.get(fecha) : 0.0;
                serie.put(fecha, actual + m.getMonto());
            }
        }
        return serie;
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
        return (cine.getListaSalas() != null) ? cine.getListaSalas() : new ArrayList<>();
    }

    private ArrayList<Funcion> obtenerFunciones() {
        return (cine.getListaFunciones() != null) ? cine.getListaFunciones() : new ArrayList<>();
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