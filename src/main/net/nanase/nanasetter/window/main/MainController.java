package net.nanase.nanasetter.window.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.nanase.nanasetter.Main;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private WebView htmlRoot;

    @FXML
    private BorderPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WebEngine webEngine = htmlRoot.getEngine();
        webEngine.setOnAlert(event -> showDialog(event.getData()));
        webEngine.load(getClass().getResource("/page/index.html").toString());
    }

    private void showDialog(String message) {
        Dialogs.create()
                .owner(this.root.getScene().getWindow())
                .title("")
                .message(message)
                .actions(new AbstractAction("OK") {
                    {
                        ButtonBar.setType(this, ButtonBar.ButtonType.YES);
                    }

                    @Override
                    public void handle(ActionEvent event) {
                        ((Dialog) event.getSource()).hide();
                    }
                })
                .showInformation();
    }
}