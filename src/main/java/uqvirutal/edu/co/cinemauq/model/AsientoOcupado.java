package uqvirutal.edu.co.cinemauq.model;

public class AsientoOcupado implements EstadoAsiento {
    @Override
    public void reservar(Asiento asiento) {
        throw new IllegalStateException("El asiento ya se encuentra ocupado.");
    }

    @Override
    public void liberar(Asiento asiento) {
        asiento.setEstado(new AsientoDisponible());
    }
}