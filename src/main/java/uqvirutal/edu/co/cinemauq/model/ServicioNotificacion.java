package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;
import java.util.List;

public class ServicioNotificacion{
    private final List<IObservador> observadores;

    public ServicioNotificacion() {
        this.observadores = new ArrayList<>();
    }

    public void agregarObservador(IObservador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void eliminarObservador(IObservador observador) {
        observadores.remove(observador);
    }

    public void notificarTodos(String mensaje) {
        Notificacion notificacion = new Notificacion(mensaje);
        for (IObservador observador : observadores) {
            observador.actualizar(notificacion);
        }
    }

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