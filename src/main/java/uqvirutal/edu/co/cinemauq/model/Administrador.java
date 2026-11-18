package uqvirutal.edu.co.cinemauq.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Administrador extends Usuario {
    private ArrayList<ProductoConfiteria> listProductos;
    private ServicioNotificacion servicioNotificacion;

    public Administrador(String id, String nombre, String correo, String contraseña) {
        super(id, nombre, correo, contraseña);
        this.listProductos = new ArrayList<>();
        this.servicioNotificacion = new ServicioNotificacion();
    }

    public ArrayList<ProductoConfiteria> getListProductos() {
        return listProductos;
    }

    public void setListProductos(ArrayList<ProductoConfiteria> listProductos) {
        this.listProductos = listProductos;
    }

    public ServicioNotificacion getServicioNotificaciones() {
        return servicioNotificacion;
    }

    public void setServicioNotificaciones(ServicioNotificacion servicioNotificacion) {
        this.servicioNotificacion = servicioNotificacion;
    }

    public void gestionarPeliculas(Pelicula pelicula) {
        if (pelicula != null) {
            Cine.getInstancia().agregarPelicula(pelicula);
            System.out.println("Película gestionada y añadida a la cartelera: " + pelicula.getTitulo());

            if (servicioNotificacion != null) {
                servicioNotificacion.notificarTodos(
                        "¡Nuevo estreno en CinemaUQ! Ya está disponible en cartelera: " + pelicula.getTitulo()
                );
            }
        }
    }

    public void gestionarFunciones(Funcion funcion) {
        if (funcion != null) {
            Cine.getInstancia().agregarFuncion(funcion);
            System.out.println("Función programada exitosamente para la película: " + funcion.getPelicula().getTitulo());

            if (servicioNotificacion != null) {
                servicioNotificacion.notificarTodos(
                        "Nueva función disponible para " + funcion.getPelicula().getTitulo() + " el " + funcion.getFechaHora()
                );
            }
        }
    }

    public void cancelarFuncion(Funcion funcion) {
        if (funcion != null) {
            funcion.cancelarFuncion();
            System.out.println("Función cancelada.");

            if (servicioNotificacion != null) {
                servicioNotificacion.notificarTodos(
                        "AVISO IMPORTANTE: La función de " + funcion.getPelicula().getTitulo() + " (" + funcion.getFechaHora() + ") ha sido cancelada."
                );
            }
        }
    }
    public void recargarTarjeta(TarjetaVirtual tarjeta, double monto) {
        if (tarjeta != null) {
            tarjeta.recargar(monto);
            System.out.println("Recarga de $" + monto + " realizada con éxito a la tarjeta: " + tarjeta.getNumero());

            if (servicioNotificacion != null) {
                for (Usuario usuario : Cine.getInstancia().getListaUsuarios()) {
                    if (usuario instanceof Cliente) {
                        Cliente cliente = (Cliente) usuario;
                        if (cliente.getTarjetaVirtual() == tarjeta) {
                            servicioNotificacion.notificarUsuario(
                                    cliente,
                                    "Recarga exitosa por $" + monto + " en su Tarjeta Virtual N° " + tarjeta.getNumero()
                            );
                        }
                    }
                }
            }
        } else {
            System.out.println("Error: La tarjeta virtual no es válida.");
        }
    }
    public void gestionarSalas(Sala sala) {
        Cine.getInstancia().agregarSala(sala);
        System.out.println("Sala gestionada y registrada: " + sala.getNombre());
    }

    public void gestionarProductos() {
        int total = 0;
        for (ProductoConfiteria p : listProductos) {
            total++;
        }
        System.out.println("Gestionando catálogo de productos de confitería. Total productos: " + total);
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

        for (ProductoConfiteria producto : productosBase) {
            builder.agregarProducto(producto);
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

    public boolean crearCliente(Cliente cliente) {
        if (cliente != null && buscarCliente(cliente.getId()) == null) {
            Cine.getInstancia().getListaUsuarios().add(cliente);
            return true;
        }
        return false;
    }

    public Cliente buscarCliente(String id) {
        for (Usuario usuario : Cine.getInstancia().getListaUsuarios()) {
            if (usuario instanceof Cliente && usuario.getId().equals(id)) {
                return (Cliente) usuario;
            }
        }
        return null;
    }

    public boolean eliminarCliente(String id) {
        Cliente cliente = buscarCliente(id);
        if (cliente != null) {
            return Cine.getInstancia().getListaUsuarios().remove(cliente);
        }
        return false;
    }

    public boolean actualizarCliente(String id, String nuevoNombre, String nuevoCorreo, String nuevaContrasena) {
        Cliente cliente = buscarCliente(id);
        if (cliente != null) {
            cliente.setNombre(nuevoNombre);
            cliente.setCorreo(nuevoCorreo);
            cliente.setContraseña(nuevaContrasena);
            return true;
        }
        return false;
    }
}
