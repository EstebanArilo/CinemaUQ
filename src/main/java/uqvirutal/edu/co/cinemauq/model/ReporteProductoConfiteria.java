package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReporteProductoConfiteria extends Reporte{
    private ArrayList<ProductoConfiteria> repoProducto;

    public ReporteProductoConfiteria(String titulo, LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador, ArrayList<ProductoConfiteria> repoProducto) {
        super(titulo, fechaInicio, fechaFin, exportador);
        this.repoProducto = repoProducto;
    }

    @Override
    public String generarContenido() {
        String contenido = "Detalle de Productos de Confiteria:\n";

        if (repoProducto != null && !repoProducto.isEmpty()) {
            for (int i = 0; i < repoProducto.size(); i++) {
                ProductoConfiteria producto = repoProducto.get(i);
                contenido = contenido + "- Producto: " + producto.getNombre()
                        + " | Precio: $" + producto.getPrecio() + "\n";
            }
        } else {
            contenido = contenido + "No hay productos de confiteria registrados.\n";
        }

        return contenido;
    }
}
