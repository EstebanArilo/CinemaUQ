package uqvirutal.edu.co.cinemauq.model;


import java.util.ArrayList;

public class Cliente extends Usuario {
    private TarjetaVirtual tarjetaVirtual;
    private ArrayList<Punto> listPuntos;
    public Cliente(String id, String nombre, String correo, String contraseña, String numero) {
        super(id, nombre, correo, contraseña);
        this.listPuntos = new ArrayList<>();
        this.tarjetaVirtual = new TarjetaVirtual(numero, 0.0, true, new ArrayList<>() );
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
}
