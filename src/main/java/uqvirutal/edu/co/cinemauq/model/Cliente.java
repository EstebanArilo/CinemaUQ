package uqvirutal.edu.co.cinemauq.model;


import java.util.ArrayList;

public class Cliente extends Usuario implements IObservador{
    private TarjetaVirtual tarjetaVirtual;
    private ArrayList<Punto> listPuntos;
    private ArrayList<Notificacion> notificaciones;
    public Cliente(String id, String nombre, String correo, String contraseña, String numero) {
        super(id, nombre, correo, contraseña);
        this.listPuntos = new ArrayList<>();
        this.tarjetaVirtual = new TarjetaVirtual(numero, 0.0, true, new ArrayList<>() );
        this.notificaciones = new ArrayList<>();

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

    @Override
    public void actualizar(Notificacion notificacion) {
        if (notificacion != null) {
            this.notificaciones.add(notificacion);
        }
    }
    public ArrayList<Notificacion> obtenerNotificaciones() {
        return notificaciones;
    }
    public void leerNotificacion(Notificacion notificacion) {
        if (notificacion != null && notificaciones.contains(notificacion)) {
            notificacion.marcarComoLeida();
        }
    }
}
