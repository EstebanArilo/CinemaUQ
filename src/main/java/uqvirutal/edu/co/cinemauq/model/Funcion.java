package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Funcion {
    private String idFuncion;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDateTime fechaHora;
    private ArrayList<Asiento> asientosFuncion;
    private double precioEntrada;

    public Funcion(String idFuncion, Pelicula pelicula, Sala sala, LocalDateTime fechaHora, double precioEntrada, ArrayList<Asiento> asientosFuncion) {
        this.idFuncion = idFuncion;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fechaHora = fechaHora;
        this.precioEntrada = precioEntrada;
        this.asientosFuncion = asientosFuncion != null ? asientosFuncion : new ArrayList<>();
    }

    public boolean disponibilidadAsiento(String numeroAsiento) {
        for (Asiento asiento : asientosFuncion) {
            if (asiento.getNumero().equalsIgnoreCase(numeroAsiento)) {
                return asiento.isDisponible();
            }
        }
        return false;
    }

    public void cancelarFuncion() {
        System.out.println("La función " + idFuncion + " de la película " + pelicula.getTitulo() + " ha sido cancelada.");
    }

    public String getIdFuncion() { return idFuncion; }
    public void setIdFuncion(String idFuncion) { this.idFuncion = idFuncion; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }

    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public ArrayList<Asiento> getAsientosFuncion() { return asientosFuncion; }
    public void setAsientosFuncion(ArrayList<Asiento> asientosFuncion) { this.asientosFuncion = asientosFuncion; }

    public double getPrecioEntrada() { return precioEntrada; }
    public void setPrecioEntrada(double precioEntrada) { this.precioEntrada = precioEntrada; }
}