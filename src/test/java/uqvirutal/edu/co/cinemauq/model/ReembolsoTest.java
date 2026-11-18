package uqvirutal.edu.co.cinemauq.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Casos 8 y 11 del PDF: cancelaciones, reembolsos (100 % / 80 % / ninguno) y cancelación de funciones. */
class ReembolsoTest {

    private static boolean rechaza(GestorReembolso gestor, Compra compra) {
        try {
            gestor.procesarReembolso(compra);
            return false;
        } catch (CancelacionNoPermitidaException e) {
            return true;
        }
    }

    // ---------- Políticas ----------

    @Test
    void reembolsoTotalDevuelveTodo() throws CancelacionNoPermitidaException {
        PoliticaReembolso p = new ReembolsoTotal();
        assertTrue(igual(p.obtenerPorcentaje(), 1.0));
        assertTrue(igual(p.calcularReembolso(50000, 48), 50000));
    }

    @Test
    void reembolsoParcialDevuelveElOchentaPorCiento() throws CancelacionNoPermitidaException {
        PoliticaReembolso p = new ReembolsoParcial();
        assertTrue(igual(p.obtenerPorcentaje(), 0.8));
        assertTrue(igual(p.calcularReembolso(50000, 10), 40000));
    }

    @Test
    void sinReembolsoLanzaExcepcionYPorcentajeCero() {
        PoliticaReembolso p = new SinReembolso();
        assertTrue(igual(p.obtenerPorcentaje(), 0.0));
        boolean lanzo = false;
        try {
            p.calcularReembolso(50000, 1);
        } catch (CancelacionNoPermitidaException e) {
            lanzo = true;
        }
        assertTrue(lanzo);
    }

    // ---------- Cancelación por el cliente ----------

    @Test
    void conMasDe24HorasSeDevuelveElCienPorCiento() throws CancelacionNoPermitidaException {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        Compra c = compraPagada("C1", t, funcion(30, 50000), 50000);

        Reembolso r = new GestorReembolso().procesarReembolso(c);

        assertTrue(igual(r.getMontoDevuelto(), 50000));
        assertTrue(igual(r.getPorcentajeAplicado(), 1.0));
        assertTrue(r.getMotivo() == MotivoReembolso.CANCELACION_CLIENTE);
        assertTrue(igual(t.getSaldo(), 50000));
        assertTrue(c.getEstadoCompra() == EstadoCompra.REEMBOLSADA);
        assertTrue(c.getReembolso() == r);
    }

    @Test
    void entre2y24HorasSeDevuelveElOchentaPorCiento() throws CancelacionNoPermitidaException {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        Compra c = compraPagada("C1", t, funcion(10, 50000), 50000);

        Reembolso r = new GestorReembolso().procesarReembolso(c);

        assertTrue(igual(r.getMontoDevuelto(), 40000));
        assertTrue(igual(r.getPorcentajeAplicado(), 0.8));
        assertTrue(igual(t.getSaldo(), 40000));
        assertTrue(c.getEstadoCompra() == EstadoCompra.REEMBOLSADA);
    }

    @Test
    void justoAntesDeLas24HorasAplicaElOchentaPorCiento() throws CancelacionNoPermitidaException {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        Compra c = compraPagada("C1", t, funcionEnMinutos(23 * 60 + 59, 10000), 10000);

        Reembolso r = new GestorReembolso().procesarReembolso(c);

        assertTrue(igual(r.getPorcentajeAplicado(), 0.8));
    }

    @Test
    void conMenosDe2HorasNoSePermiteCancelar() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 1000, true);
        Compra c = compraPagada("C1", t, funcionEnMinutos(90, 50000), 50000);
        GestorReembolso gestor = new GestorReembolso();

        assertTrue(rechaza(gestor, c));
        assertTrue(c.getEstadoCompra() == EstadoCompra.PAGADA);
        assertTrue(c.getReembolso() == null);
        assertTrue(igual(t.getSaldo(), 1000));
        assertTrue(t.getListMovimientos().isEmpty());
        assertTrue(gestor.getHistorialReembolsos().isEmpty());
    }

    @Test
    void unaFuncionQueYaEmpezoNoSePuedeCancelar() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        Compra c = compraPagada("C1", t, funcionEnMinutos(-60, 50000), 50000);

        assertTrue(rechaza(new GestorReembolso(), c));
        assertTrue(igual(t.getSaldo(), 0));
    }

    @Test
    void elReembolsoRegistraUnMovimientoEnLaTarjeta() throws CancelacionNoPermitidaException {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        Compra c = compraPagada("C1", t, funcion(30, 20000), 20000);

        new GestorReembolso().procesarReembolso(c);

        assertTrue(t.getListMovimientos().size() == 1);
        Movimiento m = t.getListMovimientos().get(0);
        assertTrue(m.getTipoMovimiento() == TipoMovimiento.REEMBOLSO);
        assertTrue(igual(m.getMonto(), 20000));
    }

    // ---------- Reembolso duplicado y compras inválidas ----------

    @Test
    void elMismoReembolsoNoSePuedeProcesarDosVeces() throws CancelacionNoPermitidaException {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        Compra c = compraPagada("C1", t, funcion(30, 50000), 50000);
        GestorReembolso gestor = new GestorReembolso();

        gestor.procesarReembolso(c);

        assertTrue(rechaza(gestor, c));
        assertTrue(igual(t.getSaldo(), 50000));
        assertTrue(gestor.getHistorialReembolsos().size() == 1);
    }

    @Test
    void compraNulaSeRechaza() {
        assertTrue(rechaza(new GestorReembolso(), null));
    }

    @Test
    void compraNoPagadaSeRechaza() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        Compra c = compraPagada("C1", t, funcion(30, 50000), 50000);
        c.setEstadoCompra(EstadoCompra.PENDIENTE);

        assertTrue(rechaza(new GestorReembolso(), c));
        assertTrue(igual(t.getSaldo(), 0));
    }

    @Test
    void elDineroVuelveSoloALaTarjetaOriginal() throws CancelacionNoPermitidaException {
        TarjetaVirtual original = new TarjetaVirtual("T1", 0, true);
        TarjetaVirtual otra = new TarjetaVirtual("T2", 5000, true);
        Compra c = compraPagada("C1", original, funcion(30, 30000), 30000);

        new GestorReembolso().procesarReembolso(c);

        assertTrue(igual(original.getSaldo(), 30000));
        assertTrue(igual(otra.getSaldo(), 5000));
        assertTrue(otra.getListMovimientos().isEmpty());
    }

    @Test
    void losIdentificadoresDeReembolsoSonConsecutivos() throws CancelacionNoPermitidaException {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        Funcion f = funcion(30, 10000);
        GestorReembolso gestor = new GestorReembolso();

        Reembolso r1 = gestor.procesarReembolso(compraPagada("C1", t, f, 10000));
        Reembolso r2 = gestor.procesarReembolso(compraPagada("C2", t, f, 10000));

        assertTrue(r1.getId().equals("REB-1"));
        assertTrue(r2.getId().equals("REB-2"));
        assertTrue(gestor.getHistorialReembolsos().size() == 2);
    }

    @Test
    void compraSinTarjetaSeReembolsaSinFallar() throws CancelacionNoPermitidaException {
        Compra c = new Compra.Builder()
                .idCompra("C1")
                .funcion(funcion(30, 10000))
                .agregarEntrada(new EntradaGeneral("E1", "F1", "A-1", 10000))
                .build();
        c.setEstadoCompra(EstadoCompra.PAGADA);

        Reembolso r = new GestorReembolso().procesarReembolso(c);

        assertTrue(igual(r.getMontoDevuelto(), 10000));
        assertTrue(c.getEstadoCompra() == EstadoCompra.REEMBOLSADA);
    }

    // ---------- Cancelación de la función por el cine (caso 11) ----------

    @Test
    void cancelarFuncionReembolsaElCienPorCientoATodasLasComprasPagadas() {
        Funcion f = funcionEnMinutos(30, 20000); // faltan menos de 2 h: igual se devuelve todo
        TarjetaVirtual t1 = new TarjetaVirtual("T1", 0, true);
        TarjetaVirtual t2 = new TarjetaVirtual("T2", 0, true);
        Compra c1 = compraPagada("C1", t1, f, 20000);
        Compra c2 = compraPagada("C2", t2, f, 40000);

        List<Compra> compras = new ArrayList<>();
        compras.add(c1);
        compras.add(c2);
        List<Reembolso> reembolsos = new GestorReembolso().procesarReembolsoPorFuncion(compras);

        assertTrue(reembolsos.size() == 2);
        assertTrue(igual(t1.getSaldo(), 20000));
        assertTrue(igual(t2.getSaldo(), 40000));
        assertTrue(c1.getEstadoCompra() == EstadoCompra.REEMBOLSADA);
        assertTrue(c2.getEstadoCompra() == EstadoCompra.REEMBOLSADA);
        for (Reembolso r : reembolsos) {
            assertTrue(r.getMotivo() == MotivoReembolso.CANCELACION_FUNCION);
            assertTrue(igual(r.getPorcentajeAplicado(), 1.0));
        }
    }

    @Test
    void cancelarFuncionIgnoraComprasPendientesYYaReembolsadas() throws CancelacionNoPermitidaException {
        Funcion f = funcion(30, 10000);
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        Compra pendiente = compraPagada("C1", t, f, 10000);
        pendiente.setEstadoCompra(EstadoCompra.PENDIENTE);
        Compra yaReembolsada = compraPagada("C2", t, f, 10000);
        GestorReembolso gestor = new GestorReembolso();
        gestor.procesarReembolso(yaReembolsada);
        Compra pagada = compraPagada("C3", t, f, 10000);

        List<Compra> compras = new ArrayList<>();
        compras.add(pendiente);
        compras.add(yaReembolsada);
        compras.add(pagada);
        List<Reembolso> reembolsos = gestor.procesarReembolsoPorFuncion(compras);

        assertTrue(reembolsos.size() == 1);
        assertTrue(reembolsos.get(0).getCompra() == pagada);
        assertTrue(pendiente.getEstadoCompra() == EstadoCompra.PENDIENTE);
        assertTrue(pendiente.getReembolso() == null);
        assertTrue(igual(t.getSaldo(), 20000)); // 10000 de C2 + 10000 de C3
    }

    @Test
    void cancelarFuncionDosVecesNoDuplicaReembolsos() {
        Funcion f = funcion(30, 10000);
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        List<Compra> compras = new ArrayList<>();
        compras.add(compraPagada("C1", t, f, 10000));
        GestorReembolso gestor = new GestorReembolso();

        gestor.procesarReembolsoPorFuncion(compras);
        List<Reembolso> segundo = gestor.procesarReembolsoPorFuncion(compras);

        assertTrue(segundo.isEmpty());
        assertTrue(igual(t.getSaldo(), 10000));
        assertTrue(gestor.getHistorialReembolsos().size() == 1);
    }

    @Test
    void cancelarFuncionSinComprasNoGeneraReembolsos() {
        List<Reembolso> reembolsos = new GestorReembolso().procesarReembolsoPorFuncion(new ArrayList<Compra>());
        assertTrue(reembolsos.isEmpty());
    }

    // ---------- Ayudas ----------

    private static boolean igual(double a, double b) {
        return Math.abs(a - b) < 0.0001;
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

    private static Funcion funcionEnMinutos(long minutos, double precio) {
        Sala sala = sala();
        return new Funcion("F1", pelicula(), sala,
                java.time.LocalDateTime.now().plusMinutes(minutos), precio, sala.getListaAsientos());
    }

    /** Compra PAGADA de un solo valor, asociada a la tarjeta y la funcion dadas. */
    private static Compra compraPagada(String id, TarjetaVirtual tarjeta, Funcion funcion, double total) {
        Compra c = new Compra.Builder()
                .idCompra(id)
                .tarjetaVirtual(tarjeta)
                .funcion(funcion)
                .agregarEntrada(new EntradaGeneral("ENT-" + id, funcion.getIdFuncion(), "A-1", total))
                .build();
        c.setEstadoCompra(EstadoCompra.PAGADA);
        return c;
    }
}