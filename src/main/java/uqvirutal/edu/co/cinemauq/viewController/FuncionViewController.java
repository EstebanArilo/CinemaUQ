package uqvirutal.edu.co.cinemauq.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.FuncionController;
import uqvirutal.edu.co.cinemauq.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FuncionViewController {

    private App app;
    private final FuncionController funcionController;

    private final ObservableList<Pelicula> listaPeliculasObs = FXCollections.observableArrayList();
    private final ObservableList<Sala> listaSalasObs = FXCollections.observableArrayList();
    private final ObservableList<String> listaFuncionesObs = FXCollections.observableArrayList();


    @FXML private TextField txtIdPelicula;
    @FXML private TextField txtTituloPelicula;
    @FXML private TextField txtGeneroPelicula;
    @FXML private TextField txtDuracionPelicula;


    @FXML private TextField txtIdFuncion;
    @FXML private ComboBox<Pelicula> cmbPeliculas;
    @FXML private ComboBox<Sala> cmbSalas;
    @FXML private DatePicker dpFechaFuncion;
    @FXML private TextField txtHoraFuncion;
    @FXML private TextField txtPrecioBase;
    @FXML private ListView<String> listViewFunciones;
    @FXML private TextArea txtSinopsisPelicula;
    @FXML private TextField txtClasificacionPelicula;

    public FuncionViewController() {
        this.funcionController = new FuncionController();
    }

    @FXML
    void initialize() {
        cargarDatosIniciales();
    }

    public void cargarDatosIniciales() {
        List<Pelicula> peliculas = funcionController.obtenerPeliculas();
        if (peliculas != null) {
            listaPeliculasObs.setAll(peliculas);
            if (cmbPeliculas != null) {
                cmbPeliculas.setItems(listaPeliculasObs);
            }
        }

        List<Sala> salas = funcionController.obtenerSalas();
        if (salas != null) {
            listaSalasObs.setAll(salas);
            if (cmbSalas != null) {
                cmbSalas.setItems(listaSalasObs);
            }
        }

        actualizarListaFunciones();
    }

    @FXML
    void onRegistrarPelicula() {
        try {
            String id = txtIdPelicula.getText();
            String titulo = txtTituloPelicula.getText();
            String genero = txtGeneroPelicula.getText();
            int duracion = Integer.parseInt(txtDuracionPelicula.getText());
            String sinopsis = txtSinopsisPelicula.getText();
            int clasificacion = Integer.parseInt(txtClasificacionPelicula.getText());
            Pelicula nuevaPelicula = new Pelicula(id, titulo, genero, duracion, sinopsis, clasificacion);

            if (id.isEmpty() || titulo.isEmpty() || genero.isEmpty()) {
                mostrarAlerta("Advertencia", "Complete todos los datos de la película.");
                return;
            }
            funcionController.registrarPelicula(nuevaPelicula);

            cargarDatosIniciales();
            limpiarCamposPelicula();
            mostrarAlerta("Éxito", "Película registrada correctamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La duración debe ser un número entero (minutos).");
        }
    }

    @FXML
    void onProgramarFuncion() {
        try {
            String idFuncion = txtIdFuncion.getText().trim();
            Pelicula pelicula = cmbPeliculas.getSelectionModel().getSelectedItem();
            Sala sala = cmbSalas.getSelectionModel().getSelectedItem();
            LocalDate fecha = dpFechaFuncion.getValue();
            String horaTexto = txtHoraFuncion.getText().trim();
            String precioTexto = txtPrecioBase.getText().trim();

            if (idFuncion.isEmpty() || pelicula == null || sala == null || fecha == null || horaTexto.isEmpty() || precioTexto.isEmpty()) {
                mostrarAlerta("Advertencia", "Complete todos los campos para programar la función.");
                return;
            }

            double precioEntrada = Double.parseDouble(precioTexto);
            LocalTime hora = LocalTime.parse(horaTexto);
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            ArrayList<Asiento> asientosNuevos = new ArrayList<>();
            if (sala.getListaAsientos() != null) {
                for (Asiento a : sala.getListaAsientos()) {
                    asientosNuevos.add(new Asiento(a.getFila(), a.getNumero(), a.getTipoAsiento(), new AsientoDisponible()));
                }
            }

            Funcion nuevaFuncion = new Funcion(idFuncion, pelicula, sala, fechaHora, precioEntrada, asientosNuevos);
            funcionController.programarFuncion(nuevaFuncion);

            actualizarListaFunciones();
            limpiarCamposFuncion();
            mostrarAlerta("Éxito", "Función programada exitosamente con " + asientosNuevos.size() + " asientos disponibles.");

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El precio debe ser un valor numérico válido (ej. 15000).");
        } catch (java.time.format.DateTimeParseException e) {
            mostrarAlerta("Error de formato", "La hora debe tener formato HH:mm de 24 horas (ej. 14:30).");
        } catch (NullPointerException e) {
            mostrarAlerta("Error de interfaz (FXML)", "Uno de los campos de texto es null. Revisa que los fx:id en funcion-view.fxml coincidan con FuncionViewController.");
        }
    }

    private void actualizarListaFunciones() {
        listaFuncionesObs.clear();
        List<Funcion> funciones = funcionController.obtenerFunciones();

        if (funciones != null) {
            for (Funcion f : funciones) {
                String info = f.getIdFuncion() + " | " + f.getPelicula().getTitulo()
                        + " | Sala: " + f.getSala().getNumeroSala()
                        + " | Fecha: " + f.getFechaHora()
                        + " | $" + f.getPrecioEntrada();
                listaFuncionesObs.add(info);
            }
        }

        if (listViewFunciones != null) {
            listViewFunciones.setItems(listaFuncionesObs);
        }
    }

    private void limpiarCamposPelicula() {
        txtIdPelicula.clear();
        txtTituloPelicula.clear();
        txtGeneroPelicula.clear();
        txtDuracionPelicula.clear();
    }

    private void limpiarCamposFuncion() {
        txtIdFuncion.clear();
        txtHoraFuncion.clear();
        txtPrecioBase.clear();
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
