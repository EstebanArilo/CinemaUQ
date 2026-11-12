package uqvirutal.edu.co.cinemauq.model;

import java.util.ArrayList;

public class Sala {
    private int numeroSala;
    private String nombre;
    private int capacidad;
    private ArrayList<Asiento> listaAsientos;

    public Sala(int numeroSala, String nombre, int cantidadFilas, int asientosPorFila) {
        this.numeroSala = numeroSala;
        this.nombre = nombre;
        this.capacidad = cantidadFilas * asientosPorFila;
        this.listaAsientos = new ArrayList<>();

        generarAsientosAutomaticos(cantidadFilas, asientosPorFila);
    }

    /**
     * Genera automáticamente la matriz de asientos (Ej: Fila A1, A2... B1, B2...)
     */
    private void generarAsientosAutomaticos(int cantidadFilas, int asientosPorFila) {
        char letraFila = 'A';
        for (int i = 0; i < cantidadFilas; i++) {
            String filaStr = String.valueOf(letraFila);
            for (int j = 1; j <= asientosPorFila; j++) {
                String numeroAsiento = filaStr + "-" + j;

                Asiento asiento = new Asiento(filaStr, numeroAsiento, TipoAsiento.GENERAL, new AsientoDisponible());
                this.listaAsientos.add(asiento);
            }
            letraFila++;
        }
    }

    /**
     * Retorna una lista con todos los asientos que actualmente se encuentran disponibles.
     */
    public ArrayList<Asiento> asientosDisponibles() {
        ArrayList<Asiento> disponibles = new ArrayList<>();
        for (Asiento asiento : listaAsientos) {
            if (asiento.isDisponible()) {
                disponibles.add(asiento);
            }
        }
        return disponibles;
    }

    public void agregarAsiento(Asiento asiento) {
        this.listaAsientos.add(asiento);
    }

    public int getNumeroSala() { return numeroSala; }
    public void setNumeroSala(int numeroSala) { this.numeroSala = numeroSala; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public ArrayList<Asiento> getListaAsientos() { return listaAsientos; }
    public void setListaAsientos(ArrayList<Asiento> listaAsientos) { this.listaAsientos = listaAsientos; }
}