package uqvirutal.edu.co.cinemauq.model;


import java.time.LocalDate;
import java.util.ArrayList;

public class Cliente extends Usuario implements IObservador{
    private TarjetaVirtual tarjetaVirtual;
    private ArrayList<Punto> listPuntos;
    private ArrayList<Compra> listCompras;

    private ArrayList<Notificacion> notificaciones;
    public Cliente(String id, String nombre, String correo, String contraseña, String numero) {
        super(id, nombre, correo, contraseña);
        this.listPuntos = new ArrayList<>();
        this.listCompras = new ArrayList<>();
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

    /**
     *Metodo para la acumulacion de puntos de lealtad.
     * @param cantidad
     */

    public void acumularPuntos(double cantidad) {
        LocalDate fechaObtencion = LocalDate.now();
        int anioVencimiento = fechaObtencion.getYear() + 1;
        LocalDate fechaVencimiento = LocalDate.of(anioVencimiento, fechaObtencion.getMonth(), fechaObtencion.getDayOfMonth());
        Punto nuevoPunto = new Punto(cantidad, fechaObtencion, fechaVencimiento);
        this.listPuntos.add(nuevoPunto);
    }

    /**
     * Recibe una notificacion y la almacena en el historial de notificaciones de cliente.
     * @param notificacion
     */
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

    public ArrayList<Compra> getHistorialCompras() {
        return listCompras;
    }

    public void setHistorialCompras(ArrayList<Compra> listCompras) {
        this.listCompras = listCompras;
    }
}
