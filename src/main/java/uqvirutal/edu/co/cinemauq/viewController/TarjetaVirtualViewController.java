package uqvirutal.edu.co.cinemauq.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.TarjetaVirtualController;
import uqvirutal.edu.co.cinemauq.model.Cliente;
import uqvirutal.edu.co.cinemauq.model.Movimiento;
import uqvirutal.edu.co.cinemauq.model.TarjetaVirtual;

import java.util.List;

public class TarjetaVirtualViewController {

    private App app;
    private final TarjetaVirtualController tarjetaController;

    private final ObservableList<Cliente> listaClientesObs = FXCollections.observableArrayList();
    private final ObservableList<String> listaMovimientosObs = FXCollections.observableArrayList();

    @FXML private ComboBox<Cliente> cmbClientes;
    @FXML private Label lblNumeroTarjeta;
    @FXML private Label lblEstadoTarjeta;
    @FXML private Label lblSaldoActual;
    @FXML private Label lblPuntosVigentes;

    @FXML private TextField txtMontoRecarga;
    @FXML private TextField txtPuntosRedimir;
    @FXML private ListView<String> listViewMovimientos;

    public TarjetaVirtualViewController() {
        this.tarjetaController = new TarjetaVirtualController();
    }

    @FXML
    void initialize() {
        cargarClientes();
        configurarListeners();
        if (listViewMovimientos != null) {
            listViewMovimientos.setItems(listaMovimientosObs);
        }
    }

    private void cargarClientes() {
        List<Cliente> clientes = tarjetaController.obtenerClientes();
        listaClientesObs.setAll(clientes);
        if (cmbClientes != null) {
            cmbClientes.setItems(listaClientesObs);
            if (!listaClientesObs.isEmpty()) {
                cmbClientes.getSelectionModel().selectFirst();
                actualizarVistaCliente(cmbClientes.getSelectionModel().getSelectedItem());
            }
        }
    }

    private void configurarListeners() {
        if (cmbClientes != null) {
            cmbClientes.getSelectionModel().selectedItemProperty().addListener((obs, viejo, clienteSeleccionado) -> {
                actualizarVistaCliente(clienteSeleccionado);
            });
        }
    }

    private void actualizarVistaCliente(Cliente cliente) {
        if (cliente == null || cliente.getTarjetaVirtual() == null) {
            lblNumeroTarjeta.setText("Número: -");
            lblEstadoTarjeta.setText("Estado: -");
            lblSaldoActual.setText("Saldo: $0.0");
            lblPuntosVigentes.setText("Puntos Vigentes: 0.0");
            listaMovimientosObs.clear();
            return;
        }

        TarjetaVirtual tarjeta = cliente.getTarjetaVirtual();
        lblNumeroTarjeta.setText("Número: " + tarjeta.getNumero());
        lblEstadoTarjeta.setText("Estado: " + (tarjeta.isActiva() ? "ACTIVA" : "INACTIVA"));
        lblSaldoActual.setText("Saldo Actual: $" + tarjeta.getSaldo());

        double puntos = tarjetaController.calcularPuntosVigentes(cliente);
        lblPuntosVigentes.setText("Puntos Vigentes: " + puntos + " pts");

        listaMovimientosObs.clear();
        List<Movimiento> movimientos = tarjetaController.obtenerMovimientos(cliente);
        if (movimientos != null) {
            for (Movimiento m : movimientos) {
                listaMovimientosObs.add(m.getId() + " | " + m.getTipoMovimiento() + " | $" + m.getMonto() + " | Fecha: " + m.getFecha());
            }
        }
    }

    @FXML
    void onRecargarSaldo() {
        if (app != null && !(app.getUsuarioLogueado() instanceof uqvirutal.edu.co.cinemauq.model.Administrador)) {
            mostrarAlerta("Acceso Restringido", "Únicamente el administrador puede realizar recargas a la tarjeta virtual.");
            return;
        }
        Cliente cliente = cmbClientes.getSelectionModel().getSelectedItem();
        if (cliente == null) {
            mostrarAlerta("Advertencia", "Seleccione un cliente.");
            return;
        }

        try {
            double monto = Double.parseDouble(txtMontoRecarga.getText());
            if (monto <= 0) {
                mostrarAlerta("Error", "Ingrese un monto mayor a cero.");
                return;
            }

            boolean exito = tarjetaController.recargarTarjeta(cliente, monto);
            if (exito) {
                txtMontoRecarga.clear();
                actualizarVistaCliente(cliente);
                mostrarAlerta("Recarga Exitosa", "Se recargaron $" + monto + " a la tarjeta " + cliente.getTarjetaVirtual().getNumero() + ".");
            } else {
                mostrarAlerta("Error", "No se pudo realizar la recarga. Verifique que la tarjeta esté activa.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El monto debe ser un valor numérico válido.");
        }
    }

    @FXML
    void onRedimirPuntos() {
        Cliente cliente = cmbClientes.getSelectionModel().getSelectedItem();
        if (cliente == null) {
            mostrarAlerta("Advertencia", "Seleccione un cliente.");
            return;
        }

        try {
            double puntos = Double.parseDouble(txtPuntosRedimir.getText());
            if (puntos <= 0) {
                mostrarAlerta("Error", "Ingrese una cantidad de puntos mayor a cero.");
                return;
            }

            boolean exito = tarjetaController.redimirPuntosPorSaldo(cliente, puntos);
            if (exito) {
                txtPuntosRedimir.clear();
                actualizarVistaCliente(cliente);
                mostrarAlerta("Puntos Redimidos", "Se convirtieron " + puntos + " puntos en $" + (puntos * 100) + " de saldo.");
            } else {
                mostrarAlerta("Error", "Puntos insuficientes o tarjeta inactiva.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese un número válido de puntos.");
        }
    }

    @FXML
    void onAlternarEstadoTarjeta() {
        Cliente cliente = cmbClientes.getSelectionModel().getSelectedItem();
        if (cliente == null || cliente.getTarjetaVirtual() == null) {
            mostrarAlerta("Advertencia", "Seleccione un cliente.");
            return;
        }

        boolean nuevoEstado = !cliente.getTarjetaVirtual().isActiva();
        tarjetaController.cambiarEstadoTarjeta(cliente, nuevoEstado);
        actualizarVistaCliente(cliente);
        mostrarAlerta("Estado Actualizado", "La tarjeta ahora se encuentra " + (nuevoEstado ? "ACTIVA" : "INACTIVA") + ".");
    }

    public void setApp(App app) {
        this.app = app;
        if (app != null && app.getUsuarioLogueado() instanceof uqvirutal.edu.co.cinemauq.model.Cliente) {
            Cliente clienteLogueado = (Cliente) app.getUsuarioLogueado();
            if (cmbClientes != null) {
                cmbClientes.getSelectionModel().select(clienteLogueado);
                cmbClientes.setDisable(true);
            }
            if (txtMontoRecarga != null) {
                txtMontoRecarga.setDisable(true);
                txtMontoRecarga.setPromptText("Solo el administrador puede recargar");
            }
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
