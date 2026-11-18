package uqvirutal.edu.co.cinemauq.controller;

import uqvirutal.edu.co.cinemauq.model.*;
import java.time.LocalDateTime;
import java.util.List;

public class FuncionController {

    private final Cine cine;

    public FuncionController() {
        this.cine = Cine.getInstancia();
    }

    public void registrarPelicula(Pelicula pelicula) {
        if (pelicula != null) {
            cine.getListaPeliculas().add(pelicula);
        }
    }

    public void programarFuncion(Funcion funcion) {
        if (funcion != null) {
            cine.getListaFunciones().add(funcion);
        }
    }

    public List<Pelicula> obtenerPeliculas() {
        return cine.getListaPeliculas();
    }

    public List<Sala> obtenerSalas() {
        return cine.getListaSalas();
    }

    public List<Funcion> obtenerFunciones() {
        return cine.getListaFunciones();
    }
}
