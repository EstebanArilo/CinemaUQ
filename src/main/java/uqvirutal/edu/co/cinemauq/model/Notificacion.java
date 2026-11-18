package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDateTime;

public class Notificacion {
    private String mensaje;
    private LocalDateTime fecha;
    private boolean leida;

    public Notificacion(String mensaje) {
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
        this.leida = false;
    }

    public Notificacion(String mensaje, LocalDateTime fecha, boolean leida) {
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leida = leida;
    }

    public void marcarComoLeida() {
        this.leida = true;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}