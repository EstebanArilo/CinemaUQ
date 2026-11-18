package uqvirutal.edu.co.cinemauq.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.CompraController;
import uqvirutal.edu.co.cinemauq.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CompraViewController {

    private App app;
    private final CompraController compraController;

    private final ObservableList<Funcion> listaFuncionesObs = FXCollections.observableArrayList();
    private final ObservableList<String> carritoResumenObs = FXCollections.observableArrayList();

    private final List<Entrada> entradasSeleccionadas = new ArrayList<>();
    private final List<ProductoConfiteria> productosSeleccionados = new ArrayList<>();
    private final ArrayList<Asiento> asientosSeleccionados = new ArrayList<>();
    private int contadorEntradas = 1;

    @FXML private ComboBox<Funcion> cmbFunciones;
    @FXML private ComboBox<String> cmbTipoEntrada;
    @FXML private CheckBox chkGafas3D;
    @FXML private Label lblAsientoActivo;
    @FXML private GridPane gridAsientos;

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
                if (!listaFuncionesObs.isEmpty()) {
                    cmbFunciones.getSelectionModel().selectFirst();
                    dibujarMapaAsientos(cmbFunciones.getSelectionModel().getSelectedItem());
                }
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
                entradasSeleccionadas.clear();
                asientosSeleccionados.clear();
                if (lblAsientoActivo != null) {
                    lblAsientoActivo.setText("Asientos seleccionados: Ninguno");
                }
                actualizarCarrito();
                dibujarMapaAsientos(funcionSeleccionada);
            });
        }
    }

    private void dibujarMapaAsientos(Funcion funcion) {
        if (gridAsientos == null) {
            return;
        }
        gridAsientos.getChildren().clear();

        if (funcion == null) {
            return;
        }

        List<Asiento> asientos = compraController.obtenerAsientosFuncion(funcion);
        if (asientos == null || asientos.isEmpty()) {
            Label lblSinAsientos = new Label("Esta función no tiene asientos asignados.");
            gridAsientos.add(lblSinAsientos, 0, 0);
            return;
        }

        Map<String, List<Asiento>> asientosPorFila = new LinkedHashMap<>();
        for (Asiento a : asientos) {
            String fila = (a.getFila() != null && !a.getFila().isEmpty()) ? a.getFila() : "F";
            if (!asientosPorFila.containsKey(fila)) {
                asientosPorFila.put(fila, new ArrayList<>());
            }
            asientosPorFila.get(fila).add(a);
        }

        int rowIndex = 0;
        for (Map.Entry<String, List<Asiento>> entry : asientosPorFila.entrySet()) {
            String nombreFila = entry.getKey();

            Label lblFila = new Label("Fila " + nombreFila);
            lblFila.setStyle("-fx-font-weight: bold; -fx-text-fill: #37474F;");
            lblFila.setMinWidth(48);
            gridAsientos.add(lblFila, 0, rowIndex);

            int colIndex = 1;
            for (Asiento asiento : entry.getValue()) {
                Button btnAsiento = crearBotonAsiento(funcion, asiento);
                gridAsientos.add(btnAsiento, colIndex, rowIndex);
                colIndex++;
            }
            rowIndex++;
        }
    }

    private Button crearBotonAsiento(Funcion funcion, Asiento asiento) {
        Button btn = new Button(asiento.getNumero());
        btn.setPrefWidth(54);
        btn.setPrefHeight(40);
        btn.setAlignment(Pos.CENTER);

        boolean estaSeleccionado = asientosSeleccionados.contains(asiento);
        boolean estaDisponible = asiento.isDisponible();

        if (!estaDisponible) {
            btn.setDisable(true);
            btn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
            btn.setTooltip(new Tooltip("Asiento Ocupado"));
        } else if (estaSeleccionado) {
            btn.setStyle("-fx-background-color: #FFB300; -fx-text-fill: #212121; -fx-font-weight: bold; -fx-background-radius: 6; -fx-border-color: #E65100; -fx-border-radius: 6; -fx-border-width: 2;");
            btn.setTooltip(new Tooltip("Seleccionado (Clic para quitar)"));
        } else if (asiento.getTipoAsiento() == TipoAsiento.VIP) {
            btn.setStyle("-fx-background-color: #6A1B9A; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
            btn.setTooltip(new Tooltip("Disponible - VIP"));
        } else {
            btn.setStyle("-fx-background-color: #3949AB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
            btn.setTooltip(new Tooltip("Disponible - " + asiento.getTipoAsiento()));
        }

        btn.setOnAction(event -> alternarSeleccionAsiento(funcion, asiento));
        return btn;
    }

    private void alternarSeleccionAsiento(Funcion funcion, Asiento asiento) {
        if (asientosSeleccionados.contains(asiento)) {
            asientosSeleccionados.remove(asiento);
            Entrada entradaAEliminar = null;
            for (Entrada e : entradasSeleccionadas) {
                if (e.getAsiento() != null && e.getAsiento().equals(asiento.getNumero())) {
                    entradaAEliminar = e;
                    break;
                }
            }
            if (entradaAEliminar != null) {
                entradasSeleccionadas.remove(entradaAEliminar);
            }
        } else {
            String tipoActual = cmbTipoEntrada.getSelectionModel().getSelectedItem();
            if (!"3D".equalsIgnoreCase(tipoActual)) {
                if (asiento.getTipoAsiento() == TipoAsiento.VIP) {
                    cmbTipoEntrada.getSelectionModel().select("VIP");
                } else {
                    cmbTipoEntrada.getSelectionModel().select("General");
                }
            }

            String tipoFinal = cmbTipoEntrada.getSelectionModel().getSelectedItem();
            boolean gafas = chkGafas3D != null && chkGafas3D.isSelected();

            String idEntrada = "ENT-" + contadorEntradas;
            contadorEntradas++;

            Entrada entrada = compraController.crearEntradaDecorada(idEntrada, funcion, asiento, tipoFinal, gafas);
            entradasSeleccionadas.add(entrada);
            asientosSeleccionados.add(asiento);
        }

        actualizarTextoAsientosSeleccionados();
        actualizarCarrito();
        dibujarMapaAsientos(funcion);
    }

    private void actualizarTextoAsientosSeleccionados() {
        if (lblAsientoActivo == null) {
            return;
        }
        if (asientosSeleccionados.isEmpty()) {
            lblAsientoActivo.setText("Asientos seleccionados: Ninguno");
        } else {
            StringBuilder sb = new StringBuilder("Asientos seleccionados: ");
            for (int i = 0; i < asientosSeleccionados.size(); i++) {
                sb.append(asientosSeleccionados.get(i).getNumero());
                if (i < asientosSeleccionados.size() - 1) {
                    sb.append(", ");
                }
            }
            lblAsientoActivo.setText(sb.toString());
        }
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
            mostrarAlerta("Advertencia", "Seleccione al menos un asiento en el mapa gráfico de la sala.");
            return;
        }

        if (txtPuntosUsar != null && !txtPuntosUsar.getText().isEmpty()) {
            try {
                Integer.parseInt(txtPuntosUsar.getText());
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
                mostrarAlerta("Compra Exitosa",
                        "¡Compra procesada para " + clienteActivo.getNombre() + "!\nSaldo restante: $" + clienteActivo.getTarjetaVirtual().getSaldo());
                limpiarCompra();
                dibujarMapaAsientos(funcion);
            } else {
                mostrarAlerta("Transacción Rechazada",
                        "No se pudo completar la compra. Verifique que la tarjeta esté activa, tenga saldo suficiente y los asientos sigan disponibles.");
            }
        } catch (Exception e) {
            mostrarAlerta("Error en la transacción", e.getMessage());
        }
    }

    private Cliente obtenerClienteActivo() {
        if (app != null && app.getUsuarioLogueado() instanceof Cliente) {
            return (Cliente) app.getUsuarioLogueado();
        }
        return null;
    }

    private void limpiarCompra() {
        entradasSeleccionadas.clear();
        productosSeleccionados.clear();
        asientosSeleccionados.clear();
        carritoResumenObs.clear();
        if (lblAsientoActivo != null) lblAsientoActivo.setText("Asientos seleccionados: Ninguno");
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