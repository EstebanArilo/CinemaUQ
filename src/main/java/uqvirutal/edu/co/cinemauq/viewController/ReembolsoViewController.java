package uqvirutal.edu.co.cinemauq.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.ReembolsoController;
import uqvirutal.edu.co.cinemauq.model.*;

import java.util.List;

public class ReembolsoViewController {

    private App app;
    private final ReembolsoController reembolsoController;

    private final ObservableList<Cliente> listaClientesObs = FXCollections.observableArrayList();
    private final ObservableList<Compra> listaComprasObs = FXCollections.observableArrayList();
    private final ObservableList<Funcion> listaFuncionesObs = FXCollections.observableArrayList();
    private final ObservableList<String> historialReembolsosObs = FXCollections.observableArrayList();

    @FXML private ComboBox<Cliente> cmbClientes;
    @FXML private ComboBox<Compra> cmbComprasCliente;
    @FXML private Label lblDetalleCompra;

    @FXML private ComboBox<Funcion> cmbFuncionesCancelar;
    @FXML private ListView<String> listViewReembolsos;

    public ReembolsoViewController() {
        this.reembolsoController = new ReembolsoController();
    }

    @FXML
    void initialize() {
        cargarDatosIniciales();
        configurarListeners();
        if (listViewReembolsos != null) {
            listViewReembolsos.setItems(historialReembolsosObs);
        }
    }

    private void cargarDatosIniciales() {
        listaClientesObs.setAll(reembolsoController.obtenerClientes());
        if (cmbClientes != null) {
            cmbClientes.setItems(listaClientesObs);
            if (!listaClientesObs.isEmpty()) {
                cmbClientes.getSelectionModel().selectFirst();
                actualizarComprasCliente(cmbClientes.getSelectionModel().getSelectedItem());
            }
        }

        listaFuncionesObs.setAll(reembolsoController.obtenerFunciones());
        if (cmbFuncionesCancelar != null) {
            cmbFuncionesCancelar.setItems(listaFuncionesObs);
        }

        actualizarHistorial();
    }

    private void configurarListeners() {
        if (cmbClientes != null) {
            cmbClientes.getSelectionModel().selectedItemProperty().addListener((obs, viejo, clienteSeleccionado) -> {
                actualizarComprasCliente(clienteSeleccionado);
            });
        }

        if (cmbComprasCliente != null) {
            cmbComprasCliente.getSelectionModel().selectedItemProperty().addListener((obs, vieja, compraSeleccionada) -> {
                if (compraSeleccionada != null && lblDetalleCompra != null) {
                    String pelicula = (compraSeleccionada.getFuncion() != null && compraSeleccionada.getFuncion().getPelicula() != null)
                            ? compraSeleccionada.getFuncion().getPelicula().getTitulo()
                            : "N/A";
                    lblDetalleCompra.setText("Compra: " + compraSeleccionada.getIdCompra()
                            + " | Película: " + pelicula
                            + " | Total Pagado: $" + compraSeleccionada.getTotal());
                } else if (lblDetalleCompra != null) {
                    lblDetalleCompra.setText("Seleccione una compra pagada para ver detalles.");
                }
            });
        }
    }

    private void actualizarComprasCliente(Cliente cliente) {
        listaComprasObs.clear();
        if (cliente != null) {
            listaComprasObs.setAll(reembolsoController.obtenerComprasPagadasCliente(cliente));
        }
        if (cmbComprasCliente != null) {
            cmbComprasCliente.setItems(listaComprasObs);
        }
        if (lblDetalleCompra != null) {
            lblDetalleCompra.setText("Seleccione una compra pagada para ver detalles.");
        }
    }

    private void actualizarHistorial() {
        historialReembolsosObs.clear();
        List<Reembolso> historial = reembolsoController.obtenerHistorialReembolsos();
        for (Reembolso r : historial) {
            String idCompra = (r.getCompra() != null) ? r.getCompra().getIdCompra() : "N/A";
            double porcentaje = r.getPorcentajeAplicado() * 100.0;
            historialReembolsosObs.add(r.getId()
                    + " | Compra: " + idCompra
                    + " | Motivo: " + r.getMotivo()
                    + " | Devuelto: $" + r.getMontoDevuelto() + " (" + porcentaje + "%)");
        }
    }

    @FXML
    void onSolicitarReembolso() {
        Compra compra = cmbComprasCliente.getSelectionModel().getSelectedItem();
        if (compra == null) {
            mostrarAlerta("Advertencia", "Seleccione una compra pagada del cliente.");
            return;
        }

        try {
            Reembolso reembolso = reembolsoController.solicitarReembolsoCliente(compra);
            double porcentaje = reembolso.getPorcentajeAplicado() * 100.0;
            mostrarAlerta("Reembolso Procesado",
                    "ID Reembolso: " + reembolso.getId()
                            + "\nPorcentaje aplicado: " + porcentaje + "%"
                            + "\nMonto devuelto a la tarjeta virtual: $" + reembolso.getMontoDevuelto());

            actualizarComprasCliente(cmbClientes.getSelectionModel().getSelectedItem());
            actualizarHistorial();
        } catch (CancelacionNoPermitidaException e) {
            mostrarAlerta("Cancelación No Permitida", e.getMessage());
        }
    }

    @FXML
    void onCancelarFuncionMasivo() {
        Funcion funcion = cmbFuncionesCancelar.getSelectionModel().getSelectedItem();
        if (funcion == null) {
            mostrarAlerta("Advertencia", "Seleccione la función que desea cancelar.");
            return;
        }

        List<Reembolso> generados = reembolsoController.cancelarFuncionYReembolsar(funcion);
        int cantidad = 0;
        for (Reembolso r : generados) {
            cantidad++;
        }

        mostrarAlerta("Función Cancelada",
                "La función fue cancelada y se notificó a los clientes.\nTotal de compras reembolsadas al 100%: " + cantidad);

        actualizarComprasCliente(cmbClientes.getSelectionModel().getSelectedItem());
        actualizarHistorial();
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