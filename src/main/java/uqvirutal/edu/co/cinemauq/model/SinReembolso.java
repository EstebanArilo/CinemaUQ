package uqvirutal.edu.co.cinemauq.model;

public class SinReembolso implements PoliticaReembolso {
    @Override
    public double calcularReembolso(double montoPagado, double horasRestantes) throws CancelacionNoPermitidaException {
        throw new CancelacionNoPermitidaException(
                "No se permite cancelar la compra cuando faltan menos de 2 horas para la función."
        );
    }

    @Override
    public double obtenerPorcentaje() {
        return 0.0;
    }
}