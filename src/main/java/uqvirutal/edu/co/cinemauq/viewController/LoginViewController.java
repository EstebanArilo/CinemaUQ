package uqvirutal.edu.co.cinemauq.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import uqvirutal.edu.co.cinemauq.App;
import uqvirutal.edu.co.cinemauq.controller.LoginController;
import uqvirutal.edu.co.cinemauq.model.Cliente;
import uqvirutal.edu.co.cinemauq.model.Usuario;

public class LoginViewController {

    private App app;
    private final LoginController loginController;

    @FXML private TextField txtCorreoLogin;
    @FXML private PasswordField txtContrasenaLogin;

    @FXML private TextField txtIdRegistro;
    @FXML private TextField txtNombreRegistro;
    @FXML private TextField txtCorreoRegistro;
    @FXML private PasswordField txtContrasenaRegistro;
    @FXML private TextField txtNumeroTarjetaRegistro;

    public LoginViewController() {
        this.loginController = new LoginController();
    }

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void onIniciarSesion() {
        String correo = txtCorreoLogin.getText().trim();
        String contrasena = txtContrasenaLogin.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Advertencia", "Ingrese su correo electrónico y contraseña.");
            return;
        }

        Usuario usuario = loginController.autenticarUsuario(correo, contrasena);
        if (usuario != null) {
            if (app != null) {
                app.setUsuarioLogueado(usuario);
                app.openPrimary();
            }
        } else {
            mostrarAlerta("Acceso Denegado", "Correo o contraseña incorrectos.");
        }
    }

    @FXML
    void onRegistrarCliente() {
        String id = txtIdRegistro.getText().trim();
        String nombre = txtNombreRegistro.getText().trim();
        String correo = txtCorreoRegistro.getText().trim();
        String contrasena = txtContrasenaRegistro.getText();
        String numeroTarjeta = txtNumeroTarjetaRegistro.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || numeroTarjeta.isEmpty()) {
            mostrarAlerta("Advertencia", "Complete todos los campos para registrarse.");
            return;
        }

        if (loginController.existeId(id)) {
            mostrarAlerta("Error", "Ya existe un usuario registrado con ese ID / Documento.");
            return;
        }

        if (loginController.existeCorreo(correo)) {
            mostrarAlerta("Error", "El correo electrónico ya está registrado en el sistema.");
            return;
        }

        Cliente nuevo = loginController.registrarNuevoCliente(id, nombre, correo, contrasena, numeroTarjeta);
        if (nuevo != null) {
            mostrarAlerta("Registro Exitoso",
                    "Bienvenido a CinemaUQ, " + nuevo.getNombre() + ".\nSe ha creado su Tarjeta Virtual N° " + numeroTarjeta + ".");
            limpiarRegistro();
            if (app != null) {
                app.setUsuarioLogueado(nuevo);
                app.openPrimary();
            }
        } else {
            mostrarAlerta("Error", "No se pudo completar el registro del cliente.");
        }
    }

    @FXML
    void onAccesoRapidoAdmin() {
        txtCorreoLogin.setText("admin@cinemauq.edu.co");
        txtContrasenaLogin.setText("admin123");
    }

    @FXML
    void onAccesoRapidoCliente() {
        txtCorreoLogin.setText("santiago@gmail.com");
        txtContrasenaLogin.setText("1234");
    }

    private void limpiarRegistro() {
        txtIdRegistro.clear();
        txtNombreRegistro.clear();
        txtCorreoRegistro.clear();
        txtContrasenaRegistro.clear();
        txtNumeroTarjetaRegistro.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}