package uqvirutal.edu.co.cinemauq.model;

public class AsientoDisponible implements EstadoAsiento {
    @Override
    public void reservar(Asiento asiento) {
        asiento.setEstado(new AsientoOcupado());
    }
    @Override
    public void liberar(Asiento asiento) {
    }
}