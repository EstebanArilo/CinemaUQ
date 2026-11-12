package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class TarjetaVirtual {
    private String numero;
    private double saldo;
    private boolean activa;
    private ArrayList<Movimiento> listMovimientos;

    private int contadorMovimientos = 1;

    public TarjetaVirtual(String numero, double saldo, boolean activa, ArrayList<Movimiento> listMovimientos) {
        this.numero = numero;
        this.saldo = saldo;
        this.activa = activa;
        this.listMovimientos = listMovimientos;
    }

    public boolean descontarSaldo(double monto) {
        if (!activa || saldo < monto) {
            return false;
        }
        this.saldo -= monto;

        String idMov = "MOV-" + contadorMovimientos;
        contadorMovimientos++;

        Movimiento movimiento = new Movimiento(
                idMov,
                TipoMovimiento.PAGO,
                monto,
                LocalDate.now()
        );
        this.listMovimientos.add(movimiento);
        return true;
    }
    public void recargar(double monto) {
        this.saldo += monto;
        Movimiento movimiento = new Movimiento(
                "MOV-" + System.currentTimeMillis(),
                TipoMovimiento.RECARGA,
                monto,
                LocalDate.now()
        );
        this.listMovimientos.add(movimiento);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public ArrayList<Movimiento> getListMovimientos() {
        return listMovimientos;
    }

    public void setListMovimientos(ArrayList<Movimiento> listMovimientos) {
        this.listMovimientos = listMovimientos;
    }
}
