package uqvirutal.edu.co.cinemauq.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Patrón Observer: ServicioNotificacion, Cliente (observador) y los avisos que envía el Administrador. */
class NotificacionesTest {

    private Administrador admin;

    @BeforeEach
    void preparar() {
        reiniciarCine();
        admin = new Administrador("ADM", "Admin", "admin@uq.edu.co", "root");
    }

    // ---------- Notificacion ----------

    @Test
    void unaNotificacionNuevaNoEstaLeida() {
        Notificacion n = new Notificacion("Hola");
        assertTrue(n.getMensaje().equals("Hola"));
        assertTrue(!n.isLeida());
        assertTrue(n.getFecha() != null);
    }

    // ---------- ServicioNotificacion ----------

    @Test
    void notificarTodosLlegaATodosLosObservadores() {
        ServicioNotificacion s = new ServicioNotificacion();
        Cliente a = cliente("1", 0);
        Cliente b = cliente("2", 0);
        s.agregarObservador(a);
        s.agregarObservador(b);

        s.notificarTodos("Estreno");

        assertTrue(a.obtenerNotificaciones().size() == 1);
        assertTrue(b.obtenerNotificaciones().size() == 1);
        assertTrue(a.obtenerNotificaciones().get(0).getMensaje().equals("Estreno"));
    }

    @Test
    void unObservadorNoSeRegistraDosVecesNiSeAceptaNulo() {
        ServicioNotificacion s = new ServicioNotificacion();
        Cliente a = cliente("1", 0);
        s.agregarObservador(a);
        s.agregarObservador(a);
        s.agregarObservador(null);

        assertTrue(s.getObservadores().size() == 1);
        s.notificarTodos("Aviso");
        assertTrue(a.obtenerNotificaciones().size() == 1);
    }

    @Test
    void unObservadorEliminadoYaNoRecibeAvisos() {
        ServicioNotificacion s = new ServicioNotificacion();
        Cliente a = cliente("1", 0);
        Cliente b = cliente("2", 0);
        s.agregarObservador(a);
        s.agregarObservador(b);
        s.eliminarObservador(a);

        s.notificarTodos("Aviso");

        assertTrue(a.obtenerNotificaciones().isEmpty());
        assertTrue(b.obtenerNotificaciones().size() == 1);
    }

    @Test
    void notificarUsuarioSoloLlegaAlDestinatario() {
        ServicioNotificacion s = new ServicioNotificacion();
        Cliente a = cliente("1", 0);
        Cliente b = cliente("2", 0);
        s.agregarObservador(a);
        s.agregarObservador(b);

        s.notificarUsuario(a, "Solo para ti");

        assertTrue(a.obtenerNotificaciones().size() == 1);
        assertTrue(b.obtenerNotificaciones().isEmpty());
    }

    @Test
    void notificarAUnDestinoNuloNoFalla() {
        ServicioNotificacion s = new ServicioNotificacion();
        s.notificarUsuario(null, "Nadie");
        assertTrue(s.getObservadores().isEmpty());
    }

    // ---------- Cliente como observador ----------

    @Test
    void leerNotificacionLaMarcaComoLeida() {
        Cliente c = cliente("1", 0);
        c.actualizar(new Notificacion("Promo"));
        Notificacion n = c.obtenerNotificaciones().get(0);

        c.leerNotificacion(n);

        assertTrue(n.isLeida());
    }

    @Test
    void leerUnaNotificacionAjenaONulaNoHaceNada() {
        Cliente c = cliente("1", 0);
        Notificacion ajena = new Notificacion("De otro cliente");

        c.leerNotificacion(ajena);
        c.leerNotificacion(null);

        assertTrue(!ajena.isLeida());
    }

    @Test
    void actualizarConNulaNoAgregaNada() {
        Cliente c = cliente("1", 0);
        c.actualizar(null);
        assertTrue(c.obtenerNotificaciones().isEmpty());
    }

    // ---------- Avisos del administrador ----------

    @Test
    void gestionarPeliculaLaAgregaALaCarteleraYAvisaALosClientes() {
        Cliente c = cliente("1", 0);
        admin.getServicioNotificaciones().agregarObservador(c);

        admin.gestionarPeliculas(pelicula());

        assertTrue(Cine.getInstancia().getListaPeliculas().size() == 1);
        assertTrue(c.obtenerNotificaciones().size() == 1);
        assertTrue(c.obtenerNotificaciones().get(0).getMensaje().contains("Dune"));
    }

    @Test
    void gestionarFuncionLaProgramaYAvisaALosClientes() {
        Cliente c = cliente("1", 0);
        admin.getServicioNotificaciones().agregarObservador(c);

        admin.gestionarFunciones(funcion(5, 10000));

        assertTrue(Cine.getInstancia().getListaFunciones().size() == 1);
        assertTrue(c.obtenerNotificaciones().get(0).getMensaje().contains("Nueva función"));
    }

    @Test
    void cancelarFuncionAvisaATodosLosClientesSuscritos() {
        Cliente a = cliente("1", 0);
        Cliente b = cliente("2", 0);
        admin.getServicioNotificaciones().agregarObservador(a);
        admin.getServicioNotificaciones().agregarObservador(b);

        admin.cancelarFuncion(funcion(5, 10000));

        assertTrue(a.obtenerNotificaciones().get(0).getMensaje().contains("cancelada"));
        assertTrue(b.obtenerNotificaciones().get(0).getMensaje().contains("Dune"));
    }

    @Test
    void recargarTarjetaAvisaSoloAlDuenioDeLaTarjeta() {
        Cliente duenio = cliente("1", 0);
        Cliente otro = cliente("2", 0);
        Cine.getInstancia().agregarUsuario(duenio);
        Cine.getInstancia().agregarUsuario(otro);

        admin.recargarTarjeta(duenio.getTarjetaVirtual(), 30000);

        assertTrue(duenio.obtenerNotificaciones().size() == 1);
        assertTrue(duenio.obtenerNotificaciones().get(0).getMensaje().contains("Recarga exitosa"));
        assertTrue(otro.obtenerNotificaciones().isEmpty());
    }

    @Test
    void elAdministradorSinServicioDeNotificacionesTrabajaNormalmente() {
        admin.setServicioNotificaciones(null);

        admin.gestionarPeliculas(pelicula());
        admin.gestionarFunciones(funcion(5, 10000));
        admin.cancelarFuncion(funcion(5, 10000));

        assertTrue(Cine.getInstancia().getListaPeliculas().size() == 1);
        assertTrue(Cine.getInstancia().getListaFunciones().size() == 1);
    }

    // ---------- Ayudas ----------

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