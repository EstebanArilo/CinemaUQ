package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Administrador extends Usuario {
    private ArrayList<ProductoConfiteria> listProductos;

    public Administrador(String id, String nombre, String correo, String contraseña) {
        super(id, nombre, correo, contraseña);
        this.listProductos = new ArrayList<>();
    }

    public void gestionarPeliculas(Pelicula pelicula) {
        Cine.getInstancia().agregarPelicula(pelicula);
        System.out.println("Película gestionada y añadida a la cartelera: " + pelicula.getTitulo());
    }

    public void gestionarSalas(Sala sala) {
        Cine.getInstancia().agregarSala(sala);
        System.out.println("Sala gestionada y registrada: " + sala.getNombre());
    }

    public void gestionarFunciones(Funcion funcion) {
        Cine.getInstancia().agregarFuncion(funcion);
        System.out.println("Función programada exitosamente para la película: " + funcion.getPelicula().getTitulo());
    }

    public void gestionarProductos() {
        System.out.println("Gestionando catálogo de productos de confitería. Total productos: " + listProductos.size());
    }

    public void recargarTarjeta(TarjetaVirtual tarjeta, double monto) {
        if (tarjeta != null) {
            tarjeta.recargar(monto);
            System.out.println("Recarga de $" + monto + " realizada con éxito a la tarjeta: " + tarjeta.getNumero());
        } else {
            System.out.println("Error: La tarjeta virtual no es válida.");
        }
    }

    public void cancelarFuncion(Funcion funcion) {
        if (funcion != null) {
            funcion.cancelarFuncion();
        }
    }

    public void generarReporte(FactoryReporte factory, LocalDate fechaInicio, LocalDate fechaFin, Exportador exportador, String rutaDestino) {
        Reporte reporte = factory.crearReporte(fechaInicio, fechaFin, exportador);
        reporte.exportar(rutaDestino);
        System.out.println("Reporte generado por el administrador exitosamente.");
    }

    public ComboConfiteria crearCombo(String id, String nombre, double descuento, ArrayList<ProductoConfiteria> productosBase) {
        ComboPersonalizado.Builder builder = new ComboPersonalizado.Builder()
                .setIdProducto(id)
                .setNombre(nombre)
                .setDescuento(descuento);

        for (int i = 0; i < productosBase.size(); i++) {
            builder.agregarProducto(productosBase.get(i));
        }

        ComboConfiteria nuevoCombo = builder.build();
        this.listProductos.add(nuevoCombo);
        return nuevoCombo;
    }

    public boolean eliminarCombo(ComboConfiteria combo) {
        return this.listProductos.remove(combo);
    }

    public ProductoSimple crearProducto(String id, String nombre, double precio, CategoriaProducto categoria) {
        ProductoSimple nuevoProducto = new ProductoSimple(id, precio, nombre, categoria);
        this.listProductos.add(nuevoProducto);
        return nuevoProducto;
    }

    public boolean eliminarProducto(ProductoConfiteria producto) {
        return this.listProductos.remove(producto);
    }

    public ArrayList<ProductoConfiteria> getListProductos() {
        return listProductos;
    }

    public void setListProductos(ArrayList<ProductoConfiteria> listProductos) {
        this.listProductos = listProductos;
    }
}