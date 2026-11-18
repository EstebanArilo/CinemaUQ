package uqvirutal.edu.co.cinemauq.controller;

import uqvirutal.edu.co.cinemauq.model.Cine;
import uqvirutal.edu.co.cinemauq.model.Cliente;
import uqvirutal.edu.co.cinemauq.model.Resena;

import java.time.LocalDate;
import java.util.List;

public class ResenaController {

    private final Cine cine;

    public ResenaController() {
        this.cine = Cine.getInstancia();
    }

    /**
     * Registra una nueva reseña en el sistema del cine.
     */
    public void registrarResena(String id, int calificacion, String comentario, LocalDate fecha, Cliente cliente) {
        Resena nuevaResena = new Resena(id, calificacion, comentario,fecha, cliente);
        cine.agregarResena(nuevaResena);
    }

    /**
     * Obtiene la lista de todas las reseñas registradas.
     */
    public List<Resena> obtenerListaResenas() {
        return cine.getListaResena();
    }

    /**
     * Calcula el promedio general de calificaciones.
     */
    public double calcularPromedioCalificacion() {
        return cine.calcularPromedioCalificacion();
    }
}