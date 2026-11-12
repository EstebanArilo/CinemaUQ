package uqvirutal.edu.co.cinemauq.model;

public class Pelicula {
    private String id;
    private String titulo;
    private String genero;
    private int duracion;
    private String sinopsis;
    private int clasificacion;

    public Pelicula(String id, String titulo, String genero, int duracion, String sinopsis, int clasificacion) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.sinopsis = sinopsis;
        this.clasificacion = clasificacion;
    }

    public String detalles() {
        return "Título: " + titulo + " | Género: " + genero + " | Duración: " + duracion + " min | Clasificación: +" + clasificacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(int clasificacion) {
        this.clasificacion = clasificacion;
    }
}