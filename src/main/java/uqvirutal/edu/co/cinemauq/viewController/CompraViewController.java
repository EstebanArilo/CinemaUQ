package uqvirutal.edu.co.cinemauq.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.CompraController;
import uqvirutal.edu.co.cinemauq.model.*;

import java.util.ArrayList;
import java.util.List;

public class CompraViewController {

    private App app;
    private final CompraController compraController;

    private final ObservableList<Funcion> listaFuncionesObs = FXCollections.observableArrayList();
    private final ObservableList<Asiento> listaAsientosObs = FXCollections.observableArrayList();
    private final ObservableList<String> carritoResumenObs = FXCollections.observableArrayList();

    private final List<Entrada> entradasSeleccionadas = new ArrayList<>();
    private final List<ProductoConfiteria> productosSeleccionados = new ArrayList<>();
    private final ArrayList<Asiento> asientosSeleccionados = new ArrayList<>();
    private int contadorEntradas = 1;
    private int contadorCompras = 1;

    @FXML private ComboBox<Funcion> cmbFunciones;
    @FXML private ComboBox<Asiento> cmbAsientos;
    @FXML private ComboBox<String> cmbTipoEntrada;
    @FXML private CheckBox chkGafas3D;

    @FXML private CheckBox chkCrispetas;
    @FXML private CheckBox chkGaseosa;
    @FXML private CheckBox chkExtraQueso;

    @FXML private TextField txtPuntosUsar;
    @FXML private ListView<String> listViewCarrito;
    @FXML private Label lblTotalCompra;

    public CompraViewController() {
        this.compraController = new CompraController();
    }

    @FXML
    void initialize() {
        cargarDatosIniciales();
        configurarListeners();
    }

    private void cargarDatosIniciales() {
        List<Funcion> funciones = compraController.obtenerFuncionesDisponibles();
        if (funciones != null) {
            listaFuncionesObs.setAll(funciones);
            if (cmbFunciones != null) {
                cmbFunciones.setItems(listaFuncionesObs);
            }
        }

        if (cmbTipoEntrada != null) {
            cmbTipoEntrada.setItems(FXCollections.observableArrayList("General", "VIP", "3D"));
            cmbTipoEntrada.getSelectionModel().selectFirst();
        }

        if (listViewCarrito != null) {
            listViewCarrito.setItems(carritoResumenObs);
        }
    }

    private void configurarListeners() {
        if (cmbFunciones != null) {
            cmbFunciones.getSelectionModel().selectedItemProperty().addListener((obs, vieja, funcionSeleccionada) -> {
                if (funcionSeleccionada != null) {
                    List<Asiento> asientos = compraController.obtenerAsientosFuncion(funcionSeleccionada);
                    listaAsientosObs.clear();
                    for (Asiento a : asientos) {
                        if (a.isDisponible()) {
                            listaAsientosObs.add(a);
                        }
                    }
                    if (cmbAsientos != null) {
                        cmbAsientos.setItems(listaAsientosObs);
                    }
                }
            });
        }
    }

    @FXML
    void onAgregarEntrada() {
        Funcion funcion = cmbFunciones.getSelectionModel().getSelectedItem();
        Asiento asiento = cmbAsientos.getSelectionModel().getSelectedItem();
        String tipo = cmbTipoEntrada.getSelectionModel().getSelectedItem();
        boolean gafas = chkGafas3D != null && chkGafas3D.isSelected();

        if (funcion == null || asiento == null) {
            mostrarAlerta("Advertencia", "Seleccione una función y un asiento disponible.");
            return;
        }

        String idEntrada = "ENT-" + contadorEntradas;
        contadorEntradas++;

        Entrada entrada = compraController.crearEntradaDecorada(idEntrada, funcion, asiento, tipo, gafas);
        entradasSeleccionadas.add(entrada);

        asientosSeleccionados.add(asiento);
        listaAsientosObs.remove(asiento);

        actualizarCarrito();
    }

    @FXML
    void onAgregarConfiteria() {
        productosSeleccionados.clear();

        if (chkCrispetas != null && chkCrispetas.isSelected()) {
            ProductoConfiteria crispetas = new ProductoSimple("PROD-1", 12000, "Crispetas Medianas", CategoriaProducto.SNACK);
            if (chkExtraQueso != null && chkExtraQueso.isSelected()) {
                crispetas = new ConQueso(crispetas);
            }
            productosSeleccionados.add(crispetas);
        }

        if (chkGaseosa != null && chkGaseosa.isSelected()) {
            ProductoConfiteria gaseosa = new ProductoSimple("PROD-2", 7000, "Gaseosa Grande", CategoriaProducto.BEBIDA);
            productosSeleccionados.add(gaseosa);
        }

        actualizarCarrito();
    }

    private void actualizarCarrito() {
        carritoResumenObs.clear();
        double total = 0.0;

        for (Entrada e : entradasSeleccionadas) {
            carritoResumenObs.add("Entrada: " + e.getDescripcion() + " - $" + e.getPrecio());
            total += e.getPrecio();
        }

        for (ProductoConfiteria p : productosSeleccionados) {
            carritoResumenObs.add("Confitería: " + p.getNombre() + " - $" + p.getPrecio());
            total += p.getPrecio();
        }

        if (lblTotalCompra != null) {
            lblTotalCompra.setText("Total Estimado: $" + total);
        }
    }

    @FXML
    void onConfirmarCompra() {
        Funcion funcion = cmbFunciones.getSelectionModel().getSelectedItem();
        if (funcion == null || entradasSeleccionadas.isEmpty()) {
            mostrarAlerta("Advertencia", "Debe seleccionar al menos una entrada antes de confirmar la compra.");
            return;
        }

        int puntosUsar = 0;
        if (txtPuntosUsar != null && !txtPuntosUsar.getText().isEmpty()) {
            try {
                puntosUsar = Integer.parseInt(txtPuntosUsar.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Los puntos a redimir deben ser un número entero.");
                return;
            }
        }

        Cliente clienteActivo = obtenerClienteActivo();
        if (clienteActivo == null) {
            mostrarAlerta("Error", "No hay un cliente activo con Tarjeta Virtual en el sistema.");
            return;
        }

        try {
            boolean compraExitosa = compraController.realizarCompra(
                    clienteActivo,
                    funcion,
                    new ArrayList<>(asientosSeleccionados),
                    new ArrayList<>(productosSeleccionados)
            );

            if (compraExitosa) {
                mostrarAlerta("Compra Exitosa", "¡Compra procesada correctamente! Revise su historial y puntos acumulados.");
                limpiarCompra();
            } else {
                mostrarAlerta("Transacción Rechazada", "No se pudo completar la compra. Verifique que la tarjeta esté activa, tenga saldo suficiente y los asientos sigan disponibles.");
            }
        } catch (Exception e) {
            mostrarAlerta("Error en la transacción", e.getMessage());
        }
    }

    private Cliente obtenerClienteActivo() {
        for (Usuario u : compraController.obtenerUsuarios()) {
            if (u instanceof Cliente) {
                return (Cliente) u;
            }
        }
        return null;
    }

    private void limpiarCompra() {
        entradasSeleccionadas.clear();
        productosSeleccionados.clear();
        asientosSeleccionados.clear();
        carritoResumenObs.clear();
        if (txtPuntosUsar != null) txtPuntosUsar.clear();
        if (lblTotalCompra != null) lblTotalCompra.setText("Total Estimado: $0.0");
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