package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;

public class Resena {
    private String id;
    private int calificacion;
    private String comentario;
    private LocalDate fecha;
    private Cliente cliente;

    public Resena(String id, int calificacion, String comentario, LocalDate fecha, Cliente cliente) {
        this.id = id;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha = LocalDate.now();
        this.cliente = cliente;
    }

    public String obtenerResumen() {
        return "Cliente: " + cliente +
                " | Calificación: " + calificacion + " Estrellas " +
                "\nComentario: \"" + comentario + "\"" +
                " | Fecha: " + fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
