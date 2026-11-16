package uqvirutal.edu.co.cinemauq.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.ResenaController;
import uqvirutal.edu.co.cinemauq.model.Cliente;
import uqvirutal.edu.co.cinemauq.model.Resena;

import java.time.LocalDate;
import java.util.List;

public class ResenaViewController {

    private App app;
    private final ResenaController resenaController;

    private final ObservableList<String> listaResenasObservable = FXCollections.observableArrayList();

    @FXML private TextField txtIdResena;
    @FXML private TextField txtCalificacion;
    @FXML private TextArea txtComentario;
    @FXML private ListView<String> listViewResenas;
    @FXML private Label lblPromedioCalificacion;

    public ResenaViewController() {
        this.resenaController = new ResenaController();
    }

    @FXML
    void initialize() {
        cargarResenas();
    }

    /**
     * Carga y actualiza los elementos visuales del ListView y el promedio.
     */
    public void cargarResenas() {
        List<Resena> resenas = resenaController.obtenerListaResenas();
        listaResenasObservable.clear();

        if (resenas != null) {
            for (Resena r : resenas) {
                listaResenasObservable.add(r.obtenerResumen());
            }
        }

        if (listViewResenas != null) {
            listViewResenas.setItems(listaResenasObservable);
        }

        double promedio = resenaController.calcularPromedioCalificacion();
        if (lblPromedioCalificacion != null) {
            lblPromedioCalificacion.setText("Calificación Promedio del Cine: " + String.format("%.2f", promedio) + " ⭐");
        }
    }

    /**
     * Evento vinculado al botón de registrar reseña en la vista FXML.
     */
    @FXML
    void onRegistrarResena() {
        try {
            String id = txtIdResena.getText();
            String califStr = txtCalificacion.getText();
            String comentario = txtComentario.getText();

            if (id.isEmpty() || califStr.isEmpty() || comentario.isEmpty()) {
                mostrarAlerta("Advertencia", "Por favor, complete todos los campos obligatorios.");
                return;
            }

            int calificacion = Integer.parseInt(califStr);
            if (calificacion < 1 || calificacion > 5) {
                mostrarAlerta("Error de validación", "La calificación debe ser un valor entero entre 1 y 5.");
                return;
            }

            // Cliente simulado o el que tenga la sesión activa
            Cliente clienteMock = new Cliente("C-001", "Cliente Frecuente", null, null, null);

            resenaController.registrarResena(id, calificacion, comentario, LocalDate.now(), clienteMock);

            cargarResenas();
            limpiarCampos();
            mostrarAlerta("Éxito", "¡Reseña registrada y publicada correctamente!");

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "La calificación numérica debe ingresarse correctamente.");
        }
    }

    private void limpiarCampos() {
        if (txtIdResena != null) txtIdResena.clear();
        if (txtCalificacion != null) txtCalificacion.clear();
        if (txtComentario != null) txtComentario.clear();
    }

    public void setApp(App app) {
        this.app = app;
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