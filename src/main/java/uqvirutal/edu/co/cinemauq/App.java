package uqvirutal.edu.co.cinemauq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uqvirutal.edu.co.cinemauq.model.*;
import uqvirutal.edu.co.cinemauq.viewController.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class App extends Application {

    private Stage primaryStage;
    private Usuario usuarioLogueado;

    @Override
    public void start(Stage stage) {
        cargarDatosIniciales();
        this.primaryStage = stage;
        this.primaryStage.setTitle("CinemaUQ - Sistema de Gestión");
        openLogin();
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    private void cargarDatosIniciales() {
        Cine cine = Cine.getInstancia();
        if (!cine.getListaUsuarios().isEmpty()) {
            return;
        }

        Administrador admin = new Administrador("ADM-01", "Admin CinemaUQ", "admin@cinemauq.edu.co", "admin123");
        cine.agregarUsuario(admin);

        Cliente cliente = new Cliente("CLI-01", "Santiago Pérez", "santiago@gmail.com", "1234", "TARJ-1001");
        cliente.getTarjetaVirtual().recargar(100000.0);
        cliente.acumularPuntos(50.0);
        cine.agregarUsuario(cliente);

        if (admin.getServicioNotificaciones() != null) {
            admin.getServicioNotificaciones().agregarObservador(cliente);
        }

        admin.crearProducto("PROD-01", "Crispetas Medianas", 12000.0, CategoriaProducto.SNACK);
        admin.crearProducto("PROD-02", "Gaseosa 400ml", 6000.0, CategoriaProducto.BEBIDA);
        admin.crearProducto("PROD-03", "Perro Caliente", 10000.0, CategoriaProducto.SNACK);

        Sala sala1 = new Sala(1, "Sala 1 - IMAX", 4, 5);
        Sala sala2 = new Sala(2, "Sala 2 - 3D", 5, 5);
        Sala sala3 = new Sala(3, "Sala 3 - Estándar", 5, 5);

        poblarAsientosSala(sala1, 4, 5);
        poblarAsientosSala(sala2, 5, 5);
        poblarAsientosSala(sala3, 5, 5);

        cine.agregarSala(sala1);
        cine.agregarSala(sala2);
        cine.agregarSala(sala3);

        Pelicula pelicula1 = new Pelicula("PEL-01", "Interestelar", "Ciencia Ficción", 169, "Exploración espacial más allá de nuestra galaxia.", 12);
        cine.agregarPelicula(pelicula1);

        Funcion funcion1 = new Funcion("FUNC-01", pelicula1, sala1, LocalDateTime.now().plusDays(2), 15000.0, clonarAsientosSala(sala1));
        cine.agregarFuncion(funcion1);
    }


    private void poblarAsientosSala(Sala sala, int numFilas, int numColumnas) {
        if (sala.getListaAsientos() == null) {
            return;
        }
        sala.getListaAsientos().clear();
        for (int f = 0; f < numFilas; f++) {
            String letraFila = String.valueOf((char) ('A' + f));
            TipoAsiento tipo = (f == numFilas - 1) ? TipoAsiento.VIP : TipoAsiento.GENERAL;
            for (int c = 1; c <= numColumnas; c++) {
                String codigoAsiento = letraFila + c;
                sala.getListaAsientos().add(new Asiento(letraFila, codigoAsiento, tipo, new AsientoDisponible()));
            }
        }
    }

    private ArrayList<Asiento> clonarAsientosSala(Sala sala) {
        ArrayList<Asiento> copia = new ArrayList<>();
        if (sala != null && sala.getListaAsientos() != null) {
            for (Asiento a : sala.getListaAsientos()) {
                copia.add(new Asiento(a.getFila(), a.getNumero(), a.getTipoAsiento(), new AsientoDisponible()));
            }
        }
        return copia;
    }

    public void openLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load());

            LoginViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openPrimary() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(loader.load());

            MenuPrincipalViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openFunciones() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("funcion-view.fxml"));
            Scene scene = new Scene(loader.load());

            FuncionViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openCompras() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("compra-view.fxml"));
            Scene scene = new Scene(loader.load());

            CompraViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openTarjetaVirtual() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("tarjeta-virtual-view.fxml"));
            Scene scene = new Scene(loader.load());

            TarjetaVirtualViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openReembolsos() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("reembolso-view.fxml"));
            Scene scene = new Scene(loader.load());

            ReembolsoViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openResenas() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("resena-view.fxml"));
            Scene scene = new Scene(loader.load());

            ResenaViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openReportes() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("reporte-view.fxml"));
            Scene scene = new Scene(loader.load());

            ReporteViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openNotificaciones() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("notificacion-view.fxml"));
            Scene scene = new Scene(loader.load());

            NotificacionViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}