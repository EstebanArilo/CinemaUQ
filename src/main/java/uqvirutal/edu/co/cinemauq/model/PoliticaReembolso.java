package uqvirutal.edu.co.cinemauq.model;

public interface PoliticaReembolso {
    double calcularReembolso(double montoPagado, double horasRestantes) throws CancelacionNoPermitidaException;
    double obtenerPorcentaje();
}