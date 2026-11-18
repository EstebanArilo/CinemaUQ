package uqvirutal.edu.co.cinemauq.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Casos 5, 6 y 7 del PDF: pagos, saldo/movimientos y recargas. */
class TarjetaVirtualTest {

    @Test
    void pagoConSaldoSuficienteDescuentaYRegistraMovimiento() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 50000, true);
        assertTrue(t.descontarSaldo(20000));
        assertTrue(igual(t.getSaldo(), 30000));
        assertTrue(t.getListMovimientos().size() == 1);
        Movimiento m = t.getListMovimientos().get(0);
        assertTrue(m.getTipoMovimiento() == TipoMovimiento.PAGO);
        assertTrue(igual(m.getMonto(), 20000));
    }

    @Test
    void pagoConSaldoInsuficienteSeRechazaSinCambios() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 10000, true);
        assertTrue(!t.descontarSaldo(10001));
        assertTrue(igual(t.getSaldo(), 10000));
        assertTrue(t.getListMovimientos().isEmpty());
    }

    @Test
    void pagoConTarjetaInactivaSeRechaza() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 100000, false);
        assertTrue(!t.descontarSaldo(5000));
        assertTrue(igual(t.getSaldo(), 100000));
        assertTrue(t.getListMovimientos().isEmpty());
    }

    @Test
    void pagoPorElSaldoExactoDejaLaTarjetaEnCero() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 8000, true);
        assertTrue(t.descontarSaldo(8000));
        assertTrue(igual(t.getSaldo(), 0));
    }

    @Test
    void recargaSumaSaldoYRegistraMovimientoDeRecarga() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 1000, true);
        t.recargar(25000);
        assertTrue(igual(t.getSaldo(), 26000));
        assertTrue(t.getListMovimientos().size() == 1);
        assertTrue(t.getListMovimientos().get(0).getTipoMovimiento() == TipoMovimiento.RECARGA);
        assertTrue(igual(t.getListMovimientos().get(0).getMonto(), 25000));
    }

    @Test
    void devolverSaldoSumaYRegistraReembolso() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        t.devolverSaldo(12000);
        assertTrue(igual(t.getSaldo(), 12000));
        assertTrue(t.getListMovimientos().get(0).getTipoMovimiento() == TipoMovimiento.REEMBOLSO);
    }

    @Test
    void historialConservaElOrdenDeLosMovimientos() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        t.recargar(50000);
        t.descontarSaldo(10000);
        t.recargar(5000);
        assertTrue(t.getListMovimientos().size() == 3);
        assertTrue(t.getListMovimientos().get(0).getTipoMovimiento() == TipoMovimiento.RECARGA);
        assertTrue(t.getListMovimientos().get(1).getTipoMovimiento() == TipoMovimiento.PAGO);
        assertTrue(t.getListMovimientos().get(2).getTipoMovimiento() == TipoMovimiento.RECARGA);
        assertTrue(t.getListMovimientos().get(0).getId().equals("MOV-1"));
        assertTrue(t.getListMovimientos().get(2).getId().equals("MOV-3"));
        assertTrue(igual(t.getSaldo(), 45000));
    }

    @Test
    void tarjetaReactivadaVuelveAPoderPagar() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 5000, false);
        assertTrue(!t.descontarSaldo(1000));
        t.setActiva(true);
        assertTrue(t.descontarSaldo(1000));
    }

    @Test
    void movimientoGuardaFechaDeHoy() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);
        t.recargar(1000);
        assertTrue(t.getListMovimientos().get(0).getFecha().equals(java.time.LocalDate.now()));
    }

    // ---------- Ayudas ----------

    private static boolean igual(double a, double b) {
        return Math.abs(a - b) < 0.0001;
    }
}