package uqvirutal.edu.co.cinemauq.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.ReporteController;

import java.time.LocalDate;
import java.util.Map;

public class ReporteViewController {

    private App app;
    private final ReporteController reporteController;

    // Pestaña 1: Reportes Exportables
    @FXML private ComboBox<String> cmbTipoReporte;
    @FXML private ComboBox<String> cmbFormato;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private TextField txtRutaArchivo;
    @FXML private TextArea txtAreaVistaPrevia;

    // Pestaña 2: Reportes Visuales y Estadísticas
    @FXML private Label lblKpiVentas;
    @FXML private Label lblKpiEntradas;
    @FXML private Label lblKpiOcupacion;
    @FXML private Label lblKpiReembolsos;

    @FXML private BarChart<String, Number> barChartVentas;
    @FXML private PieChart pieChartOcupacion;
    @FXML private LineChart<String, Number> lineChartTendencia;

    public ReporteViewController() {
        this.reporteController = new ReporteController();
    }

    @FXML
    void initialize() {
        configurarFormularioExportacion();
        actualizarDashboardVisual();
    }

    private void configurarFormularioExportacion() {
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

    @FXML
    void onActualizarGraficos() {
        if (pieChartOcupacion != null) {
            pieChartOcupacion.setAnimated(false);
            pieChartOcupacion.setLabelsVisible(false);
            pieChartOcupacion.setLegendVisible(true);
        }
        if (barChartVentas != null) {
            barChartVentas.setAnimated(false);
        }
        if (lineChartTendencia != null) {
            lineChartTendencia.setAnimated(false);
        }
        actualizarDashboardVisual();
    }

    private void actualizarDashboardVisual() {
        double totalVentas = reporteController.calcularTotalVentasPagadas();
        int totalEntradas = reporteController.contarTotalEntradasVendidas();
        int totalReembolsos = reporteController.contarTotalReembolsos();
        int[] asientos = reporteController.obtenerConteoAsientosOcupadosYDisponibles();
        int ocupados = asientos[0];
        int disponibles = asientos[1];
        int totalAsientos = ocupados + disponibles;
        double porcentajeOcupacion = (totalAsientos > 0) ? (ocupados * 100.0 / totalAsientos) : 0.0;

        if (lblKpiVentas != null) lblKpiVentas.setText("$" + String.format("%.0f", totalVentas));
        if (lblKpiEntradas != null) lblKpiEntradas.setText(String.valueOf(totalEntradas));
        if (lblKpiOcupacion != null) lblKpiOcupacion.setText(String.format("%.1f%%", porcentajeOcupacion));
        if (lblKpiReembolsos != null) lblKpiReembolsos.setText(String.valueOf(totalReembolsos));

        if (barChartVentas != null) {
            barChartVentas.getData().clear();
            XYChart.Series<String, Number> seriePeliculas = new XYChart.Series<>();
            seriePeliculas.setName("Ingresos ($)");

            Map<String, Double> ventasPelicula = reporteController.obtenerVentasPorPelicula();
            for (Map.Entry<String, Double> entry : ventasPelicula.entrySet()) {
                seriePeliculas.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barChartVentas.getData().add(seriePeliculas);
        }

        if (pieChartOcupacion != null) {
            ObservableList<PieChart.Data> datosPastel = FXCollections.observableArrayList();
            if (totalAsientos > 0) {
                datosPastel.add(new PieChart.Data("Ocupados (" + ocupados + ")", ocupados));
                datosPastel.add(new PieChart.Data("Disponibles (" + disponibles + ")", disponibles));
            } else {
                datosPastel.add(new PieChart.Data("Sin asientos programados", 1));
            }
            pieChartOcupacion.setData(datosPastel);
        }

        if (lineChartTendencia != null) {
            lineChartTendencia.getData().clear();

            XYChart.Series<String, Number> serieVentas = new XYChart.Series<>();
            serieVentas.setName("Ventas ($)");
            for (Map.Entry<String, Double> entry : reporteController.obtenerEvolucionVentasPorFecha().entrySet()) {
                serieVentas.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            XYChart.Series<String, Number> serieRecargas = new XYChart.Series<>();
            serieRecargas.setName("Recargas ($)");
            for (Map.Entry<String, Double> entry : reporteController.obtenerEvolucionRecargasPorFecha().entrySet()) {
                serieRecargas.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            lineChartTendencia.getData().addAll(serieVentas, serieRecargas);
        }
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