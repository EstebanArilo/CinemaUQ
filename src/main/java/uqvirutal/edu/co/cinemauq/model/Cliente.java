package uqvirutal.edu.co.cinemauq.model;


import java.time.LocalDate;
import java.util.ArrayList;

public class Cliente extends Usuario {
    private TarjetaVirtual tarjetaVirtual;
    private ArrayList<Punto> listPuntos;
    private ArrayList<Compra> listCompras;

    public Cliente(String id, String nombre, String correo, String contraseña, String numero) {
        super(id, nombre, correo, contraseña);
        this.listPuntos = new ArrayList<>();
        this.listCompras = new ArrayList<>();
        this.tarjetaVirtual = new TarjetaVirtual(numero, 0.0, true, new ArrayList<>());
    }

    public Cliente getPuntosCuenta() {
        return this;
    }

    public void acumularPuntos(double cantidad) {
        LocalDate fechaObtencion = LocalDate.now();
        LocalDate fechaVencimiento = fechaObtencion.plusYears(1);

        Punto nuevoPunto = new Punto(cantidad, fechaObtencion, fechaVencimiento);
        this.listPuntos.add(nuevoPunto);
    }

    public TarjetaVirtual getTarjetaVirtual() {
        return tarjetaVirtual;
    }

    public void setTarjetaVirtual(TarjetaVirtual tarjetaVirtual) {
        this.tarjetaVirtual = tarjetaVirtual;
    }

    public ArrayList<Punto> getListPuntos() {
        return listPuntos;
    }

    public void setListPuntos(ArrayList<Punto> listPuntos) {
        this.listPuntos = listPuntos;
    }

    public ArrayList<Compra> getHistorialCompras() {
        return listCompras;
    }

    public void setHistorialCompras(ArrayList<Compra> listCompras) {
        this.listCompras = listCompras;
    }
}
