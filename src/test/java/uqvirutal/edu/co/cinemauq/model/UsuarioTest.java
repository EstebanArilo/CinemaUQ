package uqvirutal.edu.co.cinemauq.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Caso 1 del PDF: registro e inicio de sesion. */
class UsuarioTest {

    private Cliente nuevoCliente() {
        return new Cliente("C1", "Ana Gomez", "ana@uq.edu.co", "1234", "TJ-1");
    }

    @Test
    void clienteRegistradoConservaSusDatos() {
        Cliente c = nuevoCliente();
        assertTrue(c.getId().equals("C1"));
        assertTrue(c.getNombre().equals("Ana Gomez"));
        assertTrue(c.getCorreo().equals("ana@uq.edu.co"));
        assertTrue(c.getContraseña().equals("1234"));
    }

    @Test
    void clienteNuevoTieneTarjetaActivaSinSaldoYSinPuntos() {
        Cliente c = nuevoCliente();
        assertTrue(c.getTarjetaVirtual() != null);
        assertTrue(c.getTarjetaVirtual().isActiva());
        assertTrue(igual(c.getTarjetaVirtual().getSaldo(), 0.0));
        assertTrue(c.getTarjetaVirtual().getNumero().equals("TJ-1"));
        assertTrue(c.getListPuntos().isEmpty());
        assertTrue(c.getHistorialCompras().isEmpty());
    }

    @Test
    void inicioDeSesionConCredencialesCorrectas() {
        assertTrue(nuevoCliente().iniciarSesion("ana@uq.edu.co", "1234"));
    }

    @Test
    void elCorreoNoDistingueMayusculas() {
        assertTrue(nuevoCliente().iniciarSesion("ANA@UQ.EDU.CO", "1234"));
    }

    @Test
    void rechazaContraseñaIncorrecta() {
        assertTrue(!nuevoCliente().iniciarSesion("ana@uq.edu.co", "9999"));
    }

    @Test
    void rechazaCorreoIncorrecto() {
        assertTrue(!nuevoCliente().iniciarSesion("otra@uq.edu.co", "1234"));
    }

    @Test
    void laContraseñaSiDistingueMayusculas() {
        Cliente c = new Cliente("C2", "Luis", "luis@uq.edu.co", "Clave", "TJ-2");
        assertTrue(!c.iniciarSesion("luis@uq.edu.co", "clave"));
        assertTrue(c.iniciarSesion("luis@uq.edu.co", "Clave"));
    }

    @Test
    void administradorTambienIniciaSesionYEsUsuario() {
        Administrador a = new Administrador("A1", "Admin", "admin@uq.edu.co", "adm");
        assertTrue(a instanceof Usuario);
        assertTrue(a.iniciarSesion("admin@uq.edu.co", "adm"));
        assertTrue(!a.iniciarSesion("admin@uq.edu.co", "mal"));
    }

    @Test
    void clienteNoTieneOperacionesAdministrativasDeRecarga() {
        boolean tieneRecarga = false;
        for (java.lang.reflect.Method m : Cliente.class.getMethods()) {
            if (m.getName().equals("recargarTarjeta") || m.getName().equals("gestionarPeliculas")) {
                tieneRecarga = true;
            }
        }
        assertTrue(!tieneRecarga);
    }

    @Test
    void sePuedeActualizarContraseñaYSesionUsaLaNueva() {
        Cliente c = nuevoCliente();
        c.setContraseña("nueva");
        assertTrue(!c.iniciarSesion("ana@uq.edu.co", "1234"));
        assertTrue(c.iniciarSesion("ana@uq.edu.co", "nueva"));
    }

    // ---------- Ayudas ----------

    private static boolean igual(double a, double b) {
        return Math.abs(a - b) < 0.0001;
    }
}