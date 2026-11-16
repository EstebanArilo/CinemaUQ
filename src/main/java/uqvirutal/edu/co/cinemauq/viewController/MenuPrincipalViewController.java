package uqvirutal.edu.co.cinemauq.viewController;

import javafx.fxml.FXML;
import uqvirutal.edu.co.cinemauq.App;

public class MenuPrincipalViewController {

    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void onAbrirFunciones() {
        if (app != null) {
            app.openFunciones();
        }
    }

    @FXML
    void onAbrirCompras() {
        if (app != null) {
            app.openCompras();
        }
    }

    @FXML
    void onAbrirTarjetaVirtual() {
        if (app != null) {
            app.openTarjetaVirtual();
        }
    }

    @FXML
    void onAbrirReembolsos() {
        if (app != null) {
            app.openReembolsos();
        }
    }

    @FXML
    void onAbrirResenas() {
        if (app != null) {
            app.openResenas();
        }
    }

    @FXML
    void onAbrirReportes() {
        if (app != null) {
            app.openReportes();
        }
    }
}