package net.nanase.nanasetter.window.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.nanase.nanasetter.window.dialog.DialogUtils;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private WebView htmlRoot;

    @FXML
    private BorderPane root;

    private DialogUtils dialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setup() {
        this.dialog = new DialogUtils(this.root.getScene().getWindow());

        WebEngine webEngine = htmlRoot.getEngine();
        webEngine.setOnAlert(event -> this.dialog.showMessage(event.getData()));
        webEngine.load(getClass().getResource("/page/index.html").toString());
    }
}