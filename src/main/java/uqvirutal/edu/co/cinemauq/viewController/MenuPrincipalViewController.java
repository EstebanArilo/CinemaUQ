package uqvirutal.edu.co.cinemauq.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.model.Administrador;
import uqvirutal.edu.co.cinemauq.model.Cliente;
import uqvirutal.edu.co.cinemauq.model.Notificacion;
import uqvirutal.edu.co.cinemauq.model.Usuario;

public class MenuPrincipalViewController {

    private App app;

    @FXML private Label lblUsuarioActivo;
    @FXML private Button btnFunciones;
    @FXML private Button btnCompras;
    @FXML private Button btnTarjeta;
    @FXML private Button btnReembolsos;
    @FXML private Button btnResenas;
    @FXML private Button btnReportes;
    @FXML private Button btnNotificaciones;

    public void setApp(App app) {
        this.app = app;
        configurarPermisosPorRol();
        actualizarContadorNotificaciones();
    }

    private void configurarPermisosPorRol() {
        if (app == null || app.getUsuarioLogueado() == null) {
            return;
        }
        Usuario usuario = app.getUsuarioLogueado();
        boolean esAdmin = usuario instanceof Administrador;
        String rol = esAdmin ? "Administrador" : "Cliente";

        if (lblUsuarioActivo != null) {
            lblUsuarioActivo.setText("Sesión activa: " + usuario.getNombre() + " [" + rol + "]");
        }

        if (btnFunciones != null) {
            btnFunciones.setVisible(esAdmin);
            btnFunciones.setManaged(esAdmin);
        }
        if (btnReportes != null) {
            btnReportes.setVisible(esAdmin);
            btnReportes.setManaged(esAdmin);
        }

        if (btnCompras != null) {
            btnCompras.setVisible(!esAdmin);
            btnCompras.setManaged(!esAdmin);
        }
        if (btnResenas != null) {
            btnResenas.setVisible(!esAdmin);
            btnResenas.setManaged(!esAdmin);
        }
    }

    private void actualizarContadorNotificaciones() {
        if (btnNotificaciones == null || app == null || app.getUsuarioLogueado() == null) {
            return;
        }
        if (app.getUsuarioLogueado() instanceof Cliente) {
            Cliente cliente = (Cliente) app.getUsuarioLogueado();
            int sinLeer = 0;
            if (cliente.obtenerNotificaciones() != null) {
                for (Notificacion n : cliente.obtenerNotificaciones()) {
                    if (!n.isLeida()) {
                        sinLeer++;
                    }
                }
            }
            btnNotificaciones.setText(sinLeer > 0 ? "Notificaciones (" + sinLeer + ")" : "Notificaciones");
        } else {
            btnNotificaciones.setText("Centro de Avisos");
        }
    }

    @FXML
    void onCerrarSesion() {
        if (app != null) {
            if (app.getUsuarioLogueado() != null) {
                app.getUsuarioLogueado().cerrarSesion();
                app.setUsuarioLogueado(null);
            }
            app.openLogin();
        }
    }

    @FXML void onAbrirFunciones() { if (app != null) app.openFunciones(); }
    @FXML void onAbrirCompras() { if (app != null) app.openCompras(); }
    @FXML void onAbrirTarjetaVirtual() { if (app != null) app.openTarjetaVirtual(); }
    @FXML void onAbrirReembolsos() { if (app != null) app.openReembolsos(); }
    @FXML void onAbrirResenas() { if (app != null) app.openResenas(); }
    @FXML void onAbrirReportes() { if (app != null) app.openReportes(); }
    @FXML void onAbrirNotificaciones() { if (app != null) app.openNotificaciones(); }
}