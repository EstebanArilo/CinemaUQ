package uqvirutal.edu.co.cinemauq.model;

public class Asiento {
    private String fila;
    private String numero;
    private TipoAsiento tipoAsiento;
    private EstadoAsiento estado;

    public Asiento(String fila, String numero, TipoAsiento tipoAsiento, EstadoAsiento estado) {
        this.fila = fila;
        this.numero = numero;
        this.tipoAsiento = tipoAsiento;
        this.estado = estado;
    }

    public void reservar() {
        estado.reservar(this);
    }

    public void liberar() {
        estado.liberar(this);
    }

    public boolean isDisponible() {
        return this.estado instanceof AsientoDisponible;
    }

    public String getFila() { return fila; }
    public void setFila(String fila) { this.fila = fila; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public TipoAsiento getTipoAsiento() {return tipoAsiento;}
    public void setTipoAsiento(TipoAsiento tipoAsiento) {this.tipoAsiento = tipoAsiento;}

    public EstadoAsiento getEstado() { return estado; }
    public void setEstado(EstadoAsiento estado) { this.estado = estado; }
}