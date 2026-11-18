package uqvirutal.edu.co.cinemauq.viewController;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.ReporteController;

import java.time.LocalDate;

public class ReporteViewController {

    private App app;
    private final ReporteController reporteController;

    @FXML private ComboBox<String> cmbTipoReporte;
    @FXML private ComboBox<String> cmbFormato;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private TextField txtRutaArchivo;
    @FXML private TextArea txtAreaVistaPrevia;

    public ReporteViewController() {
        this.reporteController = new ReporteController();
    }

    @FXML
    void initialize() {
        if (cmbTipoReporte != null) {
            cmbTipoReporte.setItems(FXCollections.observableArrayList(
                    "Ventas",
                    "Cancelaciones",
                    "Entradas Vendidas",
                    "Ocupación de Salas",
                    "Productos de Confitería",
                    "Puntos de Clientes",
                    "Recargas"
            ));
            cmbTipoReporte.getSelectionModel().selectFirst();
        }

        if (cmbFormato != null) {
            cmbFormato.setItems(FXCollections.observableArrayList("TXT", "PDF"));
            cmbFormato.getSelectionModel().selectFirst();
        }

        if (dpFechaInicio != null) {
            dpFechaInicio.setValue(LocalDate.now().minusMonths(1));
        }

        if (dpFechaFin != null) {
            dpFechaFin.setValue(LocalDate.now());
        }

        if (txtRutaArchivo != null) {
            txtRutaArchivo.setText("reportes/reporte_cinemauq");
        }
    }

    @FXML
    void onGenerarReporte() {
        String tipo = cmbTipoReporte.getSelectionModel().getSelectedItem();
        String formato = cmbFormato.getSelectionModel().getSelectedItem();
        LocalDate inicio = dpFechaInicio.getValue();
        LocalDate fin = dpFechaFin.getValue();
        String ruta = (txtRutaArchivo != null && !txtRutaArchivo.getText().isEmpty())
                ? txtRutaArchivo.getText()
                : "reporte_cinemauq";

        if (tipo == null || formato == null || inicio == null || fin == null) {
            mostrarAlerta("Advertencia", "Complete todos los campos (tipo, formato y rango de fechas).");
            return;
        }

        if (fin.isBefore(inicio)) {
            mostrarAlerta("Error", "La fecha de fin no puede ser anterior a la fecha de inicio.");
            return;
        }

        String contenidoGenerado = reporteController.generarYExportarReporte(tipo, formato, inicio, fin, ruta);
        if (txtAreaVistaPrevia != null) {
            txtAreaVistaPrevia.setText(contenidoGenerado);
        }

        mostrarAlerta("Reporte Generado",
                "Reporte de '" + tipo + "' generado y exportado en formato " + formato + " correctamente.");
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