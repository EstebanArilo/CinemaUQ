package uqvirutal.edu.co.cinemauq.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Casos 12 y 13 del PDF: reportes (Factory Method) y exportación TXT/PDF (Adapter). */
class ReporteTest {

    private static final LocalDate INICIO = LocalDate.of(2026, 11, 1);
    private static final LocalDate FIN = LocalDate.of(2026, 11, 30);

    /** Exportador falso que guarda lo que recibe, para poder verificarlo. */
    private static class Captura implements Exportador {
        String datos;
        String ruta;
        int veces;

        @Override
        public void exportar(String datos, String rutaArchivo) {
            this.datos = datos;
            this.ruta = rutaArchivo;
            this.veces++;
        }
    }

    private static Compra compra(String id, double total, EstadoCompra estado) {
        Compra c = new Compra.Builder()
                .idCompra(id)
                .agregarEntrada(new EntradaGeneral("E-" + id, "F1", "A-1", total))
                .build();
        c.setEstadoCompra(estado);
        return c;
    }

    // ---------- Encabezado ----------

    @Test
    void elEncabezadoTraeTituloFechaDeGeneracionYPeriodo() {
        Reporte r = new FactoryReporteVenta(new ArrayList<Compra>()).crearReporte(INICIO, FIN, new Captura());
        String enc = r.generarEncabezado();

        assertTrue(enc.contains("CINEMA UQ - Reporte de Ventas"));
        assertTrue(enc.contains("Fecha de Generacion:"));
        assertTrue(enc.contains("Periodo: 2026-11-01 al 2026-11-30"));
    }

    @Test
    void sinFechasElPeriodoSeMuestraComoNoAplica() {
        Reporte r = new FactoryReporteVenta(new ArrayList<Compra>()).crearReporte(null, null, new Captura());
        assertTrue(r.generarEncabezado().contains("Periodo: N/A al N/A"));
    }

    // ---------- Contenido por tipo de reporte ----------

    @Test
    void reporteDeVentasListaLasComprasYSumaElTotal() {
        ArrayList<Compra> compras = new ArrayList<>();
        compras.add(compra("C1", 30000, EstadoCompra.PAGADA));
        compras.add(compra("C2", 20000, EstadoCompra.PAGADA));

        String contenido = new FactoryReporteVenta(compras).crearReporte(INICIO, FIN, null).generarContenido();

        assertTrue(contenido.contains("Compra ID: C1"));
        assertTrue(contenido.contains("Compra ID: C2"));
        assertTrue(contenido.contains("TOTAL GENERAL DE VENTAS: $50000.0"));
    }

    @Test
    void reporteDeVentasSinComprasLoIndicaYTotalizaCero() {
        String contenido = new FactoryReporteVenta(new ArrayList<Compra>())
                .crearReporte(INICIO, FIN, null).generarContenido();

        assertTrue(contenido.contains("No se encontraron compras"));
        assertTrue(contenido.contains("TOTAL GENERAL DE VENTAS: $0.0"));
    }

    @Test
    void reporteDeRecargasSoloCuentaLosMovimientosDeRecarga() {
        ArrayList<Movimiento> movs = new ArrayList<>();
        movs.add(new Movimiento("R-1", TipoMovimiento.RECARGA, 25000, INICIO));
        movs.add(new Movimiento("P-2", TipoMovimiento.PAGO, 9000, INICIO));
        movs.add(new Movimiento("R-3", TipoMovimiento.RECARGA, 5000, INICIO));

        String contenido = new FactoryReporteRecarga(movs).crearReporte(INICIO, FIN, null).generarContenido();

        assertTrue(contenido.contains("ID Mov: R-1"));
        assertTrue(contenido.contains("ID Mov: R-3"));
        assertTrue(!contenido.contains("P-2"));
        assertTrue(contenido.contains("TOTAL RECARGADO: $30000.0"));
    }

    @Test
    void reporteDeCancelacionesSoloListaLasComprasCanceladas() {
        ArrayList<Compra> compras = new ArrayList<>();
        compras.add(compra("CAN-1", 10000, EstadoCompra.CANCELADA));
        compras.add(compra("PAG-2", 20000, EstadoCompra.PAGADA));

        String contenido = new FactoryReporteCancelacion(compras).crearReporte(INICIO, FIN, null).generarContenido();

        assertTrue(contenido.contains("CAN-1"));
        assertTrue(!contenido.contains("PAG-2"));
    }

    @Test
    void reporteDePuntosSumaLosLotesDeCadaCliente() {
        Cliente c = cliente("1", 0);
        c.acumularPuntos(100);
        c.acumularPuntos(50.5);
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(c);

        String contenido = new FactoryReportePunto(clientes).crearReporte(INICIO, FIN, null).generarContenido();

        assertTrue(contenido.contains("Cliente 1"));
        assertTrue(contenido.contains("Puntos Acumulados: 150.5"));
    }

    @Test
    void reporteDeConfiteriaListaNombreYPrecio() {
        ArrayList<ProductoConfiteria> productos = new ArrayList<>();
        productos.add(new ProductoSimple("P1", 12000, "Palomitas", CategoriaProducto.SNACK));

        String contenido = new FactoryReporteProductoConfiteria(productos)
                .crearReporte(INICIO, FIN, null).generarContenido();

        assertTrue(contenido.contains("Palomitas"));
        assertTrue(contenido.contains("$12000.0"));
    }

    @Test
    void reporteDeEntradasListaIdYAsiento() {
        ArrayList<Entrada> entradas = new ArrayList<>();
        entradas.add(new EntradaGeneral("ENT-1", "F1", "A-1", 10000));

        String contenido = new FactoryReporteEntrada(entradas).crearReporte(INICIO, FIN, null).generarContenido();

        assertTrue(contenido.contains("Entrada ID: ENT-1"));
        assertTrue(contenido.contains("Asiento: A-1"));
    }

    @Test
    void reporteDeOcupacionListaLasFunciones() {
        ArrayList<Funcion> funciones = new ArrayList<>();
        funciones.add(funcion(5, 10000));
        ArrayList<Sala> salas = new ArrayList<>();
        salas.add(sala());

        String contenido = new FactoryReporteOcupacionSala(salas, funciones)
                .crearReporte(INICIO, FIN, null).generarContenido();

        assertTrue(contenido.contains("Funcion ID: F1"));
        assertTrue(contenido.contains("Dune"));
        assertTrue(contenido.contains("Sala 1"));
    }

    @Test
    void reportesSinRegistrosIndicanQueNoHayDatos() {
        assertTrue(new FactoryReporteRecarga(new ArrayList<Movimiento>()).crearReporte(INICIO, FIN, null)
                .generarContenido().contains("No hay movimientos"));
        assertTrue(new FactoryReporteCancelacion(new ArrayList<Compra>()).crearReporte(INICIO, FIN, null)
                .generarContenido().contains("No hay compras canceladas"));
        assertTrue(new FactoryReportePunto(new ArrayList<Cliente>()).crearReporte(INICIO, FIN, null)
                .generarContenido().contains("No hay clientes"));
        assertTrue(new FactoryReporteProductoConfiteria(new ArrayList<ProductoConfiteria>()).crearReporte(INICIO, FIN, null)
                .generarContenido().contains("No hay productos"));
        assertTrue(new FactoryReporteEntrada(new ArrayList<Entrada>()).crearReporte(INICIO, FIN, null)
                .generarContenido().contains("No se encontraron entradas"));
        assertTrue(new FactoryReporteOcupacionSala(new ArrayList<Sala>(), new ArrayList<Funcion>())
                .crearReporte(INICIO, FIN, null).generarContenido().contains("No hay funciones"));
    }

    // ---------- Factory Method ----------

    @Test
    void cadaFabricaCreaSuTipoDeReporte() {
        assertTrue(new FactoryReporteVenta(new ArrayList<Compra>()).crearReporte(INICIO, FIN, null) instanceof ReporteVenta);
        assertTrue(new FactoryReporteRecarga(new ArrayList<Movimiento>()).crearReporte(INICIO, FIN, null) instanceof ReporteRecarga);
        assertTrue(new FactoryReporteCancelacion(new ArrayList<Compra>()).crearReporte(INICIO, FIN, null) instanceof ReporteCancelacion);
        assertTrue(new FactoryReportePunto(new ArrayList<Cliente>()).crearReporte(INICIO, FIN, null) instanceof ReportePunto);
        assertTrue(new FactoryReporteEntrada(new ArrayList<Entrada>()).crearReporte(INICIO, FIN, null) instanceof ReporteEntrada);
        assertTrue(new FactoryReporteProductoConfiteria(new ArrayList<ProductoConfiteria>()).crearReporte(INICIO, FIN, null) instanceof ReporteProductoConfiteria);
        assertTrue(new FactoryReporteOcupacionSala(new ArrayList<Sala>(), new ArrayList<Funcion>()).crearReporte(INICIO, FIN, null) instanceof ReporteOcupacionSala);
    }

    // ---------- Exportación ----------

    @Test
    void exportarEntregaEncabezadoMasContenidoYLaRutaIndicada() {
        Captura cap = new Captura();
        ArrayList<Compra> compras = new ArrayList<>();
        compras.add(compra("C1", 30000, EstadoCompra.PAGADA));
        Reporte r = new FactoryReporteVenta(compras).crearReporte(INICIO, FIN, cap);

        r.exportar("reportes/ventas");

        assertTrue(cap.veces == 1);
        assertTrue(cap.ruta.equals("reportes/ventas"));
        assertTrue(cap.datos.contains("CINEMA UQ - Reporte de Ventas"));
        assertTrue(cap.datos.contains("Periodo: 2026-11-01 al 2026-11-30"));
        assertTrue(cap.datos.contains("Compra ID: C1"));
        assertTrue(cap.datos.contains("TOTAL GENERAL DE VENTAS: $30000.0"));
    }

    @Test
    void exportarSinExportadorNoLanzaExcepcion() {
        Reporte r = new FactoryReporteVenta(new ArrayList<Compra>()).crearReporte(INICIO, FIN, null);
        r.exportar("ruta");
        assertTrue(r.getExportador() == null);
    }

    @Test
    void sePuedeCambiarElExportadorDeUnReporte() {
        Captura primero = new Captura();
        Captura segundo = new Captura();
        Reporte r = new FactoryReporteVenta(new ArrayList<Compra>()).crearReporte(INICIO, FIN, primero);

        r.setExportador(segundo);
        r.exportar("ruta");

        assertTrue(primero.veces == 0);
        assertTrue(segundo.veces == 1);
    }

    @Test
    void elAdministradorGeneraYExportaUnReporte() {
        Administrador admin = new Administrador("ADM", "Admin", "admin@uq.edu.co", "root");
        Captura cap = new Captura();
        ArrayList<Compra> compras = new ArrayList<>();
        compras.add(compra("C1", 15000, EstadoCompra.PAGADA));

        admin.generarReporte(new FactoryReporteVenta(compras), INICIO, FIN, cap, "salida/ventas");

        assertTrue(cap.veces == 1);
        assertTrue(cap.ruta.equals("salida/ventas"));
        assertTrue(cap.datos.contains("$15000.0"));
    }

    @Test
    void losExportadoresTxtYPdfFuncionanSinLanzarExcepciones() {
        boolean ok = true;
        try {
            new ExportadorTxt().exportar("contenido", "reporte");
            new PdfAdapter().exportar("contenido", "reporte");
        } catch (RuntimeException e) {
            ok = false;
        }
        assertTrue(ok);
    }

    @Test
    void txtYPdfSonExportadoresIntercambiables() {
        Exportador txt = new ExportadorTxt();
        Exportador pdf = new PdfAdapter();
        Reporte r = new FactoryReporteVenta(new ArrayList<Compra>()).crearReporte(INICIO, FIN, txt);

        r.exportar("a");
        r.setExportador(pdf);
        r.exportar("b");

        assertTrue(r.getExportador() == pdf);
    }

    // ---------- Ayudas ----------

    private static Cliente cliente(String id, double saldo) {
        Cliente c = new Cliente(id, "Cliente " + id, id + "@uq.edu.co", "clave" + id, "TJ-" + id);
        c.getTarjetaVirtual().setSaldo(saldo);
        return c;
    }

    private static Pelicula pelicula() {
        return new Pelicula("P1", "Dune", "Ciencia ficcion", 155, "Arrakis", 12);
    }

    /** Sala de 2 filas x 3 asientos (A-1..A-3, B-1..B-3). */
    private static Sala sala() {
        return new Sala(1, "Sala 1", 2, 3);
    }

    private static Funcion funcion(long horasHaciaAdelante, double precio) {
        Sala sala = sala();
        return new Funcion("F1", pelicula(), sala,
                java.time.LocalDateTime.now().plusMinutes(horasHaciaAdelante * 60), precio,
                sala.getListaAsientos());
    }
}