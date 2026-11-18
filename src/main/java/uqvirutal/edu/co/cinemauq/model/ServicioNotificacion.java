package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;
import java.util.List;

public class ServicioNotificacion{
    private final List<IObservador> observadores;

    public ServicioNotificacion() {
        this.observadores = new ArrayList<>();
    }

    /**
     *Suscribe un nuevo observador a la lista de destinatarios
     * verificando que no sea nulo y no se encuentre registrado anteriormente
     * @param observador
     */
    public void agregarObservador(IObservador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    /**
     *Cancela la suscripcion de un observador en el servicio de notificaciones
     * @param observador
     */
    public void eliminarObservador(IObservador observador) {
        observadores.remove(observador);
    }

    /**
     * Notifica a todos los observadores suscritos al serrvicio
     * @param mensaje
     */
    public void notificarTodos(String mensaje) {
        Notificacion notificacion = new Notificacion(mensaje);
        for (IObservador observador : observadores) {
            observador.actualizar(notificacion);
        }
    }

    /**
     * Notifica a los usuariis sucritos al servicio
     * @param destino
     * @param mensaje
     */
    public void notificarUsuario(IObservador destino, String mensaje) {
        if (destino != null) {
            Notificacion notificacion = new Notificacion(mensaje);
            destino.actualizar(notificacion);
        }
    }

    public List<IObservador> getObservadores() {
        return observadores;
    }
}