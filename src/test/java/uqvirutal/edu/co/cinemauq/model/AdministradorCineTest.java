package uqvirutal.edu.co.cinemauq.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Casos 7 y 10 del PDF: recargas, gestión administrativa, singleton Cine y reseñas. */
class AdministradorCineTest {

    private Administrador admin;

    @BeforeEach
    void preparar() {
        reiniciarCine();
        admin = new Administrador("ADM", "Admin", "admin@uq.edu.co", "root");
    }

    // ---------- Recargas (caso 7) ----------

    @Test
    void recargarTarjetaSumaSaldoYRegistraElMovimiento() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 5000, true);

        admin.recargarTarjeta(t, 20000);

        assertTrue(igual(t.getSaldo(), 25000));
        assertTrue(t.getListMovimientos().size() == 1);
        assertTrue(t.getListMovimientos().get(0).getTipoMovimiento() == TipoMovimiento.RECARGA);
        assertTrue(igual(t.getListMovimientos().get(0).getMonto(), 20000));
    }

    @Test
    void variasRecargasSeAcumulan() {
        TarjetaVirtual t = new TarjetaVirtual("T1", 0, true);

        admin.recargarTarjeta(t, 10000);
        admin.recargarTarjeta(t, 15000);

        assertTrue(igual(t.getSaldo(), 25000));
        assertTrue(t.getListMovimientos().size() == 2);
        assertTrue(!t.getListMovimientos().get(0).getId().equals(t.getListMovimientos().get(1).getId()));
    }

    @Test
    void recargarUnaTarjetaNulaNoLanzaExcepcion() {
        admin.recargarTarjeta(null, 10000);
        assertTrue(Cine.getInstancia().getListaUsuarios().isEmpty());
    }

    // ---------- Clientes (caso 10) ----------

    @Test
    void crearClienteLoRegistraEnElCine() {
        Cliente c = cliente("1", 0);

        assertTrue(admin.crearCliente(c));
        assertTrue(admin.buscarCliente("1") == c);
        assertTrue(Cine.getInstancia().getListaUsuarios().size() == 1);
    }

    @Test
    void noSePermiteUnClienteConIdRepetidoNiNulo() {
        assertTrue(admin.crearCliente(cliente("1", 0)));

        assertTrue(!admin.crearCliente(cliente("1", 0)));
        assertTrue(!admin.crearCliente(null));
        assertTrue(Cine.getInstancia().getListaUsuarios().size() == 1);
    }

    @Test
    void buscarClienteInexistenteDevuelveNulo() {
        assertTrue(admin.buscarCliente("999") == null);
    }

    @Test
    void buscarClienteIgnoraALosAdministradores() {
        Cine.getInstancia().agregarUsuario(admin);
        assertTrue(admin.buscarCliente("ADM") == null);
    }

    @Test
    void actualizarClienteCambiaSusDatosYLaNuevaClaveInicia() {
        admin.crearCliente(cliente("1", 0));

        assertTrue(admin.actualizarCliente("1", "Nuevo Nombre", "nuevo@uq.edu.co", "nuevaClave"));

        Cliente c = admin.buscarCliente("1");
        assertTrue(c.getNombre().equals("Nuevo Nombre"));
        assertTrue(c.iniciarSesion("nuevo@uq.edu.co", "nuevaClave"));
        assertTrue(!c.iniciarSesion("1@uq.edu.co", "clave1"));
    }

    @Test
    void actualizarUnClienteInexistenteDevuelveFalso() {
        assertTrue(!admin.actualizarCliente("999", "X", "x@uq.edu.co", "x"));
    }

    @Test
    void eliminarClienteLoQuitaDelCine() {
        admin.crearCliente(cliente("1", 0));

        assertTrue(admin.eliminarCliente("1"));
        assertTrue(admin.buscarCliente("1") == null);
        assertTrue(!admin.eliminarCliente("1"));
    }

    // ---------- Salas, películas y funciones (caso 10) ----------

    @Test
    void gestionarSalaLaRegistraEnElCine() {
        Sala s = sala();
        admin.gestionarSalas(s);
        assertTrue(Cine.getInstancia().getListaSalas().contains(s));
    }

    @Test
    void gestionarProductosNoModificaElCatalogo() {
        admin.crearProducto("P1", "Crispetas", 9000, CategoriaProducto.SNACK);
        admin.gestionarProductos();
        assertTrue(admin.getListProductos().size() == 1);
    }

    // ---------- Cine (Singleton) ----------

    @Test
    void cineEsUnSingleton() {
        assertTrue(Cine.getInstancia() == Cine.getInstancia());
        assertTrue(Cine.getInstancia().getTitulo().equals("CinemaUQ"));
    }

    @Test
    void cineAgregaPeliculasSalasFuncionesYUsuarios() {
        Cine cine = Cine.getInstancia();
        Cliente c = cliente("1", 0);

        cine.agregarPelicula(pelicula());
        cine.agregarSala(sala());
        cine.agregarFuncion(funcion(5, 10000));
        cine.agregarUsuario(c);

        assertTrue(cine.getListaPeliculas().size() == 1);
        assertTrue(cine.getListaSalas().size() == 1);
        assertTrue(cine.getListaFunciones().size() == 1);
        assertTrue(cine.getListaUsuarios().contains(c));
    }

    @Test
    void buscarFuncionNoDistingueMayusculasYDevuelveNuloSiNoExiste() {
        Funcion f = funcion(5, 10000);
        Cine.getInstancia().agregarFuncion(f);

        assertTrue(Cine.getInstancia().buscarFuncion("f1") == f);
        assertTrue(Cine.getInstancia().buscarFuncion("F99") == null);
    }

    // ---------- Reseñas ----------

    @Test
    void sinResenasElPromedioEsCero() {
        assertTrue(igual(Cine.getInstancia().calcularPromedioCalificacion(), 0.0));
    }

    @Test
    void elPromedioDeCalificacionSeCalculaSobreTodasLasResenas() {
        Cliente c = cliente("1", 0);
        Cine cine = Cine.getInstancia();
        cine.agregarResena(new Resena("R1", 5, "Excelente", LocalDate.now(), c));
        cine.agregarResena(new Resena("R2", 3, "Normal", LocalDate.now(), c));
        cine.agregarResena(new Resena("R3", 4, "Buena", LocalDate.now(), c));

        assertTrue(cine.getListaResena().size() == 3);
        assertTrue(igual(cine.calcularPromedioCalificacion(), 4.0));
    }

    @Test
    void laResenaMuestraSuResumenYSeFechaHoy() {
        Cliente c = cliente("1", 0);
        Resena r = new Resena("R1", 5, "Muy buena", LocalDate.of(2020, 1, 1), c);

        assertTrue(r.getFecha().equals(LocalDate.now()));
        assertTrue(r.obtenerResumen().contains("Muy buena"));
        assertTrue(r.obtenerResumen().contains("5"));
        assertTrue(r.getCliente() == c);
    }

    // ---------- Ayudas ----------

    private static boolean igual(double a, double b) {
        return Math.abs(a - b) < 0.0001;
    }

    private static void reiniciarCine() {
        Cine cine = Cine.getInstancia();
        cine.getListaPeliculas().clear();
        cine.getListaSalas().clear();
        cine.getListaFunciones().clear();
        cine.getListaUsuarios().clear();
        cine.getListaResena().clear();
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
}