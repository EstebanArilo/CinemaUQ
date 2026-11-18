package uqvirutal.edu.co.cinemauq.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Caso 9 del PDF: acumulación de puntos y vigencia de un año. */
class PuntosTest {

    @Test
    void acumularPuntosCreaUnLoteConLaCantidadIndicada() {
        Cliente c = cliente("1", 0);
        c.acumularPuntos(250);

        assertTrue(c.getListPuntos().size() == 1);
        assertTrue(igual(c.getListPuntos().get(0).getCantidad(), 250));
    }

    @Test
    void elLoteSeObtieneHoyYVenceUnAnioDespues() {
        Cliente c = cliente("1", 0);
        c.acumularPuntos(100);
        Punto p = c.getListPuntos().get(0);

        assertTrue(p.getFechaObtencion().equals(LocalDate.now()));
        assertTrue(p.getFechaVencimiento().equals(p.getFechaObtencion().plusYears(1)));
        assertTrue(p.getFechaVencimiento().isAfter(LocalDate.now()));
    }

    @Test
    void variosLotesSeConservanEnElOrdenEnQueSeObtuvieron() {
        Cliente c = cliente("1", 0);
        c.acumularPuntos(100);
        c.acumularPuntos(40);
        c.acumularPuntos(5);

        assertTrue(c.getListPuntos().size() == 3);
        assertTrue(igual(c.getListPuntos().get(0).getCantidad(), 100));
        assertTrue(igual(c.getListPuntos().get(1).getCantidad(), 40));
        assertTrue(igual(c.getListPuntos().get(2).getCantidad(), 5));
    }

    @Test
    void cadaClienteLlevaSuPropiaCuentaDePuntos() {
        Cliente a = cliente("1", 0);
        Cliente b = cliente("2", 0);
        a.acumularPuntos(100);

        assertTrue(a.getListPuntos().size() == 1);
        assertTrue(b.getListPuntos().isEmpty());
    }

    @Test
    void unaCompraPagadaAcumulaElDiezPorCientoDelTotalConConfiteria() {
        Cliente c = cliente("1", 100000);
        Funcion f = funcion(5, 10000);
        java.util.ArrayList<ProductoConfiteria> productos = new java.util.ArrayList<>();
        productos.add(new ProductoSimple("PR1", 10000, "Palomitas", CategoriaProducto.SNACK));

        boolean ok = new FachadaCompra().procesarCompra(c, f, asientos(f, "A-1", "A-2"), productos);

        assertTrue(ok);
        // total = 2 x 10000 + 10000 = 30000  ->  3000 puntos
        assertTrue(c.getListPuntos().size() == 1);
        assertTrue(igual(c.getListPuntos().get(0).getCantidad(), 3000));
    }

    @Test
    void unaCompraRechazadaNoAcumulaPuntos() {
        Cliente c = cliente("1", 100); // saldo insuficiente
        Funcion f = funcion(5, 10000);

        boolean ok = new FachadaCompra().procesarCompra(c, f, asientos(f, "A-1"), null);

        assertTrue(!ok);
        assertTrue(c.getListPuntos().isEmpty());
        assertTrue(c.getHistorialCompras().isEmpty());
    }

    @Test
    void cadaCompraExitosaAgregaUnLoteNuevo() {
        Cliente c = cliente("1", 100000);
        Funcion f = funcion(5, 10000);
        FachadaCompra fachada = new FachadaCompra();

        assertTrue(fachada.procesarCompra(c, f, asientos(f, "A-1"), null));
        assertTrue(fachada.procesarCompra(c, f, asientos(f, "A-2"), null));

        assertTrue(c.getListPuntos().size() == 2);
        assertTrue(igual(c.getListPuntos().get(0).getCantidad(), 1000));
        assertTrue(igual(c.getListPuntos().get(1).getCantidad(), 1000));
    }

    // ---------- Ayudas ----------

    private static boolean igual(double a, double b) {
        return Math.abs(a - b) < 0.0001;
    }

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

    private static Asiento asiento(Funcion f, String numero) {
        for (Asiento a : f.getAsientosFuncion()) {
            if (a.getNumero().equals(numero)) {
                return a;
            }
        }
        return null;
    }

    private static java.util.ArrayList<Asiento> asientos(Funcion f, String... numeros) {
        java.util.ArrayList<Asiento> lista = new java.util.ArrayList<>();
        for (String n : numeros) {
            lista.add(asiento(f, n));
        }
        return lista;
    }
}