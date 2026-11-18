package uqvirutal.edu.co.cinemauq.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Casos 2 y 3 del PDF: funciones, salas y seleccion de asientos (patron State). */
class AsientoSalaFuncionTest {

    // ---------- Asiento / State ----------

    @Test
    void reservarAsientoDisponibleLoDejaOcupado() {
        Asiento a = new Asiento("A", "A-1", TipoAsiento.GENERAL, new AsientoDisponible());
        assertTrue(a.isDisponible());
        a.reservar();
        assertTrue(!a.isDisponible());
        assertTrue(a.getEstado() instanceof AsientoOcupado);
    }

    @Test
    void reservarAsientoOcupadoLanzaExcepcion() {
        Asiento a = new Asiento("A", "A-1", TipoAsiento.GENERAL, new AsientoOcupado());
        boolean lanzo = false;
        try {
            a.reservar();
        } catch (IllegalStateException e) {
            lanzo = true;
        }
        assertTrue(lanzo);
        assertTrue(!a.isDisponible());
    }

    @Test
    void dobleReservaDelMismoAsientoSeImpide() {
        Asiento a = new Asiento("A", "A-1", TipoAsiento.GENERAL, new AsientoDisponible());
        a.reservar();
        boolean segundaFallo = false;
        try {
            a.reservar();
        } catch (IllegalStateException e) {
            segundaFallo = true;
        }
        assertTrue(segundaFallo);
    }

    @Test
    void liberarAsientoOcupadoLoDejaDisponible() {
        Asiento a = new Asiento("A", "A-1", TipoAsiento.GENERAL, new AsientoOcupado());
        a.liberar();
        assertTrue(a.isDisponible());
    }

    @Test
    void liberarAsientoDisponibleNoCambiaNada() {
        Asiento a = new Asiento("A", "A-1", TipoAsiento.GENERAL, new AsientoDisponible());
        a.liberar();
        assertTrue(a.isDisponible());
    }

    @Test
    void asientoSeleccionadoPasaAOcupadoOVuelveADisponible() {
        Asiento a = new Asiento("A", "A-1", TipoAsiento.VIP, new AsientoSeleccionado());
        assertTrue(!a.isDisponible());
        a.reservar();
        assertTrue(a.getEstado() instanceof AsientoOcupado);

        Asiento b = new Asiento("A", "A-2", TipoAsiento.VIP, new AsientoSeleccionado());
        b.liberar();
        assertTrue(b.isDisponible());
    }

    @Test
    void cicloReservarYLiberarVariasVeces() {
        Asiento a = new Asiento("A", "A-1", TipoAsiento.GENERAL, new AsientoDisponible());
        for (int i = 0; i < 3; i++) {
            a.reservar();
            assertTrue(!a.isDisponible());
            a.liberar();
            assertTrue(a.isDisponible());
        }
    }

    // ---------- Sala ----------

    @Test
    void salaGeneraAsientosPorFilaYColumna() {
        Sala s = new Sala(1, "Sala 1", 3, 4);
        assertTrue(s.getCapacidad() == 12);
        assertTrue(s.getListaAsientos().size() == 12);
        assertTrue(s.getListaAsientos().get(0).getNumero().equals("A-1"));
        assertTrue(s.getListaAsientos().get(11).getNumero().equals("C-4"));
        assertTrue(s.getListaAsientos().get(0).getTipoAsiento() == TipoAsiento.GENERAL);
    }

    @Test
    void todosLosAsientosDeUnaSalaNuevaEstanDisponibles() {
        Sala s = new Sala(1, "Sala 1", 2, 3);
        assertTrue(s.asientosDisponibles().size() == 6);
    }

    @Test
    void asientosDisponiblesExcluyeLosReservados() {
        Sala s = new Sala(1, "Sala 1", 2, 3);
        s.getListaAsientos().get(0).reservar();
        s.getListaAsientos().get(4).reservar();
        ArrayList<Asiento> libres = s.asientosDisponibles();
        assertTrue(libres.size() == 4);
        assertTrue(!libres.contains(s.getListaAsientos().get(0)));
        assertTrue(!libres.contains(s.getListaAsientos().get(4)));
    }

    @Test
    void nombresDeAsientosSonUnicos() {
        Sala s = new Sala(1, "Sala 1", 4, 5);
        java.util.HashSet<String> nombres = new java.util.HashSet<>();
        for (Asiento a : s.getListaAsientos()) {
            nombres.add(a.getNumero());
        }
        assertTrue(nombres.size() == 20);
    }

    @Test
    void sePuedeAgregarUnAsientoManualmente() {
        Sala s = new Sala(1, "Sala 1", 1, 1);
        s.agregarAsiento(new Asiento("Z", "Z-1", TipoAsiento.ACCESIBLE, new AsientoDisponible()));
        assertTrue(s.getListaAsientos().size() == 2);
    }

    // ---------- Funcion ----------

    @Test
    void funcionReportaDisponibilidadDeUnAsiento() {
        Funcion f = funcion(48, 20000);
        assertTrue(f.disponibilidadAsiento("A-1"));
        asiento(f, "A-1").reservar();
        assertTrue(!f.disponibilidadAsiento("A-1"));
        assertTrue(f.disponibilidadAsiento("A-2"));
    }

    @Test
    void disponibilidadNoDistingueMayusculas() {
        Funcion f = funcion(48, 20000);
        assertTrue(f.disponibilidadAsiento("a-1"));
    }

    @Test
    void asientoInexistenteNoEstaDisponible() {
        Funcion f = funcion(48, 20000);
        assertTrue(!f.disponibilidadAsiento("Z-99"));
    }

    @Test
    void funcionSinListaDeAsientosCreaListaVacia() {
        Funcion f = new Funcion("F9", pelicula(), sala(),
                java.time.LocalDateTime.now().plusDays(1), 15000, null);
        assertTrue(f.getAsientosFuncion() != null);
        assertTrue(f.getAsientosFuncion().isEmpty());
    }

    @Test
    void funcionGuardaPrecioPeliculaYSala() {
        Funcion f = funcion(48, 20000);
        assertTrue(igual(f.getPrecioEntrada(), 20000));
        assertTrue(f.getPelicula().getTitulo().equals("Dune"));
        assertTrue(f.getSala().getNombre().equals("Sala 1"));
        assertTrue(f.toString().contains("Dune"));
    }

    @Test
    void peliculaMuestraSusDetalles() {
        String d = pelicula().detalles();
        assertTrue(d.contains("Dune"));
        assertTrue(d.contains("155"));
        assertTrue(d.contains("+12"));
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

    private static Asiento asiento(Funcion f, String numero) {
        for (Asiento a : f.getAsientosFuncion()) {
            if (a.getNumero().equals(numero)) {
                return a;
            }
        }
        return null;
    }
}