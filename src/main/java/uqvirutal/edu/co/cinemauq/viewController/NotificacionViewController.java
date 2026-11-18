package uqvirutal.edu.co.cinemauq.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.NotificacionController;
import uqvirutal.edu.co.cinemauq.model.*;

import java.time.format.DateTimeFormatter;
import java.util.Set;

public class NotificacionViewController {

    private App app;
    private final NotificacionController notificacionController;
    private final ObservableList<Notificacion> notificacionesObs = FXCollections.observableArrayList();
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML private Label lblSubtituloUsuario;
    @FXML private ComboBox<String> cmbFiltroTipo;
    @FXML private ListView<Notificacion> listViewNotificaciones;

    @FXML private VBox boxPreferenciasCliente;
    @FXML private CheckBox chkInteresEstrenos;
    @FXML private CheckBox chkInteresRecargas;
    @FXML private CheckBox chkInteresCancelaciones;

    @FXML private VBox boxEnvioAdmin;
    @FXML private TextField txtMensajeMasivo;

    public NotificacionViewController() {
        this.notificacionController = new NotificacionController();
    }

    @FXML
    void initialize() {
        if (cmbFiltroTipo != null) {
            cmbFiltroTipo.setItems(FXCollections.observableArrayList(
                    "Todas",
                    "Solo No Leídas",
                    "Según mis Intereses",
                    "Estrenos y Funciones",
                    "Recargas de Tarjeta",
                    "Cancelaciones y Avisos"
            ));
            cmbFiltroTipo.getSelectionModel().selectFirst();
            cmbFiltroTipo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> cargarNotificaciones());
        }

        configurarCeldasLista();
    }

    public void setApp(App app) {
        this.app = app;
        configurarPorRol();
        cargarNotificaciones();
    }

    private void configurarPorRol() {
        if (app == null || app.getUsuarioLogueado() == null) {
            return;
        }
        Usuario usuario = app.getUsuarioLogueado();
        boolean esAdmin = usuario instanceof Administrador;

        if (lblSubtituloUsuario != null) {
            lblSubtituloUsuario.setText("Bandeja de notificaciones de: " + usuario.getNombre());
        }

        if (boxPreferenciasCliente != null) {
            boxPreferenciasCliente.setVisible(!esAdmin);
            boxPreferenciasCliente.setManaged(!esAdmin);
        }

        if (boxEnvioAdmin != null) {
            boxEnvioAdmin.setVisible(esAdmin);
            boxEnvioAdmin.setManaged(esAdmin);
        }

        if (!esAdmin) {
            Set<String> prefs = notificacionController.obtenerPreferencias(usuario.getId());
            if (chkInteresEstrenos != null) chkInteresEstrenos.setSelected(prefs.contains("ESTRENOS"));
            if (chkInteresRecargas != null) chkInteresRecargas.setSelected(prefs.contains("RECARGAS"));
            if (chkInteresCancelaciones != null) chkInteresCancelaciones.setSelected(prefs.contains("CANCELACIONES"));
        }
    }

    private void configurarCeldasLista() {
        if (listViewNotificaciones == null) {
            return;
        }
        listViewNotificaciones.setItems(notificacionesObs);
        listViewNotificaciones.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Notificacion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    String estado = item.isLeida() ? "[LEÍDA]" : "[NUEVA]";
                    String fechaStr = (item.getFecha() != null) ? item.getFecha().format(FORMATO_FECHA) : "";
                    setText(estado + " (" + fechaStr + ")  " + item.getMensaje());

                    if (!item.isLeida()) {
                        setStyle("-fx-font-weight: bold; -fx-background-color: #E3F2FD; -fx-text-fill: #0D47A1; -fx-padding: 8;");
                    } else {
                        setStyle("-fx-font-weight: normal; -fx-background-color: transparent; -fx-text-fill: #455A64; -fx-padding: 8;");
                    }
                }
            }
        });
    }

    private void cargarNotificaciones() {
        notificacionesObs.clear();
        if (app == null || app.getUsuarioLogueado() == null) {
            return;
        }
        String filtro = (cmbFiltroTipo != null && cmbFiltroTipo.getValue() != null) ? cmbFiltroTipo.getValue() : "Todas";
        notificacionesObs.setAll(notificacionController.obtenerNotificacionesUsuario(app.getUsuarioLogueado(), filtro));
    }

    @FXML
    void onGuardarPreferencias() {
        if (app == null || !(app.getUsuarioLogueado() instanceof Cliente)) {
            return;
        }
        notificacionController.guardarPreferencias(
                app.getUsuarioLogueado().getId(),
                chkInteresEstrenos != null && chkInteresEstrenos.isSelected(),
                chkInteresRecargas != null && chkInteresRecargas.isSelected(),
                chkInteresCancelaciones != null && chkInteresCancelaciones.isSelected()
        );
        cargarNotificaciones();
        mostrarAlerta("Preferencias Guardadas", "Tus intereses de notificación han sido actualizados.");
    }

    @FXML
    void onMarcarLeida() {
        Notificacion seleccionada = listViewNotificaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Seleccione una notificación de la lista.");
            return;
        }
        notificacionController.marcarComoLeida(app != null ? app.getUsuarioLogueado() : null, seleccionada);
        listViewNotificaciones.refresh();
        cargarNotificaciones();
    }

    @FXML
    void onMarcarTodasLeidas() {
        notificacionController.marcarTodasComoLeidas(app != null ? app.getUsuarioLogueado() : null, notificacionesObs);
        listViewNotificaciones.refresh();
        cargarNotificaciones();
    }

    @FXML
    void onEnviarAvisoMasivo() {
        if (app == null || !(app.getUsuarioLogueado() instanceof Administrador)) {
            return;
        }
        String mensaje = (txtMensajeMasivo != null) ? txtMensajeMasivo.getText() : "";
        boolean enviado = notificacionController.enviarAvisoMasivo((Administrador) app.getUsuarioLogueado(), mensaje);
        if (enviado) {
            txtMensajeMasivo.clear();
            cargarNotificaciones();
            mostrarAlerta("Notificación Enviada", "El aviso fue enviado a todos los clientes suscritos.");
        } else {
            mostrarAlerta("Advertencia", "Escriba el mensaje de promoción o aviso a enviar.");
        }
    }

    @FXML
    void onVolver() {
        if (app != null) {
            app.openPrimary();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}