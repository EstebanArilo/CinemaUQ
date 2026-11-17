package uqvirutal.edu.co.cinemauq.controller;

import uqvirutal.edu.co.cinemauq.model.*;

import java.util.*;

public class NotificacionController {

    private final Cine cine;
    private static final Map<String, Set<String>> preferenciasPorCliente = new HashMap<>();

    public NotificacionController() {
        this.cine = Cine.getInstancia();
    }

    public List<Notificacion> obtenerNotificacionesUsuario(Usuario usuario, String filtro) {
        List<Notificacion> fuente = new ArrayList<>();
        if (usuario == null) {
            return fuente;
        }

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            if (cliente.obtenerNotificaciones() != null) {
                fuente.addAll(cliente.obtenerNotificaciones());
            }
        } else {
            Set<String> vistas = new HashSet<>();
            for (Usuario u : cine.getListaUsuarios()) {
                if (u instanceof Cliente) {
                    for (Notificacion n : ((Cliente) u).obtenerNotificaciones()) {
                        String clave = n.getFecha() + "|" + n.getMensaje();
                        if (vistas.add(clave)) {
                            fuente.add(n);
                        }
                    }
                }
            }
        }

        Collections.reverse(fuente);

        Set<String> prefs = obtenerPreferencias(usuario.getId());
        List<Notificacion> filtradas = new ArrayList<>();
        for (Notificacion n : fuente) {
            if (cumpleFiltro(n, filtro, prefs)) {
                filtradas.add(n);
            }
        }
        return filtradas;
    }

    public int contarNoLeidas(Cliente cliente) {
        int contador = 0;
        if (cliente != null && cliente.obtenerNotificaciones() != null) {
            for (Notificacion n : cliente.obtenerNotificaciones()) {
                if (!n.isLeida()) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public void marcarComoLeida(Usuario usuario, Notificacion notificacion) {
        if (notificacion == null) {
            return;
        }
        if (usuario instanceof Cliente) {
            ((Cliente) usuario).leerNotificacion(notificacion);
        } else {
            notificacion.marcarComoLeida();
        }
    }

    public void marcarTodasComoLeidas(Usuario usuario, List<Notificacion> visibles) {
        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            for (Notificacion n : cliente.obtenerNotificaciones()) {
                cliente.leerNotificacion(n);
            }
        } else if (visibles != null) {
            for (Notificacion n : visibles) {
                n.marcarComoLeida();
            }
        }
    }

    public Set<String> obtenerPreferencias(String idUsuario) {
        return preferenciasPorCliente.computeIfAbsent(
                idUsuario,
                k -> new HashSet<>(Arrays.asList("ESTRENOS", "RECARGAS", "CANCELACIONES"))
        );
    }

    public void guardarPreferencias(String idUsuario, boolean estrenos, boolean recargas, boolean cancelaciones) {
        Set<String> nuevas = new HashSet<>();
        if (estrenos) nuevas.add("ESTRENOS");
        if (recargas) nuevas.add("RECARGAS");
        if (cancelaciones) nuevas.add("CANCELACIONES");
        preferenciasPorCliente.put(idUsuario, nuevas);
    }

    public boolean enviarAvisoMasivo(Administrador admin, String mensaje) {
        if (admin != null && admin.getServicioNotificaciones() != null && mensaje != null && !mensaje.trim().isEmpty()) {
            admin.getServicioNotificaciones().notificarTodos("AVISO CINEMAUQ: " + mensaje.trim());
            return true;
        }
        return false;
    }

    private boolean cumpleFiltro(Notificacion n, String filtro, Set<String> prefs) {
        if (filtro == null) {
            return true;
        }
        String msg = (n.getMensaje() != null) ? n.getMensaje().toLowerCase() : "";
        String categoria = clasificarMensaje(msg);

        return switch (filtro) {
            case "Solo No Leídas" -> !n.isLeida();
            case "Según mis Intereses" -> prefs.contains(categoria);
            case "Estrenos y Funciones" -> "ESTRENOS".equals(categoria);
            case "Recargas de Tarjeta" -> "RECARGAS".equals(categoria);
            case "Cancelaciones y Avisos" -> "CANCELACIONES".equals(categoria);
            default -> true;
        };
    }

    private String clasificarMensaje(String msgLower) {
        if (msgLower.contains("estreno") || msgLower.contains("función disponible") || msgLower.contains("cartelera")) {
            return "ESTRENOS";
        }
        if (msgLower.contains("recarga") || msgLower.contains("tarjeta")) {
            return "RECARGAS";
        }
        if (msgLower.contains("cancelada") || msgLower.contains("reembolso") || msgLower.contains("aviso")) {
            return "CANCELACIONES";
        }
        return "ESTRENOS";
    }
}