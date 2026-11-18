package uqvirutal.edu.co.cinemauq.model;

public class ReembolsoParcial implements PoliticaReembolso {
    @Override
    public double calcularReembolso(double montoPagado, double horasRestantes) {
        return montoPagado * obtenerPorcentaje();
    }

    @Override
    public double obtenerPorcentaje() {
        return 0.8;
    }
}