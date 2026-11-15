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
    private ArrayList<Resena> listaResena;

    private Cine() {
        this.id = "CINE-01";
        this.titulo = "CinemaUQ";
        this.listaPeliculas = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
        this.listaUsuarios = new ArrayList<>();
        this.listaResena = new ArrayList<>();
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

    public void agregarResena(Resena resena) {
        if (this.listaResena != null) {
            this.listaResena.add(resena);
            System.out.println("Reseña agregada exitosamente al sistema.");
        }
    }

    public double calcularPromedioCalificacion() {
        if (listaResena == null) {
            return 0.0;
        }
        double sumaCalificaciones = 0.0;
        int contador = 0;

        for (Resena r : listaResena) {
            sumaCalificaciones = sumaCalificaciones + r.getCalificacion();
            contador = contador + 1;
        }
        if (contador == 0) {
            return 0.0;
        }
        return sumaCalificaciones / contador;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public ArrayList<Pelicula> getListaPeliculas() { return listaPeliculas; }
    public ArrayList<Sala> getListaSalas() { return listaSalas; }
    public ArrayList<Funcion> getListaFunciones() { return listaFunciones; }
    public ArrayList<Usuario> getListaUsuarios() { return listaUsuarios; }
    public ArrayList<Resena> getListaResena() {return listaResena; }
}
