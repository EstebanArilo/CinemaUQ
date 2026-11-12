package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;

public class Cine {
    private static Cine instancia;

    private String id;
    private String titulo;
    private ArrayList<Pelicula> listaPeliculas;
    private ArrayList<Sala> listaSalas;
    private ArrayList<Funcion> listaFunciones;
    private ArrayList<Usuario> listaUsuarios;

    private Cine() {
        this.id = "CINE-01";
        this.titulo = "CinemaUQ";
        this.listaPeliculas = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
        this.listaUsuarios = new ArrayList<>();
    }

    public static Cine getInstancia() {
        if (instancia == null) {
            instancia = new Cine();
        }
        return instancia;
    }

    public void agregarPelicula(Pelicula pelicula) {
        this.listaPeliculas.add(pelicula);
    }

    public void agregarSala(Sala sala) {
        this.listaSalas.add(sala);
    }

    public void agregarFuncion(Funcion funcion) {
        this.listaFunciones.add(funcion);
    }

    public void agregarUsuario(Usuario usuario) {
        this.listaUsuarios.add(usuario);
    }

    public Funcion buscarFuncion(String idFuncion) {
        for (Funcion funcion : listaFunciones) {
            if (funcion.getIdFuncion().equalsIgnoreCase(idFuncion)) {
                return funcion;
            }
        }
        return null;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public ArrayList<Pelicula> getListaPeliculas() { return listaPeliculas; }
    public ArrayList<Sala> getListaSalas() { return listaSalas; }
    public ArrayList<Funcion> getListaFunciones() { return listaFunciones; }
    public ArrayList<Usuario> getListaUsuarios() { return listaUsuarios; }
}
