package uqvirutal.edu.co.cinemauq.controller;

import uqvirutal.edu.co.cinemauq.model.*;

public class LoginController {

    private final Cine cine;

    public LoginController() {
        this.cine = Cine.getInstancia();
    }

    public Usuario autenticarUsuario(String correo, String contrasena) {
        if (correo == null || contrasena == null || cine.getListaUsuarios() == null) {
            return null;
        }
        for (Usuario u : cine.getListaUsuarios()) {
            if (u.iniciarSesion(correo.trim(), contrasena)) {
                return u;
            }
        }
        return null;
    }

    public Cliente registrarNuevoCliente(String id, String nombre, String correo, String contrasena, String numeroTarjeta) {
        if (existeCorreo(correo)) {
            return null;
        }

        Cliente nuevoCliente = new Cliente(id, nombre, correo, contrasena, numeroTarjeta);
        Administrador admin = obtenerAdministrador();

        boolean creado;
        if (admin != null) {
            creado = admin.crearCliente(nuevoCliente);
            if (creado && admin.getServicioNotificaciones() != null) {
                admin.getServicioNotificaciones().agregarObservador(nuevoCliente);
            }
        } else {
            cine.agregarUsuario(nuevoCliente);
            creado = true;
        }

        return creado ? nuevoCliente : null;
    }

    public boolean existeCorreo(String correo) {
        if (cine.getListaUsuarios() == null) {
            return false;
        }
        for (Usuario u : cine.getListaUsuarios()) {
            if (u.getCorreo() != null && u.getCorreo().equalsIgnoreCase(correo.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean existeId(String id) {
        if (cine.getListaUsuarios() == null) {
            return false;
        }
        for (Usuario u : cine.getListaUsuarios()) {
            if (u.getId() != null && u.getId().equalsIgnoreCase(id.trim())) {
                return true;
            }
        }
        return false;
    }

    private Administrador obtenerAdministrador() {
        if (cine.getListaUsuarios() != null) {
            for (Usuario u : cine.getListaUsuarios()) {
                if (u instanceof Administrador) {
                    return (Administrador) u;
                }
            }
        }
        return null;
    }
}