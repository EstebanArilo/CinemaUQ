package uqvirutal.edu.co.cinemauq.model;

public interface EstadoAsiento {
    void reservar(Asiento asiento);
    void liberar(Asiento asiento);
}