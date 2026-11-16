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

    @Override
    public void start(Stage stage) {
        cargarDatosIniciales();
        this.primaryStage = stage;
        this.primaryStage.setTitle("CinemaUQ - Sistema de Gestión");
        openPrimary();
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

        Sala sala1 = new Sala(1, "Sala 1 - IMAX", 3, 4);
        cine.agregarSala(sala1);

        Pelicula pelicula1 = new Pelicula("PEL-01", "Interestelar", "Ciencia Ficción", 169, "Exploración espacial más allá de nuestra galaxia.", 12);
        cine.agregarPelicula(pelicula1);

        ArrayList<Asiento> asientosFuncion = new ArrayList<>();
        asientosFuncion.add(new Asiento("A", "A1", TipoAsiento.GENERAL, new AsientoDisponible()));
        asientosFuncion.add(new Asiento("A", "A2", TipoAsiento.GENERAL, new AsientoDisponible()));
        asientosFuncion.add(new Asiento("B", "B1", TipoAsiento.VIP, new AsientoDisponible()));
        asientosFuncion.add(new Asiento("B", "B2", TipoAsiento.VIP, new AsientoDisponible()));

        Funcion funcion1 = new Funcion("FUNC-01", pelicula1, sala1, LocalDateTime.now().plusDays(2), 15000.0, asientosFuncion);
        cine.agregarFuncion(funcion1);
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

    public static void main(String[] args) {
        launch();
    }
}