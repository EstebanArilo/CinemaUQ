package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;

public class TarjetaVirtual {
    private String numero;
    private double saldo;
    private boolean activa;
    private ArrayList<Movimiento> listMovimientos;

    public TarjetaVirtual(String numero, double saldo, boolean activa, ArrayList<Movimiento> listMovimientos) {
        this.numero = numero;
        this.saldo = saldo;
        this.activa = activa;
        this.listMovimientos = listMovimientos;
    }

    public TarjetaVirtual(String numero, double saldo, boolean activa) {
        this.numero = numero;
        this.saldo = saldo;
        this.activa = activa;
        this.listMovimientos = new ArrayList<>();
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
    public void descontarSaldo(double monto) {
        this.saldo -= monto;
        String idMov = "MOV-" + (this.listMovimientos.size() + 1);
        this.listMovimientos.add(new Movimiento(
                idMov,
                TipoMovimiento.PAGO,
                monto,
                java.time.LocalDate.now()
        ));
    }

    public void devolverSaldo(double monto) {
        this.saldo += monto;
        String idMov = "MOV-" + (this.listMovimientos.size() + 1);
        this.listMovimientos.add(new Movimiento(
                idMov,
                TipoMovimiento.REEMBOLSO,
                monto,
                java.time.LocalDate.now()
        ));
    }
}

